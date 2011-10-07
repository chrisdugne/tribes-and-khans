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
import com.google.appengine.api.datastore.Text;
import com.uralys.tribes.commons.Constants;
import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.entities.Ally;
import com.uralys.tribes.entities.Cell;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Message;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Report;
import com.uralys.tribes.entities.Stock;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.dto.AllyDTO;
import com.uralys.tribes.entities.dto.CellDTO;
import com.uralys.tribes.entities.dto.CityDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.entities.dto.MessageDTO;
import com.uralys.tribes.entities.dto.MoveDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.ServerDataDTO;
import com.uralys.tribes.entities.dto.StockDTO;
import com.uralys.tribes.entities.dto.UnitDTO;
import com.uralys.tribes.utils.TribesUtils;
import com.uralys.utils.Utils;

public class GameDAO  extends MainDAO implements IGameDAO {
	
	//-----------------------------------------------------------------------//
	// local

//	private static String ITEM_UID_BOW = "_bow";
//	private static String ITEM_UID_SWORD = "_sword";
//	private static String ITEM_UID_ARMOR = "_armor";

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
		

		String cityUID = createCity(null, uralysUID, pm, serverData);
		CityDTO newCity = pm.getObjectById(CityDTO.class, cityUID);
		pm.close();
		
		createCityUnit(uralysUID, newCity);

