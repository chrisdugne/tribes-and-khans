package com.uralys.tribes.domain.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.domain.IGameManager;
import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Conflict;
import com.uralys.tribes.entities.Equipment;
import com.uralys.tribes.entities.Gathering;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Smith;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.converters.EntitiesConverter;
import com.uralys.tribes.entities.dto.CaseDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.utils.TribesUtils;

public class GameManager implements IGameManager {

	
	/*
	 * 1 case 15min
	 * 4 cases 1h
	 * 96 cases/24h
	 * plateau : 96h : 400x400 = 160000 tuiles
	 * 
	 * pour commencer
	 * joueur i, place en (200 + 10i)(200 + 10i)
	 * 
	 * 
	 * tile : height = 86
	 * 		  width = 50+25+25 = 100
	 * 
	 * 50^2 = 25^2+ x^2 => x=sqrt(2500-625)
	 * sqrt(1875) = 43
	 * 
	 * plateau height : 86 * 400 = 
	 * plateau width  : 100 * 400 = 40000px
	 * 
	 */
	private static final Logger log = Logger.getLogger(GameManager.class.getName());

	//==================================================================================================//
	// game COEFF

	public final static int WHEAT_EARNING_COEFF = 5;
	public final static int WOOD_EARNING_COEFF = 3;
	public final static int IRON_EARNING_COEFF = 2;

	public final static boolean debug = true;
	public final static boolean topdebug = true;

	//==================================================================================================//

	private IGameDAO gameDao;

	public GameManager (IGameDAO gameDao){
		this.gameDao = gameDao;
	}

	//==================================================================================================//
	
	public String createPlayer(String uralysUID, String email) {
		return gameDao.createPlayer(uralysUID, email);			
	}
	
	private HashMap<String, Player> playerAlreadyLoaded = new HashMap<String, Player>();
	public Player getPlayer(String uralysUID, boolean cacheAvailable) {
		
		if(uralysUID.startsWith("p")){
			if(uralysUID.equals("player1"))
				return player1;
			if(uralysUID.equals("player2"))
				return player2;
			if(uralysUID.equals("player3"))
				return player3;
			
			return null;			
		}
		
		if(!cacheAvailable || playerAlreadyLoaded.get(uralysUID) == null)
			playerAlreadyLoaded.put(uralysUID, EntitiesConverter.convertPlayerDTO(gameDao.getPlayer(uralysUID)));
			
		return playerAlreadyLoaded.get(uralysUID);
	}

	public void savePlayer(Player player) {
		if(debug)log.info("-------------------------------------------------");
		if(debug)log.info("Save turn pour joueur : " + player.getName());

		//----------------------------------------------------------------//
		// Cities

		if(debug)log.info("--------------------");
		if(debug)log.info("Saving cities");

		for(City city : player.getCities()){

			if(city.getCityUID().equals("new")){
				gameDao.createCity(city, player.getUralysUID());
			}
			else{
				gameDao.updateCityResources(city);

				//----------------------------------------//
				// Forge
				
				for(Smith smith : city.getSmiths()){
					gameDao.updateSmith(smith.getSmithUID(), smith.getPeople());				
				}

				//----------------------------------------//
				// Equipment stock
				
				for(Equipment stock : city.getEquipmentStock()){
					gameDao.updateStock(stock.getEquimentUID(), stock.getSize());	
				}
			}
		}
		
		//----------------------------------------------------------------//
		// Player
		
		gameDao.updatePlayer(player); // lastTurnPLayed, remove last Reports
	}
	
	//==================================================================================================//

	
	public void createUnit(String uralysUID, Unit unit, String cityUID, boolean needReplacing){

		if(debug)log.info("-----------------------------------");
		if(debug)log.info("createUnit : " + unit.getUnitUID() + " for uralysUID : " + uralysUID);
		
		gameDao.createUnit(unit, cityUID);
		gameDao.linkNewUnit(uralysUID, unit.getUnitUID());

		if(needReplacing){
			if(debug)log.info("placing the unit");
			unitsLoaded = new HashMap<String, Unit>();
			playerAlreadyLoaded = new HashMap<String, Player>();
			placeUnit(unit, unit.getMoves(), new ArrayList<Unit>());
		}
	}
	
	public void updateUnit(Unit unit, String cityUID, boolean needReplacing){

		if(debug)log.info("-----------------------------------");
		if(debug)log.info("updateUnit : " + unit.getUnitUID());

		gameDao.updateUnit(unit, cityUID);
		
		if(needReplacing){
			if(debug)log.info("placing the unit");
			unitsLoaded = new HashMap<String, Unit>();
			playerAlreadyLoaded = new HashMap<String, Player>();
			placeUnit(unit, unit.getMoves(), new ArrayList<Unit>());
		}
	}
	
	public void deleteUnit(String uralysUID, String unitUID){
		gameDao.deleteUnit(uralysUID, unitUID);
	}

	public void deleteMove(String moveUID) {
		gameDao.deleteMove(moveUID);
	}
	
	//==================================================================================================//

	public List<Item> loadItems() {
		List<Item> items = new ArrayList<Item>();

		for(ItemDTO itemDTO : gameDao.loadItems()){
			items.add(EntitiesConverter.convertItemDTO(itemDTO));
		}

		return items;
	}

