package com.uralys.tribes.domain.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.domain.IMovesManager;
import com.uralys.tribes.entities.Cell;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.ObjectsAltered;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.converters.EntitiesConverter;
import com.uralys.tribes.entities.dto.CellDTO;
import com.uralys.tribes.utils.TribesUtils;
import com.uralys.utils.Utils;

public class MovesManager implements IMovesManager{

	//==================================================================================================//

	public final static boolean debug = true;
	
	//==================================================================================================//

	private IGameDAO gameDao;

	public MovesManager (IGameDAO gameDao){
		this.gameDao = gameDao;
	}

	//==================================================================================================//

	public ObjectsAltered refreshUnitMoves(Unit unit) 
	{
		Utils.print("--------------------");
		Utils.print("refreshUnitMoves");

		//-------------------------------------------------------//
		// - 0 : init des donnees pour le calcul

		Utils.print("--------------------");
		Utils.print(" 0 : init des donnees pour le calcul");
		
		DataContainer dataContainer = new DataContainer();
		
		//-------------------------------------------------------//
		// - 1 : on verifie si on a croisé une unitMet avant
		//		 on fera un refreshUnitMoves pour cet unitMet

		Utils.print("--------------------");
		Utils.print(" 1 : on verifie si on a croisé une unitMet avant");
		
		Unit unitToReplace = resetPreviousMeeting(unit, dataContainer);

		//-------------------------------------------------------//
		// - 2 : on supprime les anciens moves

		Utils.print("--------------------");
		Utils.print(" 2 : on supprime les anciens moves");
		
		gameDao.deleteMoves(unit.getUnitUID());
		
		//-------------------------------------------------------//
		// - 3 : on verifie si on croise une unit sur le trajet

		Utils.print("--------------------");
		Utils.print(" 3 : on verifie si on croise une unit sur le trajet");
		
		Object[] result = getMoveMetAndLastMove(unit, dataContainer);
		
		Move moveMet = (Move) result[0];
		Move lastMove = (Move) result[1];
		
		//-------------------------------------------------------//
		// - 4 : on calcule le resultat du meeting s'il y en a eu un

		if(moveMet != null){
			resolveMeeting(unit, moveMet, dataContainer);
		}
		else{
			Utils.print("--------------------");
			Utils.print(" 4 : pas de meeting");
		}
		
		//-------------------------------------------------------//
		// - 5 : on enregistre tous les nouveaux moves

		Utils.print("--------------------");
		Utils.print(" 5 : on enregistre tous les nouveaux moves");
		
		for(Move move : unit.getMoves()){
			gameDao.createMove(move);
		}

		//-------------------------------------------------------//
		// - 6 : setChallengerAndUpdateUnit 
		//		- updateUnit
		//      - tryToSetChallenger
		//      - set des dataContainer.objectsAltered
		
		Utils.print("--------------------");
		Utils.print(" 6 : setChallengerAndUpdateUnit");

		setChallengerAndUpdateUnit(unit, moveMet, lastMove, dataContainer);

		//-------------------------------------------------------//
		// - 6 : on replace l'ancienne unitMet si elle existait

		if(unitToReplace != null){
			Utils.print("--------------------------------");
			Utils.print(" 6 : replace previous unitMet");
			
			refreshUnitMoves(unitToReplace);
		}
		
		//-------------------------------------------------------//
		
		return dataContainer.objectsAltered;
	}
	

	


	//==================================================================================================//

	private void setChallengerAndUpdateUnit(Unit unit, Move moveMet, Move lastMove, DataContainer dataContainer) 
	{
		// on enregistre la derniere case où on tombe si il n'y a pas de meeting
		if(moveMet == null){
			unit.setCellUIDExpectedForLand(lastMove.getCellUID());

			// on set le nouveau challenger sur la case finale, si elle touche le royaume
			if(unit.getType() == Unit.ARMY)
			{
				CellDTO finalCell = gameDao.tryToSetChallenger(unit, lastMove.getTimeFrom());
				if(finalCell != null){
					// refresh la derniere case dans 'objectsAltered' pour set challenger et timeFromChallenging
					dataContainer.objectsAltered.addCellAltered(EntitiesConverter.convertCellDTO(finalCell));
				}
			}
		}

		// on enregistre tous les parametres modifiés sur notre unitArriving
		gameDao.updateUnit(unit);
		
		// et on enregistre l'unite dans les datacontainer.unitsAltered
		dataContainer.objectsAltered.addUnitAltered(unit);
	}
	