		return uralysUID;
	}
	
	public PlayerDTO getPlayer(String uralysUID) {
		return getPlayer(uralysUID, false);
	}

	public PlayerDTO getPlayer(String uralysUID, boolean newConnection) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		try{
			PlayerDTO player = pm.getObjectById(PlayerDTO.class, uralysUID);
			
			if(newConnection)
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
		if(debug)Utils.print("createNewFirstCity");
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ServerDataDTO serverData = pm.getObjectById(ServerDataDTO.class, "serverData");
		
		String cityUID = createCity(null, playerUID, pm, serverData);
		CityDTO newCity = pm.getObjectById(CityDTO.class, cityUID);
		pm.close();
		
		createCityUnit(playerUID, newCity);
		
		return newCity;
	}
	
	private void createCityUnit(String playerUID, CityDTO newCity) 
	{
		if(debug)Utils.print("createCityUnit");
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		String unitUID = newCity.getCityUID();
		String moveUID = newCity.getBeginTime()+"_"+newCity.getX()+"_"+newCity.getY()+"_"+unitUID;
		String cellUID = "cell_"+newCity.getX()+"_"+newCity.getY();

		//--------------------------------------//
		
		MoveDTO moveDTO = new MoveDTO(); 
		Key key = KeyFactory.createKey(MoveDTO.class.getSimpleName(), moveUID);

		moveDTO.setKey(KeyFactory.keyToString(key));
		moveDTO.setMoveUID(moveUID);
		moveDTO.setCellUID(cellUID);
		moveDTO.setTimeFrom(newCity.getBeginTime());
		moveDTO.setTimeTo(-1);
		moveDTO.setUnitUID(unitUID);
		
		pm.makePersistent(moveDTO);

		//--------------------------------------//
		
		CellDTO cell = pm.getObjectById(CellDTO.class, cellUID);
		cell.getMoveUIDs().add(moveUID);

		//--------------------------------------//
		
		UnitDTO unitDTO = new UnitDTO(); 
		Key keyUnit = KeyFactory.createKey(UnitDTO.class.getSimpleName(), unitUID);

		unitDTO.setKey(KeyFactory.keyToString(keyUnit));
		unitDTO.setUnitUID(unitUID);
		unitDTO.setPlayerUID(playerUID);
		
		unitDTO.setType(Unit.CITY);
		unitDTO.setSize(newCity.getPopulation());

		unitDTO.setBeginTime(newCity.getBeginTime());
		unitDTO.setEndTime(-1);
		
		unitDTO.setCellUIDExpectedForLand(cellUID);
		unitDTO.getMoveUIDs().add(moveUID);

		//--------------------------------------//
		
		pm.makePersistent(unitDTO);
		
		pm.close();
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
		city.setPeopleCreatingWheat(city.getPopulation()/5);
		city.setPeopleCreatingWood(0);
		city.setPeopleCreatingIron(0);
//		city.setTimeToChangeOwner(-1l);
//		city.setNextOwnerUID(null);
//		city.setPopulationLost(null);

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
				
				if(getCell(cityX, cityY).getLandOwnerUID() == null)
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
		
		city.setBows(0);
		city.setSwords(0);
		city.setArmors(0);
		
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

		createOrRefreshCell(city.getX(), city.getY(), cityUID, Cell.CITY, playerUID, pm);
		
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

		//--------------------------------------//
		
		return cityUID;
	}


	/*
	 * 0-404 * 0-404 = 27*27 groups
	 * 15*15*27*27 = 405*405
	 * 
	 */
	private CellDTO createCell(int x, int y, String cityUID, int type, String landOwnerUID, PersistenceManager pm) 
	{
		CellDTO _cell = new CellDTO();

		String caseUID = "cell_"+x+"_"+y;
		int group = TribesUtils.getGroup(x,y); 
		
		Key key = KeyFactory.createKey(CellDTO.class.getSimpleName(), caseUID);

		_cell.setKey(KeyFactory.keyToString(key));
		_cell.setCellUID(caseUID);
		_cell.setX(x);
		_cell.setY(y);
		_cell.setGroupCell(group);
		_cell.setCityUID(cityUID);
		_cell.setLandOwnerUID(landOwnerUID);
		_cell.setChallengerUID(null);
		_cell.setTimeFromChallenging(-1);
		_cell.setType(type);

		pm.makePersistent(_cell);
		return _cell;
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
	public List<CellDTO> loadCells(int[] groups, boolean refreshLandOwners) 
	{
		long now = new Date().getTime();

		List<CellDTO> result = new ArrayList<CellDTO>();
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		for(int group : groups){
			Query query = pm.newQuery("select from " + CellDTO.class.getName() + " where groupCell == :group");
			Collection<? extends CellDTO> cells = (Collection<? extends CellDTO>) query.execute(group);
			
			if(refreshLandOwners){
				for(CellDTO cell : cells)
				{
					if(cell.getChallengerUID() != null)
					{
						if(now - cell.getTimeFromChallenging() > Constants.LAND_TIME*60*1000)
						{
							cell = newLandOwner(cell.getCellUID());
						}
					}
				}
			}
			
			result.addAll(cells);
		}
		
		pm.close();
		return result;
	}

	private CellDTO newLandOwner(String cellUID) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		CellDTO cell = pm.getObjectById(CellDTO.class, cellUID);
		
		if(cell.getLandOwnerUID() != null){
			PlayerDTO owner = pm.getObjectById(PlayerDTO.class, cell.getLandOwnerUID());
			decreaseLandsCount(owner, pm);
		}
		
		PlayerDTO challenger = pm.getObjectById(PlayerDTO.class, cell.getChallengerUID());
		increaseLandsCount(challenger, pm);
		
		cell.setLandOwnerUID(cell.getChallengerUID());
		cell.setChallengerUID(null);
		cell.setTimeFromChallenging(-1);
		
		pm.close();
		
		return cell;
	}
	
	//==================================================================================================//

	public CellDTO getCell(String cellUID) {
		return getCell(TribesUtils.getX(cellUID), TribesUtils.getY(cellUID));
	}

		
	public CellDTO getCell(int i, int j) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		try{
			CellDTO cell =  pm.getObjectById(CellDTO.class, "cell_"+i+"_"+j);
			long now = new Date().getTime();
			
			if(cell.getChallengerUID() != null)
			{
				if(now - cell.getTimeFromChallenging() > Constants.LAND_TIME*60*1000)
				{
					cell = newLandOwner(cell.getCellUID());
				}
			}
			
			return cell;
		}
		catch(JDOObjectNotFoundException e){
			return createCell(i, j, null, Cell.FOREST, null, pm);
		}
	}

	private void createOrRefreshCell(int x, int y, String cityUID, int type, String landOwnerUID, PersistenceManager pm) 
	{
		try{
			CellDTO cellDTO = pm.getObjectById(CellDTO.class, "cell_"+x+"_"+y);
			cellDTO.setLandOwnerUID(landOwnerUID);
			cellDTO.setType(type);
			cellDTO.setCityUID(cityUID);
		}
		catch(JDOObjectNotFoundException e){
			createCell(x, y, cityUID, type, landOwnerUID, pm);
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

		cityDTO.setBows(city.getBows());
		cityDTO.setArmors(city.getArmors());
		cityDTO.setSwords(city.getSwords());
			
		if(newStep){
			// securite si il y a eu un pb 
			int population = city.getPopulation();
			if(population < 0)
				population = 0;
			else if(population > 12000)
				population = 12000;
			
			cityDTO.setPopulation(population);
			cityDTO.setPeopleCreatingWheat(city.getPeopleCreatingWheat() < 0 ? 0 : city.getPeopleCreatingWheat());

			UnitDTO unitDTO = pm.getObjectById(UnitDTO.class, city.getCityUID());
			unitDTO.setSize(population);
		}

		// securite si il y a eu un pb et data < 0
		cityDTO.setPeopleCreatingWheat(city.getPeopleCreatingWheat() < 0 ? 0 : city.getPeopleCreatingWheat());
		cityDTO.setPeopleCreatingWood(city.getPeopleCreatingWood() < 0 ? 0 : city.getPeopleCreatingWood());
		cityDTO.setPeopleCreatingIron(city.getPeopleCreatingIron() < 0 ? 0 : city.getPeopleCreatingIron());
		
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
		
		stockDTO.setSmiths(stock.getSmiths());
		stockDTO.setItemsBeingBuilt(stock.getItemsBeingBuilt());
		stockDTO.setItemsBeingBuiltBeginTime(stock.getItemsBeingBuiltBeginTime());
		stockDTO.setItemsBeingBuiltEndTime(stock.getItemsBeingBuiltEndTime());
		
		pm.close();
	}
	
	//==================================================================================================//

	public String createCity(City city, String playerUID)
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		String cityUID = createCity(city, playerUID, pm, null);
		pm.close();
		return cityUID;
	}

	public void createUnit(Unit unit, PersistenceManager pm)
	{
		boolean localPm = pm == null;
		
		if(localPm){
			pm = PMF.getInstance().getPersistenceManager();
		}
		
		UnitDTO unitDTO = new UnitDTO(); 
		
		Key key = KeyFactory.createKey(UnitDTO.class.getSimpleName(), unit.getUnitUID());

		unitDTO.setKey(KeyFactory.keyToString(key));
		unitDTO.setUnitUID(unit.getUnitUID());
		unitDTO.setPlayerUID(unit.getPlayer().getUralysUID());
		
		unitDTO.setType(unit.getType());
		unitDTO.setSize(unit.getSize());
		unitDTO.setSpeed(unit.getSpeed());
		
		unitDTO.setWheat(unit.getWheat());
		unitDTO.setWood(unit.getWood());
		unitDTO.setIron(unit.getIron());
		unitDTO.setGold(unit.getGold());

		unitDTO.setBows(unit.getBows());
		unitDTO.setSwords(unit.getSwords());
		unitDTO.setArmors(unit.getArmors());

		unitDTO.setBeginTime(unit.getBeginTime());
		unitDTO.setEndTime(-1);
		unitDTO.setLastShotTime(unit.getBeginTime());
		
		unitDTO.setCellUIDExpectedForLand(unit.getCellUIDExpectedForLand());
		unitDTO.setUnitMetUID(unit.getUnitMetUID());

		//--------------------------------------//
		
		pm.makePersistent(unitDTO);
		
		if(localPm)
			pm.close();
	}

	public UnitDTO getUnit(String unitUID) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		return pm.getObjectById(UnitDTO.class, unitUID);
	}
	
	public void updateUnit(Unit unit)
	{
		if(debug)Utils.print("dao.updateUnit, unit.getEndTime() : " + unit.getEndTime());
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		UnitDTO unitDTO = pm.getObjectById(UnitDTO.class, unit.getUnitUID());
		
		unitDTO.setSize(unit.getSize());
		unitDTO.setSpeed(unit.getSpeed());
	
		unitDTO.setWheat(unit.getWheat());
		unitDTO.setWood(unit.getWood());
		unitDTO.setIron(unit.getIron());
		unitDTO.setGold(unit.getGold());

		unitDTO.setBows(unit.getBows());
		unitDTO.setSwords(unit.getSwords());
		unitDTO.setArmors(unit.getArmors());

		unitDTO.setCellUIDExpectedForLand(unit.getCellUIDExpectedForLand());
		unitDTO.setUnitMetUID(unit.getUnitMetUID());
		unitDTO.setUnitNextUID(unit.getUnitNextUID());
		unitDTO.setMessageUID(unit.getMessageUID());
		unitDTO.setBeginTime(unit.getBeginTime());
		unitDTO.setEndTime(unit.getEndTime());
		unitDTO.setLastShotTime(unit.getLastShotTime());

		pm.close();
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
			
			if(unitDTO.getMoveUIDs().size() > 0){
				Query query = pm.newQuery("select from " + MoveDTO.class.getName() + " where :uids.contains(key)");
				List<MoveDTO> moves = (List<MoveDTO>) query.execute(unitDTO.getMoveUIDs());
				
				for(MoveDTO moveDTO : moves){
					deleteMove(moveDTO.getMoveUID());
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
		
		if(unitDTO.getMoveUIDs().size() > 0){
			Query query = pm.newQuery("select from " + MoveDTO.class.getName() + " where :uids.contains(key)");
			List<MoveDTO> moves = (List<MoveDTO>) query.execute(unitDTO.getMoveUIDs());
			
			for(MoveDTO moveDTO : moves){
				deleteMove(moveDTO.getMoveUID());
			}
		}
		
		pm.deletePersistent(unitDTO);
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

	public void cityIsTaken(String cityUID, String newOwnerUID, long timeToChangeOwner, int populationLost)
	{
		if(debug)Utils.print("---------------------------");
		if(debug)Utils.print("cityIsTaken");
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, newOwnerUID);
		CityDTO cityDTO = pm.getObjectById(CityDTO.class, cityUID);

		String cityBeingOwnData = cityUID + "_" + timeToChangeOwner + "_" + populationLost;
		if(debug)Utils.print("cityIsTaken : cityBeingOwnData : " + cityBeingOwnData);
		playerDTO.getCityBeingOwnedUIDs().add(cityBeingOwnData);
		cityDTO.getNextOwnerUIDs().add(newOwnerUID);
		
		pm.close();
	}

	public void removeCityBeingOwned(String uralysUID, String cityUID)
	{
		if(debug)Utils.print("---------------------------");
		if(debug)Utils.print("removeCityBeingOwned | uralysUID : " + uralysUID + " | cityUID : " + cityUID);
	
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO player = pm.getObjectById(PlayerDTO.class, uralysUID);
		CityDTO cityDTO = pm.getObjectById(CityDTO.class, cityUID);
		
		int indexToRemove = 0;
		for(String cityBeingOwned : player.getCityBeingOwnedUIDs()){
			if(cityBeingOwned.startsWith(cityUID))
				break;
			
			indexToRemove++;
		}
		
		if(debug)Utils.print("indexToRemove : " + indexToRemove);
		player.getCityBeingOwnedUIDs().remove(indexToRemove);

		cityDTO.getNextOwnerUIDs().remove(uralysUID);
		
		pm.close();
	}
	
	public void changeCityOwner(String cityUID, String newOwnerUID, int populationLost, PersistenceManager pm)
	{
		if(debug)Utils.print("-------------------------------------------------");
		if(debug)Utils.print("dao.changeCityOwner | cityUID : " + cityUID);
		
		CityDTO cityDTO = pm.getObjectById(CityDTO.class, cityUID);
		
		PlayerDTO previousOwner = pm.getObjectById(PlayerDTO.class, cityDTO.getOwnerUID());
		previousOwner.getCityUIDs().remove(cityUID);
		previousOwner.setNbCities(previousOwner.getNbCities() - 1);
		previousOwner.setNbPopulation(previousOwner.getNbPopulation() - cityDTO.getPopulation());
		decreaseLandsCount(previousOwner, pm);

		PlayerDTO newOwner = pm.getObjectById(PlayerDTO.class, newOwnerUID);
		newOwner.getCityUIDs().add(cityUID);
		newOwner.getCityBeingOwnedUIDs().remove(cityUID);
		newOwner.setNbCities(newOwner.getNbCities() + 1);
		increaseLandsCount(newOwner, pm);
		
		cityDTO.setPopulation(cityDTO.getPopulation() - populationLost);
		cityDTO.setOwnerUID(newOwnerUID);
		cityDTO.getNextOwnerUIDs().remove(newOwnerUID);
		
		CellDTO cell = pm.getObjectById(CellDTO.class, "cell_"+cityDTO.getX()+"_"+cityDTO.getY());
		cell.setLandOwnerUID(newOwner.getUralysUID());
	}
	
	//========================================================================================//

	public String createMove(Move move, String nextMoveUID, PersistenceManager pm) 
	{
		boolean localPm = pm == null;
		if(debug)Utils.print("createMove | localPm : " + localPm);
		
		
		if(localPm){
			pm = PMF.getInstance().getPersistenceManager();
		}
		
		MoveDTO moveDTO = new MoveDTO(); 
		
		String moveUID = move.getMoveUID().contains("NEW") ? move.getMoveUID().substring(4) : move.getMoveUID();
		
		if(nextMoveUID != null)
			nextMoveUID = nextMoveUID.contains("NEW") ? nextMoveUID.substring(4) : nextMoveUID;
	
		Key key = KeyFactory.createKey(MoveDTO.class.getSimpleName(), moveUID);

		moveDTO.setKey(KeyFactory.keyToString(key));
		moveDTO.setMoveUID(moveUID);
		moveDTO.setNextMoveUID(nextMoveUID);
		moveDTO.setCellUID(move.getCellUID());
		moveDTO.setTimeFrom(move.getTimeFrom());
		moveDTO.setTimeTo(move.getTimeTo());
		moveDTO.setUnitUID(move.getUnitUID());
		
		pm.makePersistent(moveDTO);
		

		CellDTO cell = pm.getObjectById(CellDTO.class, move.getCellUID());
		cell.getMoveUIDs().add(moveUID);
		if(debug)Utils.print("link to cell | "+cell.getCellUID()+" : " + cell.getMoveUIDs().size());
		
		UnitDTO unit = pm.getObjectById(UnitDTO.class, move.getUnitUID());
		unit.getMoveUIDs().add(moveUID);
		if(debug)Utils.print("link to unit | "+unit.getUnitUID()+" : " + unit.getMoveUIDs().size());
		
		if(localPm)
			pm.close();
		
		return moveUID;
	}
	
	public void deleteMoves(String unitUID) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		UnitDTO unitDTO = pm.getObjectById(UnitDTO.class, unitUID);
		
		for(MoveDTO moveDTO : unitDTO.getMoves()){
			deleteMove(moveDTO.getMoveUID());
		}
	}

		
	public void deleteMove(String moveUID) 
	{
		try{
			if(moveUID.contains("NEW"))
				moveUID =  moveUID.substring(4);
			
			if(debug)Utils.print("delete moveDTO : " + moveUID);

			PersistenceManager pm = PMF.getInstance().getPersistenceManager();
			MoveDTO moveDTO = pm.getObjectById(MoveDTO.class, moveUID);
			CellDTO caseDTO = pm.getObjectById(CellDTO.class, moveDTO.getCellUID());
			UnitDTO unitDTO = pm.getObjectById(UnitDTO.class, moveDTO.getUnitUID());
			
			caseDTO.getMoveUIDs().remove(moveUID);
			unitDTO.getMoveUIDs().remove(moveUID);
			
			pm.deletePersistent(moveDTO);
			pm.close();			
		}
		catch(Exception e){
			// le move n'existe pas encore
		}
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

//	public void resetChallenger(String caseUID)
//	{
//		if(debug)Utils.print("resetChallenger : caseUID : " + caseUID);
//		if(caseUID == null){
//			if(debug)Utils.print("resetChallenger : caseUID is NULL");
//			return;
//		}
//		
//		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
//		CellDTO _case;
//		
//		try{
//			_case = pm.getObjectById(CellDTO.class, caseUID);
//		}
//		catch (Exception e) {
//			if(debug)Utils.print("la case n'existe pas : pas de challenger");
//			return;
//		}
//		
//		_case.setChallengerUID(null);
//		_case.setTimeFromChallenging(-1);
//		
//		CityDTO city = _case.getCity();
//		if(city != null){
//			if(debug)Utils.print("resetChallenger : city : " + city.getCityUID());
//			CityDTO _city = pm.getObjectById(CityDTO.class, city.getCityUID());
//			if(debug)Utils.print("city.getNextOwnerUID() : " + city.getNextOwnerUID());
//			
//			if(city.getNextOwnerUID() != null){
//				if(debug)Utils.print("reset the attacker cityBeingOwnedUIDs");
//				PlayerDTO _attacker = pm.getObjectById(PlayerDTO.class, city.getNextOwnerUID());
//				_attacker.getCityBeingOwnedUIDs().remove(city.getCityUID());
//			}
//
//			_city.setNextOwnerUID(null);
//			_city.setPopulationLost(null);
//			_city.setTimeToChangeOwner(-1l);
//		}
//		
//		pm.close();
//	}
	
	public void tryToSetChallenger(Unit unit, long timeFromChallenging)
	{
		if(debug)Utils.print("tryToSetChallenger");

		int x = TribesUtils.getX(unit.getCellUIDExpectedForLand());
		int y = TribesUtils.getY(unit.getCellUIDExpectedForLand());

		CellDTO finalCase = null;
		
		// TODO : on peut optimiser ici et ne pas faire à chaque fois les 6 getCases
		if(unit.getPlayer().getUralysUID().equals(getCell(x-1, y-1).getLandOwnerUID())
		|| unit.getPlayer().getUralysUID().equals(getCell(x-1, y+1).getLandOwnerUID())
		|| unit.getPlayer().getUralysUID().equals(getCell(x, y-2).getLandOwnerUID())
		|| unit.getPlayer().getUralysUID().equals(getCell(x, y+2).getLandOwnerUID())
		|| unit.getPlayer().getUralysUID().equals(getCell(x+1, y-1).getLandOwnerUID())
		|| unit.getPlayer().getUralysUID().equals(getCell(x+1, y+1).getLandOwnerUID()))
		{

			PersistenceManager pm = PMF.getInstance().getPersistenceManager();
			try{
				finalCase = pm.getObjectById(CellDTO.class, "cell_"+x+"_"+y);
			}
			catch(JDOObjectNotFoundException e){
				finalCase = createCell(x, y, null, Cell.FOREST, null, pm);
			}
			
			// le challenger possede deja cette contree
			if(finalCase.getLandOwnerUID() != null && finalCase.getLandOwnerUID().equals(unit.getPlayer().getUralysUID()))
				return;
			
			if(debug)Utils.print("finalCase : " + finalCase.getCellUID());
			finalCase.setChallengerUID(unit.getPlayer().getUralysUID());
			finalCase.setTimeFromChallenging(timeFromChallenging);
			pm.close();
		}

	}
	
	//==================================================================================================//
	// PRIVATE METHODS
	
	private String createStock(String cityUID, String stockId, Integer capacity) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		StockDTO stock = new StockDTO();

		String stockUID = cityUID+"_"+stockId;
		
		Key key = KeyFactory.createKey(StockDTO.class.getSimpleName(), stockUID);
		
		stock.setKey(KeyFactory.keyToString(key));
		
		stock.setStockUID(stockUID);
		stock.setStockCapacity(capacity);
		stock.setStockNextCapacity(0);
		stock.setPeopleBuildingStock(0);
		stock.setStockBeginTime(-1l);
		stock.setStockEndTime(new Date().getTime());
		
		stock.setSmiths(0);
		stock.setItemsBeingBuilt(0);
		stock.setItemsBeingBuiltBeginTime(-1l);
		stock.setItemsBeingBuiltEndTime(new Date().getTime());
		
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
	
	public String sendReport(Report report, String recipientUID, long time)
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO recepientDTO = pm.getObjectById(PlayerDTO.class, recipientUID);

		MessageDTO messageDTO = new MessageDTO();
		
		String messageUID = Utils.generateUID();
		Key key = KeyFactory.createKey(MessageDTO.class.getSimpleName(), messageUID);
		
		messageDTO.setKey(KeyFactory.keyToString(key));
		messageDTO.setMessageUID(messageUID);
		
		messageDTO.setStatus(Message.UNREAD);
		
		messageDTO.setSenderUID("uralys");
		messageDTO.setSenderName("Report");
		
		messageDTO.setTime(time);
		messageDTO.setReportCellUID(report.getCellUID());
		
		messageDTO.setReport(true);
		messageDTO.setReportType(report.getReportType());
		
		messageDTO.setUnit1_unitUID(report.getUnit1().getUnitUID());
		messageDTO.setUnit1_ownerUID(report.getUnit1().getOwnerUID());
		messageDTO.setUnit1_ownerName(report.getUnit1().getOwnerName());
		messageDTO.setUnit1_size(report.getUnit1().getSize());
		messageDTO.setUnit1_type(report.getUnit1().getType());
		messageDTO.setUnit1_bows(report.getUnit1().getBows());
		messageDTO.setUnit1_swords(report.getUnit1().getSwords());
		messageDTO.setUnit1_armors(report.getUnit1().getArmors());
		messageDTO.setUnit1_value(report.getUnit1().getValue());

		messageDTO.setUnit1_wheat(report.getUnit1().getWheat());
		messageDTO.setUnit1_wood(report.getUnit1().getWood());
		messageDTO.setUnit1_iron(report.getUnit1().getIron());
		messageDTO.setUnit1_gold(report.getUnit1().getGold());
		
		messageDTO.setUnit1_attackACity(report.getUnit1().isDefendACity());
		messageDTO.setUnit1_defendACity(report.getUnit1().isAttackACity());


		messageDTO.setUnit2_unitUID(report.getUnit2().getUnitUID());
		messageDTO.setUnit2_ownerUID(report.getUnit2().getOwnerUID());
		messageDTO.setUnit2_ownerName(report.getUnit2().getOwnerName());
		messageDTO.setUnit2_size(report.getUnit2().getSize());
		messageDTO.setUnit2_type(report.getUnit2().getType());
		messageDTO.setUnit2_bows(report.getUnit2().getBows());
		messageDTO.setUnit2_swords(report.getUnit2().getSwords());
		messageDTO.setUnit2_armors(report.getUnit2().getArmors());
		messageDTO.setUnit2_value(report.getUnit2().getValue());

		messageDTO.setUnit2_wheat(report.getUnit2().getWheat());
		messageDTO.setUnit2_wood(report.getUnit2().getWood());
		messageDTO.setUnit2_iron(report.getUnit2().getIron());
		messageDTO.setUnit2_gold(report.getUnit2().getGold());
		
		messageDTO.setNextUnit_unitUID(report.getNextUnit().getUnitUID());
		messageDTO.setNextUnit_ownerUID(report.getNextUnit().getOwnerUID());
		messageDTO.setNextUnit_ownerName(report.getNextUnit().getOwnerName());
		messageDTO.setNextUnit_size(report.getNextUnit().getSize());
		messageDTO.setNextUnit_type(report.getUnit1().getType());
		messageDTO.setNextUnit_bows(report.getNextUnit().getBows());
		messageDTO.setNextUnit_swords(report.getNextUnit().getSwords());
		messageDTO.setNextUnit_armors(report.getNextUnit().getArmors());
		messageDTO.setNextUnit_value(report.getNextUnit().getValue());
		
		messageDTO.setNextUnit_wheat(report.getNextUnit().getWheat());
		messageDTO.setNextUnit_wood(report.getNextUnit().getWood());
		messageDTO.setNextUnit_iron(report.getNextUnit().getIron());
		messageDTO.setNextUnit_gold(report.getNextUnit().getGold());
		
		recepientDTO.getMessageUIDs().add(messageUID);
		
		pm.makePersistent(messageDTO);
		pm.close();
		
		return messageUID;
	}
	
	
	public String sendMessage(String senderUID, String recipientUID, String message)
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO recepientDTO = pm.getObjectById(PlayerDTO.class, recipientUID);

		PlayerDTO senderDTO = pm.getObjectById(PlayerDTO.class, senderUID);
		String senderName = senderDTO.getName();
		
		MessageDTO messageDTO = new MessageDTO();
		
		String messageUID = Utils.generateUID();
		Key key = KeyFactory.createKey(MessageDTO.class.getSimpleName(), messageUID);
		
		messageDTO.setKey(KeyFactory.keyToString(key));
		messageDTO.setMessageUID(messageUID);

		messageDTO.setReport(false);
		messageDTO.setContent(new Text(message));
		messageDTO.setStatus(Message.UNREAD);
		
		messageDTO.setSenderUID(senderUID);
		messageDTO.setSenderName(senderName);
		
		messageDTO.setTime(new Date().getTime());
		
		recepientDTO.getMessageUIDs().add(messageUID);
		
		pm.makePersistent(messageDTO);
		pm.close();
		
		return messageUID;
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
			if(!message.isReport() && message.getContent().getValue().toString().contains("____allyInvitation")){
				AllyDTO ally = pm.getObjectById(AllyDTO.class, Utils.getAllyUID(message.getContent().toString()));
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
