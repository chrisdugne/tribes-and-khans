package com.uralys.tribes.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.uralys.tribes.commons.Constants;
import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.entities.Ally;
import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Equipment;
import com.uralys.tribes.entities.Message;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Stock;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.dto.AllyDTO;
import com.uralys.tribes.entities.dto.CaseDTO;
import com.uralys.tribes.entities.dto.CityDTO;
import com.uralys.tribes.entities.dto.ConflictDTO;
import com.uralys.tribes.entities.dto.EquipmentDTO;
import com.uralys.tribes.entities.dto.GatheringDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.entities.dto.MessageDTO;
import com.uralys.tribes.entities.dto.MoveDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.ServerDataDTO;
import com.uralys.tribes.entities.dto.SmithDTO;
import com.uralys.tribes.entities.dto.StockDTO;
import com.uralys.tribes.entities.dto.UnitDTO;
import com.uralys.tribes.utils.TribesUtils;
import com.uralys.utils.Utils;

public class GameDAO  extends MainDAO implements IGameDAO {
	
	//-----------------------------------------------------------------------//
	// local

	private static String ITEM_UID_BOW = "_bow";
	private static String ITEM_UID_SWORD = "_sword";
	private static String ITEM_UID_ARMOR = "_armor";

	private static String WHEAT_STOCK_ID = "_wheat_stock";
	private static String WOOD_STOCK_ID = "_wood_stock";
	private static String IRON_STOCK_ID = "_iron_stock";
	private static String BOW_STOCK_ID = "_bow_stock";
	private static String SWORD_STOCK_ID = "_sword_stock";
	private static String ARMOR_STOCK_ID = "_armor_stock";
	
	private static boolean debug = true; 

	//-----------------------------------------------------------------------//


	public String createPlayer(String uralysUID, String email)
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		try{
			// il peut arriver qu'il y ait un probleme dans le getPlayer
			// dans ce cas Flex demande un createPlayer
			// on verifie ici que le player n'existe pas pour ne pas l'ecraser si il existe.
			PlayerDTO playerDTOinDatastore = pm.getObjectById(PlayerDTO.class, uralysUID);
			if(playerDTOinDatastore != null)
				return uralysUID;
		}
		catch (Exception e) {}
		
		ServerDataDTO serverData = pm.getObjectById(ServerDataDTO.class, "serverData");
		serverData.setNbPlayers(serverData.getNbPlayers()+1);

		PlayerDTO playerDTO =  new PlayerDTO();
		
		Key key = KeyFactory.createKey(PlayerDTO.class.getSimpleName(), uralysUID);
		
		playerDTO.setKey(KeyFactory.keyToString(key));
		playerDTO.setUralysUID(uralysUID);
		playerDTO.setName("New Player");
		playerDTO.setAllyUID(uralysUID);
		playerDTO.setNbLands(0);
		playerDTO.setNbCities(0);
		playerDTO.setNbArmies(0);
		playerDTO.setNbPopulation(500);
		playerDTO.setNbConnections(0);
		playerDTO.setMusicOn(true);
		
		long now = new Date().getTime();
		long timeSpentMillis = now - Constants.SERVER_START;
		
		playerDTO.setLastStep(timeSpentMillis/(Constants.SERVER_STEP*60*1000));
		
		persist(playerDTO);
		
		createCity(null, uralysUID, pm, serverData);
		
		pm.close();