	//==================================================================================================//

	public List<Case> loadCases(List<String> caseUIDs) {
		List<Case> cases = new ArrayList<Case>();

		for(CaseDTO caseDTO : gameDao.loadCases(caseUIDs)){
			cases.add(EntitiesConverter.convertCaseDTO(caseDTO));
		}

		return cases;
	}
	
	//==================================================================================================//
	// nouvel algo pour le deplacement des unites

	private static Case[][] board = new Case[100][100];
	private static Unit army1;
	private static Unit army2;
	private static Unit army3;

	private static Player player1;
	private static Player player2;
	private static Player player3;
	private static boolean debugLoop = false;

	public static void main(String[] args){

		if(topdebug)log.info("entree dans placeUnit | " + new Date());
		//-----------------------------------------------------------------------------------//
		
		debugLoop = true;
		
		GameManager game = new GameManager(null);

		for(int i=0; i<100; i++){
			for(int j=0; j<100; j++){
				board[i][j] = new Case(i,j);
			}
		}

		//-----------------------------------------------------------------------------------//

		player1 = new Player();
		player1.setUralysUID("player1");
		player1.setAllyUID("alliance1");

		//-----------------------------------------------------------------------------------//

		player2 = new Player();
		player2.setUralysUID("player2");
		player2.setAllyUID("alliance2");

		//-----------------------------------------------------------------------------------//

		player3 = new Player();
		player3.setUralysUID("player3");
		player3.setAllyUID("alliance2");

		//-----------------------------------------------------------------------------------//

		army1 = new Unit();
		army1.setUnitUID("army1");
		army1.setValue(50);
		army1.setType(Unit.ARMY);
		army1.setPlayerUID(player1.getUralysUID());

		//-----------------------------------------------------------------------------------//

		army2 = new Unit();
		army2.setUnitUID("army2");
		army2.setValue(230);
		army2.setType(Unit.ARMY);
		army2.setPlayerUID(player2.getUralysUID());

		//-----------------------------------------------------------------------------------//

		army3 = new Unit();
		army3.setUnitUID("army3");
		army3.setValue(120);
		army3.setType(Unit.ARMY);
		army3.setPlayerUID(player3.getUralysUID());

		//-----------------------------------------------------------------------------------//
		// le joueur cree son mouvement pour army1
		// le premier move est sur la case actuelle de l'unite, de 'now' à 'now + temps_de_deplacement' 

		ArrayList<Move> moves1 = new ArrayList<Move>();
		
//		NewMove move10 = new NewMove(1,0,0,1);
//		move10.setMoveUID("move10");
//		NewMove move11 = new NewMove(1,1,1,3);
//		move11.setMoveUID("move11");
//		NewMove move12 = new NewMove(1,2,3,5);
//		move12.setMoveUID("move12");
//		NewMove move13 = new NewMove(1,3,5,7);
//		move13.setMoveUID("move13");
//		NewMove move14 = new NewMove(2,3,7,9);
//		move14.setMoveUID("move14");
//		NewMove move15 = new NewMove(2,4,9,11);
//		move15.setMoveUID("move15");
//
//		moves1.add(move10);
//		moves1.add(move11);
//		moves1.add(move12);
//		moves1.add(move13);
//		moves1.add(move14);
//		moves1.add(move15);

		Move move10 = new Move(3,3,0,1);
		move10.setMoveUID("move10");
		Move move11 = new Move(3,4,1,5);
		move11.setMoveUID("move11");
		Move move12 = new Move(3,5,5,7);
		move12.setMoveUID("move12");
		
		moves1.add(move10);
		moves1.add(move11);
		moves1.add(move12);
		
		//-----------------------------------------------------------------------------------//
		// le joueur cree son mouvement pour army2

		ArrayList<Move> moves2 = new ArrayList<Move>();

//		NewMove move20 = new NewMove(2,7,0,2);
//		move20.setMoveUID("move20");
//		NewMove move21 = new NewMove(2,6,2,4);
//		move21.setMoveUID("move21");
//		NewMove move22 = new NewMove(2,5,4,6);
//		move22.setMoveUID("move22");
//		NewMove move23 = new NewMove(2,4,6,8);
//		move23.setMoveUID("move23");
//		NewMove move24 = new NewMove(2,3,8,10);
//		move24.setMoveUID("move24");
//		NewMove move25 = new NewMove(2,2,10,12);
//		move25.setMoveUID("move25");
//
//		moves2.add(move20);
//		moves2.add(move21);
//		moves2.add(move22);
//		moves2.add(move23);
//		moves2.add(move24);
//		moves2.add(move25);
		

		Move move20 = new Move(4,4,0,1);
		move20.setMoveUID("move20");
		Move move21 = new Move(3,4,1,6);
		move21.setMoveUID("move21");
		Move move22 = new Move(2,4,6,8);
		move22.setMoveUID("move22");
		
		moves2.add(move20);
		moves2.add(move21);
		moves2.add(move22);

		//-----------------------------------------------------------------------------------//
		// le joueur cree son mouvement pour army3

		ArrayList<Move> moves3 = new ArrayList<Move>();

//		NewMove move30 = new NewMove(7,6,-9,-7);
//		move30.setMoveUID("move30");
//		NewMove move31 = new NewMove(6,6,-7,-5);
//		move31.setMoveUID("move31");
//		NewMove move32 = new NewMove(5,6,-5,-3);
//		move32.setMoveUID("move32");
//		NewMove move33 = new NewMove(4,6,-3,-1);
//		move33.setMoveUID("move33");
//		NewMove move34 = new NewMove(3,6,-1,1);
//		move34.setMoveUID("move34");
//		NewMove move35 = new NewMove(2,6,1,3);
//		move35.setMoveUID("move35");
//		NewMove move36 = new NewMove(1,6,1,3);
//		move36.setMoveUID("move36");
//		NewMove move37 = new NewMove(0,6,1,3);
//		move37.setMoveUID("move37");
//
//		moves3.add(move30);
//		moves3.add(move31);
//		moves3.add(move32);
//		moves3.add(move33);
//		moves3.add(move34);
//		moves3.add(move35);
//		moves3.add(move36);
//		moves3.add(move37);


		Move move30 = new Move(3,5,0,2);
		move30.setMoveUID("move30");
		Move move31 = new Move(3,4,2,7);
		move31.setMoveUID("move31");
		Move move32 = new Move(4,4,7,9);
		move32.setMoveUID("move32");
		
		moves3.add(move30);
		moves3.add(move31);
		moves3.add(move32);
		
		//-----------------------------------------------------------------------------------//		game.placeUnit(army1, moves1, new ArrayList<Unit>());
		game.placeUnit(army2, moves2, new ArrayList<Unit>());
		game.placeUnit(army3, moves3, new ArrayList<Unit>());

		//-----------------------------------------------------------------------------------//
		
		if(debug)log.info("================================================");
		if(debug)log.info("Affichage final des deplacements");
		
		printMove(army1);
		printMove(army2);
		printMove(army3);
		if(debug)log.info("---");
		printConflict(army1);
		printConflict(army2);
		printConflict(army3);
		//-----------------------------------------------------------------------------------//

	}


