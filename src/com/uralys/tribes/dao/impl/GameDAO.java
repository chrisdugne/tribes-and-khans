package com.uralys.tribes.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.entities.Army;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Equipment;
import com.uralys.tribes.entities.Game;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.dto.ArmyDTO;
import com.uralys.tribes.entities.dto.CityDTO;
import com.uralys.tribes.entities.dto.EquipmentDTO;
import com.uralys.tribes.entities.dto.GameDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.entities.dto.MoveDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.ProfilDTO;
import com.uralys.tribes.entities.dto.SmithDTO;
import com.uralys.utils.Utils;

public class GameDAO  extends MainDAO implements IGameDAO {
	
	//-----------------------------------------------------------------------//
	// local

	private static String ITEM_UID_BOW = "129694476134541891626653";
	private static String ITEM_UID_SWORD = "12969447896146126305843";
	private static String ITEM_UID_ARMOR = "129694480859891628060737";

	//-----------------------------------------------------------------------//
	
	public void createGame(String uralysUID, String gameName, String playerName, int nbMinByTurn) {

		//-----------------------------------------------------------------------//

		String playerUID = Utils.generateUID();
		String gameUID = Utils.generateUID();

		//-----------------------------------------------------------------------//

		/*public var periods:ArrayCollection = new ArrayCollection(
				[   {periodLabel:"1 h", period:6},
					{periodLabel:"2 h", period:7},
					{periodLabel:"6 h", period:8},
					{periodLabel:"12 h", period:9},
					{periodLabel:"24 h", period:10},
					{periodLabel:"36 h", period:11},
					{periodLabel:"48 h", period:12},
					{periodLabel:"1 week", period:13} ]);
		*/
		
		switch (nbMinByTurn) {
			case 6:
				nbMinByTurn = 60;
				break;
			case 7:
				nbMinByTurn = 120;
				break;
			case 8:
				nbMinByTurn = 360;
				break;
			case 9:
				nbMinByTurn = 720;
				break;
			case 10:
				nbMinByTurn = 24*60;
				break;
			case 11:
				nbMinByTurn = 36*60;
				break;
			case 12:
				nbMinByTurn = 48*60;
				break;
			case 13:
				nbMinByTurn = 7*24*60;
				break;
	
			default:
				break;
		}

		//-----------------------------------------------------------------------//
		
		GameDTO gameDTO =  new GameDTO();
		
		Key key = KeyFactory.createKey(GameDTO.class.getSimpleName(), gameUID);
		
		gameDTO.setKey(KeyFactory.keyToString(key));
		gameDTO.setGameUID(gameUID);
		gameDTO.setCreatorUralysUID(uralysUID);
		gameDTO.setName(gameName);
		gameDTO.setStatus(Game.IN_CREATION);
		gameDTO.setCurrentTurn(0);
		gameDTO.setNbMinByTurn(nbMinByTurn);
		gameDTO.getPlayerUIDs().add(playerUID);
		
		persist(gameDTO);

		//-----------------------------------------------------------------------//
		
		PlayerDTO playerDTO = new PlayerDTO();
		key = KeyFactory.createKey(PlayerDTO.class.getSimpleName(), playerUID);
		
		playerDTO.setKey(KeyFactory.keyToString(key));
		playerDTO.setPlayerUID(playerUID);
		playerDTO.setGameUID(gameUID);
		playerDTO.setGameName(gameName);
		playerDTO.setName(playerName);
		playerDTO.setLastTurnPlayed(0);
		
		persist(playerDTO);

		//-----------------------------------------------------------------------//
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ProfilDTO profil = pm.getObjectById(ProfilDTO.class, uralysUID);
		profil.getPlayerUIDs().add(playerUID);
		pm.close();

		//-----------------------------------------------------------------------//
	}
	
	public void joinGame(String uralysUID, String gameUID, String playerName) {
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();

		//-----------------------------------------------------------------------//

		String playerUID = Utils.generateUID();
		
		//-----------------------------------------------------------------------//
		
		GameDTO game = pm.getObjectById(GameDTO.class, gameUID);
		game.getPlayerUIDs().add(playerUID);
		
		//-----------------------------------------------------------------------//
		
		ProfilDTO profil = pm.getObjectById(ProfilDTO.class, uralysUID);
		profil.getPlayerUIDs().add(playerUID);
		
		//-----------------------------------------------------------------------//

		pm.close();

		//-----------------------------------------------------------------------//
		
		PlayerDTO playerDTO = new PlayerDTO();
		Key key = KeyFactory.createKey(PlayerDTO.class.getSimpleName(), playerUID);
		
		playerDTO.setKey(KeyFactory.keyToString(key));
		playerDTO.setPlayerUID(playerUID);
		playerDTO.setGameUID(gameUID);
		playerDTO.setGameName(game.getName());
		playerDTO.setName(playerName);
		
		persist(playerDTO);
		
		//-----------------------------------------------------------------------//

	}
	
	public List<GameDTO> getCurrentGames(String uralysUID) {

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ProfilDTO profil = pm.getObjectById(ProfilDTO.class, uralysUID);

		List<String> gameUIDs = new ArrayList<String>();
		
		for(PlayerDTO player : profil.getPlayers())
			gameUIDs.add(player.getGameUID());
		

		return UniversalDAO.getInstance().getListDTO(gameUIDs, GameDTO.class);
		
	}

	
	@SuppressWarnings("unchecked")
	public List<GameDTO> getGamesToJoin(){
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query query = pm.newQuery("select from " + GameDTO.class.getName());
		query.setFilter("status == :status");
		
		return (List<GameDTO>) query.execute(Game.IN_CREATION);
	}


