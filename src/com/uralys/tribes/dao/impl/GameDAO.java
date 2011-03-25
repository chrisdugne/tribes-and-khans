package com.uralys.tribes.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
import com.uralys.tribes.entities.dto.ConflictDTO;
import com.uralys.tribes.entities.dto.EquipmentDTO;
import com.uralys.tribes.entities.dto.GameDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.entities.dto.MoveConflictDTO;
import com.uralys.tribes.entities.dto.MoveDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.ProfilDTO;
import com.uralys.tribes.entities.dto.ReportDTO;
import com.uralys.tribes.entities.dto.SmithDTO;
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
	// prod
	
//	private static String ITEM_UID_BOW = "129788477896702024115844";
//	private static String ITEM_UID_SWORD = "129788479280211748841386";
//	private static String ITEM_UID_ARMOR = "129788480608331160020597";
//	private static boolean debug = true;

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
		playerDTO.setReportUIDs(new ArrayList<String>());
		
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
		int numplayer=1;
		for (String playerUID : game.getPlayerUIDs()){
			createCity(null, playerUID, pm, numplayer++);
		}
		
		//-----------------------------------------------------------------------------------//
		
		pm.close();
		
		return true;
	}


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
		
		city.setCreationTurn(cityFromFlex == null ? 0 : cityFromFlex.getCreationTurn());
		
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
		
		if(cityFromFlex == null)
			player.setLastTurnPlayed(0);
		
		player.getCityUIDs().add(cityUID);
	}

	private int getInitialCityX(int numplayer) {
		switch(numplayer){
			case 1:
				return 1500;
			case 2:
				return 1500;
			case 3:
				return 250;
			case 4:
				return 2750;
			case 5:
				return 250;
			case 6:
				return 2750;
			case 7:
				return 2750;
			case 8:
				return 250;
			case 9:
				return 1500;
		
			default :
				return Utils.random(2400) + 300;
		}
	}

	private int getInitialCityY(int numplayer) {
		switch(numplayer){
		case 1:
			return 250;
		case 2:
			return 2750;
		case 3:
			return 1500;
		case 4:
			return 1500;
		case 5:
			return 250;
		case 6:
			return 250;
		case 7:
			return 2750;
		case 8:
			return 2750;
		case 9:
			return 1500;
			
		default :
			return Utils.random(2400) + 300;
		}
	}

	//==================================================================================================//
	
	public List<ItemDTO> loadItems() {
		return UniversalDAO.getInstance().getListDTO(ItemDTO.class, 1, 100);
	}

	//==================================================================================================//
	
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
	
	public void updatePlayer(Player player){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, player.getPlayerUID());
		
		
		playerDTO.setLastTurnPlayed(player.getLastTurnPlayed());
		playerDTO.setLands(player.getLands());
		playerDTO.setReportUIDs(new ArrayList<String>());
		
		try{
			ReportDTO reportDTO = pm.getObjectById(ReportDTO.class, playerDTO.getReportUIDs().get(0));
			pm.deletePersistent(reportDTO);
		}
		catch(Exception e){/* pas de rapport*/}

		pm.close();
	}
	
	public void updateGame(Game game){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		GameDTO gameDTO = pm.getObjectById(GameDTO.class, game.getGameUID());
		
		gameDTO.setCurrentTurn(game.getCurrentTurn());
		gameDTO.setBeginTurnTimeMillis(game.getBeginTurnTimeMillis());
		pm.close();
	}
	
	@SuppressWarnings("unchecked")
	public void checkEndTurn(String gameUID){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		GameDTO gameDTO = pm.getObjectById(GameDTO.class, gameUID);
		
		boolean waitingForPlayers = false;

		Query query = pm.newQuery("select from " + PlayerDTO.class.getName() + " where :uids.contains(key)");

		List<PlayerDTO> players = (List<PlayerDTO>) query.execute(gameDTO.getPlayerUIDs());
		
		for(PlayerDTO player : players){
			if(player.getLastTurnPlayed() < gameDTO.getCurrentTurn()){
				waitingForPlayers = true;
				break;
			}
		}
		
		if(!waitingForPlayers){
			removePreviousConflicts(players);
			calculateMovesAndFights(players);
			gameDTO.setCurrentTurn(gameDTO.getCurrentTurn() + 1);
			gameDTO.setBeginTurnTimeMillis(new Date().getTime());
		}
		
		pm.close();
	}
	
	//==================================================================================================//

	@SuppressWarnings("unchecked")
	private void removePreviousConflicts(List<PlayerDTO> players) {
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		for(PlayerDTO player : players){

			if(player.getConflictsUIDs().size() == 0)
				continue;
			
			Query query = pm.newQuery("select from " + ConflictDTO.class.getName() + " where :uids.contains(key)");
			List<ConflictDTO> conflicts = (List<ConflictDTO>) query.execute(player.getConflictsUIDs());
			
			for(ConflictDTO conflict : conflicts){
				query = pm.newQuery("select from " + MoveConflictDTO.class.getName() + " where :uids.contains(key)");
				List<MoveConflictDTO> moveAllies = (List<MoveConflictDTO>) query.execute(conflict.getMoveAlliesUIDs());
				List<MoveConflictDTO> moveEnnemies = (List<MoveConflictDTO>) query.execute(conflict.getMoveEnnemiesUIDs());
				
				for(MoveConflictDTO moveConflict : moveAllies){
					pm.deletePersistent(moveConflict);
				}
				
				for(MoveConflictDTO moveConflict : moveEnnemies){
					pm.deletePersistent(moveConflict);
				}

				pm.deletePersistent(conflict);
			}
		}
		
		pm.close();
	}

	public void createCity(City city, String playerUID){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		createCity(city, playerUID, pm, -1);
		pm.close();
	}

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

	public List<String> linkNewMerchantsAndGetPreviousMerchantUIDs(String playerUID, List<String> newMerchantUIDs){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, playerUID);
		
		List<String> previousUIDList = new ArrayList<String>();
		for(String merchantUID : playerDTO.getMerchantUIDs()){
			previousUIDList.add(merchantUID);
		}
		
		playerDTO.getMerchantUIDs().addAll(newMerchantUIDs);
		pm.close();
		
		return previousUIDList;
	}

	
	public void updateArmy(Army army){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ArmyDTO armyDTO = pm.getObjectById(ArmyDTO.class, army.getArmyUID());
		
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

		playerDTO.getArmyUIDs().removeAll(toDeleteArmyUIDs);
		playerDTO.getMerchantUIDs().removeAll(toDeleteArmyUIDs);

		for(String armyUID : toDeleteArmyUIDs){
			if(debug)log.info("delete army : " + armyUID);
			ArmyDTO armyDTO = pm.getObjectById(ArmyDTO.class, armyUID);
			
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

	
	//======================================================================================================//
	
	

	private Map<String, Integer> debugMoveIds = new HashMap<String, Integer>();
	
	@SuppressWarnings("unchecked")
	private void calculateMovesAndFights(List<PlayerDTO> players) {
		if(debug)log.info("==========================");
		if(debug)log.info("calculateMovesAndFights");

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		List<MoveDTO> allMoves = new ArrayList<MoveDTO>();
		List<String> dummyMoves = new ArrayList<String>();
		List<Meeting> meetings = new ArrayList<Meeting>();
		List<String> armiesToDelete = new ArrayList<String>();
		
		Map<MoveDTO, ArmyDTO> moveArmyMap = new HashMap<MoveDTO, ArmyDTO>();
		Map<ArmyDTO, MoveDTO> armyMoveMap = new HashMap<ArmyDTO, MoveDTO>();
		Map<ArmyDTO, String> armyInfoMoveMap = new HashMap<ArmyDTO, String>();
		Map<MoveDTO, PlayerDTO> movePlayerMap = new HashMap<MoveDTO, PlayerDTO>();
		Map<String, PlayerDTO> cityPlayerMap = new HashMap<String, PlayerDTO>();
		Map<PlayerDTO, CityDTO> playerCapitalMap = new HashMap<PlayerDTO, CityDTO>();
		Map<String, Integer> cityXMap = new HashMap<String, Integer>();
		Map<String, Integer> cityYMap = new HashMap<String, Integer>();
		Map<ArmyDTO, CityDTO> armyCityMap = new HashMap<ArmyDTO, CityDTO>();
		Map<PlayerDTO, ArrayList<Integer>> landsMap = new HashMap<PlayerDTO, ArrayList<Integer>>();
		Map<PlayerDTO, String> reports = new HashMap<PlayerDTO, String>();
		Map<PlayerDTO, Conflit> playerConflitMap = new HashMap<PlayerDTO, Conflit>();
		
		if(debug)log.info("-----------");
		if(debug)log.info("rangement dans les maps/listes");
		int debugMoveId = 1;
		for(PlayerDTO player : players){
			if(debug)log.info("player " + player.getName());
			landsMap.put(player, new ArrayList<Integer>());
			player.setConflictsUIDs(new ArrayList<String>());

			//-----------------------------------------//

			ArrayList<String> allUnitUIDs = new ArrayList<String>();
			
			if(debug)log.info("armies");
			
			for(String army : player.getArmyUIDs()){
				if(debug)log.info(army);
			}
			
			if(debug)log.info("merchants");

			for(String army : player.getMerchantUIDs()){
				if(debug)log.info(army);
			}
			
			
			allUnitUIDs.addAll(player.getArmyUIDs());
			allUnitUIDs.addAll(player.getMerchantUIDs());
			

			if(debug)log.info("allUnits");

			for(String army : allUnitUIDs){
				if(debug)log.info(army);
			}
			
			Query query = pm.newQuery("select from " + ArmyDTO.class.getName() + " where :uids.contains(key)");
			List<ArmyDTO> armies;
			if(allUnitUIDs.size() > 0){
				armies = (List<ArmyDTO>) query.execute(allUnitUIDs);
			}
			else
				armies = new ArrayList<ArmyDTO>();

			//-----------------------------------------//


			if(debug)log.info("nbUnits " + armies.size());
			for(ArmyDTO army : armies){
				if(army.getMoves().size()>0){
					MoveDTO move = army.getMoves().get(0);

					// si le mouvement est immobile, on fabrique un mouvement le long du diametre du cercle representant l'entite deplacee 
					// pour simuler le croisement de cette entite.
					// *racinecarre(2)sur2 = cospi/4 = sinpi/4
					double size = rayon(army.getSize()) * Math.sqrt(2)/2;
					
					if(move.getxFrom() == move.getxTo() && move.getyFrom() == move.getyTo()){
						
						if(debug)log.info("armee fixe : creation du mouvement sur le diametre");
						move.setxFrom((int)(move.getxFrom() - size));
						move.setxTo((int)(move.getxTo() + size));
						move.setyFrom((int)(move.getyFrom() - size));
						move.setyTo((int)(move.getyTo() + size));
						dummyMoves.add(move.getMoveUID());
					}
					
					debugMoveIds.put(move.getMoveUID(), debugMoveId);
					if(debug)log.info("register a move from " + move.getxFrom() + "," + move.getyFrom() + " | to " + move.getxTo() + "," + move.getyTo() + " | " + move.getMoveUID() + " | num : " + debugMoveId++);
					allMoves.add(move);
					moveArmyMap.put(move, army);
					armyMoveMap.put(army, move);
					movePlayerMap.put(move, player);
				}
			}
			
			query = pm.newQuery("select from " + CityDTO.class.getName() + " where :uids.contains(key)");
			query.setOrdering("key");
			
			List<CityDTO> cities;
			if(player.getCityUIDs().size() > 0){
				cities = (List<CityDTO>) query.execute(player.getCityUIDs());
			}
			else
				cities = new ArrayList<CityDTO>();

			playerCapitalMap.put(player, cities.get(0));
			
			if(debug)log.info("nbcities " + cities.size());
			for(CityDTO city : cities){
				if(debug)log.info("city.x : " + city.getX());
				if(debug)log.info("city.y : " + city.getY());
				MoveDTO move = new MoveDTO();
				ArmyDTO army = new ArmyDTO();
				
				move.setMoveUID("_" + city.getCityUID());
				army.setArmyUID("_" + city.getCityUID());
				army.setSize(city.getPopulation());
				army.setSpeed(200); // il ne faut pas que ce soit = 0 pour le calcul de temps1/temps2
				
				double size = rayon(city.getPopulation());
				
				move.setxFrom((int)(city.getX() - size));
				move.setxTo((int)(city.getX() + size));
				move.setyFrom((int)(city.getY() - size));
				move.setyTo((int)(city.getY() + size));
				
				debugMoveIds.put(move.getMoveUID(), debugMoveId);
				if(debug)log.info("register a dummy-city-move from " + move.getxFrom() + "," + move.getyFrom() + " | to " + move.getxTo() + "," + move.getyTo() + " | " + move.getMoveUID() + " | num : " + debugMoveId++);
				allMoves.add(move);
				moveArmyMap.put(move, army);
				armyMoveMap.put(army, move);
				movePlayerMap.put(move, player);
				
				cityPlayerMap.put(city.getCityUID(), player);
				cityXMap.put(city.getCityUID(), city.getX());
				cityYMap.put(city.getCityUID(), city.getY());
				armyCityMap.put(army, city);
			}
		}
		

		if(debug)log.info("---------------------------------------------------------");
		if(debug)log.info("tri");
		triDesMoves(allMoves, moveArmyMap, movePlayerMap, meetings);

		if(debug)log.info("---------------------------------------------------------");
		if(debug)log.info("tri termine, affichage des meetings");

		for(Meeting meeting : meetings){
			if(debug)log.info("[" + debugMoveIds.get(meeting.move1.getMoveUID()) + "," + debugMoveIds.get(meeting.move2.getMoveUID()) + "] | d1 : " + meeting.distance1 + ", d2 : " +  meeting.distance2);
		}

		if(debug)log.info("---------------------------------------------------------");
		if(debug)log.info("deplacement des troupes");
		

		for(MoveDTO move : allMoves){
			
			if(dummyMoves.contains(move.getMoveUID())) // on ne deplace pas les unites pour lesquelles on a genere le move sur leur diametre
				continue;
			
			String moveOriginalInfo = " : mouvement prevu de [" + move.getxFrom() + "," + move.getyFrom()  + "] à [" + move.getxTo() + "," + move.getyTo()  + "]";
			armyInfoMoveMap.put(moveArmyMap.get(move), moveOriginalInfo);
			
			boolean freeWay = true;
			for(Meeting meeting : meetings){
				if(meeting.contains(move)){
					move.setxTo(meeting.meetingX);
					move.setyTo(meeting.meetingY);
					freeWay = false;
					break;
				}
			}
			
			moveArmyMap.get(move).setX(move.getxTo());
			moveArmyMap.get(move).setY(move.getyTo());
			
			if(freeWay){
				if(debug)log.info("way safe for the move : proceeding to the land calculus");
				
				int newLand = testNewLand(moveArmyMap.get(move), movePlayerMap.get(move));
				if(newLand>0)
					landsMap.get(movePlayerMap.get(move)).add(newLand);
			}
		}
		
		
		if(debug)log.info("---------------------------------------------------------------------");
		if(debug)log.info("generation des combats d'apres les meetings");

		List<Conflit> conflits = new ArrayList<Conflit>();

		for(Meeting meeting1 : meetings){
			if(meeting1.status == 1) // already merged
				continue;
			
			Conflit conflit = new Conflit();
			conflit.x = meeting1.meetingX;
			conflit.y = meeting1.meetingY;
			conflit.allies.add(moveArmyMap.get(meeting1.move1));
			conflit.ennemies.add(moveArmyMap.get(meeting1.move2));
			
			PlayerDTO ally = movePlayerMap.get(meeting1.move1);
			playerConflitMap.put(ally, conflit);
			
			for(Meeting meeting2 : meetings){
				if(meeting1.equals(meeting2))
					continue;
				
				if(meeting1.meetingX == meeting2.meetingX
				&& meeting1.meetingY == meeting2.meetingY
				&& meeting2.status == 0){
					
					PlayerDTO player1 = movePlayerMap.get(meeting2.move1);
					playerConflitMap.put(player1, conflit);
					
					if(player1.getPlayerUID().equals(ally.getPlayerUID()) // une de tes armees
					|| player1.getAllyUIDs().contains(ally.getPlayerUID())){ // une armee de tes potes

						if(!conflit.allies.contains(moveArmyMap.get(meeting2.move1)))
							conflit.allies.add(moveArmyMap.get(meeting2.move1));
						if(!conflit.ennemies.contains(moveArmyMap.get(meeting2.move2)))
							conflit.ennemies.add(moveArmyMap.get(meeting2.move2));
					}
					else{
						if(!conflit.ennemies.contains(moveArmyMap.get(meeting2.move1)))
							conflit.ennemies.add(moveArmyMap.get(meeting2.move1));						
						if(!conflit.allies.contains(moveArmyMap.get(meeting2.move2)))
							conflit.allies.add(moveArmyMap.get(meeting2.move2));
					}
						
					meeting2.status = 1;
				}
			}
		
			conflits.add(conflit);
			meeting1.status = 1;
		}

		
		if(debug)log.info("---------------------------------------------------------------------");
		if(debug)log.info("resolution des combats");
		

		for(Conflit conflit : conflits){
			
			if(debug)log.info("resolution du conflit en [" + conflit.x + "," + conflit.y +"]");
			StringBuffer newReport = new StringBuffer();
			newReport.append("\nConflit en [  " + conflit.x + "  ,  " + conflit.y +"  ]\n");

			//------------------------------------------------------------------------//
			// creation des ConflictDTOs pour chaque joueur du conflit
			if(debug)log.info("creation des ConflictDTOs pour chaque joueur du conflit");

			Map<PlayerDTO, ConflictDTO> playerConflictsAllyMap = new HashMap<PlayerDTO, ConflictDTO>();
			Map<PlayerDTO, ConflictDTO> playerConflictsEnnemyMap = new HashMap<PlayerDTO, ConflictDTO>();
			
			for(ArmyDTO army : conflit.allies){
				
				PlayerDTO playerOwningThisArmy = movePlayerMap.get(armyMoveMap.get(army));
				ConflictDTO conflictDTO = playerConflictsAllyMap.get(playerOwningThisArmy);
				
				if(conflictDTO == null){
					String conflictUID = Utils.generateUID();
					Key key = KeyFactory.createKey(ConflictDTO.class.getSimpleName(), conflictUID);
					conflictDTO = new ConflictDTO();

					conflictDTO.setKey(KeyFactory.keyToString(key));
					conflictDTO.setConflictUID(conflictUID);
					conflictDTO.setX(conflit.x);
					conflictDTO.setY(conflit.y);
					conflictDTO.setStatus(0);
					
					playerOwningThisArmy.getConflictsUIDs().add(conflictUID);
					playerConflictsAllyMap.put(playerOwningThisArmy, conflictDTO);
				}
			}

			for(ArmyDTO army : conflit.ennemies){
				
				PlayerDTO playerOwningThisArmy = movePlayerMap.get(armyMoveMap.get(army));
				ConflictDTO conflictDTO = playerConflictsEnnemyMap.get(playerOwningThisArmy);
				
				if(conflictDTO == null){
					String conflictUID = Utils.generateUID();
					Key key = KeyFactory.createKey(ConflictDTO.class.getSimpleName(), conflictUID);
					conflictDTO = new ConflictDTO();
					
					conflictDTO.setKey(KeyFactory.keyToString(key));
					conflictDTO.setConflictUID(conflictUID);
					conflictDTO.setX(conflit.x);
					conflictDTO.setY(conflit.y);
					conflictDTO.setStatus(0);

					playerOwningThisArmy.getConflictsUIDs().add(conflictUID);
					playerConflictsEnnemyMap.put(playerOwningThisArmy, conflictDTO);
				}
			}
			
			//------------------------------------------------------------------------//
			// affectation des MoveConflictDTOs à chaque ConflictsDTO
			if(debug)log.info("affectation des MoveConflictDTOs à chaque ConflictsDTO");
			
			for(ArmyDTO army : conflit.allies){

				String moveConflictUID = Utils.generateUID();
				Key key = KeyFactory.createKey(MoveConflictDTO.class.getSimpleName(), moveConflictUID);
				MoveConflictDTO moveConflictDTO = new MoveConflictDTO();

				moveConflictDTO.setKey(KeyFactory.keyToString(key));
				moveConflictDTO.setMoveConflictUID(moveConflictUID);
				moveConflictDTO.setArmySize(army.getSize());
				
				moveConflictDTO.setxFrom(armyMoveMap.get(army).getxFrom());
				moveConflictDTO.setyFrom(armyMoveMap.get(army).getyFrom());
				
				if(dummyMoves.contains(armyMoveMap.get(army).getMoveUID()))
					moveConflictDTO.setArmyStanding(true);
				else
					moveConflictDTO.setArmyStanding(false);
				
				for(EquipmentDTO equipment : army.getEquipments()){
					int equipmentSize = equipment.getSize() > army.getSize() ? army.getSize() : equipment.getSize();
					ItemDTO item = equipment.getItem();
					
					if(item.getItemUID().equals("_bow"))
						moveConflictDTO.setArmyBows(equipmentSize);
					else if(item.getItemUID().equals("_sword"))
						moveConflictDTO.setArmySwords(equipmentSize);
					else if(item.getItemUID().equals("_armor"))
						moveConflictDTO.setArmyArmors(equipmentSize);
				}
			
				pm.makePersistent(moveConflictDTO);
				
				for(ConflictDTO conflict : playerConflictsAllyMap.values()){
					conflict.getMoveAlliesUIDs().add(moveConflictUID);
				}
				for(ConflictDTO conflict : playerConflictsEnnemyMap.values()){
					conflict.getMoveEnnemiesUIDs().add(moveConflictUID);
				}
			}
			
			for(ArmyDTO army : conflit.ennemies){
				
				String moveConflictUID = Utils.generateUID();
				Key key = KeyFactory.createKey(MoveConflictDTO.class.getSimpleName(), moveConflictUID);
				MoveConflictDTO moveConflictDTO = new MoveConflictDTO();
				
				moveConflictDTO.setKey(KeyFactory.keyToString(key));
				moveConflictDTO.setMoveConflictUID(moveConflictUID);
				moveConflictDTO.setArmySize(army.getSize());
				
				moveConflictDTO.setxFrom(armyMoveMap.get(army).getxFrom());
				moveConflictDTO.setyFrom(armyMoveMap.get(army).getyFrom());

				if(dummyMoves.contains(armyMoveMap.get(army).getMoveUID()))
					moveConflictDTO.setArmyStanding(true);
				else
					moveConflictDTO.setArmyStanding(false);
				
				for(EquipmentDTO equipment : army.getEquipments()){
					int equipmentSize = equipment.getSize() > army.getSize() ? army.getSize() : equipment.getSize();
					ItemDTO item = equipment.getItem();
					
					if(item.getItemUID().equals("_bow"))
						moveConflictDTO.setArmyBows(equipmentSize);
					else if(item.getItemUID().equals("_sword"))
						moveConflictDTO.setArmySwords(equipmentSize);
					else if(item.getItemUID().equals("_armor"))
						moveConflictDTO.setArmyArmors(equipmentSize);
				}
				
				pm.makePersistent(moveConflictDTO);
				
				for(ConflictDTO conflict : playerConflictsAllyMap.values()){
					conflict.getMoveEnnemiesUIDs().add(moveConflictUID);
				}
				for(ConflictDTO conflict : playerConflictsEnnemyMap.values()){
					conflict.getMoveAlliesUIDs().add(moveConflictUID);
				}
			}
			
			
			// enregistrement des ConflictDTO
			for(ConflictDTO conflict : playerConflictsAllyMap.values()){
				pm.makePersistent(conflict);
			}
			for(ConflictDTO conflict : playerConflictsEnnemyMap.values()){
				pm.makePersistent(conflict);
			}
				
			//------------------------------------------------------------------------//
			// calcul des degats
			
			int valueAlly = 0;
			int valueEnnemy = 0;
			
			
			if(debug)log.info("Equipe 1");
			newReport.append("\n\nEquipe 1");
			for(ArmyDTO army : conflit.allies){

				if(debug)log.info(army.getArmyUID());
				newReport.append("\narmy " + armyInfoMoveMap.get(army));

				int valueArmy = 0;
				
				int size = army.getSize();
				valueArmy += size;
				
				for(EquipmentDTO equipment : army.getEquipments()){
					int equipmentSize = equipment.getSize() > size ? size : equipment.getSize();
					ItemDTO item = equipment.getItem();
					
					valueArmy += equipmentSize * item.getValue();
					if(debug)log.info(equipmentSize + " " + item.getName());
				}
				
				newReport.append(" | size : " + size + " | value : " + valueArmy);
				valueAlly += valueArmy;
			}
			
			
			if(debug)log.info("Equipe 2");
			newReport.append("\n\nEquipe 2");
			for(ArmyDTO army : conflit.ennemies){
				if(debug)log.info(army.getArmyUID());
				newReport.append("\narmy " + armyInfoMoveMap.get(army));

				int valueArmy = 0;
				
				int size = army.getSize();
				valueArmy += size;
				
				for(EquipmentDTO equipment : army.getEquipments()){
					int equipmentSize = equipment.getSize() > size ? size : equipment.getSize();
					ItemDTO item = equipment.getItem();
					
					valueArmy += equipmentSize * item.getValue();
					if(debug)log.info(equipmentSize + " " + item.getName());
				}
				newReport.append("  |  size :  " + size + "  |  value :  " + valueArmy);
				valueEnnemy += valueArmy;
			}
			
			if(debug)log.info("valueAlly : " + valueAlly);
			if(debug)log.info("valueEnnemy : " + valueEnnemy);

			newReport.append("\n\nEquipe 1 value : " + valueAlly);
			newReport.append("\nEquipe 2 value : " + valueEnnemy);
			
			String cityTaken = null;
			
			if(valueAlly > valueEnnemy){
				int sizeRemaining = 0;
				if(debug)log.info("Ally wins");
				
				
				for(ArmyDTO ennemy : conflit.ennemies){
					if(!ennemy.getArmyUID().startsWith("_")){
						armiesToDelete.add(ennemy.getArmyUID());
					}
					else{
						// army representing a city : no need to delete
						newReport.append("La ville a été prise !");
						cityTaken = ennemy.getArmyUID().substring(1);
						armyCityMap.get(ennemy).setPopulation(armyCityMap.get(ennemy).getPopulation()/4);
					}
				}

				for(ArmyDTO ally : conflit.allies){
					if(ally.getArmyUID().startsWith("_")){
						armyCityMap.get(ally).setPopulation((int)(armyCityMap.get(ally).getPopulation() - ((double)valueEnnemy/valueAlly)*armyCityMap.get(ally).getPopulation()));
						
						// ville attaquee : tous les gens restant font du ble
						for(SmithDTO smith : armyCityMap.get(ally).getSmiths()){
							smith.setPeople(0);
						}
						
						armyCityMap.get(ally).setPeopleCreatingWheat(armyCityMap.get(ally).getPopulation());
						armyCityMap.get(ally).setPeopleCreatingWood(0);
						armyCityMap.get(ally).setPeopleCreatingIron(0);
						//---//
						
					}
					else{
						ally.setSize((int)(ally.getSize() - ((double)valueEnnemy/valueAlly)*ally.getSize()));
						sizeRemaining += ally.getSize();
						
						if(ally.getSize() == 0)
							armiesToDelete.add(ally.getArmyUID());
					}
					
					if(cityTaken != null){ // on place les armees sur les coordonnees de la ville, le conflit n'etant pas pile poil au centre
						ally.setX(cityXMap.get(cityTaken));
						ally.setY(cityYMap.get(cityTaken));
					}
				}
				
				if(sizeRemaining == 0){
					newReport.append("\n\nDraw !");
				}
				else
					newReport.append("\n\nEquipe 1 won !");
				
				if(cityTaken != null){
					
					PlayerDTO oldPlayer = cityPlayerMap.get(cityTaken);
					PlayerDTO newPlayer = movePlayerMap.get(armyMoveMap.get(conflit.allies.get(0)));
					
					oldPlayer.getCityUIDs().remove(cityTaken);
					newPlayer.getCityUIDs().add(cityTaken);
				}
			}
			else{
				if(debug)log.info("Ennemy wins");
				int sizeRemaining = 0;
				
				for(ArmyDTO ally : conflit.allies){
					if(!ally.getArmyUID().startsWith("_")){
						armiesToDelete.add(ally.getArmyUID());
					}
					else{
						// army representing a city : no need to delete		
						newReport.append("La ville a été prise !");				
						cityTaken = ally.getArmyUID().substring(1);
						armyCityMap.get(ally).setPopulation(armyCityMap.get(ally).getPopulation()/4);
					}
					
				}
				

				for(ArmyDTO ennemy : conflit.ennemies){
					
					if(ennemy.getArmyUID().startsWith("_")){						
						armyCityMap.get(ennemy).setPopulation((int)(armyCityMap.get(ennemy).getPopulation() - ((double)valueAlly/valueEnnemy)*armyCityMap.get(ennemy).getPopulation()));

						// ville attaquee : tous les gens restant font du ble
						for(SmithDTO smith : armyCityMap.get(ennemy).getSmiths()){
							smith.setPeople(0);
						}
						
						armyCityMap.get(ennemy).setPeopleCreatingWheat(armyCityMap.get(ennemy).getPopulation());
						armyCityMap.get(ennemy).setPeopleCreatingWood(0);
						armyCityMap.get(ennemy).setPeopleCreatingIron(0);
						//---//
					}
					else{
						ennemy.setSize((int)(ennemy.getSize() - ((double)valueAlly/valueEnnemy)*ennemy.getSize()));
						sizeRemaining += ennemy.getSize();
						
						if(ennemy.getSize() == 0)
							armiesToDelete.add(ennemy.getArmyUID());
					}
					
					if(cityTaken != null){ // on place les armees sur les coordonnees de la ville, le conflit n'etant pas pile poil au centre
						ennemy.setX(cityXMap.get(cityTaken));
						ennemy.setY(cityYMap.get(cityTaken));
					}
				}

				if(sizeRemaining == 0){
					newReport.append("\n\nDraw !");
				}
				else
					newReport.append("\n\nEquipe 2 won !");
				
				if(cityTaken != null){
					
					PlayerDTO oldPlayer = cityPlayerMap.get(cityTaken);
					PlayerDTO newPlayer = movePlayerMap.get(armyMoveMap.get(conflit.ennemies.get(0)));
					
					oldPlayer.getCityUIDs().remove(cityTaken);
					newPlayer.getCityUIDs().add(cityTaken);
				}
			}
			
			for(ArmyDTO ally : conflit.allies){
				reports.put(movePlayerMap.get(armyMoveMap.get(ally)), newReport.toString());
			}

			for(ArmyDTO ennemy : conflit.ennemies){
				reports.put(movePlayerMap.get(armyMoveMap.get(ennemy)), newReport.toString());
			}
		}
		
		
		if(debug)log.info("-----------");
		if(debug)log.info("attribution des nouvelles contrees");


		for(PlayerDTO player : landsMap.keySet()){
			for(int landExpected : landsMap.get(player)){
				if(debug)log.info("player " + player.getName() + " wants land " + landExpected);

				
				// check si un autre joueur peut avoir la case aussi (peut arriver si non conflit sur une meme case)
				boolean landIsFree = true;
				for(PlayerDTO opponent : landsMap.keySet()){
					if(opponent.getPlayerUID().equals(player.getPlayerUID()))
						continue;
					
					for(int opponentLandExpected : landsMap.get(opponent)){
						if(landExpected == opponentLandExpected){
							landIsFree = false;
							break;
						}
					}
				}
				
				if(landIsFree){
					if(debug)log.info("land is free");
					player.getLands().add(landExpected);
					
					for(PlayerDTO otherPlayer : players){
						if(otherPlayer.getPlayerUID().equals(player.getPlayerUID()))
							continue;
						
						int indexToRemove = -1;
						for(int landOfOtherPlayer : otherPlayer.getLands()){
							if(landOfOtherPlayer == landExpected){
								indexToRemove = otherPlayer.getLands().indexOf(landOfOtherPlayer);
								if(debug)log.info("found another player having this land");
							}
						}
						
						if(indexToRemove > 0){
							otherPlayer.getLands().remove(indexToRemove);
							if(debug)log.info("the land was removed");

							//------------//
							// reporting
							
							String report = reports.get(otherPlayer);
							if(report == null)
								report = "";
							
							report += "\n contrée perdue";
							reports.put(player, report);
							
							//------------//
							
							break;
						}
					}
				}
			}
			
		}
		
		// on verifie qu un autre joueur n'a pas pique la case d'ou on vient ! dans ce cas on a perdu sa case et on ne pique pas la nouvelle du coup
		if(debug)log.info("verification des nouvelles contrees");
		for(PlayerDTO player : landsMap.keySet()){
			int landsWon = 0;
			for(int landExpected : landsMap.get(player)){
				if(!testLand(landExpected, player)){
					if(debug)log.info("lien au royaume casse !");
					player.getLands().remove(player.getLands().indexOf(landExpected));
					break;
				}
				else{
					landsWon ++;
				}
			}

			if(landsWon > 0){
				String report = reports.get(player);
				if(report == null)
					report = "Pas de conflits";
				
				report += "\n" + landsWon + " contrées conquises";
				reports.put(player, report);
			}
			
			if(debug)log.info("updating gold");
			playerCapitalMap.get(player).setGold(playerCapitalMap.get(player).getGold() + player.getLands().size()*10);
		}
		
		if(debug)log.info("-----------------------------------------------");
		if(debug)log.info("suppression des moves et des armees detruites");
		
		// delete the moves
		for(ArmyDTO army : armyMoveMap.keySet()){
			
			if(army.getArmyUID().startsWith("_")) // army representing a city : no move to delete
				continue;

			Query query = pm.newQuery("select from " + MoveDTO.class.getName() + " where :uids.contains(key)");
			List<MoveDTO> moves = (List<MoveDTO>) query.execute(army.getMoveUIDs());

			MoveDTO moveToDelete = moves.get(0);
			army.getMoveUIDs().remove(moveToDelete.getMoveUID());
			pm.deletePersistent(moveToDelete);
			
			if(armiesToDelete.contains(army.getArmyUID())){
				query = pm.newQuery("select from " + EquipmentDTO.class.getName() + " where :uids.contains(key)");
				List<EquipmentDTO> equipments = (List<EquipmentDTO>) query.execute(army.getEquipmentUIDs());
	
				for(EquipmentDTO equipmentDTO : equipments){
					if(debug)log.info("delete equipmentDTO : " + equipmentDTO.getEquimentUID());
					pm.deletePersistent(equipmentDTO);
				}
	
				PlayerDTO player = movePlayerMap.get(armyMoveMap.get(army));
				if(debug)log.info("unlink army to player : " + player.getPlayerUID());
				player.getArmyUIDs().remove(army.getArmyUID());
				if(debug)log.info("delete army : " + army.getArmyUID());
				pm.deletePersistent(army);
			}
		}
		
		if(debug)log.info("-----------");
		if(debug)log.info("enregistrement des rapports");
		
//		for(PlayerDTO player : reports.keySet()){
//			ReportDTO reportDTO = new ReportDTO();
//			String report = reports.get(player);
//			
//			String reportUID = Utils.generateUID();
//			Key key = KeyFactory.createKey(ReportDTO.class.getSimpleName(), reportUID);
//
//			reportDTO.setKey(KeyFactory.keyToString(key));
//			reportDTO.setReportUID(reportUID);
//			reportDTO.setReport(report);
//			reportDTO.setStatus(0);
//			
//			player.getReportUIDs().add(reportUID);
//			
//			pm.makePersistent(reportDTO);
//		}
		
		
		if(debug)log.info("-----------");
		
		pm.close();
		
	}


	private void triDesMoves(List<MoveDTO> allMoves, Map<MoveDTO, ArmyDTO> moveArmyMap, Map<MoveDTO, PlayerDTO> movePlayerMap, List<Meeting> meetings) {
		if(debug)log.info("---------------------------------------------------------");
		if(debug)log.info("passage par triDesMoves, allMoves : " + allMoves.size());

		for(MoveDTO move_i : allMoves){
			if(debug)log.info("======================================================================================");
			if(debug)log.info("traitement du move : " + debugMoveIds.get(move_i.getMoveUID()));

			PlayerDTO player = movePlayerMap.get(move_i);
			
			for(MoveDTO move_j : allMoves){
				if(debug)log.info("--------------------------------------------------");
				if(debug)log.info("test de move : " + debugMoveIds.get(move_j.getMoveUID()));
				
				if(move_i.getMoveUID().equals(move_j.getMoveUID()))
					continue;
				
				PlayerDTO opponent = movePlayerMap.get(move_j);
				
				if(opponent.getPlayerUID().equals(player.getPlayerUID())
				|| player.getAllyUIDs().contains(opponent.getPlayerUID()))
							continue;

				if(debug)log.info("--------------------------------------------------");
				if(debug)log.info("recherche de meeting point");
				
				int[] newMeetingPoint = checkMeeting(move_i, 
													 moveArmyMap.get(move_i).getSpeed(), 
													 moveArmyMap.get(move_i).getSize(), 
													 move_j, 
													 moveArmyMap.get(move_j).getSpeed(),
													 moveArmyMap.get(move_j).getSize());
				
				if(newMeetingPoint[0] > 0){
					if(debug)log.info("------------------");
					if(debug)log.info("meeting point found ! [" + newMeetingPoint[0] + "," + newMeetingPoint[1] +"]" );
					
					// on verifie si move_j est deja dans un meeting
					int distance_i = newMeetingPoint[2];
					int distance_j = newMeetingPoint[3];

					if(distance_i <= getCurrentDistance(move_i, meetings)){
						if(debug)log.info("Nouveau meeting possible pour " + debugMoveIds.get(move_i.getMoveUID()));
						
						
						if(distance_j <= getCurrentDistance(move_j, meetings)){
							if(debug)log.info("Nouveau meeting possible pour " + debugMoveIds.get(move_j.getMoveUID()));
							
							
							if(debug)log.info("Le croisement peut avoir lieu, enregistrement du meilleur meeting trouve pour ces 2 moves, so far");
							if(!existsMeeting(meetings, move_j, move_i)){

								if(debug)log.info("Le meeting [" + debugMoveIds.get(move_i.getMoveUID()) + "," + debugMoveIds.get(move_j.getMoveUID()) + "] n'existe pas encore");
								removeFromMeeting(meetings, move_j, newMeetingPoint);
								removeFromMeeting(meetings, move_j, newMeetingPoint);
								meetings.add(new Meeting(move_i, move_j, newMeetingPoint));
								
								if(debug)log.info("-----------------");
								if(debug)log.info("Nouvel etat des meeting enregistres");
								
								int i= 0;
								for(Meeting meeting : meetings){
									if(debug)log.info("------");
									if(debug)log.info((++i)+"");
									if(debug)log.info("[" + debugMoveIds.get(meeting.move1.getMoveUID()) + "," + debugMoveIds.get(meeting.move2.getMoveUID()) + "] | d1 : " + meeting.distance1 + ", d2 : " +  meeting.distance2);
								}
							}
							else
								if(debug)log.info("Le meeting [" + debugMoveIds.get(move_i.getMoveUID()) + "," + debugMoveIds.get(move_j.getMoveUID()) + "] est deja enregistre");
							
						}
						else
							if(debug)log.info("Le croisement n'aura pas lieu car move_j croise un autre move en amont");
					}
					else
						if(debug)log.info("Le croisement n'aura pas lieu car move_i croise un autre move en amont");
					
					
				}
			}
		}

		return;
			
	}
	
	/**
	 * pour chercher si le meeting j.i existe deja, et ne pas enregistrer meeting i.j
	 * @param meetings
	 * @param move_j
	 * @param move_i
	 * @return
	 */
	private boolean existsMeeting(List<Meeting> meetings, MoveDTO move_j, MoveDTO move_i) {
		
		for(Meeting meeting : meetings){
			if(meeting.move1.getMoveUID().equals(move_j.getMoveUID())
			&& meeting.move2.getMoveUID().equals(move_i.getMoveUID()))
				return true;
		}
		
		return false;
	}

	/*
	 * remove a meeting, and return the other move to be recorded as 'toRecalculate'
	 */
	private void removeFromMeeting(List<Meeting> meetings, MoveDTO move, int[] newMeetingPoint) {
		if(debug)log.info("try remove to remove a meeting containing " + debugMoveIds.get(move.getMoveUID()));
		Meeting meetingToRemove = null;
		
		for(Meeting meeting : meetings){
			if(meeting.contains(move)){
				meetingToRemove = meeting;
				break;
			}
		}

		if(meetingToRemove != null){
			if(debug)log.info("check if the meeting happens in the same place as the registered one");
			if(Math.abs(newMeetingPoint[0] - meetingToRemove.meetingX) < 10
			&&
			  Math.abs(newMeetingPoint[1] - meetingToRemove.meetingY) < 10){
				if(debug)log.info("keep the previous meeting !!");				
			}
			else{
				if(debug)log.info("removing");
				if(debug)log.info("m1 : " + debugMoveIds.get(meetingToRemove.move1.getMoveUID()) + " | d : " + meetingToRemove.distance1);
				if(debug)log.info("m2 : " + debugMoveIds.get(meetingToRemove.move2.getMoveUID()) + " | d : " + meetingToRemove.distance2);
				meetings.remove(meetingToRemove);				
			}
			
		}
		else
			if(debug)log.info("no meeting found");
	}

	private double getCurrentDistance(MoveDTO move, List<Meeting> meetings) {
		if(debug)log.info("getCurrentDistance parmi " + meetings.size() + " meetings");
		
		for(Meeting meeting : meetings){
			if(meeting.contains(move)){
				if(debug)log.info("move found : " + debugMoveIds.get(move.getMoveUID()));
				double distance = meeting.getDistance(move.getMoveUID());
				if(debug)log.info("currentDistance : " + distance);				
				return distance;
			}
		}
		
		if(debug)log.info("move not existing in a meeting yet");
		return 9999999;
	}

	//======================================================================================================//
	

	// test si le move passe par le meeting point
	/*
	 * return x,y,distancePlayerMove,distanceOpponentMove
	 */
	private int[] checkMeeting(MoveDTO playerMove, int playerMoveSpeed, int playerMoveSize, MoveDTO opponentMove, int opponentMoveSpeed, int opponentMoveSize) {

		//-----------------------------------------------------------------------------------------------//

		int[] croisement = new int[4];
		croisement[0] = -1; // x
		croisement[1] = -1; // y

		//-----------------------------------------------------------------------------------------------//
		
		int playerLeft = playerMove.getxFrom() < playerMove.getxTo() ? playerMove.getxFrom() : playerMove.getxTo();
		int playerRight = playerMove.getxFrom() > playerMove.getxTo() ? playerMove.getxFrom() : playerMove.getxTo();
		int opponentLeft = opponentMove.getxFrom() < opponentMove.getxTo() ? opponentMove.getxFrom() : opponentMove.getxTo();
		int opponentRight = opponentMove.getxFrom() > opponentMove.getxTo() ? opponentMove.getxFrom() : opponentMove.getxTo();
		int playerTop = playerMove.getyFrom() < playerMove.getyTo() ? playerMove.getyFrom() : playerMove.getyTo();
		int playerBottom = playerMove.getyFrom() > playerMove.getyTo() ? playerMove.getyFrom() : playerMove.getyTo();
		int opponentTop = opponentMove.getyFrom() < opponentMove.getyTo() ? opponentMove.getyFrom() : opponentMove.getyTo();
		int opponentBottom = opponentMove.getyFrom() > opponentMove.getyTo() ? opponentMove.getyFrom() : opponentMove.getyTo();

		if(debug)log.info("-----------");
		if(debug)log.info("playerLeft : " + playerLeft);
		if(debug)log.info("playerRight : " + playerRight);
		if(debug)log.info("opponentLeft : " + opponentLeft);
		if(debug)log.info("opponentRight : " + opponentRight);
		if(debug)log.info("playerTop : " + playerTop);
		if(debug)log.info("playerBottom : " + playerBottom);
		if(debug)log.info("opponentTop : " + opponentTop);
		if(debug)log.info("opponentBottom : " + opponentBottom);
		if(debug)log.info("-----------");

		//-----------------------------------------------------------------------------------------------//
		
		double d1 = Math.sqrt(Math.pow(playerRight - playerLeft, 2) + Math.pow(playerBottom - playerTop, 2));
		double d2 = Math.sqrt(Math.pow(opponentRight - opponentLeft, 2) + Math.pow(opponentBottom - opponentTop, 2));
		if(debug)log.info("d1+d2 : " + (d1+d2));
		if(debug)log.info("-----------");
		
		//-----------------------------------------------------------------------------------------------//
		// premier check : proximite des moves

		if(debug)log.info("premier check : proximite des moves");
		
		if(Math.abs(opponentRight - playerLeft) > d1+d2 || Math.abs(opponentLeft - playerRight) > d1+d2)
			return croisement;
		if(Math.abs(opponentBottom - playerTop) > d1+d2 || Math.abs(opponentTop - playerBottom) > d1+d2)
			return croisement;

		if(debug)log.info("moves suffisament proches pour tester le croisement");
		
		//-----------------------------------------------------------------------------------------------//
		// test du croisement
		
		if(debug)log.info("Test de croisement");
		
		int x = meetingX(playerMove.getxFrom(), playerMove.getxTo(), playerMove.getyFrom(), playerMove.getyTo(), opponentMove.getxFrom(), opponentMove.getxTo(), opponentMove.getyFrom(), opponentMove.getyTo());
		boolean meeting = x >= 0;
		
		
		
		// verif du croisement selon les y
		// pour cela, choix du move qui a la pente la moins verticale (y le plus fiable)
		// donc on se fie au moins a la difference des xfrom xto, ce qui est une bonne estimation de pente pour nos petits vecteurs
		MoveDTO moveSelectedToCalculatY;
		if(Math.abs(playerMove.getxTo() - playerMove.getxFrom()) > Math.abs(opponentMove.getxTo() - opponentMove.getxFrom()))
			moveSelectedToCalculatY = playerMove;
		else
			moveSelectedToCalculatY = opponentMove;
			
		
		int y = yFx(x, moveSelectedToCalculatY.getxFrom(), moveSelectedToCalculatY.getxTo(), moveSelectedToCalculatY.getyFrom(), moveSelectedToCalculatY.getyTo());
		
		// verif du croisement selon les x
		
		if(x < playerLeft - rayon(playerMoveSize))
			meeting = false;
		else if(x > playerRight + rayon(playerMoveSize))
			meeting = false;
		else if(x < opponentLeft - rayon(opponentMoveSize))
			meeting = false;
		else if(x > opponentRight + rayon(opponentMoveSize))
			meeting = false;

		if(!meeting){
			boolean parallel = false;
			if(debug)log.info("Pas de croisement");
			
			if(x>=0){
				// pas de croisement selon les x
				// verification de la distance du point xMeeting
				// s'il est suffisament loin : test des mouvements presque paralleles !
				if(debug)log.info("On regarde si les mouvements sont presque paralleles");
				
				int topLeft = playerLeft < opponentLeft ? playerLeft : opponentLeft;
				int topRight = playerRight > opponentRight ? playerRight : opponentRight;
				
				
				if(x < topLeft){
					if(debug)log.info("xMeeting est sur la gauche");
					
					int topLeftX = playerLeft < opponentLeft ? playerLeft : opponentLeft;
					int topLeftY = topLeftX == playerLeft ? (playerMove.getxFrom() == playerLeft ? playerMove.getyFrom() : playerMove.getyTo())
														  : (opponentMove.getxFrom() == opponentLeft ? opponentMove.getyFrom() : opponentMove.getyTo());
					
					double distanceToTopLeft = Math.sqrt(Math.pow((x - topLeftX), 2) + Math.pow((y - topLeftY), 2));

					if(distanceToTopLeft > 100){
						if(debug)log.info("xMeeting depasse le coeff ("+distanceToTopLeft+" > 100) ! test paralleles");
						parallel = true;
					}
				}
				else if(x > topRight){
					if(debug)log.info("xMeeting est sur la droite");
					
					int topRightX = playerRight < opponentRight ? playerRight : opponentRight;
					int topRightY = topRightX == playerRight ? (playerMove.getxFrom() == playerRight ? playerMove.getyFrom() : playerMove.getyTo())
														     : (opponentMove.getxFrom() == opponentRight ? opponentMove.getyFrom() : opponentMove.getyTo());
					
					double distanceToTopRight = Math.sqrt(Math.pow((x - topRightX), 2) + Math.pow((y - topRightY), 2));

					if(distanceToTopRight > 100){
						if(debug)log.info("xMeeting depasse le coeff ("+distanceToTopRight+" > 100) ! test paralleles");
						parallel = true;
					}
				}
				else
					if(debug)log.info("xMeeting n'est ni a droite ni a gauche : impossible car sinon croisement");
			}
			else{
				if(debug)log.info("Pente identique ! les mouvements sont paralleles.");								
				parallel = true;
			}
			
			if(parallel)
				return checkMeetingSiLesMovesSontParalleles(playerMove, playerMoveSpeed, playerMoveSize, opponentMove, opponentMoveSpeed, opponentMoveSize);
			else
				return croisement;
		}

		
		if(y < playerTop - rayon(playerMoveSize))
			meeting = false;
		else if(y > playerBottom + rayon(playerMoveSize))
			meeting = false;
		else if(y < opponentTop - rayon(opponentMoveSize))
			meeting = false;
		else if(y > opponentBottom + rayon(opponentMoveSize))
			meeting = false;

		if(!meeting) // pas de croisement selon les y
			return croisement;
		
		//----------------------------------------------------------------------------//
		// croisement - calcul des distances/vitesses pour savoir si il y a conflit

		if(debug)log.info("----------");
		if(debug)log.info("croisement !");
		if(debug)log.info("----------");
		
		double distanceAuPointDeCroisement1 = Math.sqrt(Math.pow((x - playerMove.getxFrom()), 2) + Math.pow((y - playerMove.getyFrom()), 2));
		double distanceAuPointDeCroisement2 = Math.sqrt(Math.pow((x - opponentMove.getxFrom()), 2) + Math.pow((y - opponentMove.getyFrom()), 2));

		double distanceTotaleAParcourir1 = Math.sqrt(Math.pow((playerMove.getxTo() - playerMove.getxFrom()), 2) + Math.pow((playerMove.getyTo() - playerMove.getyFrom()), 2));
		double distanceTotaleAParcourir2 = Math.sqrt(Math.pow((opponentMove.getxTo() - opponentMove.getxFrom()), 2) + Math.pow((opponentMove.getyTo() - opponentMove.getyFrom()), 2));

		if(debug)log.info("distanceAuPointDeCroisement1 : " + distanceAuPointDeCroisement1);
		if(debug)log.info("distanceAuPointDeCroisement2 : " + distanceAuPointDeCroisement2);
		if(debug)log.info("distanceTotaleAParcourir1 : " + distanceTotaleAParcourir1);
		if(debug)log.info("distanceTotaleAParcourir2 : " + distanceTotaleAParcourir2);
		if(debug)log.info("----------");
		
		double temps1 = distanceAuPointDeCroisement1/playerMoveSpeed;
		double temps2 = distanceAuPointDeCroisement2/opponentMoveSpeed;
		if(debug)log.info("temps1 : " + temps1);
		if(debug)log.info("temps2 : " + temps2);
		if(debug)log.info("----------");
		
		boolean conflit = false;
		if(temps1 == 0 && playerMove.getxFrom() == playerMove.getxTo() && playerMove.getyFrom() == playerMove.getyTo()){
			// pas de deplacement de player
			if(debug)log.info("CONFLIT 1 !");
			conflit = true;
		}
		else if(temps2 == 0 && playerMove.getxFrom() == playerMove.getxTo() && playerMove.getyFrom() == playerMove.getyTo()){
			// pas de deplacement de opponent
			if(debug)log.info("CONFLIT 2 !");
			conflit = true;
		}
		else if(temps1 > temps2 && temps1/temps2 < 2){
			if(debug)log.info("CONFLIT 3 !");
			conflit = true;
		}
		else if(temps2 > temps1 && temps2/temps1 < 2){
			if(debug)log.info("CONFLIT 4 !");
			conflit = true;
		}
		else if(distanceTotaleAParcourir1 <= 2.5*rayon(playerMoveSize)){ // coeff 2.5 pour etre large pour les mouvements 'unites fixes' (devrait etre = 2)
			if(debug)log.info("CONFLIT 5 !");
			conflit = true;
		}
		else if(distanceTotaleAParcourir2 <= 2.5*rayon(opponentMoveSize)){
			if(debug)log.info("CONFLIT 6 !");
			conflit = true;
		}
			
		if(conflit){
			if(debug)log.info("CONFLIT !");
			croisement[0] = x;
			croisement[1] = y;
			croisement[2] = (int)distanceAuPointDeCroisement1;
			croisement[3] = (int)distanceAuPointDeCroisement2;
		}
		else
			if(debug)log.info("ouf ! de peu !");
		
		return croisement;
	}
	

	public static void main(String[] args){
		//armyCityMap.get(ennemy).setPopulation((int)(armyCityMap.get(ennemy).getPopulation() - ((double)valueAlly*100/valueEnnemy)*armyCityMap.get(ennemy).getPopulation()));
	}
	
	private int[] checkMeetingSiLesMovesSontParalleles(MoveDTO playerMove, int playerMoveSpeed, int playerMoveSize, MoveDTO opponentMove, int opponentMoveSpeed, int opponentMoveSize) {
		if(debug)log.info("checkMeetingSiLesMovesSontParalleles");
		
		int[] croisement = new int[4];
		croisement[0] = -1; // x
		croisement[1] = -1; // y
		
		// 1 - on check si les 2 pentes sont plutot verticales : rotation repere 90 degre sens horaire
		boolean pentesPlutotVerticales = false;
		
		if(Math.abs(playerMove.getxTo() - playerMove.getxFrom()) < 50 && Math.abs(opponentMove.getxTo() - opponentMove.getxFrom()) < 50){
			pentesPlutotVerticales = true;
			if(debug)log.info("pentesPlutotVerticales");
		}
		
		// 2 - on enregistre les 4 points des moves
		
		int x1From = pentesPlutotVerticales ? playerMove.getyFrom() : playerMove.getxFrom();
		int y1From = pentesPlutotVerticales ? playerMove.getxFrom() : playerMove.getyFrom();
		int x1To = pentesPlutotVerticales ? playerMove.getyTo() : playerMove.getxTo();
		int y1To = pentesPlutotVerticales ? playerMove.getxTo() : playerMove.getyTo();
		int x2From = pentesPlutotVerticales ? opponentMove.getyFrom() : opponentMove.getxFrom();
		int y2From = pentesPlutotVerticales ? opponentMove.getxFrom() : opponentMove.getyFrom();
		int x2To = pentesPlutotVerticales ? opponentMove.getyTo() : opponentMove.getxTo();
		int y2To = pentesPlutotVerticales ? opponentMove.getxTo() : opponentMove.getyTo();
		
		
		// 3 - on check quels points sont au milieu des autres selon les x de notre repere
		
		boolean x1FromEstAuMilieuDuMove2 = false;
		boolean x1ToEstAuMilieuDuMove2 = false;
		
		if(x2To > x1From && x1From > x2From
		|| x2From > x1From && x1From > x2To)
			x1FromEstAuMilieuDuMove2 = true;

		if(x2To > x1To && x1To > x2From
		|| x2From > x1To && x1To > x2To)
			x1ToEstAuMilieuDuMove2 = true;


		// 4 - calcul du point de croisement selon les cas
		
		if(!x1FromEstAuMilieuDuMove2 
		&& !x1ToEstAuMilieuDuMove2){
			if(debug)log.info("les moves sont independants");
			return croisement;
		}
		
		if(x1ToEstAuMilieuDuMove2){
			if(x1FromEstAuMilieuDuMove2){
				// move 1 au milieu de move 2 : croisement a larrivee de move 1 : en x1To
				// il faut calculer la distance y1To et yFx(x1To, move2) et verifier si elle est < somme rayons des armees (les 2 movesize/2)
				if(debug)log.info("move1 au milieu de move2");
				
				int y2Meeting = yFx(x1To, x2From, x2To, y2From, y2To);
				
				return calculDuCroisementParallele(x1To, y1To, y2Meeting, x1From, y1From, x2From, y2From, playerMoveSize, opponentMoveSize, pentesPlutotVerticales);
			}
			else{
				if(x1To > x1From && x2To > x2From){
					if(debug)log.info("mouvements 'paralleles' dans le meme sens, decales et vers la droite");
				}
				else if(x1To < x1From && x2To < x2From){
					if(debug)log.info("mouvements 'paralleles' dans le meme sens, decales et vers la gauche");
				}
				else{
					if(debug)log.info("mouvements 'paralleles' dans un sens oppose !");
					if(debug)log.info("calcul du croisement");
					
					int xMeeting = (x1To + x2To)/2;
					if(debug)log.info("xMeeting : " + xMeeting);

					int y1Meeting = yFx(xMeeting, x1From, x1To, y1From, y1To);
					int y2Meeting = yFx(xMeeting, x2From, x2To, y2From, y2To);

					return calculDuCroisementParallele(xMeeting, y1Meeting, y2Meeting, x1From, y1From, x2From, y2From, playerMoveSize, opponentMoveSize, pentesPlutotVerticales);
				}
			}
		}
		else if(x1FromEstAuMilieuDuMove2){
			if(x1To > x1From && x2To > x2From){
				if(debug)log.info("mouvements 'paralleles' dans le meme sens, decales et vers la droite");
			}
			else if(x1To < x1From && x2To < x2From){
				if(debug)log.info("mouvements 'paralleles' dans le meme sens, decales et vers la gauche");
			}
			else{
				if(debug)log.info("mouvements 'paralleles' dans un sens oppose !");
				if(debug)log.info("calcul du croisement");
				
				int xMeeting = (x1From + x2From)/2;

				int y1Meeting = yFx(xMeeting, x1From, x1To, y1From, y1To);
				int y2Meeting = yFx(xMeeting, x2From, x2To, y2From, y2To);

				return calculDuCroisementParallele(xMeeting, y1Meeting, y2Meeting, x1From, y1From, x2From, y2From, playerMoveSize, opponentMoveSize, pentesPlutotVerticales);
			}
		}
		
		
		return croisement;
	}
	

	
	private int[] calculDuCroisementParallele(int xMeeting, int y1Meeting, int y2Meeting, int x1From, int y1From, int x2From, int y2From, int playerMoveSize, int opponentMoveSize, boolean pentesPlutotVerticales) {
		
		if(debug)log.info("----------");
		if(debug)log.info("calculDuCroisementParallele");
		int distanceAuCroisement = Math.abs(y1Meeting - y2Meeting);
		
		int[] croisement = new int[4];
		croisement[0] = -1; // x
		croisement[1] = -1; // y
		
		if(debug)log.info("xMeeting : " + xMeeting);
		if(debug)log.info("y1Meeting : " + y1Meeting);
		if(debug)log.info("y2Meeting : " + y2Meeting);
		if(debug)log.info("distanceAuCroisement : " + distanceAuCroisement);
		if(debug)log.info("rayon(playerMoveSize) : " + rayon(playerMoveSize));
		if(debug)log.info("rayon(opponentMoveSize) : " + rayon(opponentMoveSize));
		
		if(distanceAuCroisement < rayon(playerMoveSize) + rayon(opponentMoveSize) + 2){ // petite marge de 2 pixels en hauteur, a voir (sinon faire =< au lieu de < )
			if(debug)log.info("croisement !");
			
			double distance1 = Math.sqrt(Math.pow((xMeeting - x1From), 2) + Math.pow((y1Meeting - y1From), 2));
			double distance2 = Math.sqrt(Math.pow((xMeeting - x2From), 2) + Math.pow((y2Meeting - y2From), 2));

			if(debug)log.info("distance1 : " + distance1);
			if(debug)log.info("distance2 : " + distance2);
			if(debug)log.info("CONFLIT PARALLELE !");
			
			int yMeeting = (y1Meeting + y2Meeting)/2;
			if(debug)log.info("yMeeting : " + yMeeting);
			
			// on change de nouveau les x/y si on a change le sens du repere avant
			croisement[0] = pentesPlutotVerticales ? yMeeting 		: xMeeting;
			croisement[1] = pentesPlutotVerticales ? xMeeting 		: yMeeting;
			croisement[2] = pentesPlutotVerticales ? (int)distance2 : (int)distance1;
			croisement[3] = pentesPlutotVerticales ? (int)distance1 : (int)distance2;
			if(debug)log.info("----------");
			
		}
		else{
			if(debug)log.info("pas de croisement");
		}
		
		return croisement;
	}

	/*
	 * rayon d'une armee
	 */
	private static int rayon(int size) {
		return (int) (Math.sqrt(size)/4 + 5); // marge 5 pixels
	}

	private static int meetingX(int x1From, int x1To, int y1From, int y1To, int x2From, int x2To, int y2From, int y2To){
		
//		if(debug)log.info("----------");
//		if(debug)log.info("meetingX");
//		if(debug)log.info("----------");
//		if(debug)log.info("x1From : " + x1From);
//		if(debug)log.info("x1To : " + x1To);
//		if(debug)log.info("y1From : " + y1From);
//		if(debug)log.info("y1To : " + y1To);
//		if(debug)log.info("----------");
//		if(debug)log.info("x2From : " + x2From);
//		if(debug)log.info("x2To : " + x2To);
//		if(debug)log.info("y2From : " + y2From);
//		if(debug)log.info("y2To : " + y2To);
//		if(debug)log.info("----------");
		
		// on evite la division par 0, qui correspond au croisement sur le x du move vertical
		if(x1From == x1To)
			return x1From;
		if(x2From == x2To)
			return x2From;
		
		double pente1 = (double)(y1To - y1From)/(x1To - x1From);
		double pente2 = (double)(y2To - y2From)/(x2To - x2From);
//		if(debug)log.info("pente1 : " + pente1);
//		if(debug)log.info("pente2 : " + pente2);
//		if(debug)log.info("----------");
//		
//		if(debug)log.info("pente2*x2From : " + pente2*x2From);
//		if(debug)log.info("pente1*x1From : " + pente1*x1From);
//		if(debug)log.info(pente1*x1From - pente2*x2From);
		double numerateur = y2From - pente2*x2From - y1From +pente1*x1From;
		double denominateur = pente1 - pente2;
//		if(debug)log.info("numerateur : " + numerateur);
//		if(debug)log.info("denominateur : " + denominateur);
//		if(debug)log.info("----------");
		
		if(denominateur == 0) // meme pente = pas de croisement
			return -1;
			
		int x = (int) (numerateur/denominateur);
		if(debug)log.info("x : " + x);
		if(debug)log.info("----------");
		
		return x;
	}
	
	// calcult de y = f(x), en x, pour la droite dont on donne 2 points
	private int yFx(int x, int x1From, int x1To, int y1From, int y1To){
		
//		if(debug)log.info("----------");
//		if(debug)log.info("yFx");
//		if(debug)log.info("----------");
	
		double pente = (double)(y1To - y1From)/(x1To - x1From);
//		if(debug)log.info("pente : " + pente);
	
	
		int y = (int) (pente * x + y1From - pente * x1From);
		if(debug)log.info("y : " + y);
		if(debug)log.info("----------");
	
		return y;
	}
	
	private int testNewLand(ArmyDTO army, PlayerDTO player) {
		
		if(player.getMerchantUIDs().contains(army.getArmyUID())){
			if(debug)log.info("testNewLand : no need for a merchant caravan");
			return -1;
		}
		
		if(debug)log.info("----------");
		if(debug)log.info("testNewLand");
		// verifie si la case est accessible pour etre rajoutee aux contrees
			
		int boardX = army.getX();
		int boardY = army.getY();
		if(debug)log.info("boardX : " + boardX);
		if(debug)log.info("boardY : " + boardY);

		int landX = (int) Math.floor(boardX/100);	
		int landY = (int) Math.floor(boardY/100);
		if(debug)log.info("landX : " + landX);
		if(debug)log.info("landY : " + landY);
			
		int landExpected = landY*30+landX;
		if(debug)log.info("landExpected : " + landExpected);
		

		boolean landIsExpectedYet = false;
		boolean landIsAccessible = false;
		
		// cest deja une contree enregistree
		if(player.getLands().contains(landExpected))
			landIsExpectedYet = true;
		
		if(!landIsExpectedYet)
			landIsAccessible = testLand(landExpected, player);
			
		if(landIsAccessible){
			if(debug)log.info(landExpected + " is Accessible : added to player lands");
			return landExpected;
		}
		else
			return -1;
	}
	
	
	
	private boolean testLand(int landExpected, PlayerDTO player) {
		boolean landIsAccessible = false;
			
		// si les tests precedents sont ok, on regarde si la contree touche le royaume
		for(int land : player.getLands()){
			if(land == landExpected+1
			|| land == landExpected-1
			|| land == landExpected-30
			|| land == landExpected+30){
				landIsAccessible = true;
				break;
			}
		}
		
		return landIsAccessible;
	}



	private class Conflit{
		int x;
		int y;
		List<ArmyDTO> ennemies = new ArrayList<ArmyDTO>();
		List<ArmyDTO> allies = new ArrayList<ArmyDTO>();
	}
	
	private class Meeting{
		
		int status = 0; // 0 free, 1 already merged (generation des combats) 
		
		int meetingX;
		int meetingY;
		MoveDTO move1;
		double distance1;
		MoveDTO move2;
		double distance2;
		
		public Meeting(MoveDTO move_i, MoveDTO move_j, int[] newMeetingPoint) {
			if(debug)log.info("--------------");
			if(debug)log.info("Nouveau Meeting !");
			move1 = move_i;
			move2 = move_j;
			meetingX = newMeetingPoint[0];
			meetingY = newMeetingPoint[1];
			distance1 = newMeetingPoint[2];
			distance2 = newMeetingPoint[3];
			
			if(debug)log.info("meetingPoint : [" + meetingX + "," + meetingY + "]");	
			if(debug)log.info("[" + debugMoveIds.get(move1.getMoveUID()) + "," + debugMoveIds.get(move2.getMoveUID()) + "] | d1 : " + distance1 + ", d2 : " +  distance2);
		}

		public boolean contains(MoveDTO move) {
			return move.getMoveUID().equals(move1.getMoveUID()) || move.getMoveUID().equals(move2.getMoveUID());
		}

		public double getDistance(String moveUID) {
			if(moveUID.equals(move1.getMoveUID()))
				return distance1;
			else
				return distance2;
				
		}
		
		public boolean equals(Meeting meeting){
			return meeting.move1.getMoveUID().equals(move1.getMoveUID()) && meeting.move2.getMoveUID().equals(move2.getMoveUID());
		}
	}
	
}