	private static void printMove(Unit unit) {
		if(debug)log.info("---");
		if(debug)log.info("moves pour unit : " + unit.getUnitUID());
		
		for(Move move : unit.getMoves()){
			int i = TribesUtils.getX(move.getCaseUID());
			int j = TribesUtils.getY(move.getCaseUID());
			if(debug)log.info("["+i+"]["+j+"] | value : " + move.getValue());
		}
	}
	
	private static void printConflict(Unit unit) {
		if(debug)log.info("---");
		if(debug)log.info((unit.getConflictUIDExpected() == null ? "aucun " : "") + "conflict pour unit : " + unit.getUnitUID());

//		for(Conflict meeting : unit.getConflicts()){
//			int i = getXFromCaseUID(meeting.getCaseUID());
//			int j = getYFromCaseUID(meeting.getCaseUID());
//			if(debug)log.info("["+i+"]["+j+"]");
			
//			for(Unit opponent : meeting.getUnits()){
//				if(debug)log.info("unit : " + opponent.getUnitUID());			
//			}
			
//		}
	}
	
	/**
	 * 02 mai 2011
	 * HYPER IMPORTANT !!
	 * lorsqu'on fait les replaceUnit, on rentre dans de grosses recursions, il suffit de 12-15 pour deja perdre du temps, 
	 * donc un rassemblement de beauvcoup darmees ne peut pas marcher (et cest ce qui risque darriver tres vite )
	 * peut etre une solution : UNIFIER LES GROUPES DARMEES PAR ALLIANCE
	 * il faut une hierarchie d'alliance, pour determiner qui controle la nouvelle armee par defaut
	 * il faut plus tard pouvoir separer une armee en plusieurs si on les deplace (fixer chaque scission a 3 pour ne pas renouveler le probleme, comme ca larmee se divisera par 3, puis par 3 etc et ca sera accessible)
	 * il faut pouvoir donner le control dune armee a qqun de lalliance.
	 * 
	 */
	// le premier Move est la case actuelle ou est posee l'unite avant son depart
	// donc le premier waySafe est bien = true
	private List<Unit> placeUnit(Unit unitArriving, List<Move> moves, List<Unit> unitsMakingThisReplacing) {
		
		
		if(topdebug)log.info("entree dans placeUnit | " + new Date());
		
		if(debug)log.info("---------------------------------------");
		if(debug)log.info("place unit " + unitArriving.getUnitUID());
		
		int currentUnitValue = unitArriving.getValue();
		String allyUID = getPlayer(unitArriving.getPlayerUID(), true).getAllyUID();

		if(debug){
			System.out
			.println("initial value : " + currentUnitValue);
			log.info("ally : " + allyUID);
			log.info("moves to check and record");

			for(Move move : moves){
				int i = TribesUtils.getX(move.getCaseUID());
				int j = TribesUtils.getY(move.getCaseUID());
				
				log.info("["+i+"]["+j+"]");
			}

			log.info("unitsMakingThisReplacing :");
			
			for(Unit unitMakingThisReplacing : unitsMakingThisReplacing){
				log.info(unitMakingThisReplacing.getUnitUID());
			}
		}
		
		List<Unit> unitsReplacedByThisPlacement = new ArrayList<Unit>();

		if(debug)log.info("****");
		List<Unit> previousUnitsMet = checkAndRecalculatePreviousGatheringsAndConflictsAndMovesAndReturnPreviousOpponentsToBeReplaced(unitArriving, unitsMakingThisReplacing);
		if(debug)log.info("****");

		ArrayList<Unit> unitsToReplace = new ArrayList<Unit>();

		if(topdebug)log.info("debut du loop dans les moves  | " + new Date());

		boolean foundAGathering = false;
		for(Move arrivalOnThisCaseMove : moves){

			if(foundAGathering){
				if(debug)log.info("foundAGathering yet => mouvement non appliqué");
				currentUnitValue = 0;
			}
			
			arrivalOnThisCaseMove.setValue(currentUnitValue);
			arrivalOnThisCaseMove.setUnitUID(unitArriving.getUnitUID());
			arrivalOnThisCaseMove.setGathering(new Gathering(allyUID));
			arrivalOnThisCaseMove.getGathering().getUnits().add(unitArriving);
			
			if(debug){
				log.info("gathering : " + arrivalOnThisCaseMove.getGathering().getGatheringUID());
				int i = TribesUtils.getX(arrivalOnThisCaseMove.getCaseUID());
				int j = TribesUtils.getY(arrivalOnThisCaseMove.getCaseUID());
				log.info("passage par case ["+i+"]["+j+"], de temps("+arrivalOnThisCaseMove.getTimeFrom()+") à temps("+arrivalOnThisCaseMove.getTimeTo()+")");
			}

			Case _case = getCase(arrivalOnThisCaseMove.getCaseUID());

			//-----------------------------------------------------------------------------------//

			if(currentUnitValue == 0){
				if(debug)log.info("mouvement non appliqué, pas de calcul de croisement");
			}
			else if(_case.getRecordedMoves().size() == 0){
				if(debug)log.info("case libre");
			}

			if(currentUnitValue > 0 && _case.getRecordedMoves().size() > 0)
			{
				// le move est valable et qu'il y a des recordedMoves sur la case
				// loop sur les recordedMoves pour determiner s'ils ont lieu au meme moment
				for(Move recordedMove : _case.getRecordedMoves())
				{
					String unitRecordedUID = recordedMove.getUnitUID();
					
					if(debug)log.info(" - trouvé un recordedMove : unit " + unitRecordedUID);

					if(recordedMove.getValue() == 0){
						if(debug)log.info("	recordedMove non appliqué (value = 0) : pas de croisement avec ce recordedMove");
						continue;
					}

					if(debug)log.info(" 	recordedMove de ["+new Date(recordedMove.getTimeFrom())+"] à ["+new Date(recordedMove.getTimeTo())+"]");
					
					//---------------------------------------------------------------------//
					// tests de croisement
					
					boolean meetingHappens = false;
					
					if(arrivalOnThisCaseMove.getTimeTo() == -1 && recordedMove.getTimeTo() == -1)
					{
						if(debug)log.info("	les 2 timeTo sont à -1 : croisement car les 2 unites s'arretent sur la meme case");
						meetingHappens = true;
					}
					
					if(!meetingHappens)
					{
						Move move1 = null;
						Move move2 = null;

						// on determine celui qui n'est pas à -1 
						if(arrivalOnThisCaseMove.getTimeTo() != -1){
							move1 = arrivalOnThisCaseMove;
							move2 = recordedMove;
						}
						else{
							move1 = recordedMove;
							move2 = arrivalOnThisCaseMove;
						}

						if(move1.getTimeTo() >= move2.getTimeFrom() 
								&& (move2.getTimeTo() == -1
										||
										move1.getTimeTo() <= move2.getTimeTo()))
							
						{
							if(debug)log.info("	croisement lors d'un passage par la case");
							meetingHappens = true;
						}
					}

					
					//---------------------------------------------------------------------//
					
					if(meetingHappens)
					{
						if(debug)log.info(" - meetingHappens !");
						Gathering existingGathering = recordedMove.getGathering();

						if(existingGathering.getAllyUID().equals(allyUID))
						{
							if(debug)log.info(" - trouvé un ally : unit " + unitRecordedUID);
							Unit unitRecorded = getUnit(unitRecordedUID, true);
							
							if(unitRecorded.getType() != unitArriving.getType()){
								if(debug)log.info("  les unités sont de types differents : pas de gathering");
							}
							else{
								foundAGathering = true;

								if(existingGathering.getUnits().size() == 2){
									if(debug)log.info("  il est deja dans un gathering de 2 units");
									// ici il faut refaire la newarmy1 et faire une newarmy2 avec le premier arrive de ces 2 la 
								}
								else{
									
									if(debug)log.info(" - addUnitInGathering  " + unitRecordedUID);
									arrivalOnThisCaseMove.setGathering(existingGathering);
									gameDao.addUnitInGathering(existingGathering.getGatheringUID(), unitArriving.getUnitUID());
									
									if(debug)log.info(" - creation de la nouvelle army");
									createNewArmy(unitArriving, unitRecorded, arrivalOnThisCaseMove);
									
									unitArriving.setEndTime(arrivalOnThisCaseMove.getTimeFrom());
									unitRecorded.setEndTime(arrivalOnThisCaseMove.getTimeFrom());
									
									unitArriving.setGatheringUIDExpected(existingGathering.getGatheringUID());
									unitRecorded.setGatheringUIDExpected(existingGathering.getGatheringUID());
									
									if(debug)log.info(" - annulation des moves prevus de unitRecorded");
									unitsToReplace.addAll(cancelRecordedMovesAndReturnAllUnitsToReplace(unitRecorded, arrivalOnThisCaseMove.getTimeFrom()));
									
									updateUnit(unitArriving, null, false);
									updateUnit(unitRecorded, null, false);
								}
							}
						}

//						if(debug)log.info(" - enregistrement du gathering dans le meeting");
//						storeNewGatheringInTheConflict(conflict, newMove.getGathering());

//						if(debug)log.info(" - enregistrement de l'unite à replacer");
//						if(unitsMakingThisReplacing.size() == 0 || !contains(unitsMakingThisReplacing, unitRecordedUID)){
//							unitsToReplace.add(getUnit(unitRecordedUID, true));							
//						}
					}
					else{
						if(debug)log.info("	pas de croisement avec ce passage");
					}

				}
				
				// les gatherings sont remplis
				// si il y en a plus de 2 => combats.
//				
//				if(conflict.getGatherings().size() > 1){
//
//					Conflict conflict = new Conflict();
//					conflict.setConflictUID(Utils.generateUID());
//					conflict.setCaseUID(_case.getCaseUID());
//					
//					if(debug)log.info(" - plusieurs gatherings !! => conflit");
//					
//					int[] result = calculateFinalValue(conflict, unit, currentUnitValue, _case);
//					currentUnitValue = result[1];
//					if(result[0] < 0){
//						// on a un Rassemblement
//						// il faut effectuer le gathering
//						
//						
//					}
//					else{
//						// on a un Conflit !
//						unit.setConflictUIDExpected(conflict.getConflictUID());
//					}
//				}
			}

			//-----------------------------------------------------------------------------------//
			// enregistrement du move dans les recordedMoves APRES check de ceux ci (sinon on va le checker avec lui meme)
		
			if(debug)log.info("recording move " + arrivalOnThisCaseMove.getMoveUID());
			if(debugLoop){
				unitArriving.getMoves().add(arrivalOnThisCaseMove);
				_case.getRecordedMoves().add(arrivalOnThisCaseMove);
			}
			else{
				gameDao.saveMove(arrivalOnThisCaseMove);
			}
		
		} // fin du loop sur les moves 

		if(topdebug)log.info("fin du loop dans les moves  | " + new Date());
		
		//-----------------------------------------------------------------------------------//
		// ON REPLACE TOUTES LES UNITES IMPACTEES

		if(topdebug)log.info("debut du replacement  | " + new Date());
		unitsMakingThisReplacing.add(unitArriving);
		
		if(debug)log.info("----------------");
		if(debug)log.info("replacing new opponents : " + unitsToReplace.size());
		
		for(Unit unitToReplace : unitsToReplace){
			if(!contains(unitsReplacedByThisPlacement, unitToReplace.getUnitUID())){
				if(debug)log.info("replacing : " + unitToReplace.getUnitUID());
				List<Unit> unitsReplacedConsequently = placeUnit(unitToReplace, unitToReplace.getMoves(), unitsMakingThisReplacing);
				unitsReplacedByThisPlacement.add(unitToReplace);
				unitsReplacedByThisPlacement.addAll(unitsReplacedConsequently);				
			}
		}
		
		if(debug)log.info("----------------");

		if(debug)log.info("unitsReplacedByThisPlacement :");
		for(Unit unitReplacedByThisPlacement : unitsReplacedByThisPlacement){
			if(debug)log.info(unitReplacedByThisPlacement.getUnitUID());
		}
		
		if(debug)log.info("----------------");
		if(debug)log.info("replacing old opponents : " + previousUnitsMet.size());

		for(Unit previousOpponentToReplace : previousUnitsMet){
			if(!contains(unitsReplacedByThisPlacement, previousOpponentToReplace.getUnitUID())){
				if(debug)log.info("replacing : " + previousOpponentToReplace.getUnitUID());
				List<Unit> unitsReplacedConsequently = placeUnit(previousOpponentToReplace, previousOpponentToReplace.getMoves(), unitsMakingThisReplacing);
				unitsReplacedByThisPlacement.add(previousOpponentToReplace);
				unitsReplacedByThisPlacement.addAll(unitsReplacedConsequently);
			}
		}
		
		if(debug)log.info("----------------");
		if(debug)log.info("fin du placement");
		if(debug)log.info("----------------");

		if(topdebug)log.info("fin du replacement, et du placement  | " + new Date());
		return unitsReplacedByThisPlacement;

	}

	
	private List<Unit> cancelRecordedMovesAndReturnAllUnitsToReplace(Unit unit, long timeFromWhichMovesMustBeCancelled){ 
		if(debug)log.info("cancelRecordedMovesAndReturnAllUnitsToReplace");
		
		List<Unit> unitsToReplace = new ArrayList<Unit>();
		
		for(Move move : unit.getMoves()){
			if(debug)log.info("move " + move.getMoveUID());
			if(move.getTimeFrom() > timeFromWhichMovesMustBeCancelled){
				if(debug)log.info("isCancelled");
				gameDao.unvalidateMove(move);
				
				if(move.getGathering().getUnits().size() > 1)
				{
					Unit unitToReplace;
					if(move.getGathering().getUnits().get(0).getUnitUID().equals(unit))
						unitToReplace = move.getGathering().getUnits().get(1);
					else
						unitToReplace = move.getGathering().getUnits().get(0);

					if(debug)log.info("found a unit to replace : " + unitToReplace.getUnitUID());
					unitsToReplace.add(unitToReplace);
				}
				
			}
			else
				if(debug)log.info("is still available");
		}

		return unitsToReplace;
	}

