package com.uralys.tribes.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.uralys.tribes.commons.Constants;
import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Equipment;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.dto.CaseDTO;
import com.uralys.tribes.entities.dto.CityDTO;
import com.uralys.tribes.entities.dto.ConflictDTO;
import com.uralys.tribes.entities.dto.EquipmentDTO;
import com.uralys.tribes.entities.dto.GatheringDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.entities.dto.MoveDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.ServerDataDTO;
import com.uralys.tribes.entities.dto.SmithDTO;
import com.uralys.tribes.entities.dto.UnitDTO;
import com.uralys.utils.Utils;

public class GameDAO  extends MainDAO implements IGameDAO {
	
	//-----------------------------------------------------------------------//
	// local

	private static String ITEM_UID_BOW = "_bow";
	private static String ITEM_UID_SWORD = "_sword";
	private static String ITEM_UID_ARMOR = "_armor";
	private static boolean debug = true; 

	private static final Logger log = Logger.getLogger(GameDAO.class.getName());
	
	//-----------------------------------------------------------------------//


	public String createPlayer(String uralysUID, String email) {

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ServerDataDTO serverData = pm.getObjectById(ServerDataDTO.class, "serverData");
		serverData.setNbPlayers(serverData.getNbPlayers()+1);

		PlayerDTO playerDTO =  new PlayerDTO();
		
		Key key = KeyFactory.createKey(PlayerDTO.class.getSimpleName(), uralysUID);
		
		playerDTO.setKey(KeyFactory.keyToString(key));
		playerDTO.setUralysUID(uralysUID);
		playerDTO.setName("New Player");
		playerDTO.setAllyUID(uralysUID);
		playerDTO.setNbLands(7);
		
		long now = new Date().getTime();
		long timeSpentMillis = now - Constants.SERVER_START;
		
		playerDTO.setLastStep(timeSpentMillis/(Constants.SERVER_STEP*60*1000));
		
		persist(playerDTO);
		
		createCity(null, uralysUID, pm, serverData.getNbPlayers());
		
		pm.close();

		return uralysUID;
	}
	