	//-----------------------------------------------------------------------------------//
		private void resolveMeeting(Unit unit, Move moveMet, DataContainer dataContainer) 
	{
		Utils.print("--------------------");
		Utils.print(" 4 : on calcule le resultat du meeting");
		
		Unit unitMet = getUnit(moveMet.getUnitUID(), dataContainer);
		Unit nextUnit;
		Object[] nextUnitAndReport;

		if(debug)Utils.print("meeting avec : " + moveMet.getMoveUID());
		
		if(isGathering(unit, unitMet)){
			if(debug)Utils.print("gathering");
			nextUnitAndReport = createNextUnitFromGathering(unit, unitMet);
		}
		else{
			if(debug)Utils.print("conflict");
			nextUnitAndReport = createNextUnitFromConflict(unit, unitMet);
		}

		nextUnit = (Unit) nextUnitAndReport[0];

		unit.setMessageUID((String) nextUnitAndReport[1]);
		unit.setUnitNextUID(nextUnit.getUnitUID());
		unit.setUnitMetUID(unitMet.getUnitUID());
		
		unitMet.setMessageUID((String) nextUnitAndReport[1]);
		unitMet.setUnitNextUID(nextUnit.getUnitUID());
		unitMet.setUnitMetUID(unit.getUnitUID());
		
		
		if(debug)Utils.print("meeting avec : " + moveMet.getMoveUID());
		gameDao.updateUnit(unitMet);	
	}
	
	//-----------------------------------------------------------------------------------//
	
	private Object[] getMoveMetAndLastMove(Unit unit, DataContainer dataContainer) 
	{
		Move moveMet = null;
		Move lastMove = null;
		
		String allyUID = getAllyUID(unit);
		for(Move move : unit.getMoves())
		{
			if(moveMet != null){
				if(debug)Utils.print("foundAMeeting avant : move devient hidden, continue");
				move.setHidden(true);
				continue;
			}
			
			//-----------------------------------------------------------------------------------//

			Cell cell = getCell(move.getCellUID());

			//-----------------------------------------------------------------------------------//

			boolean[] attackOrDefendACity = checkAttackOrDefendACity(cell, dataContainer, allyUID);
			boolean attackACity = attackOrDefendACity[0];
			//boolean defendACity = attackOrDefendACity[1];
			
			//-----------------------------------------------------------------------------------//
			
			if(cell.getRecordedMoves().size() == 0 && attackACity)
				;//attackACityWithNoArmyInDefense
				
			moveMet = getMoveMet(move, cell, unit, dataContainer);
			
			//-----------------------------------------------------------------------------------//
			
			// on met à jour le contenu de la cellule, puisqu'elle va etre renvoyee à Flex tout de suite
			// la vrai sauvegarde du recordedMove se fera lors du gameDao.createMove dans l'etape 4
			cell.getRecordedMoves().add(move);
			dataContainer.objectsAltered.addCellAltered(cell);
			lastMove = move;
		}
		
		//------------------------------------------//
		
		Object[] result = new Object[2];
		result[0] = moveMet;
		result[1] = lastMove;
		
		return result;
	}

	//-------------------------------------------------------------------------------------//

	private Unit resetPreviousMeeting(Unit unit, DataContainer dataContainer) 
	{
		Unit unitToReplace = null;

		if(unit.getUnitMetUID() != null)
		{
			if(debug)Utils.print("-----");
			if(debug)Utils.print("resetPreviousMeeting");
			
			unitToReplace = getUnit(unit.getUnitMetUID(), dataContainer);
			Unit unitNextToDelete = getUnit(unit.getUnitNextUID(), dataContainer);
			
			if(debug)Utils.print("found a unitToReplace, deleting the previous nextUnit : " + unitToReplace.getUnitMetUID());
			deleteUnit(unitNextToDelete);
			
			if(debug)Utils.print("deleting the messages");
			List<String> unitMessage = new ArrayList<String>();
			unitMessage.add(unit.getMessageUID());
			gameDao.deleteMessages(unit.getPlayer().getUralysUID(), unitMessage);
			
			List<String> unitToReplaceMessage = new ArrayList<String>();
			unitToReplaceMessage.add(unitToReplace.getMessageUID());
			gameDao.deleteMessages(unitToReplace.getPlayer().getUralysUID(), unitToReplaceMessage);
			
			unitToReplace.setUnitMetUID(null); // on va faire un refreshMoves de cette unit plus tard. c'est là qu'on fera son updateUnit
			unitToReplace.setMessageUID(null);
			
			unit.setUnitMetUID(null);
			unit.setMessageUID(null);
		}
		
		return unitToReplace;
	}

