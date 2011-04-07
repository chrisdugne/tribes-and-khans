package com.uralys.tribes.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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
import com.uralys.tribes.entities.dto.EquipmentDTO;
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
		
		String bowsStockUID = createEquipment(ITEM_UID_BOW, 0);
		String swordsStockUID = createEquipment(ITEM_UID_SWORD, 0);
		String armorsStockUID = createEquipment(ITEM_UID_ARMOR, 0);
		
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

	private void createCase(int x, int y, String cityUID, int type, String landOwnerUID, PersistenceManager pm) {
		
		CaseDTO _case = new CaseDTO();

		String caseUID = "case_"+x+"_"+y;
		Key key = KeyFactory.createKey(CaseDTO.class.getSimpleName(), caseUID);

		_case.setKey(KeyFactory.keyToString(key));
		_case.setCaseUID(caseUID);
		_case.setX(x);
		_case.setY(y);
		_case.setCityUID(cityUID);
		_case.setType(type);

		pm.makePersistent(_case);
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

	public String createArmy(Unit army){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		UnitDTO armyDTO = new UnitDTO(); 
		
		String armyUID = Utils.generateUID();
		Key key = KeyFactory.createKey(UnitDTO.class.getSimpleName(), armyUID);

		armyDTO.setKey(KeyFactory.keyToString(key));
		armyDTO.setUnitUID(armyUID);
		armyDTO.setSize(army.getSize());
		armyDTO.setSpeed(army.getSpeed());
		armyDTO.setWheat(army.getWheat());
		armyDTO.setWood(army.getWood());
		armyDTO.setIron(army.getIron());
		armyDTO.setGold(army.getGold());
		if(army.getMoves().size() > 0)
			armyDTO.getMoveUIDs().add(army.getMoves().get(0).getMoveUID());
		
		
		//--------------------------------------//
		// init Equipment
		
		for(Equipment equipment : army.getEquipments()){
			if(equipment.getItem().getName().equals("bow")){
				String bowsStockUID = createEquipment(ITEM_UID_BOW, equipment.getSize());
				armyDTO.getEquipmentUIDs().add(bowsStockUID);
			}
			else if(equipment.getItem().getName().equals("sword")){
				String swordsStockUID = createEquipment(ITEM_UID_SWORD, equipment.getSize());
				armyDTO.getEquipmentUIDs().add(swordsStockUID);
			}
			else if(equipment.getItem().getName().equals("armor")){
				String armorsStockUID = createEquipment(ITEM_UID_ARMOR, equipment.getSize());
				armyDTO.getEquipmentUIDs().add(armorsStockUID);
			}
		}
		
		//--------------------------------------//
		
		pm.makePersistent(armyDTO);
		pm.close();
		
		return armyUID;
	}
	
	public void updateArmy(Unit army){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		UnitDTO armyDTO = pm.getObjectById(UnitDTO.class, army.getUnitUID());
		
		armyDTO.setSize(army.getSize());
		
		if(army.getMoves().size() > 0)
			armyDTO.getMoveUIDs().add(army.getMoves().get(0).getMoveUID());
		
		armyDTO.setWheat(army.getWheat());
		armyDTO.setWood(army.getWood());
		armyDTO.setIron(army.getIron());
		armyDTO.setGold(army.getGold());

		pm.close();
		
		for(Equipment equipment : army.getEquipments()){
			updateStock(equipment.getEquimentUID(), equipment.getSize());
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void deleteArmies(List<String> toDeleteArmyUIDs, String playerUID){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, playerUID);

		playerDTO.getUnitUIDs().removeAll(toDeleteArmyUIDs);

		for(String armyUID : toDeleteArmyUIDs){
			if(debug)log.info("delete army : " + armyUID);
			UnitDTO armyDTO = pm.getObjectById(UnitDTO.class, armyUID);
			
			// pas le cas pour les marchants
			if(armyDTO.getEquipmentUIDs().size() > 0){
				Query query = pm.newQuery("select from " + EquipmentDTO.class.getName() + " where :uids.contains(key)");
				List<EquipmentDTO> equipments = (List<EquipmentDTO>) query.execute(armyDTO.getEquipmentUIDs());
				
				for(EquipmentDTO equipmentDTO : equipments){
					if(debug)log.info("delete equipmentDTO : " + equipmentDTO.getEquimentUID());
					pm.deletePersistent(equipmentDTO);
				}
			}

			
			pm.deletePersistent(armyDTO);
		}
		
		pm.close();
	}
	

	public String saveMove(Move move) {
		MoveDTO moveDTO = new MoveDTO(); 
		
		String moveUID = Utils.generateUID();
		Key key = KeyFactory.createKey(MoveDTO.class.getSimpleName(), moveUID);

		moveDTO.setKey(KeyFactory.keyToString(key));
		moveDTO.setMoveUID(moveUID);
		
		persist(moveDTO);
		return moveUID;
	}
	
	//==================================================================================================//
	// PRIVATE METHODS
	
	
	private String createEquipment(String item, int size) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		EquipmentDTO bowsStock = new EquipmentDTO();

		String equipmentUID = Utils.generateUID();
		
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