	private void createNewArmy(Unit unitArriving, Unit unitRecorded, Move arrivalOnThisCaseMove) {
		
		// TODO : le ownerUID doit etre celui du plus haut placé des deux alliés
		String ownerUID = unitArriving.getPlayerUID();

		Unit newArmUnit = new Unit();
		newArmUnit.setUnitUID(ownerUID+"_X_"+arrivalOnThisCaseMove.getTimeFrom());
		newArmUnit.setBeginTime(arrivalOnThisCaseMove.getTimeFrom());
		newArmUnit.setStatus(unitArriving.getStatus());
		newArmUnit.setSize(unitArriving.getSize() + unitRecorded.getSize());
	
		newArmUnit.setSpeed(unitArriving.getSpeed());
		newArmUnit.setType(unitArriving.getType());
		
		ArrayList<Move> moves = new ArrayList<Move>();
		
		arrivalOnThisCaseMove.setMoveUID(arrivalOnThisCaseMove+"_"+TribesUtils.getX(arrivalOnThisCaseMove.getCaseUID())+"_"+TribesUtils.getY(arrivalOnThisCaseMove.getCaseUID())+"_"+newArmUnit.getUnitUID());
		arrivalOnThisCaseMove.setUnitUID(newArmUnit.getUnitUID());
		arrivalOnThisCaseMove.setTimeTo(-1);
		
		moves.add(arrivalOnThisCaseMove);
		newArmUnit.setMoves(moves);
		
		List<Equipment> equipments = new ArrayList<Equipment>();
		
		for (Equipment equipment1 : unitArriving.getEquipments()) {
			for (Equipment equipment2 : unitRecorded.getEquipments()) {
				if(equipment1.getItem().getName().equals(equipment2.getItem().getName())){
					equipment1.setSize(equipment1.getSize() + equipment2.getSize());
					equipments.add(equipment1);
				}
			}
		}

		newArmUnit.setEquipments(equipments);
		
		createUnit(ownerUID, newArmUnit, null, false);
	}

//	private void storeNewGatheringInTheConflict(Conflict conflict, Gathering gatheringToRecord) {
//
//		boolean alreadyRecorded = false;
//		for(Gathering gatheringRecorded : conflict.getGatherings())
//		{
//			if(gatheringRecorded.getGatheringUID().equals(gatheringToRecord.getGatheringUID())){
//				alreadyRecorded = true;
//				break;
//			}
//		}
//		
//		if(!alreadyRecorded)
//		{
//			if(debug)log.info("	croisement pendant le passage : enregistrement du meeting");
//			conflict.getGatherings().add(gatheringToRecord);
//		}
//	}