	//-------------------------------------------------------------------------------------//

	private String getAllyUID(Unit unit) {
		return unit.getPlayer().getAlly() == null ? unit.getPlayer().getUralysUID() : unit.getPlayer().getAlly().getAllyUID();
	}

	//-------------------------------------------------------------------------------------//
	
	private Object[] createNextUnitFromConflict(Unit unit, Unit unitMet) {
		// TODO Auto-generated method stub
		return null;
	}

	//-------------------------------------------------------------------------------------//
	
	private Object[] createNextUnitFromGathering(Unit unit, Unit unitMet) {
		// TODO Auto-generated method stub
		return null;
	}

	//-------------------------------------------------------------------------------------//
	
	private boolean isGathering(Unit unit, Unit unitMet) {
		return getAllyUID(unit).equals(getAllyUID(unitMet)); 
	}

	//-------------------------------------------------------------------------------------//
	
	// on doit choisir le plus grand timeFrom  avec timeFrom < celui auquel on arrive
	// et s'il n'y en a pas, le timeFrom le plus petit
	private Move getMoveMet(Move move, Cell cell, Unit unit, DataContainer dataContainer) 
	{
		if(debug)Utils.print("---------------------");
		if(debug)Utils.print("getMoveMet");
		
		Move moveSelected = null;
		ArrayList<Move> movesPossible = getPossibleMoves(move, cell, unit, dataContainer);
	
		boolean thereIsAnEarlierTimeFrom = existAnEarlierTimeFrom(move, movesPossible);
		
		for(Move movePossible : movesPossible)
		{
			if(moveSelected == null)
				moveSelected = movePossible;
			else{
				if(thereIsAnEarlierTimeFrom)
				{
					if(movePossible.getTimeFrom() < move.getTimeFrom() 
					&& moveSelected.getTimeFrom() < movePossible.getTimeFrom())
						moveSelected = movePossible;
				}
				else{
					if(movePossible.getTimeFrom() < moveSelected.getTimeFrom())
						moveSelected = movePossible;
				}
			}
		}
		
		if(debug)Utils.print("moveMet : " + (moveSelected == null ? "None" : moveSelected.getMoveUID()));
		return moveSelected;
	}

	// return true si il y a un move qui a un timeFrom < que celui auquel on arrive
	private boolean existAnEarlierTimeFrom(Move move, ArrayList<Move> movesPossible) 
	{
		for(Move movePossible : movesPossible)
		{
			if(movePossible.getTimeFrom() < move.getTimeFrom())
				return true;
		}
		
		return false;
	}

	//-------------------------------------------------------------------------------------//
	
	private ArrayList<Move> getPossibleMoves(Move move, Cell cell, Unit unit, DataContainer dataContainer) 
	{
		if(debug)Utils.print("---------------------");
		if(debug)Utils.print("getPossibleMoves");
		ArrayList<Move> movesPossible = new ArrayList<Move>(); 
			
		for(Move recordedMove : cell.getRecordedMoves())
		{
			if(recordedMove.isHidden()){
				if(debug)Utils.print("move hidden, continue");
				continue;
			}
			
			if(meetingExpected(move, recordedMove))
			{
				Unit unitMet = getUnit(recordedMove.getUnitUID(), dataContainer);
				boolean sameAlly = areOnSameAlly(unit, unitMet);
				boolean sameType = areOfSameType(unit, unitMet);
				
				if(sameAlly && sameType){
					if(debug)Utils.print("sameAlly + sameType, continue");
					continue;
				}
				
				if(unitMet.getUnitNextUID() != null)
				{
					// ici, la unitMet va devenir une autre unit : nextUnit
					if(debug)Utils.print("check de la nextUnit...");
					
					if(recordedMove.getTimeFrom() > move.getTimeFrom()){
						//mais ce apres l'arrivee de notre nouveau move
						// on croise donc l'unite, et on devra calculer une nouvelle nextUnit si c'est ce move qui est choisi
						if(debug)Utils.print("movePossible !");
						movesPossible.add(recordedMove);
					}
					else
						if(debug)Utils.print("on croisera la nextUnit");
				}
				else{
					// pas de unitNext pour ce movePossible
					if(debug)Utils.print("movePossible !");
					movesPossible.add(recordedMove);
				}
			}
		}
		
		return movesPossible;
	}

	//-------------------------------------------------------------------------------------//
	