	public PlayerDTO getPlayer(String uralysUID) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		try{
			return pm.getObjectById(PlayerDTO.class, uralysUID);
		}
		catch(Exception e){
			// no Player !!
			return null;
		}
	}

	
	//-----------------------------------------------------------------------//

	private void createCity(City cityFromFlex, String playerUID, PersistenceManager pm, int numplayer) {
		PlayerDTO player = pm.getObjectById(PlayerDTO.class, playerUID);
		
		CityDTO city = new CityDTO();

		String cityUID = Utils.generateUID();

		Key key = KeyFactory.createKey(CityDTO.class.getSimpleName(), cityUID);

		city.setKey(KeyFactory.keyToString(key));
		city.setCityUID(cityUID);
		city.setName(cityFromFlex == null ? "Ville de " + player.getName() : "Nouvelle Ville");
		city.setPopulation(cityFromFlex == null ? 1000 : cityFromFlex.getPopulation());
		city.setWheat(cityFromFlex == null ? 400 : cityFromFlex.getWheat());
		city.setWood(cityFromFlex == null ? 200 : cityFromFlex.getWood());
		city.setIron(cityFromFlex == null ? 200 : cityFromFlex.getIron());
		city.setGold(cityFromFlex == null ? 100 : cityFromFlex.getGold());
		city.setPeopleCreatingWheat(0);
		city.setPeopleCreatingWood(0);
		city.setPeopleCreatingIron(0);

		city.setX(cityFromFlex == null ? getInitialCityX(numplayer) : cityFromFlex.getX());
		city.setY(cityFromFlex == null ? getInitialCityY(numplayer) : cityFromFlex.getY());
		
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
		
		pm.makePersistent(city);
		
		//--------------------------------------//
		// creation des cases autour de la ville

		createCase(city.getX(), city.getY(), cityUID, Case.CITY, playerUID, pm);
		createCase(city.getX()-1, city.getY()-1, null, Case.FOREST, playerUID, pm);
		createCase(city.getX()-1, city.getY()+1, null, Case.FOREST, playerUID, pm);
		createCase(city.getX(), city.getY()-2, null, Case.FOREST, playerUID, pm);
		createCase(city.getX(), city.getY()+2, null, Case.FOREST, playerUID, pm);
		createCase(city.getX()+1, city.getY()-1, null, Case.FOREST, playerUID, pm);
		createCase(city.getX()+1, city.getY()+1, null, Case.FOREST, playerUID, pm);
		
		//--------------------------------------//

		player.getCityUIDs().add(cityUID);
	}

	private CaseDTO createCase(int x, int y, String cityUID, int type, String landOwnerUID, PersistenceManager pm) {
		
		CaseDTO _case = new CaseDTO();

		String caseUID = "case_"+x+"_"+y;
		Key key = KeyFactory.createKey(CaseDTO.class.getSimpleName(), caseUID);

		_case.setKey(KeyFactory.keyToString(key));
		_case.setCaseUID(caseUID);
		_case.setX(x);
		_case.setY(y);
		_case.setCityUID(cityUID);
		_case.setLandOwnerUID(landOwnerUID);
		_case.setType(type);

		pm.makePersistent(_case);
		return _case;
	}

	private int getInitialCityX(int numplayer) {
		return 200 + 10*numplayer;
	}

	private int getInitialCityY(int numplayer) {
		return 200 + 10*numplayer;
	}
	
	//==================================================================================================//
	
	public List<ItemDTO> loadItems() {
		return UniversalDAO.getInstance().getListDTO(ItemDTO.class, 1, 100);
	}

	public List<CaseDTO> loadCases(List<String> caseUIDs) {
		return UniversalDAO.getInstance().getListDTO(caseUIDs, CaseDTO.class);
	}

	public CaseDTO getCase(int i, int j) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		try{
			return pm.getObjectById(CaseDTO.class, "case_"+i+"_"+j);
		}
		catch(JDOObjectNotFoundException e){
			return createCase(i, j, null, Case.FOREST, null, pm);
		}
	}

	//==================================================================================================//
	
	public void updatePlayer(Player player){

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, player.getUralysUID());
	
		long now = new Date().getTime();
		long timeSpentMillis = now - Constants.SERVER_START;
		
		playerDTO.setLastStep(timeSpentMillis/(Constants.SERVER_STEP*60*1000));
		playerDTO.setNbLands(player.getNbLands());

		pm.close();
	}
	
	public void updateCityResources(City city){
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		CityDTO cityDTO = pm.getObjectById(CityDTO.class, city.getCityUID());
		
		cityDTO.setName(city.getName());
		cityDTO.setWheat(city.getWheat());
		cityDTO.setWood(city.getWood());
		cityDTO.setIron(city.getIron());
		cityDTO.setGold(city.getGold());
		
		cityDTO.setPeopleCreatingWheat(city.getPeopleCreatingWheat());
		cityDTO.setPeopleCreatingWood(city.getPeopleCreatingWood());
		cityDTO.setPeopleCreatingIron(city.getPeopleCreatingIron());
		
		cityDTO.setPopulation(city.getPopulation());
		
		pm.close();
	}
	
	
	public void updateSmith(String smithUID, int people){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		SmithDTO smithDTO = pm.getObjectById(SmithDTO.class, smithUID);
		
		smithDTO.setPeople(people);
		pm.close();
	}
	
	public void updateStock(String stockUID, int size){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		EquipmentDTO equipmentDTO = pm.getObjectById(EquipmentDTO.class, stockUID);
		
		equipmentDTO.setSize(size);
		pm.close();
	}
	
	//==================================================================================================//

	public void createCity(City city, String playerUID){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		createCity(city, playerUID, pm, -1);
		pm.close();
	}

	public void createUnit(Unit unit, String cityUID){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		UnitDTO unitDTO = new UnitDTO(); 
		
		Key key = KeyFactory.createKey(UnitDTO.class.getSimpleName(), unit.getUnitUID());

		unitDTO.setKey(KeyFactory.keyToString(key));
		unitDTO.setUnitUID(unit.getUnitUID());
		unitDTO.setPlayerUID(unit.getPlayerUID());
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
	
	public void updateUnit(Unit unit, String cityUID){
		
		System.out.println("dao.updateUnit, unit.getEndTime() : " + unit.getEndTime());
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
		unitDTO.setConflictUIDExpected(unit.getConflictUIDExpected());
		unitDTO.setBeginTime(unit.getBeginTime());
		unitDTO.setEndTime(unit.getEndTime());

		pm.close();
		
		for(Equipment equipment : unit.getEquipments()){
			updateStock(equipment.getEquimentUID(), equipment.getSize());
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
	public void deleteUnits(List<String> toDeleteUnitUIDs, String uralysUID){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, uralysUID);

		playerDTO.getUnitUIDs().removeAll(toDeleteUnitUIDs);

		for(String unitUID : toDeleteUnitUIDs){
			if(debug)log.info("delete unit : " + unitUID);
			UnitDTO unitDTO = pm.getObjectById(UnitDTO.class, unitUID);
			
			if(unitDTO.getEquipmentUIDs().size() > 0){
				Query query = pm.newQuery("select from " + EquipmentDTO.class.getName() + " where :uids.contains(key)");
				List<EquipmentDTO> equipments = (List<EquipmentDTO>) query.execute(unitDTO.getEquipmentUIDs());
				
				for(EquipmentDTO equipmentDTO : equipments){
					if(debug)log.info("delete equipmentDTO : " + equipmentDTO.getEquimentUID());
					pm.deletePersistent(equipmentDTO);
				}
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
		
		if(debug)log.info("delete unit : " + unitUID);
		UnitDTO unitDTO = pm.getObjectById(UnitDTO.class, unitUID);
		
		if(unitDTO.getEquipmentUIDs().size() > 0){
			Query query = pm.newQuery("select from " + EquipmentDTO.class.getName() + " where :uids.contains(key)");
			List<EquipmentDTO> equipments = (List<EquipmentDTO>) query.execute(unitDTO.getEquipmentUIDs());
			
			for(EquipmentDTO equipmentDTO : equipments){
				if(debug)log.info("delete equipmentDTO : " + equipmentDTO.getEquimentUID());
				pm.deletePersistent(equipmentDTO);
			}
		}
		
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
	

	public void unvalidateMove(Move move) {

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		MoveDTO moveDTO = pm.getObjectById(MoveDTO.class, move.getMoveUID());
		
		moveDTO.setValue(0);
		
		pm.close();
	}
	
	public void setTimeToForMove(String moveUID, long timeTo) {
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		MoveDTO moveDTO = pm.getObjectById(MoveDTO.class, moveUID);

		moveDTO.setTimeTo(timeTo);
		
		pm.close();
	}

	public String createMove(Move move, boolean requireLinks) {
		
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
		
		if(debug)log.info("move.getGathering().getGatheringUID() ; " + move.getGathering().getGatheringUID());
		if(move.getGathering().getGatheringUID().equals("notcreatedyet")){
			GatheringDTO gatheringDTO = new GatheringDTO(); 
			
			String gatheringUID = Utils.generateUID();
			Key key2 = KeyFactory.createKey(GatheringDTO.class.getSimpleName(), gatheringUID);
			
			gatheringDTO.setKey(KeyFactory.keyToString(key2));
			gatheringDTO.setGatheringUID(gatheringUID);
			gatheringDTO.setAllyUID(move.getGathering().getAllyUID());
			gatheringDTO.setUnitUIDs(move.getGathering().getUnitUIDs());
			
			pm.makePersistent(gatheringDTO);
			move.getGathering().setGatheringUID(gatheringUID);
		}

		moveDTO.setGatheringUID(move.getGathering().getGatheringUID());
		
		pm.makePersistent(moveDTO);
		
		if(requireLinks){
			CaseDTO _case = pm.getObjectById(CaseDTO.class, move.getCaseUID());
			_case.getMoveUIDs().add(moveUID);
			
			UnitDTO _unit = pm.getObjectById(UnitDTO.class, move.getUnitUID());
			_unit.getMoveUIDs().add(moveUID);
		}
		
		pm.close();
		
		return moveUID;
	}
	
	public void deleteMoves(String unitUID) {

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		UnitDTO unitDTO = pm.getObjectById(UnitDTO.class, unitUID);
		
		for(MoveDTO moveDTO : unitDTO.getMoves()){
			deleteMove(moveDTO.getMoveUID());
		}
	}

		
	public void deleteMove(String moveUID) {
		try{

			if(moveUID.contains("NEW"))
				moveUID =  moveUID.substring(4);
			
			if(debug)log.info("delete moveDTO : " + moveUID);

			PersistenceManager pm = PMF.getInstance().getPersistenceManager();
			MoveDTO moveDTO = pm.getObjectById(MoveDTO.class, moveUID);
			CaseDTO caseDTO = pm.getObjectById(CaseDTO.class, moveDTO.getCaseUID());
			UnitDTO unitDTO = pm.getObjectById(UnitDTO.class, moveDTO.getUnitUID());
			GatheringDTO gatheringDTO = pm.getObjectById(GatheringDTO.class, moveDTO.getGatheringUID());
			
			caseDTO.getMoveUIDs().remove(moveUID);
			unitDTO.getMoveUIDs().remove(moveUID);
			
			if(gatheringDTO.getUnitUIDs().size() == 1){
				pm.deletePersistent(gatheringDTO);
			}
			else
				gatheringDTO.getUnitUIDs().remove(moveDTO.getUnitUID());
				
			pm.deletePersistent(moveDTO);
			pm.close();			
		}
		catch(Exception e){
			// le move n'existe pas encore
		}
	}

	public void addUnitInGathering(String gatheringUID, String unitUID) 
	{
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		GatheringDTO gathering = pm.getObjectById(GatheringDTO.class, gatheringUID);
		
		gathering.getUnitUIDs().add(unitUID);
		
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
	
	//======================================================================================================//
	
}