	// - verifie s'il y avait un gathering en aval
	// et return la liste des armees à replacer
	private List<Unit> checkAndRecalculatePreviousGatheringsAndConflictsAndMovesAndReturnPreviousOpponentsToBeReplaced(Unit unit, List<Unit> unitsMakingThisReplacing) 
	{
		if(debug)log.info("checkAndRecalculatePreviousGatheringsAndConflictsAndMovesAndReturnPreviousOpponentsToBeReplaced for unit " + unit.getUnitUID());
		List<Unit> unitsToReplace = new ArrayList<Unit>();

		// d'abord on regarde si il y avait un gathering de prevu
		// si oui, il etait forcement plus loin puisqu'on vient de croiser cette unit et qu'elle s'arrete forcement au gathering.
		if(unit.getGatheringUIDExpected() != null){
			if(debug)log.info("found a gathering expected !");
			Gathering gatheringExpected = EntitiesConverter.convertGatheringDTO(gameDao.getGathering(unit.getGatheringUIDExpected()));
			if(debug)log.info("gathering : " + gatheringExpected.getGatheringUID());

			// on doit modifier ce gathering
			// il faut simplement que l'armee restante (la 2e) devienne gathering.newArmy
			// on met donc toutes les proprietes de l'armee restante dans gathering.newArmy
			// quant a notre unit, elle est enlevee de gathering.units
			// s'il n'y a pas d'autre gathering suivant, ou de conflit prevu sur cette case, on replace l'autre unit pour qu'elle suive le reste de son chemin
			
			Unit unitStaying;
			if(gatheringExpected.getUnits().get(0).getUnitUID().equals(unit.getUnitUID()))
				unitStaying = gatheringExpected.getUnits().get(1);
			else
				unitStaying = gatheringExpected.getUnits().get(0);
			
			if(debug)log.info("unitStaying : " + unitStaying.getUnitUID());
			Unit newUnit = getUnit(gatheringExpected.getNewArmyUID(), true);
			transformUnitInNewUnit(unitStaying, newUnit);
			
			if(debug)log.info("removing the unit");
			gatheringExpected.remove(unit.getUnitUID());
			
			updateUnit(unitStaying, null, false);
			updateUnit(newUnit, null, false);
			
			if(newUnit.getGatheringUIDExpected() != null)
				unitsToReplace.add(unitStaying);
		}
		
		// enuite on regarde si il y avait un conflit de prevu
		// si oui, il etait forcement plus loin puisqu'on vient de croiser cette unit et qu'elle s'arrete forcement au conflit.
		if(unit.getGatheringUIDExpected() != null)
		{
			if(debug)log.info("found a confict expected !");
			Conflict conflictExpected = EntitiesConverter.convertConflictDTO(gameDao.getConflict(unit.getConflictUIDExpected()));
			if(debug)log.info("conflict : " + conflictExpected.getConflictUID());
		
			for(Gathering gathering : conflictExpected.getGatherings())
			{
				if(debug)log.info("found a gathering in a conflict");
				for(Unit opponentOrAlly : gathering.getUnits())
				{
					if(debug)log.info("unit : " + opponentOrAlly.getUnitUID());
//					if(!opponentOrAlly.equals(unit) && !contains(unitsMakingThisReplacing, opponentOrAlly.getUnitUID()))
//					{
//						if(debug)log.info("unit : " + opponentOrAlly.getUnitUID());
//						unitsToReplace.add(opponentOrAlly);
//					}
				}
			}
		}

		// on supprime tous les moves (+ gathering, + case.recordedMove, + unit.move)
		gameDao.deleteMoves(unit.getUnitUID());

		unit.setMoves(new ArrayList<Move>());
		unit.setGatheringUIDExpected(null);
		unit.setConflictUIDExpected(null);
		

		return unitsToReplace;
	}