	private boolean areOnSameAlly(Unit unit, Unit unitMet) 
	{
		return unit.getPlayer().getAlly() != null 
			 && unitMet.getPlayer().getAlly() != null 
			 && unitMet.getPlayer().getAlly().getAllyUID().equals(unit.getPlayer().getAlly().getAllyUID());
	}

	private boolean areOfSameType(Unit unit, Unit unitMet) {
		return unit.getType() == unitMet.getType();
	}

	//-------------------------------------------------------------------------------------//
	// tests de croisement

	private boolean meetingExpected(Move move, Move recordedMove) 
	{
		boolean meetingHappens = false;
		
		if(move.getTimeTo() == -1 && recordedMove.getTimeTo() == -1)
		{
			if(debug)Utils.print("	les 2 timeTo sont à -1 : croisement car les 2 unites s'arretent sur la meme case");
			meetingHappens = true;
		}
		else
		{
			Move move1 = null;
			Move move2 = null;
			
			// on determine celui qui n'est pas à -1 
			if(move.getTimeTo() != -1){
				move1 = move;
				move2 = recordedMove;
			}
			else{
				move1 = recordedMove;
				move2 = move;
			}
			
			if(move1.getTimeTo() > move2.getTimeFrom() 
					&& (move2.getTimeTo() == -1
							||
							move1.getTimeTo() < move2.getTimeTo()))
				
			{
				if(debug)Utils.print("	croisement lors d'un passage par la case");
				meetingHappens = true;
			}
		}
		
		return meetingHappens;
	}

	//-------------------------------------------------------------------------------------//
	
	private boolean[] checkAttackOrDefendACity(Cell cell, DataContainer dataContainer, String allyUID)
	{
		boolean[] result = new boolean[2];
		result[0] = false;
		result[1] = false;
		       
		if(cell.getCity() != null)
		{
			Player playerOfTheCity = getPlayer(cell.getCity().getOwnerUID(), dataContainer);
			String allyUIDOfTheCity = playerOfTheCity.getAlly() == null ? playerOfTheCity.getUralysUID() : playerOfTheCity.getAlly().getAllyUID(); 
			
			if(!allyUIDOfTheCity.equals(allyUID))
			{
				result[0] = true;
				if(debug)Utils.print("attackACity !!");
			}
			else{
				result[1] = true;
				if(debug)Utils.print("defendACity !!");
			}
		}
		if(!result[0])
			if(debug)Utils.print("NOT attacking a city");
		
		return result;
	}

	//-----------------------------------------------------------------------------------//	
	private Player getPlayer(String uralysUID, DataContainer datacontainer) 
	{
		if(datacontainer.playerAlreadyLoaded.get(uralysUID) == null){
			datacontainer.playerAlreadyLoaded.put(uralysUID, EntitiesConverter.convertPlayerDTO(gameDao.getPlayer(uralysUID), false));
		}
		
		return datacontainer.playerAlreadyLoaded.get(uralysUID);
	}
	
	@SuppressWarnings("unused")
	private boolean contains(List<Unit> units, String unitUID) 
	{
		List<String> unitUIDs = new ArrayList<String>();
				
		for(Unit unit : units){
			unitUIDs.add(unit.getUnitUID());
		}
		
		return unitUIDs.contains(unitUID);
	}
	
	private Cell getCell(String caseUID) {
		return EntitiesConverter.convertCellDTO(gameDao.getCase(TribesUtils.getX(caseUID),TribesUtils.getY(caseUID)));
	}

	private Unit getUnit(String unitUID, DataContainer dataContainer) {
		if(unitUID == null)
			return null;
		
		if(dataContainer != null){
			if(dataContainer.unitsLoaded.get(unitUID) == null){
				dataContainer.unitsLoaded.put(unitUID, EntitiesConverter.convertUnitDTO(gameDao.getUnit(unitUID), true));
			}
			
			return dataContainer.unitsLoaded.get(unitUID);
		}
		else
			return EntitiesConverter.convertUnitDTO(gameDao.getUnit(unitUID), true);

	}

	private void deleteUnit(Unit unit) {
		gameDao.deleteUnit(unit.getPlayer().getUralysUID(), unit.getUnitUID());
	}
	
	//-----------------------------------------------------------------------------------//
	
	private class DataContainer{
		private HashMap<String, Unit> unitsLoaded = new HashMap<String, Unit>();
		private HashMap<String, Player> playerAlreadyLoaded = new HashMap<String, Player>();
		private ObjectsAltered objectsAltered = new ObjectsAltered();
	}

}