	//==================================================================================================//	
	
	public boolean launchGame(String gameUID) {

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		GameDTO game = pm.getObjectById(GameDTO.class, gameUID);
		
		game.setBeginTurnTimeMillis(new Date().getTime());
		game.setStatus(Game.RUNNING);
		game.setCurrentTurn(1);
		
		
		//-----------------------------------------------------------------------------------//
		for (String playerUID : game.getPlayerUIDs()){
			PlayerDTO player = pm.getObjectById(PlayerDTO.class, playerUID);
			
			CityDTO city = new CityDTO();

			String cityUID = Utils.generateUID();

			Key key = KeyFactory.createKey(CityDTO.class.getSimpleName(), cityUID);

			city.setKey(KeyFactory.keyToString(key));
			city.setCityUID(cityUID);
			city.setName("Ville de " + player.getName());
			city.setPopulation(1000);
			city.setWheat(400);
			city.setWood(200);
			city.setIron(200);
			city.setPeopleCreatingWheat(0);
			city.setPeopleCreatingWood(0);
			city.setPeopleCreatingIron(0);
			city.setGold(100);

			city.setX(Utils.random(2400) + 300);
			city.setY(Utils.random(2400) + 300);
			
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
			
			player.setLastTurnPlayed(0);
			player.getCityUIDs().add(cityUID);
			
		}
		
		//-----------------------------------------------------------------------------------//
		
		pm.close();
		
		return true;
	}


	//==================================================================================================//
	
	public List<ItemDTO> loadItems() {
		return UniversalDAO.getInstance().getListDTO(ItemDTO.class, 1, 100);
	}

	//==================================================================================================//
	
	public void updateCityResources(City city){
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		CityDTO cityDTO = pm.getObjectById(CityDTO.class, city.getCityUID());
		
		cityDTO.setWheat(city.getWheat());
		cityDTO.setWood(city.getWood());
		cityDTO.setIron(city.getIron());
		
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
	
	public void updatePlayer(Player player){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, player.getPlayerUID());
		
		playerDTO.setLastTurnPlayed(player.getLastTurnPlayed());
		playerDTO.setLands(player.getLands());
		pm.close();
	}
	
	public void updateGame(Game game){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		GameDTO gameDTO = pm.getObjectById(GameDTO.class, game.getGameUID());
		
		gameDTO.setCurrentTurn(game.getCurrentTurn());
		gameDTO.setBeginTurnTimeMillis(game.getBeginTurnTimeMillis());
		pm.close();
	}
	
	public void checkEndTurn(String gameUID){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		GameDTO gameDTO = pm.getObjectById(GameDTO.class, gameUID);
		
		boolean waitingForPlayers = false;
		for(PlayerDTO player : gameDTO.getPlayers()){
			if(player.getLastTurnPlayed() < gameDTO.getCurrentTurn()){
				waitingForPlayers = true;
				break;
			}
		}
		
		if(!waitingForPlayers){
			gameDTO.setCurrentTurn(gameDTO.getCurrentTurn() + 1);
			gameDTO.setBeginTurnTimeMillis(new Date().getTime());
		}
		
		pm.close();
	}
	
	//==================================================================================================//
	
	public String createArmy(Army army){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ArmyDTO armyDTO = new ArmyDTO(); 
		
		String armyUID = Utils.generateUID();
		Key key = KeyFactory.createKey(ArmyDTO.class.getSimpleName(), armyUID);

		armyDTO.setKey(KeyFactory.keyToString(key));
		armyDTO.setArmyUID(armyUID);
		armyDTO.setSize(army.getSize());
		armyDTO.setSpeed(army.getSpeed());
		armyDTO.setX(army.getX());
		armyDTO.setY(army.getY());
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

	
	public List<String> linkNewArmiesAndGetPreviousArmyUIDs(String playerUID, List<String> newArmyUIDs){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, playerUID);
		
		List<String> previousUIDList = new ArrayList<String>();
		for(String armyUID : playerDTO.getArmyUIDs()){
			previousUIDList.add(armyUID);
		}
		
		playerDTO.getArmyUIDs().addAll(newArmyUIDs);
		pm.close();
		
		return previousUIDList;
	}

	
	public void updateArmy(Army army){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ArmyDTO armyDTO = pm.getObjectById(ArmyDTO.class, army.getArmyUID());
		
		armyDTO.setSize(army.getSize());
		armyDTO.getMoveUIDs().add(army.getMoves().get(0).getMoveUID());
		pm.close();
		
		for(Equipment equipment : army.getEquipments()){
			updateStock(equipment.getEquimentUID(), equipment.getSize());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void deleteArmies(List<String> toDeleteArmyUIDs){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();

		for(String armyUID : toDeleteArmyUIDs){
			System.out.println("delete army : " + armyUID);
			ArmyDTO armyDTO = pm.getObjectById(ArmyDTO.class, armyUID);
			
			Query query = pm.newQuery("select from " + EquipmentDTO.class.getName() + " where :uids.contains(key)");
			List<EquipmentDTO> equipments = (List<EquipmentDTO>) query.execute(armyDTO.getEquipmentUIDs());

			for(EquipmentDTO equipmentDTO : equipments){
				System.out.println("delete equipmentDTO : " + equipmentDTO.getEquimentUID());
				pm.deletePersistent(equipmentDTO);
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
		moveDTO.setxFrom(move.getxFrom());
		moveDTO.setxTo(move.getxTo());
		moveDTO.setyFrom(move.getyFrom());
		moveDTO.setyTo(move.getyTo());
		
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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