	/**
	 * on ne change pas la reference 
	 * comme ca la newUnit est toujours liee au gathering1 et appartient à gathering2.units
	 * donc on modifie tous les champs de newUnit
	 * 
	 * le begintime ne change pas.
	 * le endtime ne change pas.
	 * le gatheringUIDExpected ne change pas.
	 * le conflictUIDExpected ne change pas.
	 * 
	 * @param unit
	 * @param newUnit
	 */
	private void transformUnitInNewUnit(Unit unit, Unit newUnit) {
		
		newUnit.setType(unit.getType());
		newUnit.setSize(unit.getSize());
		newUnit.setValue(unit.getValue());
		newUnit.setSpeed(unit.getSpeed());
		newUnit.setStatus(unit.getStatus());

		newUnit.setGold(unit.getGold());
		newUnit.setIron(unit.getIron());
		newUnit.setWheat(unit.getWheat());
		newUnit.setWood(unit.getWood());

		newUnit.setPlayerUID(unit.getPlayerUID());
		
		newUnit.setMoves(unit.getMoves());
		newUnit.setEquipments(newUnit.getEquipments());
		
	}

//	private void removeUnitFromNewArmy(Unit unitToRemove, Unit newArmy) 
//	{
//		newArmy.setValue(newArmy.getValue() - unitToRemove.getValue());
//		newArmy.setGold(newArmy.getGold() - unitToRemove.getGold());
//		newArmy.setIron(newArmy.getIron() - unitToRemove.getIron());
//		newArmy.setWheat(newArmy.getWheat() - unitToRemove.getWheat());
//		newArmy.setWood(newArmy.getWood() - unitToRemove.getWood());
//		newArmy.set(newArmy.get - unitToRemove.get);
//		newArmy.set(newArmy.get - unitToRemove.get);
//		newArmy.set(newArmy.get - unitToRemove.get);
//		
//	}

