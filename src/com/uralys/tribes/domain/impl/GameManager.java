package com.uralys.tribes.domain.impl;

import java.util.ArrayList;
import java.util.List;

import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.domain.IGameManager;
import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Conflict;
import com.uralys.tribes.entities.Equipment;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Smith;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.converters.EntitiesConverter;
import com.uralys.tribes.entities.dto.CaseDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.utils.Utils;

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
	//private static final Logger log = Logger.getLogger(GameManager.class.getName());

	//==================================================================================================//
	// game COEFF

	public final static int WHEAT_EARNING_COEFF = 5;
	public final static int WOOD_EARNING_COEFF = 3;
	public final static int IRON_EARNING_COEFF = 2;

	//==================================================================================================//

	private IGameDAO gameDao;

	public GameManager (IGameDAO gameDao){
		this.gameDao = gameDao;
	}

	//==================================================================================================//
	
	public String createPlayer(String uralysUID, String email) {
		return gameDao.createPlayer(uralysUID, email);			
	}
	
	public Player getPlayer(String uralysUID) {
		
		if(uralysUID.startsWith("p")){
			if(uralysUID.equals("player1"))
				return player1;
			if(uralysUID.equals("player2"))
				return player2;
			if(uralysUID.equals("player3"))
				return player3;
			
			return null;			
		}

		
		try{
			return EntitiesConverter.convertPlayerDTO(gameDao.getPlayer(uralysUID));
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public void savePlayer(Player player) {
		System.out.println("-------------------------------------------------");
		System.out.println("Save turn pour joueur : " + player.getName());

		//----------------------------------------------------------------//
		// Cities

		System.out.println("--------------------");
		System.out.println("Saving cities");

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

	public void saveUnit(String uralysUID, Unit unit){
		System.out.println("-----------------------------------");
		System.out.println("saveUnit : " + unit.getUnitUID() + " for uralysUID : " + uralysUID);
		
		if(unit.getUnitUID().startsWith("NEW_")){ // flex newUnit : unitUID = 'playerUralysUID-nbUnits'
			System.out.println("creating the unit");
			
			// remove the 'NEW_' tag
			unit.setUnitUID(unit.getUnitUID().substring(4));
			
			gameDao.createUnit(unit);
			gameDao.linkNewUnit(uralysUID, unit.getUnitUID());
		}
		else{
			System.out.println("updating the unit");

			gameDao.updateUnit(unit);
		}

		//place or replace Unit
		
		System.out.println("placing the unit");
		placeUnit(unit, unit.getMoves(), new ArrayList<Unit>());
	}
	
	public void deleteUnit(String uralysUID, String unitUID){
		gameDao.deleteUnit(uralysUID, unitUID);
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
		army1.setCurrentCaseUID("case_1_0");
		army1.setType(Unit.ARMY);
		army1.setPlayerUID(player1.getUralysUID());

		//-----------------------------------------------------------------------------------//

		army2 = new Unit();
		army2.setUnitUID("army2");
		army2.setValue(230);
		army1.setCurrentCaseUID("case_2_7");
		army2.setType(Unit.ARMY);
		army2.setPlayerUID(player2.getUralysUID());

		//-----------------------------------------------------------------------------------//

		army3 = new Unit();
		army3.setUnitUID("army3");
		army3.setValue(120);
		army1.setCurrentCaseUID("case_7_6");
		army3.setType(Unit.ARMY);
		army3.setPlayerUID(player3.getUralysUID());

		//-----------------------------------------------------------------------------------//
		// le joueur cree son mouvement pour army1
		// le premier move est sur la case actuelle de l'unite, de 'now' ˆ 'now + temps_de_deplacement' 

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
		
		System.out.println("================================================");
		System.out.println("Affichage final des deplacements");
		
		printMove(army1);
		printMove(army2);
		printMove(army3);
		System.out.println("---");
		printConflict(army1);
		printConflict(army2);
		printConflict(army3);
		//-----------------------------------------------------------------------------------//

	}


	private static void printMove(Unit unit) {
		System.out.println("---");
		System.out.println("moves pour unit : " + unit.getUnitUID());
		
		for(Move move : unit.getMoves()){
			int i = move.getX();
			int j = move.getY();
			System.out.println("["+i+"]["+j+"] | value : " + move.getValue());
		}
	}
	
	private static void printConflict(Unit unit) {
		System.out.println("---");
		System.out.println((unit.getConflicts().size() == 0 ? "aucun " : "") + "conflict(s) pour unit : " + unit.getUnitUID());

		for(Conflict conflict : unit.getConflicts()){
			int i = conflict.getCase().getX();
			int j = conflict.getCase().getY();
			System.out.println("["+i+"]["+j+"]");
			
			for(Unit opponent : conflict.getUnits()){
				System.out.println("unit : " + opponent.getUnitUID());			
			}
			
		}
	}

	// le premier Move est la case actuelle ou est posee l'unite avant son depart
	// donc le premier waySafe est bien = true
	private List<Unit> placeUnit(Unit unit, List<Move> moves, List<Unit> unitsMakingThisReplacing) {
		
		System.out.println("---------------------------------------");
		System.out.println("place unit " + unit.getUnitUID());
		
		int currentUnitValue = unit.getValue();
		System.out.println("initial value " + currentUnitValue);
		
		System.out.println("moves to check and record");
		for(Move move : moves){
			int i = move.getX();
			int j = move.getY();

			System.out.println("["+i+"]["+j+"]");
		}

		System.out.println("unitsMakingThisReplacing :");
		for(Unit unitMakingThisReplacing : unitsMakingThisReplacing){
			System.out.println(unitMakingThisReplacing.getUnitUID());
		}
		
		List<Unit> unitsReplacedByThisPlacement = new ArrayList<Unit>();

		System.out.println("----------");
		List<Unit> previousOpponents = resetPreviousConflictsAndMovesAndGetPreviousOpponents(unit, unitsMakingThisReplacing);
		System.out.println("----------");

		ArrayList<Unit> unitsToReplace = new ArrayList<Unit>();

		for(Move newMove : moves){

			newMove.setValue(currentUnitValue);
			newMove.setUnitUID(unit.getUnitUID());

			int i = newMove.getX();
			int j = newMove.getY();

			System.out.println("passage par case ["+i+"]["+j+"], de temps("+newMove.getTimeFrom()+") ˆ temps("+newMove.getTimeTo()+")");
			Case _case = getCase(i,j);

			//-----------------------------------------------------------------------------------//

			if(currentUnitValue == 0){
				System.out.println("mouvement non appliquŽ, pas de calcul de croisement");
			}
			else if(_case.getRecordedMoves().size() == 0){
				System.out.println("case libre");
			}

			if(currentUnitValue > 0 && _case.getRecordedMoves().size() > 0){
				
				Conflict conflict = new Conflict();
				conflict.setConflictUID(Utils.generateUID());
				conflict.setCase(_case);
				conflict.getUnits().add(unit);
				
				for(Move recordedMove : _case.getRecordedMoves()){

					String unitRecordedUID = recordedMove.getUnitUID();
					System.out.println(" - trouvŽ un autre passage : unit " + unitRecordedUID);

					if(recordedMove.getValue() == 0){
						System.out.println("	mouvement non appliquŽ : pas de croisement avec ce passage");
						continue;
					}

					System.out.println(" 	passage de temps("+recordedMove.getTimeFrom()+") ˆ temps("+recordedMove.getTimeTo()+")");

					if((newMove.getTimeFrom() >= recordedMove.getTimeFrom() 
							&& newMove.getTimeFrom() <= recordedMove.getTimeTo())
							||
							(newMove.getTimeTo() >= recordedMove.getTimeFrom() 
									&& newMove.getTimeTo() <= recordedMove.getTimeTo())){
						
						System.out.println("	croisement pendant le passage : enregistrement du conflit");
						conflict.getUnits().add(getUnit(unitRecordedUID));

						if(unitsMakingThisReplacing.size() == 0 || !contains(unitsMakingThisReplacing, unitRecordedUID)){
							unitsToReplace.add(getUnit(unitRecordedUID));							
						}
					}
					else{
						System.out.println("	pas de croisement avec ce passage");
					}

				}
				
				if(conflict.getUnits().size() > 1){
					currentUnitValue = calculateFinalValue(conflict, unit, currentUnitValue);
					unit.getConflicts().add(conflict);
				}
			}

			//-----------------------------------------------------------------------------------//
			// enregistrement du move dans les recordedMoves APRES check de ceux ci (sinon on va le checker avec lui meme)
		
			System.out.println("recording move " + newMove.getMoveUID());
			if(debugLoop){
				unit.getMoves().add(newMove);
				_case.getRecordedMoves().add(newMove);
			}
			else{
				gameDao.saveMove(newMove, unit.getUnitUID());
			}
		
		} // fin du loop sur les moves 

		//-----------------------------------------------------------------------------------//
		// ON REPLACE TOUTES LES UNITES IMPACTEES

		unitsMakingThisReplacing.add(unit);
		
		System.out.println("----------------");
		System.out.println("replacing new opponents : " + unitsToReplace.size());
		
		for(Unit unitToReplace : unitsToReplace){
			if(!contains(unitsReplacedByThisPlacement, unitToReplace.getUnitUID())){
				System.out.println("replacing : " + unitToReplace.getUnitUID());
				List<Unit> unitsReplacedConsequently = placeUnit(unitToReplace, unitToReplace.getMoves(), unitsMakingThisReplacing);
				unitsReplacedByThisPlacement.add(unitToReplace);
				unitsReplacedByThisPlacement.addAll(unitsReplacedConsequently);				
			}
		}
		
		System.out.println("----------------");

		System.out.println("unitsReplacedByThisPlacement :");
		for(Unit unitReplacedByThisPlacement : unitsReplacedByThisPlacement){
			System.out.println(unitReplacedByThisPlacement.getUnitUID());
		}
		
		System.out.println("----------------");
		System.out.println("replacing old opponents : " + previousOpponents.size());

		for(Unit previousOpponentToReplace : previousOpponents){
			if(!contains(unitsReplacedByThisPlacement, previousOpponentToReplace.getUnitUID())){
				System.out.println("replacing : " + previousOpponentToReplace.getUnitUID());
				List<Unit> unitsReplacedConsequently = placeUnit(previousOpponentToReplace, previousOpponentToReplace.getMoves(), unitsMakingThisReplacing);
				unitsReplacedByThisPlacement.add(previousOpponentToReplace);
				unitsReplacedByThisPlacement.addAll(unitsReplacedConsequently);
			}
		}
		
		System.out.println("----------------");
		System.out.println("fin du placement");
		System.out.println("----------------");

		return unitsReplacedByThisPlacement;

	}

	
	
	
	// reset le conflit en memoire
	// et return la liste des armees ennemies a replacer
	private List<Unit> resetPreviousConflictsAndMovesAndGetPreviousOpponents(Unit unit, List<Unit> unitsMakingThisReplacing) {
		System.out.println("resetPreviousConflictAndGetPreviousOpponents for unit " + unit.getUnitUID());

		List<Unit> unitsToReplace = new ArrayList<Unit>();

		for(Conflict conflict : unit.getConflicts()){
			for(Unit opponentOrAlly : conflict.getUnits()){
				if(!opponentOrAlly.equals(unit) && !contains(unitsMakingThisReplacing, opponentOrAlly.getUnitUID())){
					System.out.println("found " + opponentOrAlly.getUnitUID());
					unitsToReplace.add(opponentOrAlly);
				}
			}
		}

		for(Move move : unit.getMoves()){
			gameDao.deleteMove(move.getMoveUID(), move.getCaseUID(), unit.getUnitUID());
		}

		unit.setMoves(new ArrayList<Move>());
		unit.setConflicts(new ArrayList<Conflict>());
		

		return unitsToReplace;
	}

	
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
//			System.out.println("remove conflict " + unit.getConflicts().get(indexToRemove).getConflictUID());
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
//			System.out.println("remove move " + _case.getRecordedMoves().get(indexToRemove).getMoveUID());
//			_case.getRecordedMoves().remove(indexToRemove);
//		}
//	}

	private Case getCase(int i, int j) {
		if(debugLoop)
			return board[i][j];
		else{
			return EntitiesConverter.convertCaseDTO(gameDao.getCase(i,j));
		}
		
	}

//	private boolean sameAlly(Unit unit, Unit opponent) {
//		return unit.getPlayer().getAllyUID().equals(opponent.getPlayer().getAllyUID());
//	}

	private Unit getUnit(String unitUID) {
		if(debugLoop){
			if(unitUID.equals("army1"))
				return army1;
			if(unitUID.equals("army2"))
				return army2;
			if(unitUID.equals("army3"))
				return army3;

		}

		return EntitiesConverter.convertUnitDTO(gameDao.getUnit(unitUID));
	}
	

	//-----------------------------------------------------------------------------------//
	
	private int calculateFinalValue(Conflict conflict, Unit unitInspected, int currentUnitValue) {
		
		ArrayList<ArrayList<Unit>> allies = new ArrayList<ArrayList<Unit>>();
		int allyIndexOfTheUnitInspected = -1;
		
		// --------   rangement dans les allies sur la case
		
		for(Unit unit : conflict.getUnits()){
			int allyIndex = -1;
			
			for(ArrayList<Unit> ally : allies){
				if(getPlayer(ally.get(0).getPlayerUID()).getAllyUID().equals(getPlayer(unit.getPlayerUID()).getAllyUID())){
					allyIndex = allies.indexOf(ally);
					break;
				}
			}
			
			if(allyIndex >= 0)
				allies.get(allyIndex).add(unit);
			else{
				ArrayList<Unit> newAlly = new ArrayList<Unit>();
				newAlly.add(unit);
				allies.add(newAlly);
				allyIndex = allies.size()-1;
			}
			
			if(unit.equals(unitInspected)){
				allyIndexOfTheUnitInspected = allyIndex;
			}
		}
		
		// --------   calcul des valeurs des allies

		int[] allyValues = new int[allies.size()];
		
		for(ArrayList<Unit> ally : allies){
			int allyValue = 0;

			for(Unit unit : ally){
				// si cest la unitInspected, sa valeur n'est pas encore enregistree dans le 'case.move' du conflit
				// donc on prend sa valeur ˆ l'entree de la case
				// sinon on recupere la valeur de larmee rencontree sur cette case
				if(unit.equals(unitInspected)){
					System.out.println("unitInspected value : " + currentUnitValue);
					allyValue += currentUnitValue;
				}
				else
					allyValue += getValueInTheRecordedMove(unit, conflict.getCase());
			}
			
			allyValues[allies.indexOf(ally)] = allyValue;
			
		}
		
		// ---------  calculate the winner points remaining
		
		int firstAllyValue = 0;
		int secondAllyValue = 0;
		int indexOfBestAlly = -1;
		
		for(int i=0; i < allies.size(); i++){
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
		
		System.out.println("firstAllyValue : " + firstAllyValue);
		System.out.println("secondAllyValue : " + secondAllyValue);

		//----------------------//
		
		if(indexOfBestAlly != allyIndexOfTheUnitInspected)
			return 0;
		else{
			
			System.out.println("battle won ! (or draw if returns 0 :p)");
			double rateRemaining = (firstAllyValue - secondAllyValue)/(double)firstAllyValue;
			System.out.println("rateRemaining : " + rateRemaining);
			System.out.println("currentUnitValue returned : " + ((int)(currentUnitValue*rateRemaining)));
			
			
			return (int)(currentUnitValue*rateRemaining);
		}
			
		
	}
	
	private int getValueInTheRecordedMove(Unit unit, Case _case) {
		
		for(Move move : _case.getRecordedMoves()){
			if(move.getUnitUID().equals(unit.getUnitUID())){
				System.out.println("value of " + unit.getUnitUID() + " in this conflict : " + move.getValue());
				return move.getValue();
			}
		}
		
		// never 
		System.out.println("??? I said NEVER !");
		return -1;
	}
}