		return uralysUID;
	}
	
	public PlayerDTO getPlayer(String uralysUID) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		try{
			PlayerDTO player = pm.getObjectById(PlayerDTO.class, uralysUID);
			player.setNbConnections(player.getNbConnections()+1);
			pm.close();
			return player;
		}
		catch(Exception e){
			// no Player !!
			return null;
		}
	}

	//-----------------------------------------------------------------------//

	public CityDTO createNewFirstCity(String playerUID)
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ServerDataDTO serverData = pm.getObjectById(ServerDataDTO.class, "serverData");
		
		String cityUID = createCity(null, playerUID, pm, serverData);
		CityDTO newCity = pm.getObjectById(CityDTO.class, cityUID);
		pm.close();
		return newCity;
	}
	
	//-----------------------------------------------------------------------//

	private String createCity(City cityFromFlex, String playerUID, PersistenceManager pm, ServerDataDTO serverData) 
	{
		PlayerDTO player = pm.getObjectById(PlayerDTO.class, playerUID);
		
		CityDTO city = new CityDTO();
		long now = new Date().getTime();

		String cityUID = Utils.generateUID();

		Key key = KeyFactory.createKey(CityDTO.class.getSimpleName(), cityUID);

		city.setKey(KeyFactory.keyToString(key));
		city.setCityUID(cityUID);
		city.setOwnerUID(playerUID);
		city.setName(cityFromFlex == null ? "Nouvelle ville" : cityFromFlex.getName());
		city.setPopulation(cityFromFlex == null ? 1000 : cityFromFlex.getPopulation());
		city.setWheat(cityFromFlex == null ? 400 : cityFromFlex.getWheat());
		city.setWood(cityFromFlex == null ? 200 : cityFromFlex.getWood());
		city.setIron(cityFromFlex == null ? 200 : cityFromFlex.getIron());
		city.setGold(cityFromFlex == null ? 100 : cityFromFlex.getGold());
		city.setBeginTime(cityFromFlex == null ? now : cityFromFlex.getBeginTime());
		city.setEndTime(-1);
		city.setPeopleCreatingWheat(0);
		city.setPeopleCreatingWood(0);
		city.setPeopleCreatingIron(0);
		city.setTimeToChangeOwner(-1l);
		city.setNextOwnerUID(null);
		city.setPopulationLost(null);

		int cityX = 0; 
		int cityY = 0; 
			
		if(cityFromFlex == null)
		{
			boolean caseFound = false;
			
			while(!caseFound){
				cityX = getInitialCityX(serverData.getAlpha());
				cityY = getInitialCityY(serverData.getAlpha());
				serverData.setAlpha(increaseAlpha(serverData.getAlpha()));
				
				if(Math.abs(cityX-cityY)%2 !=0){
					// la difference entre x et y n'est pas paire
					// c'est une 'non case' : un hexagone intermediaire qui ne fait pas partie du plateau
					// on prend la case d'a coté
					cityY++;
				}
				
				if(getCase(cityX, cityY).getLandOwnerUID() == null)
					caseFound = true;
			}
			
		}
		else{
			cityX = cityFromFlex.getX();
			cityY = cityFromFlex.getY();
		}
		
		city.setX(cityX);
		city.setY(cityY);
		
		//--------------------------------------//
		// init Equipment
		
		String bowsStockUID = createEquipment(cityUID, ITEM_UID_BOW, 0);
		String swordsStockUID = createEquipment(cityUID, ITEM_UID_SWORD, 0);
		String armorsStockUID = createEquipment(cityUID, ITEM_UID_ARMOR, 0);
		
		city.getEquipmentStockUIDs().add(bowsStockUID);
		city.getEquipmentStockUIDs().add(swordsStockUID);
		city.getEquipmentStockUIDs().add(armorsStockUID);
		
		//--------------------------------------//
		// init Smith 
		
		String bowWorkersUID = createSmith(ITEM_UID_BOW);
		String swordWorkersUID = createSmith(ITEM_UID_SWORD);
		String armorWorkersUID = createSmith(ITEM_UID_ARMOR);
		
		city.getSmithUIDs().add(bowWorkersUID);
		city.getSmithUIDs().add(swordWorkersUID);
		city.getSmithUIDs().add(armorWorkersUID);

		//--------------------------------------//
		// init Stocks
		
		String wheatStockUID = createStock(cityUID, WHEAT_STOCK_ID, 500);
		String woodStockUID = createStock(cityUID, WOOD_STOCK_ID, 500);
		String ironStockUID = createStock(cityUID, IRON_STOCK_ID, 500);
		String bowStockUID = createStock(cityUID, BOW_STOCK_ID, 50);
		String swordStockUID = createStock(cityUID, SWORD_STOCK_ID, 50);
		String armorStockUID = createStock(cityUID, ARMOR_STOCK_ID, 50);
		
		city.getStockUIDs().add(wheatStockUID);
		city.getStockUIDs().add(woodStockUID);
		city.getStockUIDs().add(ironStockUID);
		city.getStockUIDs().add(bowStockUID);
		city.getStockUIDs().add(swordStockUID);
		city.getStockUIDs().add(armorStockUID);

		//--------------------------------------//
		
		pm.makePersistent(city);
		
		//--------------------------------------//
		// creation des cases autour de la ville

		createOrRefreshCase(city.getX(), city.getY(), cityUID, Case.CITY, playerUID, pm);
		
		//--------------------------------------//

		player.getCityUIDs().add(cityUID);
		player.setNbCities(player.getNbCities() + 1);
		
		increaseLandsCount(player, pm);
		
//		player.setNbLands(player.getNbLands() + 1);
//		
//		if(player.getAlly() != null){
//			AllyDTO ally = pm.getObjectById(AllyDTO.class, player.getAllyUID());	
//			ally.setNbLands(ally.getNbLands() + 1);
//		}
		
		return cityUID;
	}


	/*
	 * 0-404 * 0-404 = 27*27 groups
	 * 15*15*27*27 = 405*405
	 * 
	 */
	private CaseDTO createCase(int x, int y, String cityUID, int type, String landOwnerUID, PersistenceManager pm) 
	{
		CaseDTO _case = new CaseDTO();

		String caseUID = "case_"+x+"_"+y;
		int group = TribesUtils.getGroup(x,y); 
		
		Key key = KeyFactory.createKey(CaseDTO.class.getSimpleName(), caseUID);

		_case.setKey(KeyFactory.keyToString(key));
		_case.setCaseUID(caseUID);
		_case.setX(x);
		_case.setY(y);
		_case.setGroupCase(group);
		_case.setCityUID(cityUID);
		_case.setLandOwnerUID(landOwnerUID);
		_case.setChallengerUID(null);
		_case.setTimeFromChallenging(-1);
		_case.setType(type);

		pm.makePersistent(_case);
		return _case;
	}
	

	//==================================================================================================//
	/*
	
	nbplayers/3 = colonne
	nbplayers/30 = ligne
	
	x = 100 + colonne*20 à 120 + colonne*20
	y = 100 + ligne*20 à 120 + ligne*20
	
	line1
	p1 random [(100-105),(100-105)]
	p2 random [(110-115),(100-105)]
	p3 random [(120-125),(100-105)]
	p20 random [(290-295),(100-105)]
	
	line2
	p21 random [(100-105),(110-115)]
	p22 random [(110-115),(110-115)]
	p23 random [(120-125),(110-115)]
	p40 random [(290-295),(110-115)]

	line20
	p381 random [(100-105),(290-295)]
	p382 random [(110-115),(290-295)]
	p383 random [(120-125),(290-295)]
	p400 random [(290-295),(290-295)]
	 
	400 joueurs
	
	*/
	
	// 20 joueurs par colonnes et par ligne
	private int getInitialCityX(double alpha) 
	{
		int turn = (int) (alpha/(2*Math.PI)) + 1;
		return (int) (200 + turn*Math.cos(alpha)*Constants.PLAYER_DISTANCE);
	}

	private int getInitialCityY(double alpha) 
	{
		int turn = (int) (alpha/(2*Math.PI)) + 1;
		return (int) (200 + turn*Math.sin(alpha)*Constants.PLAYER_DISTANCE);
	}

	private double increaseAlpha(double alpha) 
	{
		int turn = (int) (alpha/(2*Math.PI)) + 1;
		return alpha += Math.PI/(4*turn);
	}
	
	//==================================================================================================//
	
	public List<ItemDTO> loadItems() {
		return UniversalDAO.getInstance().getListDTO(ItemDTO.class, 1, 100);
	}

	@SuppressWarnings("unchecked")
	public List<CaseDTO> loadCases(int[] groups, boolean refreshLandOwners) 
	{
		long now = new Date().getTime();

		List<CaseDTO> result = new ArrayList<CaseDTO>();
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		for(int group : groups){
			Query query = pm.newQuery("select from " + CaseDTO.class.getName() + " where groupCase == :group");
			Collection<? extends CaseDTO> cases = (Collection<? extends CaseDTO>) query.execute(group);
			
			if(refreshLandOwners){
				for(CaseDTO _case : cases)
				{
					if(_case.getChallengerUID() != null)
					{
						if(now - _case.getTimeFromChallenging() > Constants.LAND_TIME*60*1000)
						{
							newLandOwner(_case.getCaseUID());
						}
					}
				}
			}
			
			result.addAll(cases);
		}
		
		pm.close();
		return result;
	}

	private void newLandOwner(String caseUID) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		CaseDTO _case = pm.getObjectById(CaseDTO.class, caseUID);
		
		if(_case.getLandOwnerUID() != null){
			PlayerDTO owner = pm.getObjectById(PlayerDTO.class, _case.getLandOwnerUID());
			decreaseLandsCount(owner, pm);
		}
		
		PlayerDTO challenger = pm.getObjectById(PlayerDTO.class, _case.getChallengerUID());
		increaseLandsCount(challenger, pm);
		
		_case.setLandOwnerUID(_case.getChallengerUID());
		_case.setChallengerUID(null);
		_case.setTimeFromChallenging(-1);
		
		pm.close();
	}
	
	//==================================================================================================//

	public CaseDTO getCase(int i, int j) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		try{
			return pm.getObjectById(CaseDTO.class, "case_"+i+"_"+j);
		}
		catch(JDOObjectNotFoundException e){
			return createCase(i, j, null, Case.FOREST, null, pm);
		}
	}

	private void createOrRefreshCase(int x, int y, String cityUID, int type, String landOwnerUID, PersistenceManager pm) 
	{
		try{
			CaseDTO caseDTO = pm.getObjectById(CaseDTO.class, "case_"+x+"_"+y);
			caseDTO.setLandOwnerUID(landOwnerUID);
			caseDTO.setType(type);
			caseDTO.setCityUID(cityUID);
		}
		catch(JDOObjectNotFoundException e){
			createCase(x, y, cityUID, type, landOwnerUID, pm);
		}
	}

	//==================================================================================================//
	
	private void increaseLandsCount(PlayerDTO playerDTO, PersistenceManager pm)
	{
		playerDTO.setNbLands(playerDTO.getNbLands() + 1);

		if(!playerDTO.getUralysUID().equals(playerDTO.getAllyUID())){
			AllyDTO ally = pm.getObjectById(AllyDTO.class, playerDTO.getAllyUID());	
			ally.setNbLands(ally.getNbLands() + 1);
		}
	}
	
	private void decreaseLandsCount(PlayerDTO playerDTO, PersistenceManager pm)
	{
		playerDTO.setNbLands(playerDTO.getNbLands() - 1);

		if(!playerDTO.getUralysUID().equals(playerDTO.getAllyUID())){
			AllyDTO ally = pm.getObjectById(AllyDTO.class, playerDTO.getAllyUID());	
			ally.setNbLands(ally.getNbLands() - 1);
		}
	}

	//==================================================================================================//

	public void changeMusicOn(String uralysUID, boolean musicOn)
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, uralysUID);
	
		playerDTO.setMusicOn(musicOn);

		pm.close();
	}
	
	//==================================================================================================//
	
	public void updatePlayerPoints(Player player)
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, player.getUralysUID());
		
		playerDTO.setNbPopulation(player.getNbPopulation());
		playerDTO.setNbCities(player.getNbCities());
		playerDTO.setNbArmies(player.getNbArmies());
		
		pm.close();
	}

	public void updateAllyPoints(Ally ally)
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		AllyDTO allyDTO = pm.getObjectById(AllyDTO.class, ally.getAllyUID());
		
		allyDTO.setNbPopulation(ally.getNbPopulation());
		allyDTO.setNbCities(ally.getNbCities());
		allyDTO.setNbArmies(ally.getNbArmies());
		
		pm.close();
	}

	public void updatePlayer(Player player)
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, player.getUralysUID());
		
		long now = new Date().getTime();
		long timeSpentMillis = now - Constants.SERVER_START;
		
		playerDTO.setLastStep(timeSpentMillis/(Constants.SERVER_STEP*60*1000));
		
		pm.close();
	}
	
	public void updateCityResources(City city, boolean newStep)
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		CityDTO cityDTO = pm.getObjectById(CityDTO.class, city.getCityUID());

		cityDTO.setWheat(city.getWheat());
		cityDTO.setWood(city.getWood());
		cityDTO.setIron(city.getIron());
		cityDTO.setGold(city.getGold());
			
		if(newStep){
			// securite si il y a eu un pb 
			int population = city.getPopulation();
			if(population < 0)
				population = 0;
			else if(population > 12000)
				population = 12000;
			
			cityDTO.setPopulation(population);
			cityDTO.setPeopleCreatingWheat(city.getPeopleCreatingWheat() < 0 ? 0 : city.getPeopleCreatingWheat());
		}

		// securite si il y a eu un pb et data < 0
		cityDTO.setPeopleCreatingWheat(city.getPeopleCreatingWheat() < 0 ? 0 : city.getPeopleCreatingWheat());
		cityDTO.setPeopleCreatingWood(city.getPeopleCreatingWood() < 0 ? 0 : city.getPeopleCreatingWood());
		cityDTO.setPeopleCreatingIron(city.getPeopleCreatingIron() < 0 ? 0 : city.getPeopleCreatingIron());
		
		pm.close();
	}
	
	
	public void updateSmith(String smithUID, int people){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		SmithDTO smithDTO = pm.getObjectById(SmithDTO.class, smithUID);
		
		smithDTO.setPeople(people);
		pm.close();
	}
	
	public void updateEquipmentStock(String stockUID, int size){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		EquipmentDTO equipmentDTO = pm.getObjectById(EquipmentDTO.class, stockUID);
		
		equipmentDTO.setSize(size);
		pm.close();
	}
	
	public void updateStock(Stock stock){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		StockDTO stockDTO = pm.getObjectById(StockDTO.class, stock.getStockUID());
		
		stockDTO.setPeopleBuildingStock(stock.getPeopleBuildingStock());
		stockDTO.setStockBeginTime(stock.getStockBeginTime());
		stockDTO.setStockEndTime(stock.getStockEndTime());
		stockDTO.setStockCapacity(stock.getStockCapacity());
		stockDTO.setStockNextCapacity(stock.getStockNextCapacity());
		
		pm.close();
	}
	
	//==================================================================================================//

	public String createCity(City city, String playerUID){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		String cityUID = createCity(city, playerUID, pm, null);
		pm.close();
		return cityUID;
	}

	public void createUnit(Unit unit, String cityUID)
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		UnitDTO unitDTO = new UnitDTO(); 
		
		Key key = KeyFactory.createKey(UnitDTO.class.getSimpleName(), unit.getUnitUID());

		unitDTO.setKey(KeyFactory.keyToString(key));
		unitDTO.setUnitUID(unit.getUnitUID());
		unitDTO.setPlayerUID(unit.getPlayer().getUralysUID());
		unitDTO.setSize(unit.getSize());
		unitDTO.setSpeed(unit.getSpeed());
		unitDTO.setWheat(unit.getWheat());
		unitDTO.setWood(unit.getWood());
		unitDTO.setIron(unit.getIron());
		unitDTO.setGold(unit.getGold());

		unitDTO.setValue(unit.getValue());
		unitDTO.setType(unit.getType());
		
		unitDTO.setBeginTime(unit.getBeginTime());
		unitDTO.setEndTime(-1);
		
		unitDTO.setFinalCaseUIDExpected(unit.getFinalCaseUIDExpected());
		unitDTO.setGatheringUIDExpected(unit.getGatheringUIDExpected());

		//--------------------------------------//
		// init Equipment
		
		for(Equipment equipment : unit.getEquipments()){
			if(equipment.getItem().getName().equals("bow")){
				String bowsStockUID = createEquipment(unit.getUnitUID(), ITEM_UID_BOW, equipment.getSize());
				unitDTO.getEquipmentUIDs().add(bowsStockUID);
			}
			else if(equipment.getItem().getName().equals("sword")){
				String swordsStockUID = createEquipment(unit.getUnitUID(), ITEM_UID_SWORD, equipment.getSize());
				unitDTO.getEquipmentUIDs().add(swordsStockUID);
			}
			else if(equipment.getItem().getName().equals("armor")){
				String armorsStockUID = createEquipment(unit.getUnitUID(), ITEM_UID_ARMOR, equipment.getSize());
				unitDTO.getEquipmentUIDs().add(armorsStockUID);
			}
		}
		
		//--------------------------------------//

		if(cityUID != null)
		{
			CityDTO city = pm.getObjectById(CityDTO.class, cityUID);
			city.setPopulation(city.getPopulation() - unit.getSize());
		}
		
		//--------------------------------------//
		
		pm.makePersistent(unitDTO);
		pm.close();
	}

	public UnitDTO getUnit(String unitUID) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		return pm.getObjectById(UnitDTO.class, unitUID);
	}
	
	public void updateUnit(Unit unit, String cityUID)
	{
		if(debug)Utils.print("dao.updateUnit, unit.getEndTime() : " + unit.getEndTime());
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		UnitDTO unitDTO = pm.getObjectById(UnitDTO.class, unit.getUnitUID());
		
		if(cityUID != null){
			CityDTO city = pm.getObjectById(CityDTO.class, cityUID);
			city.setPopulation(city.getPopulation() - unit.getSize() + unitDTO.getSize());
		}
		
		unitDTO.setSize(unit.getSize());
		unitDTO.setValue(unit.getValue());
		unitDTO.setSpeed(unit.getSpeed());
	
		unitDTO.setWheat(unit.getWheat());
		unitDTO.setWood(unit.getWood());
		unitDTO.setIron(unit.getIron());
		unitDTO.setGold(unit.getGold());

		unitDTO.setGatheringUIDExpected(unit.getGatheringUIDExpected());
		unitDTO.setFinalCaseUIDExpected(unit.getFinalCaseUIDExpected());
		unitDTO.setBeginTime(unit.getBeginTime());
		unitDTO.setEndTime(unit.getEndTime());

		pm.close();
		
		for(Equipment equipment : unit.getEquipments()){
			updateEquipmentStock(equipment.getEquimentUID(), equipment.getSize());
		}
		
	}


	public List<String> linkNewUnitsAndGetPreviousUnitUIDs(String uralysUID, List<String> createdUnitUIDs) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, uralysUID);
		
		List<String> previousUIDList = new ArrayList<String>();
		for(String unitUID : playerDTO.getUnitUIDs()){
			previousUIDList.add(unitUID);
		}
		
		playerDTO.getUnitUIDs().addAll(createdUnitUIDs);
		pm.close();
		
		return previousUIDList;
	}
	
	public void linkNewUnit(String uralysUID, String unitUID) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, uralysUID);
		
		playerDTO.getUnitUIDs().add(unitUID);
		pm.close();
	}

	
	@SuppressWarnings("unchecked")
	public void deleteUnits(String uralysUID, List<String> toDeleteUnitUIDs){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, uralysUID);

		playerDTO.getUnitUIDs().removeAll(toDeleteUnitUIDs);

		for(String unitUID : toDeleteUnitUIDs){
			if(debug)Utils.print("delete unit : " + unitUID);
			
			UnitDTO unitDTO;
			
			try{
				unitDTO = pm.getObjectById(UnitDTO.class, unitUID);
			}
			catch(Exception e){
				// unit already deleted previously
				continue;
			}
			
			if(unitDTO.getEquipmentUIDs().size() > 0){
				Query query = pm.newQuery("select from " + EquipmentDTO.class.getName() + " where :uids.contains(key)");
				List<EquipmentDTO> equipments = (List<EquipmentDTO>) query.execute(unitDTO.getEquipmentUIDs());
				
				for(EquipmentDTO equipmentDTO : equipments){
					if(debug)Utils.print("delete equipmentDTO : " + equipmentDTO.getEquimentUID());
					pm.deletePersistent(equipmentDTO);
				}
			}

			if(unitDTO.getMoveUIDs().size() > 0){
				Query query = pm.newQuery("select from " + MoveDTO.class.getName() + " where :uids.contains(key)");
				List<MoveDTO> moves = (List<MoveDTO>) query.execute(unitDTO.getMoveUIDs());
				
				for(MoveDTO moveDTO : moves){
					deleteMove(moveDTO.getMoveUID(), false);
				}
			}
			
			pm.deletePersistent(unitDTO);
		}
		
		pm.close();
	}
	
	@SuppressWarnings("unchecked")
	public void deleteUnit(String uralysUID, String unitUID){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, uralysUID);
		
		playerDTO.getUnitUIDs().remove(unitUID);
		
		if(debug)Utils.print("delete unit : " + unitUID);
		UnitDTO unitDTO = pm.getObjectById(UnitDTO.class, unitUID);
		
		if(unitDTO.getEquipmentUIDs().size() > 0){
			Query query = pm.newQuery("select from " + EquipmentDTO.class.getName() + " where :uids.contains(key)");
			List<EquipmentDTO> equipments = (List<EquipmentDTO>) query.execute(unitDTO.getEquipmentUIDs());
			
			for(EquipmentDTO equipmentDTO : equipments){
				if(debug)Utils.print("delete equipmentDTO : " + equipmentDTO.getEquimentUID());
				pm.deletePersistent(equipmentDTO);
			}
		}
		
		if(unitDTO.getMoveUIDs().size() > 0){
			Query query = pm.newQuery("select from " + MoveDTO.class.getName() + " where :uids.contains(key)");
			List<MoveDTO> moves = (List<MoveDTO>) query.execute(unitDTO.getMoveUIDs());
			
			for(MoveDTO moveDTO : moves){
				deleteMove(moveDTO.getMoveUID(), false);
			}
		}
		
		pm.deletePersistent(unitDTO);
		pm.close();
	}
	

	public void setValueForMove(String moveUID, int value) {

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		MoveDTO moveDTO = pm.getObjectById(MoveDTO.class, moveUID);
		
		moveDTO.setValue(value);
		
		pm.close();
	}
	
	public void setTimeToForMove(String moveUID, long timeTo) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		MoveDTO moveDTO = pm.getObjectById(MoveDTO.class, moveUID);

		moveDTO.setTimeTo(timeTo);
		
		pm.close();
	}
	
	//========================================================================================//

	public void refreshCityOwner(String cityUID, String newOwnerUID, long timeToChangeOwner, int populationLost)
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		CityDTO cityDTO = pm.getObjectById(CityDTO.class, cityUID);
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, newOwnerUID);

		cityDTO.setTimeToChangeOwner(timeToChangeOwner);
		cityDTO.setNextOwnerUID(newOwnerUID);
		cityDTO.setPopulationLost(populationLost);
		
		if(!playerDTO.getCityBeingOwnedUIDs().contains(cityDTO.getCityUID()))
			playerDTO.getCityBeingOwnedUIDs().add(cityDTO.getCityUID());
		
		pm.close();
	}
	
	public void checkCityOwner(String cityUID)
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		CityDTO cityDTO = pm.getObjectById(CityDTO.class, cityUID);

		if(debug)Utils.print("cityDTO.getTimeToChangeOwner() : " + cityDTO.getTimeToChangeOwner());
		if(debug)Utils.print("now : " + new Date().getTime());
		
		if(cityDTO.getTimeToChangeOwner() != -1 && cityDTO.getTimeToChangeOwner() < new Date().getTime())
		{
			if(!cityDTO.getOwnerUID().equals(cityDTO.getNextOwnerUID()))
			{
				// changing owner !
				PlayerDTO previousOwner = pm.getObjectById(PlayerDTO.class, cityDTO.getOwnerUID());
				previousOwner.getCityUIDs().remove(cityUID);
				previousOwner.setNbCities(previousOwner.getNbCities() - 1);
				decreaseLandsCount(previousOwner, pm);
				
				PlayerDTO newOwner = pm.getObjectById(PlayerDTO.class, cityDTO.getNextOwnerUID());
				newOwner.getCityUIDs().add(cityUID);
				newOwner.getCityBeingOwnedUIDs().remove(cityUID);
				newOwner.setNbCities(newOwner.getNbCities() + 1);
				increaseLandsCount(newOwner, pm);

				cityDTO.setOwnerUID(newOwner.getUralysUID());

				CaseDTO _case = pm.getObjectById(CaseDTO.class, "case_"+cityDTO.getX()+"_"+cityDTO.getY());
				_case.setLandOwnerUID(newOwner.getUralysUID());
			}
			
			cityDTO.setPopulation(cityDTO.getPopulation() - cityDTO.getPopulationLost());
			
			cityDTO.setPopulationLost(null);
			cityDTO.setNextOwnerUID(null);
			cityDTO.setTimeToChangeOwner(-1L);
		}
		
		pm.close();
	}

	//========================================================================================//

	public void setNewGatheringForMoveAndDeletePreviousGathering(String moveUID, String gatheringUID)
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		String uid = moveUID.contains("NEW") ? moveUID.substring(4) : moveUID;
		MoveDTO moveDTO = pm.getObjectById(MoveDTO.class, uid);

		GatheringDTO previousGatheringDTO = pm.getObjectById(GatheringDTO.class, moveDTO.getGatheringUID());
		pm.deletePersistent(previousGatheringDTO);

		moveDTO.setGatheringUID(gatheringUID);
		
		pm.close();
	}
	
	public String createMove(Move move) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		MoveDTO moveDTO = new MoveDTO(); 
		
		String moveUID = move.getMoveUID().contains("NEW") ? move.getMoveUID().substring(4) : move.getMoveUID();
		Key key = KeyFactory.createKey(MoveDTO.class.getSimpleName(), moveUID);

		moveDTO.setKey(KeyFactory.keyToString(key));
		moveDTO.setMoveUID(moveUID);
		moveDTO.setCaseUID(move.getCaseUID());
		moveDTO.setTimeFrom(move.getTimeFrom());
		moveDTO.setTimeTo(move.getTimeTo());
		moveDTO.setUnitUID(move.getUnitUID());
		moveDTO.setValue(move.getValue());
		
		if(debug)Utils.print("move.getGathering().getGatheringUID() : " + move.getGathering().getGatheringUID());
		if(move.getGathering().getGatheringUID().equals("notcreatedyet")){
			GatheringDTO gatheringDTO = new GatheringDTO(); 
			
			String gatheringUID = Utils.generateUID();
			Key key2 = KeyFactory.createKey(GatheringDTO.class.getSimpleName(), gatheringUID);
			
			gatheringDTO.setKey(KeyFactory.keyToString(key2));
			gatheringDTO.setGatheringUID(gatheringUID);
			gatheringDTO.setAllyUID(move.getGathering().getAllyUID());
			gatheringDTO.setUnitUIDs(move.getGathering().getUnitUIDs());

			if(debug)Utils.print("creating gathering "+gatheringUID+" for unit " + gatheringDTO.getUnitUIDs().get(0));
			if(move.getGathering().getUnitUIDs().size() > 1)
				if(debug)Utils.print("and unit " + gatheringDTO.getUnitUIDs().get(1));
			
			pm.makePersistent(gatheringDTO);
			move.getGathering().setGatheringUID(gatheringUID);
		}

		moveDTO.setGatheringUID(move.getGathering().getGatheringUID());
		
		pm.makePersistent(moveDTO);
		
		CaseDTO _case = pm.getObjectById(CaseDTO.class, move.getCaseUID());
		_case.getMoveUIDs().add(moveUID);
		
		UnitDTO _unit = pm.getObjectById(UnitDTO.class, move.getUnitUID());
		_unit.getMoveUIDs().add(moveUID);
		
		pm.close();
		
		return moveUID;
	}
	
	public void deleteMoves(String unitUID) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		UnitDTO unitDTO = pm.getObjectById(UnitDTO.class, unitUID);
		
		for(MoveDTO moveDTO : unitDTO.getMoves()){
			deleteMove(moveDTO.getMoveUID(), false);
		}
	}

		
	public void deleteMove(String moveUID, boolean keepGatheringBecauseItIsLinkedWithAnotherMoveNow) 
	{
		try{
			if(moveUID.contains("NEW"))
				moveUID =  moveUID.substring(4);
			
			if(debug)Utils.print("delete moveDTO : " + moveUID);

			PersistenceManager pm = PMF.getInstance().getPersistenceManager();
			MoveDTO moveDTO = pm.getObjectById(MoveDTO.class, moveUID);
			CaseDTO caseDTO = pm.getObjectById(CaseDTO.class, moveDTO.getCaseUID());
			UnitDTO unitDTO = pm.getObjectById(UnitDTO.class, moveDTO.getUnitUID());
			
			caseDTO.getMoveUIDs().remove(moveUID);
			unitDTO.getMoveUIDs().remove(moveUID);
			
			if(!keepGatheringBecauseItIsLinkedWithAnotherMoveNow && moveDTO.getGatheringUID() != null)
			{
				if(debug)Utils.print("delete gatheringDTO : " + moveDTO.getGatheringUID());
				GatheringDTO gatheringDTO = pm.getObjectById(GatheringDTO.class, moveDTO.getGatheringUID());
				
				if(gatheringDTO.getUnitUIDs().size() == 1){
					pm.deletePersistent(gatheringDTO);
				}
				else
					gatheringDTO.getUnitUIDs().remove(moveDTO.getUnitUID());
			}
				
			pm.deletePersistent(moveDTO);
			pm.close();			
		}
		catch(Exception e){
			// le move n'existe pas encore
		}
	}

	public void addUnitInGatheringAndSetNewArmy(String gatheringUID, String unitUID, String newUnitUID) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		GatheringDTO gathering = pm.getObjectById(GatheringDTO.class, gatheringUID);
		
		gathering.getUnitUIDs().add(unitUID);
		gathering.setNewUnitUID(newUnitUID);
		
		pm.close();
	}

	public ConflictDTO getConflict(String conflictUID) 
	{
		if(conflictUID == null)
			return null;
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		return pm.getObjectById(ConflictDTO.class, conflictUID);
	}

	public GatheringDTO getGathering(String gatheringUID) 
	{
		if(gatheringUID == null)
			return null;
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		return pm.getObjectById(GatheringDTO.class, gatheringUID);
	}

	public String createConflict(String caseUID, String unitUID, String unitUID2) {

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ConflictDTO conflictDTO = new ConflictDTO(); 
		
		String conflictUID = Utils.generateUID();
		Key key = KeyFactory.createKey(ConflictDTO.class.getSimpleName(), conflictUID);
		
		conflictDTO.setKey(KeyFactory.keyToString(key));
		conflictDTO.setConflictUID(conflictUID);
		conflictDTO.setCaseUID(caseUID);
		
		List<String> unitUIDs = new ArrayList<String>();
		unitUIDs.add(unitUID);
		unitUIDs.add(unitUID2);
		
		conflictDTO.setUnitUIDs(unitUIDs);
		
		pm.makePersistent(conflictDTO);
		pm.close();
		
		return conflictUID;
	}

	public void changeName(String uralysUID, String newName) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, uralysUID);
		
		playerDTO.setName(newName);
		pm.close();
	}
	
	public void changeCityName(String cityUID, String newName) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		CityDTO cityDTO = pm.getObjectById(CityDTO.class, cityUID);
		
		cityDTO.setName(newName);
		pm.close();
	}

	public void resetChallenger(String caseUID)
	{
		if(debug)Utils.print("resetChallenger : caseUID : " + caseUID);
		if(caseUID == null){
			if(debug)Utils.print("resetChallenger : caseUID is NULL");
			return;
		}
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		CaseDTO _case;
		
		try{
			_case = pm.getObjectById(CaseDTO.class, caseUID);
		}
		catch (Exception e) {
			if(debug)Utils.print("la case n'existe pas : pas de challenger");
			return;
		}
		
		_case.setChallengerUID(null);
		_case.setTimeFromChallenging(-1);
		
		CityDTO city = _case.getCity();
		if(city != null){
			if(debug)Utils.print("resetChallenger : city : " + city.getCityUID());
			CityDTO _city = pm.getObjectById(CityDTO.class, city.getCityUID());
			if(debug)Utils.print("city.getNextOwnerUID() : " + city.getNextOwnerUID());
			
			if(city.getNextOwnerUID() != null){
				if(debug)Utils.print("reset the attacker cityBeingOwnedUIDs");
				PlayerDTO _attacker = pm.getObjectById(PlayerDTO.class, city.getNextOwnerUID());
				_attacker.getCityBeingOwnedUIDs().remove(city.getCityUID());
			}

			_city.setNextOwnerUID(null);
			_city.setPopulationLost(null);
			_city.setTimeToChangeOwner(-1l);
		}
		
		pm.close();
	}
	
	public CaseDTO tryToSetChallenger(Unit unit, long timeFromChallenging)
	{
		if(debug)Utils.print("tryToSetChallenger");

		int x = TribesUtils.getX(unit.getFinalCaseUIDExpected());
		int y = TribesUtils.getY(unit.getFinalCaseUIDExpected());

		CaseDTO finalCase = null;
		
		// TODO : on peut optimiser ici et ne pas faire à chaque fois les 6 getCases
		if(unit.getPlayer().getUralysUID().equals(getCase(x-1, y-1).getLandOwnerUID())
		|| unit.getPlayer().getUralysUID().equals(getCase(x-1, y+1).getLandOwnerUID())
		|| unit.getPlayer().getUralysUID().equals(getCase(x, y-2).getLandOwnerUID())
		|| unit.getPlayer().getUralysUID().equals(getCase(x, y+2).getLandOwnerUID())
		|| unit.getPlayer().getUralysUID().equals(getCase(x+1, y-1).getLandOwnerUID())
		|| unit.getPlayer().getUralysUID().equals(getCase(x+1, y+1).getLandOwnerUID()))
		{

			PersistenceManager pm = PMF.getInstance().getPersistenceManager();
			try{
				finalCase = pm.getObjectById(CaseDTO.class, "case_"+x+"_"+y);
			}
			catch(JDOObjectNotFoundException e){
				finalCase = createCase(x, y, null, Case.FOREST, null, pm);
			}
			
			// le challenger possede deja cette contree
			if(finalCase.getLandOwnerUID() != null && finalCase.getLandOwnerUID().equals(unit.getPlayer().getUralysUID()))
				return null;
			
			if(debug)Utils.print("finalCase : " + finalCase.getCaseUID());
			finalCase.setChallengerUID(unit.getPlayer().getUralysUID());
			finalCase.setTimeFromChallenging(timeFromChallenging);
			pm.close();
		}

		return finalCase;
	}
	
	//==================================================================================================//
	// PRIVATE METHODS
	
	
	private String createEquipment(String unitUID, String item, int size) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		EquipmentDTO bowsStock = new EquipmentDTO();

		String equipmentUID = unitUID+"_"+item;
		
		Key key = KeyFactory.createKey(EquipmentDTO.class.getSimpleName(), equipmentUID);

		bowsStock.setKey(KeyFactory.keyToString(key));
		bowsStock.setEquimentUID(equipmentUID);
		bowsStock.setItemUID(item);
		bowsStock.setSize(size);
		
		pm.makePersistent(bowsStock);
		pm.close();
		
		return equipmentUID;
	}
	
	
	private String createSmith(String item) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		SmithDTO smith = new SmithDTO();
		
		String smithUID = Utils.generateUID();
		
		Key key = KeyFactory.createKey(SmithDTO.class.getSimpleName(), smithUID);
		
		smith.setKey(KeyFactory.keyToString(key));
		smith.setSmithUID(smithUID);
		smith.setItemUID(item);
		smith.setPeople(0);
		
		pm.makePersistent(smith);
		pm.close();
		
		return smithUID;
	}
	
	
	private String createStock(String cityUID, String stockId, int capacity) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		StockDTO stock = new StockDTO();

		String stockUID = cityUID+"_"+stockId;
		
		Key key = KeyFactory.createKey(StockDTO.class.getSimpleName(), stockUID);
		
		stock.setKey(KeyFactory.keyToString(key));
		
		stock.setStockUID(stockUID);
		stock.setStockCapacity(capacity);
		stock.setStockNextCapacity(0);
		stock.setPeopleBuildingStock(0);
		stock.setStockBeginTime(-1);
		stock.setStockEndTime(new Date().getTime());
		
		pm.makePersistent(stock);
		pm.close();
		
		return stockUID;
	}
	
	//======================================================================================================//

	@SuppressWarnings("unchecked")
	public List<PlayerDTO> getCitiesBoard() 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query q = pm.newQuery("select from " + PlayerDTO.class.getName());
		
		q.setOrdering("nbCities descending");
		q.setRange(0,30);
		
		return (List<PlayerDTO>) q.execute();
	}
	
	@SuppressWarnings("unchecked")
	public List<PlayerDTO> getLandsBoard() 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query q = pm.newQuery("select from " + PlayerDTO.class.getName());
		
		q.setOrdering("nbLands descending");
		q.setRange(0,30);
		
		return (List<PlayerDTO>) q.execute();
	}
	
	@SuppressWarnings("unchecked")
	public List<PlayerDTO> getPopulationBoard() 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query q = pm.newQuery("select from " + PlayerDTO.class.getName());
		
		q.setOrdering("nbPopulation descending");
		q.setRange(0,30);
		
		return (List<PlayerDTO>) q.execute();
	}
	
	@SuppressWarnings("unchecked")
	public List<PlayerDTO> getArmiesBoard() 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query q = pm.newQuery("select from " + PlayerDTO.class.getName());
		
		q.setOrdering("nbArmies descending");
		q.setRange(0,30);
		
		return (List<PlayerDTO>) q.execute();
	}

	//------------------------------------------------------------------------------//
	
	public void updatePlayerProfile(String playerUID, String profile) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, playerUID);
		
		playerDTO.setProfile(profile);
		
		pm.close();
	}
	
	public void updateAllyProfile(String allyUID, String profile) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		AllyDTO allyDTO = pm.getObjectById(AllyDTO.class, allyUID);
		
		allyDTO.setProfile(profile);
		
		pm.close();
	}

	//------------------------------------------------------------------------------//
	
	public void sendMessage(String senderUID, String recipientUID, String message)
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO senderDTO = pm.getObjectById(PlayerDTO.class, senderUID);
		PlayerDTO recepientDTO = pm.getObjectById(PlayerDTO.class, recipientUID);
		
		MessageDTO messageDTO = new MessageDTO();
		
		String messageUID = Utils.generateUID();
		Key key = KeyFactory.createKey(MessageDTO.class.getSimpleName(), messageUID);
		
		messageDTO.setKey(KeyFactory.keyToString(key));
		messageDTO.setMessageUID(messageUID);
		
		messageDTO.setContent(message);
		messageDTO.setStatus(Message.UNREAD);
		
		messageDTO.setSenderUID(senderUID);
		messageDTO.setSenderName(senderDTO.getName());
		
		messageDTO.setTime(new Date().getTime());
		
		recepientDTO.getMessageUIDs().add(messageUID);
		
		pm.makePersistent(messageDTO);
		pm.close();
	}
		


	@SuppressWarnings("unchecked")
	public void markAsRead(List<String> messageUIDs) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query query = pm.newQuery("select from " + MessageDTO.class.getName() + " where :messageUIDs.contains(key)");
		List<MessageDTO> messages = (List<MessageDTO>) query.execute(messageUIDs);
		
		for(MessageDTO message : messages){
			message.setStatus(Message.READ);
		}
		
		pm.close();
	}

	@SuppressWarnings("unchecked")
	public void archiveMessages(List<String> messageUIDs) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query query = pm.newQuery("select from " + MessageDTO.class.getName() + " where :messageUIDs.contains(key)");
		List<MessageDTO> messages = (List<MessageDTO>) query.execute(messageUIDs);
		
		for(MessageDTO message : messages){
			message.setStatus(Message.ARCHIVED);
		}
		
		pm.close();
	}

	@SuppressWarnings("unchecked")
	public void deleteMessages(String uralysUID, List<String> messageUIDs) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query query = pm.newQuery("select from " + MessageDTO.class.getName() + " where :messageUIDs.contains(key)");
		List<MessageDTO> messages = (List<MessageDTO>) query.execute(messageUIDs);
		
		PlayerDTO player = pm.getObjectById(PlayerDTO.class, uralysUID);
		player.getMessageUIDs().removeAll(messageUIDs);
		
		for(MessageDTO message : messages){
			if(message.getContent().contains("____allyInvitation")){
				AllyDTO ally = pm.getObjectById(AllyDTO.class, Utils.getAllyUID(message.getContent()));
				ally.getInvitedUIDs().remove(uralysUID);
			}
		}
		
		pm.deletePersistentAll(messages);
		pm.close();
	}
	
	//-----------------------------------------------------------------------------------//

	public AllyDTO createAlly(String uralysUID, String allyName) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO player = pm.getObjectById(PlayerDTO.class, uralysUID);
		AllyDTO ally = new AllyDTO();

		String allyUID = Utils.generateUID();
		Key key = KeyFactory.createKey(AllyDTO.class.getSimpleName(), allyUID);
		
		ally.setKey(KeyFactory.keyToString(key));
		ally.setAllyUID(allyUID);
		ally.setName(allyName);
		ally.setProfile("");
		ally.getPlayerUIDs().add(uralysUID);
		
		ally.setNbArmies(player.getNbArmies());
		ally.setNbCities(player.getNbCities());
		ally.setNbPopulation(player.getNbPopulation());
		ally.setNbLands(player.getNbLands());
		
		player.setAllyUID(allyUID);
		
		pm.makePersistent(ally);
		pm.close();
		
		return ally;
	}

	public AllyDTO getAlly(String allyUID) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		return pm.getObjectById(AllyDTO.class, allyUID);
	}

	public void inviteInAlly(String uralysUID, String allyUID, String inviteInAllyMessage) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		AllyDTO allyDTO = pm.getObjectById(AllyDTO.class, allyUID);
		
		allyDTO.getInvitedUIDs().add(uralysUID);
		sendMessage(allyDTO.getPlayerUIDs().get(0), uralysUID, inviteInAllyMessage);
		
		pm.close();
	}

	public void joinAlly(String uralysUID, String allyUID) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		AllyDTO allyDTO = pm.getObjectById(AllyDTO.class, allyUID);
		PlayerDTO player = pm.getObjectById(PlayerDTO.class, uralysUID);
		
		player.setAllyUID(allyUID);
		allyDTO.getPlayerUIDs().add(uralysUID);
		
		allyDTO.setNbArmies(allyDTO.getNbArmies() + player.getNbArmies());
		allyDTO.setNbCities(allyDTO.getNbCities() + player.getNbCities());
		allyDTO.setNbPopulation(allyDTO.getNbPopulation() + player.getNbPopulation());
		allyDTO.setNbLands(allyDTO.getNbLands() + player.getNbLands());
		
		pm.close();
	}

	public void removeFromAlly(String uralysUID, String allyUID) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		AllyDTO allyDTO = pm.getObjectById(AllyDTO.class, allyUID);
		PlayerDTO player = pm.getObjectById(PlayerDTO.class, uralysUID);
		
		player.setAllyUID(uralysUID);
		
		if(allyDTO.getPlayerUIDs().size() == 1){
			pm.deletePersistent(allyDTO);
		}
		else{
			allyDTO.getPlayerUIDs().remove(uralysUID);
			
			allyDTO.setNbArmies(allyDTO.getNbArmies() - player.getNbArmies());
			allyDTO.setNbCities(allyDTO.getNbCities() - player.getNbCities());
			allyDTO.setNbPopulation(allyDTO.getNbPopulation() - player.getNbPopulation());
			allyDTO.setNbLands(allyDTO.getNbLands() - player.getNbLands());
		}
		
		
		pm.close();
	}

	public void saveAllyPlayers(String allyUID, List<String> playerUIDs) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		AllyDTO allyDTO = pm.getObjectById(AllyDTO.class, allyUID);
		
		allyDTO.setPlayerUIDs(playerUIDs);
		
		pm.close();
	}

	//-----------------------------------------------------------------------------------//
	@SuppressWarnings("unchecked")
	public List<AllyDTO> getTopAlliesByPopulation() 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query q = pm.newQuery("select from " + AllyDTO.class.getName());
		
		q.setOrdering("nbPopulation descending");
		q.setRange(0,30);
		
		return (List<AllyDTO>) q.execute();
	}

	@SuppressWarnings("unchecked")
	public List<AllyDTO> getTopAlliesByArmies() 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query q = pm.newQuery("select from " + AllyDTO.class.getName());
		
		q.setOrdering("nbArmies descending");
		q.setRange(0,30);
		
		return (List<AllyDTO>) q.execute();
	}

	@SuppressWarnings("unchecked")
	public List<AllyDTO> getTopAlliesByCities() 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query q = pm.newQuery("select from " + AllyDTO.class.getName());
		
		q.setOrdering("nbCities descending");
		q.setRange(0,30);
		
		return (List<AllyDTO>) q.execute();
	}

	@SuppressWarnings("unchecked")
	public List<AllyDTO> getTopAlliesByLands() 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query q = pm.newQuery("select from " + AllyDTO.class.getName());
		
		q.setOrdering("nbLands descending");
		q.setRange(0,30);
		
		return (List<AllyDTO>) q.execute();
	}

}