	private boolean contains(List<Unit> unitsMakingThisReplacing, String unitUID) {
		
		List<String> unitUIDsMakingThisReplacing = new ArrayList<String>();
				
		for(Unit unitMakingThisReplacing : unitsMakingThisReplacing){
			unitUIDsMakingThisReplacing.add(unitMakingThisReplacing.getUnitUID());
		}
		
		return unitUIDsMakingThisReplacing.contains(unitUID);
	}

//
//	private void removeConflictForUnit(Unit unit, String conflictUID) {
//		int indexToRemove = -1;
//
//		for(NewConflict conflict : unit.getConflicts()){
//			if(conflict.getConflictUID().equals(conflictUID)){
//				indexToRemove = unit.getConflicts().indexOf(conflict);
//				break;
//			}
//		}
//
//		if(indexToRemove >= 0){
//			if(debug)log.info("remove conflict " + unit.getConflicts().get(indexToRemove).getConflictUID());
//			unit.getConflicts().remove(indexToRemove);
//		}
//	}

//	private void removeMoveFromRecordedMoves(int i, int j, String moveUID) {
//		Case _case = getCase(i,j);
//		int indexToRemove = -1;
//
//		for(Move recordedMove : _case.getRecordedMoves()){
//			if(recordedMove.getMoveUID().equals(moveUID)){
//				indexToRemove = _case.getRecordedMoves().indexOf(recordedMove);
//				break;
//			}
//		}
//
//		if(indexToRemove >= 0){
//			if(debug)log.info("remove move " + _case.getRecordedMoves().get(indexToRemove).getMoveUID());
//			_case.getRecordedMoves().remove(indexToRemove);
//		}
//	}

