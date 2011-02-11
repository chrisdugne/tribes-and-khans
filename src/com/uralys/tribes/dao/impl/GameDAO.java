package com.uralys.tribes.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private static String ITEM_UID_BOW = "12974260733340336278727";
	private static String ITEM_UID_SWORD = "129742608837521210944772";
	private static String ITEM_UID_ARMOR = "129742610263041471525653";
	private static boolean debug = true;

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
		playerDTO.setGold(100);
		
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
		playerDTO.setGold(100);
		
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
			calculateMovesAndFights(players);
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

	
	public void updateArmy(Army army){
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ArmyDTO armyDTO = pm.getObjectById(ArmyDTO.class, army.getArmyUID());
		
		armyDTO.setSize(army.getSize());
		
		if(army.getMoves().size() > 0)
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
			if(debug)System.out.println("delete army : " + armyUID);
			ArmyDTO armyDTO = pm.getObjectById(ArmyDTO.class, armyUID);
			
			Query query = pm.newQuery("select from " + EquipmentDTO.class.getName() + " where :uids.contains(key)");
			List<EquipmentDTO> equipments = (List<EquipmentDTO>) query.execute(armyDTO.getEquipmentUIDs());

			for(EquipmentDTO equipmentDTO : equipments){
				if(debug)System.out.println("delete equipmentDTO : " + equipmentDTO.getEquimentUID());
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

	
	//======================================================================================================//

	private Map<String, Integer> debugMoveIds = new HashMap<String, Integer>();
	
	@SuppressWarnings("unchecked")
	private void calculateMovesAndFights(List<PlayerDTO> players) {
		if(debug)System.out.println("==========================");
		if(debug)System.out.println("calculateMovesAndFights");

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		List<MoveDTO> allMoves = new ArrayList<MoveDTO>();
		List<Meeting> meetings = new ArrayList<Meeting>();
		
		Map<MoveDTO, ArmyDTO> moveArmyMap = new HashMap<MoveDTO, ArmyDTO>();
		Map<ArmyDTO, MoveDTO> armyMoveMap = new HashMap<ArmyDTO, MoveDTO>();
		Map<MoveDTO, PlayerDTO> movePlayerMap = new HashMap<MoveDTO, PlayerDTO>();
		Map<PlayerDTO, ArrayList<Integer>> landsMap = new HashMap<PlayerDTO, ArrayList<Integer>>();
		
		if(debug)System.out.println("-----------");
		if(debug)System.out.println("rangement dans les maps/listes");
		int debugMoveId = 1;
		for(PlayerDTO player : players){
			if(debug)System.out.println("player " + player.getName());
			landsMap.put(player, new ArrayList<Integer>());

			Query query = pm.newQuery("select from " + ArmyDTO.class.getName() + " where :uids.contains(key)");
			List<ArmyDTO> armies = (List<ArmyDTO>) query.execute(player.getArmyUIDs());


			if(debug)System.out.println("nbArmies " + armies.size());
			for(ArmyDTO army : armies){
				if(army.getMoves().size()>0){
					MoveDTO move = army.getMoves().get(0);
					

					// si le mouvement est immobile, on fabrique un mouvement le long du diametre du cercle representant l'entite deplacee 
					// pour simuler le croisement de cette entite.
					// approximation a recalculer avec cosPi/4
					double size = Math.sqrt(army.getSize())/5;
					
					if(move.getxFrom() == move.getxTo() && move.getyFrom() == move.getyTo()){
						move.setxFrom((int)(move.getxFrom() - size));
						move.setxTo((int)(move.getxFrom() + size));
						move.setyFrom((int)(move.getyFrom() - size));
						move.setyTo((int)(move.getyFrom() + size));
					}
					else{
						// on rajoute en gros le rayon en plus a chaque bout du move
						if(move.getxFrom() < move.getxTo()){
							move.setxFrom((int)(move.getxFrom() - size));
							move.setxTo((int)(move.getxFrom() + size));
						}
						else{
							move.setxFrom((int)(move.getxFrom() + size));
							move.setxTo((int)(move.getxFrom() - size));
						}

						if(move.getyFrom() < move.getyTo()){
							move.setyFrom((int)(move.getyFrom() - size));
							move.setyTo((int)(move.getyFrom() + size));
						}
						else{
							move.setyFrom((int)(move.getyFrom() + size));
							move.setyTo((int)(move.getyFrom() - size));
						}
					}
					
					
					debugMoveIds.put(move.getMoveUID(), debugMoveId);
					if(debug)System.out.println("register a move from " + move.getxFrom() + "," + move.getyFrom() + " | to " + move.getxTo() + "," + move.getyTo() + " | " + move.getMoveUID() + " | num : " + debugMoveId++);
					allMoves.add(move);
					moveArmyMap.put(move, army);
					armyMoveMap.put(army, move);
					movePlayerMap.put(move, player);
				}
			}
		}
		

		if(debug)System.out.println("---------------------------------------------------------");
		if(debug)System.out.println("tri");
		triDesMoves(allMoves, moveArmyMap, movePlayerMap, meetings);

		if(debug)System.out.println("---------------------------------------------------------");
		if(debug)System.out.println("tri termine, affichage des meetings");

		for(Meeting meeting : meetings){
			if(debug)System.out.println("[" + debugMoveIds.get(meeting.move1.getMoveUID()) + "," + debugMoveIds.get(meeting.move2.getMoveUID()) + "] | d1 : " + meeting.distance1 + ", d2 : " +  meeting.distance2);
		}

		if(debug)System.out.println("---------------------------------------------------------");
		if(debug)System.out.println("deplacement des troupes");
		

		for(MoveDTO move : allMoves){
			
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
				if(debug)System.out.println("way safe for the move : proceeding to the land calculus");
				int newLand = testNewLand(moveArmyMap.get(move), movePlayerMap.get(move));
				if(newLand>0)
					landsMap.get(movePlayerMap.get(move)).add(newLand);
			}
		}
		
		
		if(debug)System.out.println("---------------------------------------------------------------------");
		if(debug)System.out.println("generation des combats d'apres les meetings");

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
			
			for(Meeting meeting2 : meetings){
				if(meeting1.equals(meeting2))
					continue;
				
				if(meeting1.meetingX == meeting2.meetingX
				&& meeting1.meetingY == meeting2.meetingY
				&& meeting2.status == 0){
					
					PlayerDTO player1 = movePlayerMap.get(meeting2.move1);
					
					if(player1.getPlayerUID().equals(ally.getPlayerUID()) // une de tes armees
					|| player1.getAllyUIDs().contains(ally.getPlayerUID())){ // une armee de tes potes

						conflit.allies.add(moveArmyMap.get(meeting2.move1));
						conflit.ennemies.add(moveArmyMap.get(meeting2.move2));
					}
					else{
						conflit.ennemies.add(moveArmyMap.get(meeting2.move1));						
						conflit.allies.add(moveArmyMap.get(meeting2.move2));
					}
						
					meeting2.status = 1;
				}
			}
		
			conflits.add(conflit);
			meeting1.status = 1;
		}

		
		if(debug)System.out.println("-----------");
		if(debug)System.out.println("les conflits sont ranges !");
		if(debug)System.out.println("-----------");
		
		for(Conflit conflit : conflits){
			if(debug)System.out.println("conflit en [" + conflit.x + "," + conflit.y +"]");
			
			if(debug)System.out.println("Equipe 1");
			for(ArmyDTO army : conflit.allies){
				if(debug)System.out.println(army.getArmyUID());
			}
			
			if(debug)System.out.println("Equipe 2");
			for(ArmyDTO army : conflit.ennemies){
				if(debug)System.out.println(army.getArmyUID());
			}
		}

		if(debug)System.out.println("---------------------------------------------------------------------");
		if(debug)System.out.println("resolution des combats");
		

		if(debug)System.out.println("-----------");
		if(debug)System.out.println("attribution des nouvelles contrees");


		for(PlayerDTO player : landsMap.keySet()){
			for(int landExpected : landsMap.get(player)){
				if(debug)System.out.println("player " + player.getName() + " wants land " + landExpected);

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
					if(debug)System.out.println("land is free");
					player.getLands().add(landExpected);
				}
			}
			
			if(debug)System.out.println("updating gold");
			player.setGold(player.getGold() + player.getLands().size()*10);
		}
		
		if(debug)System.out.println("-----------");
		if(debug)System.out.println("suppression des moves");
		
		// delete the moves
		for(ArmyDTO army : armyMoveMap.keySet()){
			
			Query query = pm.newQuery("select from " + MoveDTO.class.getName() + " where :uids.contains(key)");
			List<MoveDTO> moves = (List<MoveDTO>) query.execute(army.getMoveUIDs());

			MoveDTO moveToDelete = moves.get(0);
			army.getMoveUIDs().remove(moveToDelete.getMoveUID());
			pm.deletePersistent(moveToDelete);
		}
		
		pm.close();
		
	}

	private void triDesMoves(List<MoveDTO> allMoves, Map<MoveDTO, ArmyDTO> moveArmyMap, Map<MoveDTO, PlayerDTO> movePlayerMap, List<Meeting> meetings) {
		if(debug)System.out.println("---------------------------------------------------------");
		if(debug)System.out.println("passage par triDesMoves, allMoves : " + allMoves.size());

		for(MoveDTO move_i : allMoves){
			if(debug)System.out.println("======================================================================================");
			if(debug)System.out.println("traitement du move : " + debugMoveIds.get(move_i.getMoveUID()));

			PlayerDTO player = movePlayerMap.get(move_i);
			
			for(MoveDTO move_j : allMoves){
				if(debug)System.out.println("--------------------------------------------------");
				if(debug)System.out.println("test de move : " + debugMoveIds.get(move_j.getMoveUID()));
				
				if(move_i.getMoveUID().equals(move_j.getMoveUID()))
					continue;
				
				PlayerDTO opponent = movePlayerMap.get(move_j);
				
				if(opponent.getPlayerUID().equals(player.getPlayerUID())
				|| player.getAllyUIDs().contains(opponent.getPlayerUID()))
							continue;

				if(debug)System.out.println("--------------------------------------------------");
				if(debug)System.out.println("recherche demeeting point");
				
				int[] newMeetingPoint = checkMeeting(move_i, 
													 moveArmyMap.get(move_i).getSpeed(), 
													 moveArmyMap.get(move_i).getSize(), 
													 move_j, 
													 moveArmyMap.get(move_j).getSpeed(),
													 moveArmyMap.get(move_j).getSize());
				
				if(newMeetingPoint[0] > 0){
					if(debug)System.out.println("------------------");
					if(debug)System.out.println("meeting point found ! [" + newMeetingPoint[0] + "," + newMeetingPoint[1] +"]" );
					
					// on verifie si move_j est deja dans un meeting
					int distance_i = newMeetingPoint[2];
					int distance_j = newMeetingPoint[3];

					if(distance_i <= getCurrentDistance(move_i, meetings)){
						if(debug)System.out.println("Nouveau meeting possible pour " + debugMoveIds.get(move_i.getMoveUID()));
						
						
						if(distance_j <= getCurrentDistance(move_j, meetings)){
							if(debug)System.out.println("Nouveau meeting possible pour " + debugMoveIds.get(move_j.getMoveUID()));
							
							
							if(debug)System.out.println("Le croisement peut avoir lieu, enregistrement du meilleur meeting trouve pour ces 2 moves, so far");
							if(!existsMeeting(meetings, move_j, move_i)){

								if(debug)System.out.println("Le meeting [" + debugMoveIds.get(move_i.getMoveUID()) + "," + debugMoveIds.get(move_j.getMoveUID()) + "] n'existe pas encore");
								removeFromMeeting(meetings, move_j);
								removeFromMeeting(meetings, move_j);
								meetings.add(new Meeting(move_i, move_j, newMeetingPoint));
								
								if(debug)System.out.println("-----------------");
								if(debug)System.out.println("Nouvel etat des meeting enregistres");
								
								int i= 0;
								for(Meeting meeting : meetings){
									if(debug)System.out.println("------");
									if(debug)System.out.println(++i);
									if(debug)System.out.println("[" + debugMoveIds.get(meeting.move1.getMoveUID()) + "," + debugMoveIds.get(meeting.move2.getMoveUID()) + "] | d1 : " + meeting.distance1 + ", d2 : " +  meeting.distance2);
								}
							}
							
						}
						else
							if(debug)System.out.println("Le croisement n'aura pas lieu car move_j croise un autre move en amont");
					}
					else
						if(debug)System.out.println("Le croisement n'aura pas lieu car move_i croise un autre move en amont");
					
					
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
	private void removeFromMeeting(List<Meeting> meetings, MoveDTO move) {
		if(debug)System.out.println("try remove to remove a meeting containing " + debugMoveIds.get(move.getMoveUID()));
		Meeting meetingToRemove = null;
		
		for(Meeting meeting : meetings){
			if(meeting.contains(move)){
				meetingToRemove = meeting;
				break;
			}
		}

		if(meetingToRemove != null){
			if(debug)System.out.println("removing");
			if(debug)System.out.println("m1 : " + debugMoveIds.get(meetingToRemove.move1.getMoveUID()) + " | d : " + meetingToRemove.distance1);
			if(debug)System.out.println("m2 : " + debugMoveIds.get(meetingToRemove.move2.getMoveUID()) + " | d : " + meetingToRemove.distance2);
			meetings.remove(meetingToRemove);
		}
		else
			if(debug)System.out.println("no meeting found");
	}

	private double getCurrentDistance(MoveDTO move, List<Meeting> meetings) {
		if(debug)System.out.println("getCurrentDistance parmi " + meetings.size() + " meetings");
		
		for(Meeting meeting : meetings){
			if(meeting.contains(move)){
				if(debug)System.out.println("move found : " + debugMoveIds.get(move.getMoveUID()));
				double distance = meeting.getDistance(move.getMoveUID());
				if(debug)System.out.println("currentDistance : " + distance);				
				return distance;
			}
		}
		
		if(debug)System.out.println("move not existing in a meeting yet");
		return 9999999;
	}

	//======================================================================================================//
	

	// test si le move passe par le meeting point
	/*
	 * return x,y,distancePlayerMove,distanceOpponentMove
	 */
	private int[] checkMeeting(MoveDTO playerMove, int playerMoveSpeed, int playerMoveSize, MoveDTO opponentMove, int opponentMoveSpeed, int opponentMoveSize) {
		
		int[] croisement = new int[4];
		croisement[0] = -1; // x
		croisement[1] = -1; // y
		
		int x = meetingX(playerMove.getxFrom(), playerMove.getxTo(), playerMove.getyFrom(), playerMove.getyTo(), opponentMove.getxFrom(), opponentMove.getxTo(), opponentMove.getyFrom(), opponentMove.getyTo());
		boolean meeting = x >= 0;
		
		// verif du croisement selon les x
		int playerLeft = playerMove.getxFrom() < playerMove.getxTo() ? playerMove.getxFrom() : playerMove.getxTo();
		int playerRight = playerMove.getxFrom() > playerMove.getxTo() ? playerMove.getxFrom() : playerMove.getxTo();
		int opponentLeft = opponentMove.getxFrom() < opponentMove.getxTo() ? opponentMove.getxFrom() : opponentMove.getxTo();
		int opponentRight = opponentMove.getxFrom() > opponentMove.getxTo() ? opponentMove.getxFrom() : opponentMove.getxTo();
		
		if(x < playerLeft - playerMoveSize)
			meeting = false;
		else if(x > playerRight + playerMoveSize)
			meeting = false;
		else if(x < opponentLeft - opponentMoveSize)
			meeting = false;
		else if(x > opponentRight + opponentMoveSize)
			meeting = false;

		if(!meeting) // pas de croisement selon les x
			return checkMeetingSiLesMovesSontParalleles(playerMove, playerMoveSpeed, playerMoveSize, opponentMove, opponentMoveSpeed, opponentMoveSize);
		
		
		// verif du croisement selon les y
		// pour cela, choix du move qui a la pente la moins verticale (y le plus fiable)
		// donc on se fie au moins a la difference des xfrom xto, ce qui est une bonne estimation de pente pour nos petits vecteurs
		MoveDTO moveSelectedToCalculatY;
		if(Math.abs(playerMove.getxTo() - playerMove.getxFrom()) > Math.abs(opponentMove.getxTo() - opponentMove.getxFrom()))
			moveSelectedToCalculatY = playerMove;
		else
			moveSelectedToCalculatY = opponentMove;
			
		
		int y = yFx(x, moveSelectedToCalculatY.getxFrom(), moveSelectedToCalculatY.getxTo(), moveSelectedToCalculatY.getyFrom(), moveSelectedToCalculatY.getyTo());
		

		int playerTop = playerMove.getyFrom() < playerMove.getyTo() ? playerMove.getyFrom() : playerMove.getyTo();
		int playerBottom = playerMove.getyFrom() > playerMove.getyTo() ? playerMove.getyFrom() : playerMove.getyTo();
		int opponentTop = opponentMove.getyFrom() < opponentMove.getyTo() ? opponentMove.getyFrom() : opponentMove.getyTo();
		int opponentBottom= opponentMove.getyFrom() > opponentMove.getyTo() ? opponentMove.getyFrom() : opponentMove.getyTo();
		
		
		if(y < playerTop - playerMoveSize)
			meeting = false;
		else if(y > playerBottom + playerMoveSize)
			meeting = false;
		else if(y < opponentTop - opponentMoveSize)
			meeting = false;
		else if(y > opponentBottom + opponentMoveSize)
			meeting = false;

		if(!meeting) // pas de croisement selon les y
			return croisement;
		
		//----------------------------------------------------------------------------//
		// croisement - calcul des distances/vitesses pour savoir si il y a conflit

		if(debug)System.out.println("----------");
		if(debug)System.out.println("croisement !");
		if(debug)System.out.println("----------");
		
		double distance1 = Math.sqrt(Math.pow((x - playerMove.getxFrom()), 2) + Math.pow((y - playerMove.getyFrom()), 2));
		double distance2 = Math.sqrt(Math.pow((x - opponentMove.getxFrom()), 2) + Math.pow((y - opponentMove.getyFrom()), 2));

		if(debug)System.out.println("distance1 : " + distance1);
		if(debug)System.out.println("distance2 : " + distance2);
		if(debug)System.out.println("----------");
		
		double temps1 = distance1/playerMoveSpeed;
		double temps2 = distance2/opponentMoveSpeed;
		if(debug)System.out.println("temps1 : " + temps1);
		if(debug)System.out.println("temps2 : " + temps2);
		if(debug)System.out.println("----------");
		
		if((temps1 == 0 && playerMove.getxFrom() == playerMove.getxTo() && playerMove.getyFrom() == playerMove.getyTo()) // pas de deplacement de player
			||
			(temps2 == 0 && playerMove.getxFrom() == playerMove.getxTo() && playerMove.getyFrom() == playerMove.getyTo()) // pas de deplacement de opponent
			||
			(temps1 > temps2 && temps1/temps2 < 2)
			||
			(temps2 > temps1 && temps2/temps1 < 2)){
			if(debug)System.out.println("CONFLIIIITTT !");
			croisement[0] = x;
			croisement[1] = y;
			croisement[2] = (int)distance1;
			croisement[3] = (int)distance2;
		}
		else
			if(debug)System.out.println("ouf ! de peu !");
		
		return croisement;
	}
	

	public static void main(String[] args){
		int x1From = 1388;
		int x1To = 1253;
		int y1From = 455;
		int y1To = 339;
		int x2From = 2076;
		int x2To = 1935;
		int y2From = 2508;
		int y2To = 2432;
		if(debug)System.out.println(meetingX(x1From, x1To, y1From, y1To, x2From, x2To, y2From, y2To));
	}
	
	private int[] checkMeetingSiLesMovesSontParalleles(MoveDTO playerMove, int playerMoveSpeed, int playerMoveSize, MoveDTO opponentMove, int opponentMoveSpeed, int opponentMoveSize) {
		if(debug)System.out.println("checkMeetingSiLesMovesSontParalleles");
		
		int[] croisement = new int[4];
		croisement[0] = -1; // x
		croisement[1] = -1; // y
		
		// 1 - on check si les 2 pentes sont plutot verticales : rotation repere 90 degre sens horaire
		boolean pentesPlutotVerticales = false;
		
		if(Math.abs(playerMove.getxTo() - playerMove.getxFrom()) < 50 && Math.abs(opponentMove.getxTo() - opponentMove.getxFrom()) < 50){
			pentesPlutotVerticales = true;
			if(debug)System.out.println("pentesPlutotVerticales");
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
		boolean x2FromEstAuMilieuDuMove1 = false;
		boolean x2ToEstAuMilieuDuMove1 = false;
		
		if(x2To > x1From && x1From > x2From
		|| x2From > x1From && x1From > x2To)
			x1FromEstAuMilieuDuMove2 = true;

		if(x2To > x1To && x1To > x2From
		|| x2From > x1To && x1To > x2To)
			x1ToEstAuMilieuDuMove2 = true;

		if(x1To > x2From && x2From > x1From
				|| x1From > x2From && x2From > x1To)
			x2FromEstAuMilieuDuMove1 = true;
		
		if(x1To > x2To && x2To > x1From
				|| x1From > x2To && x2To > x1To)
			x2ToEstAuMilieuDuMove1 = true;
		

		// 4 - calcul du point de croisement selon les cas
		
		if(!x1FromEstAuMilieuDuMove2 
		&& x1ToEstAuMilieuDuMove2
		&& x2FromEstAuMilieuDuMove1
		&& x2ToEstAuMilieuDuMove1){
			if(debug)System.out.println("les moves sont independants");
			return croisement;
		}
		
		if(x1ToEstAuMilieuDuMove2){
			if(x1FromEstAuMilieuDuMove2){
				// move 1 au milieu de move 2 : croisement a larrivee de move 1 : en x1To
				// il faut calculer la distance y1To et yFx(x1To, move2) et verifier si elle est < somme rayons des armees (les 2 movesize/2)
				
			}
		}
		
		
		return croisement;
	}
	

	
	
	private static int meetingX(int x1From, int x1To, int y1From, int y1To, int x2From, int x2To, int y2From, int y2To){
		
//		if(debug)System.out.println("----------");
//		if(debug)System.out.println("meetingX");
//		if(debug)System.out.println("----------");
//		if(debug)System.out.println("x1From : " + x1From);
//		if(debug)System.out.println("x1To : " + x1To);
//		if(debug)System.out.println("y1From : " + y1From);
//		if(debug)System.out.println("y1To : " + y1To);
//		if(debug)System.out.println("----------");
//		if(debug)System.out.println("x2From : " + x2From);
//		if(debug)System.out.println("x2To : " + x2To);
//		if(debug)System.out.println("y2From : " + y2From);
//		if(debug)System.out.println("y2To : " + y2To);
//		if(debug)System.out.println("----------");
		
		// on evite la division par 0, qui correspond au croisement sur le x du move vertical
		if(x1From == x1To)
			return x1From;
		if(x2From == x2To)
			return x2From;
		
		double pente1 = (double)(y1To - y1From)/(x1To - x1From);
		double pente2 = (double)(y2To - y2From)/(x2To - x2From);
//		if(debug)System.out.println("pente1 : " + pente1);
//		if(debug)System.out.println("pente2 : " + pente2);
//		if(debug)System.out.println("----------");
//		
//		if(debug)System.out.println("pente2*x2From : " + pente2*x2From);
//		if(debug)System.out.println("pente1*x1From : " + pente1*x1From);
//		if(debug)System.out.println(pente1*x1From - pente2*x2From);
		double numerateur = y2From - pente2*x2From - y1From +pente1*x1From;
		double denominateur = pente1 - pente2;
//		if(debug)System.out.println("numerateur : " + numerateur);
//		if(debug)System.out.println("denominateur : " + denominateur);
//		if(debug)System.out.println("----------");
		
		if(denominateur == 0) // meme pente = pas de croisement
			return -1;
			
		int x = (int) (numerateur/denominateur);
		if(debug)System.out.println("x : " + x);
		if(debug)System.out.println("----------");
		
		return x;
	}
	
	// calcult de y = f(x), en x, pour la droite dont on donne 2 points
	private int yFx(int x, int x1From, int x1To, int y1From, int y1To){
		
//		if(debug)System.out.println("----------");
//		if(debug)System.out.println("yFx");
//		if(debug)System.out.println("----------");
	
		double pente = (double)(y1To - y1From)/(x1To - x1From);
//		if(debug)System.out.println("pente : " + pente);
	
	
		int y = (int) (pente * x + y1From - pente * x1From);
		if(debug)System.out.println("y : " + y);
		if(debug)System.out.println("----------");
	
		return y;
	}
	
	private int testNewLand(ArmyDTO army, PlayerDTO player) {
		if(debug)System.out.println("----------");
		if(debug)System.out.println("testNewLand");
		if(debug)System.out.println("----------");
		// verifie si la case est accessible pour etre rajoutee aux contrees
			
		int boardX = army.getX();
		int boardY = army.getY();
		if(debug)System.out.println("boardX : " + boardX);
		if(debug)System.out.println("boardY : " + boardY);

		int landX = (int) Math.floor(boardX/100);	
		int landY = (int) Math.floor(boardY/100);
		if(debug)System.out.println("landX : " + landX);
		if(debug)System.out.println("landY : " + landY);
			
		int landExpected = landY*30+landX;
		if(debug)System.out.println("landExpected : " + landExpected);
			
		boolean landIsAccessible = false;
		boolean landIsExpectedYet = false;
			
		// cest deja une contree enregistree
		if(player.getLands().contains(landExpected))
			landIsExpectedYet = true;
			
		// si les tests precedents sont ok, on regarde si la contree touche le royaume
		if(!landIsExpectedYet){
			for(int land : player.getLands()){
				if(land == landExpected+1
				|| land == landExpected-1
				|| land == landExpected-30
				|| land == landExpected+30){
					landIsAccessible = true;
					break;
				}
			}
		}
			
		if(landIsAccessible){
			if(debug)System.out.println(landExpected + " is Accessible : added to player lands");
			return landExpected;
		}
		else
			return -1;
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
			if(debug)System.out.println("--------------");
			if(debug)System.out.println("Nouveau Meeting !");
			move1 = move_i;
			move2 = move_j;
			meetingX = newMeetingPoint[0];
			meetingY = newMeetingPoint[1];
			distance1 = newMeetingPoint[2];
			distance2 = newMeetingPoint[3];
			
			if(debug)System.out.println("meetingPoint : [" + meetingX + "," + meetingY + "]");	
			if(debug)System.out.println("[" + debugMoveIds.get(move1.getMoveUID()) + "," + debugMoveIds.get(move2.getMoveUID()) + "] | d1 : " + distance1 + ", d2 : " +  distance2);
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