	private Case getCase(String caseUID) {
		if(debugLoop){
						
			return board[TribesUtils.getX(caseUID)][TribesUtils.getY(caseUID)];
		}
		else{
			return EntitiesConverter.convertCaseDTO(gameDao.getCase(TribesUtils.getX(caseUID),TribesUtils.getY(caseUID)));
		}
		
	}

//	private boolean sameAlly(Unit unit, Unit opponent) {
//		return unit.getPlayer().getAllyUID().equals(opponent.getPlayer().getAllyUID());
//	}

	private HashMap<String, Unit> unitsLoaded = new HashMap<String, Unit>();
	private Unit getUnit(String unitUID, boolean cacheAvailable) {
		if(debugLoop){
			if(unitUID.equals("army1"))
				return army1;
			if(unitUID.equals("army2"))
				return army2;
			if(unitUID.equals("army3"))
				return army3;

		}

		if(!cacheAvailable || unitsLoaded.get(unitUID) == null){
			unitsLoaded.put(unitUID, EntitiesConverter.convertUnitDTO(gameDao.getUnit(unitUID), true));
		}

		return unitsLoaded.get(unitUID);
	}
	

	//-----------------------------------------------------------------------------------//
	
	@SuppressWarnings("unused")
	private int[] calculateFinalValue(Conflict meeting, Unit unitInspected, int currentUnitValue, Case _case) {

		// result[0] : conflict -1 ou 1 (il y a conflit ou non)
		// result[1] : currentUnitValue
		int[] result = new int[2];
		
		if(debug)log.info("------------------");
		if(topdebug)log.info("debut du calculateFinalValue  | " + new Date());
		if(debug)log.info("calculateFinalValue : unitInspected : " + unitInspected.getUnitUID());
		int allyIndexOfTheUnitInspected = -1;
		
		// --------   rassemblement sur la meme case par une alliance : pas de conflit

		if(meeting.getGatherings().size() == 1){
			if(topdebug)log.info("fin du calculateFinalValue  | " + new Date());		
			if(debug)log.info("rassemblement sur la meme case par une alliance : pas de conflit");
			
			result[0] = -1;
			result[1] = currentUnitValue;
			return result;
		}
		
		// --------   calcul des valeurs des allies

		int[] allyValues = new int[meeting.getGatherings().size()];
		
		int index = 0;
		for(Gathering ally : meeting.getGatherings()){
			int allyValue = 0;

			for(Unit unit : ally.getUnits()){
				// si cest la unitInspected, sa valeur n'est pas encore enregistree dans le 'case.move' du conflit
				// donc on prend sa valeur à l'entree de la case
				// sinon on recupere la valeur de larmee rencontree sur cette case
				if(unit.equals(unitInspected)){
					if(debug)log.info("unitInspected value : " + currentUnitValue);
					allyValue += currentUnitValue;
				}
				else
					allyValue += getValueInTheRecordedMove(unit, meeting.getCaseUID(), _case);
			}
			
			allyValues[index++] = allyValue;
			
		}
		
		// ---------  calculate the winner points remaining
		
		int firstAllyValue = 0;
		int secondAllyValue = 0;
		int indexOfBestAlly = -1;
		
		for(int i=0; i < meeting.getGatherings().size(); i++){
			if(allyValues[i] > firstAllyValue){
				int oldFirstValue = firstAllyValue;
				firstAllyValue = allyValues[i];
				
				if(oldFirstValue > secondAllyValue)
					secondAllyValue = oldFirstValue;
				
				indexOfBestAlly = i;
			}
			else if(allyValues[i] > secondAllyValue){
				secondAllyValue = allyValues[i];
			}
		}
		
		if(debug)log.info("firstAllyValue : " + firstAllyValue);
		if(debug)log.info("secondAllyValue : " + secondAllyValue);

		//----------------------//
		
		if(indexOfBestAlly != allyIndexOfTheUnitInspected){	

			result[0] = -1;
			result[1] = 0;
			return result;
		}
		else{
			
			if(debug)log.info("battle won ! (or draw if returns 0 :p)");
			double rateRemaining = (firstAllyValue - secondAllyValue)/(double)firstAllyValue;
			if(debug)log.info("rateRemaining : " + rateRemaining);
			if(debug)log.info("currentUnitValue returned : " + ((int)(currentUnitValue*rateRemaining)));
			

			result[0] = -1;
			result[1] = (int)(currentUnitValue*rateRemaining);
			return result;
		}
			
		
	}
	
	private int getValueInTheRecordedMove(Unit unit, String caseUID, Case _case) {
		
		for(Move move : _case.getRecordedMoves()){
			if(move.getUnitUID().equals(unit.getUnitUID())){
				if(debug)log.info("value of " + unit.getUnitUID() + " in this conflict : " + move.getValue());
				return move.getValue();
			}
		}
		
		// never 
		if(debug)log.info("??? I said NEVER !");
		return -1;
	}
}
