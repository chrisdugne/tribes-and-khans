package com.uralys.tribes.domain.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.domain.IMovesManager;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.MoveResult;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Report;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.converters.EntitiesConverter;
import com.uralys.tribes.entities.dto.AllyDTO;
import com.uralys.tribes.entities.dto.CellDTO;
import com.uralys.tribes.entities.dto.CityDTO;
import com.uralys.tribes.entities.dto.MoveDTO;
import com.uralys.tribes.utils.TribesUtils;
import com.uralys.utils.Utils;

public class MovesManager implements IMovesManager{

	//==================================================================================================//

	public final static boolean debug = true;

	//==================================================================================================//

	private static final int REPORT_GROUND_FIGHT = 1;
	private static final int REPORT_GROUND_GATHERING = 2;
	
	//==================================================================================================//

	private IGameDAO gameDao;

	public MovesManager (IGameDAO gameDao){
		this.gameDao = gameDao;
	}

	//==================================================================================================//

	public MoveResult refreshUnitMoves(Unit unit) 
	{
		if(debug)Utils.print("--------------------");
		if(debug)Utils.print("refreshUnitMoves");
		if(debug)Utils.print("unit : " + unit.getUnitUID());
		if(debug)Utils.print("player : " + unit.getPlayer().getUralysUID());

		//-------------------------------------------------------//
		// - 0 : init des donnees pour le calcul

		if(debug)Utils.print("--------------------");
		if(debug)Utils.print(" 0 : init des donnees pour le calcul");
		
		DataContainer dataContainer = new DataContainer();
		
		//-------------------------------------------------------//
		// - 1 : on verifie si on a croisé une unitMet avant
		//		 on fera un refreshUnitMoves pour cet unitMet

		if(debug)Utils.print("--------------------");
		if(debug)Utils.print(" 1 : on verifie si on a croisé une unitMet avant");
		
		Unit unitToReplace = resetPreviousMeeting(unit, dataContainer);

		//-------------------------------------------------------//
		// - 2 : on supprime les anciens moves

		if(debug)Utils.print("--------------------");
		if(debug)Utils.print(" 2 : on supprime les anciens moves");
		
		gameDao.deleteMoves(unit.getUnitUID());
		
		//-------------------------------------------------------//
		// - 3 : on verifie si on croise une unit sur le trajet

		if(debug)Utils.print("--------------------");
		if(debug)Utils.print(" 3 : on verifie si on croise une unit sur le trajet");
		
		Object[] result = getMoveMetAndLastMove(unit, dataContainer);
		
		Move moveMet = (Move) result[0];
		Move lastMove = (Move) result[1];
		
		//-------------------------------------------------------//
		// - 4 : on calcule le resultat du meeting s'il y en a eu un
		
		if(debug)Utils.print("--------------------");
		if(debug)Utils.print(" 4 : resolveMeeting");
		
		resolveMeeting(unit, moveMet, lastMove, dataContainer);
		
		//-------------------------------------------------------//
		// - 5 : on enregistre tous les nouveaux moves

		if(debug)Utils.print("--------------------");
		if(debug)Utils.print(" 5 : on enregistre tous les nouveaux moves");
		
		for(int i = 0; i < unit.getMoves().size(); i++){
			Move move = unit.getMoves().get(i);
			String nextMoveUID = i == unit.getMoves().size() - 1 ? null : unit.getMoves().get(i+1).getMoveUID(); 
			
			gameDao.createMove(move, nextMoveUID, null);
		}

		//-------------------------------------------------------//
		// - 6 : setChallengerAndUpdateUnit 
		
		if(debug)Utils.print("--------------------");
		if(debug)Utils.print(" 6 : setChallengerAndUpdateUnit");

		setChallengerAndUpdateUnit(unit, moveMet, lastMove, dataContainer);

		//--------------------------------------------------------------//
		// - 7 : on replace l'ancienne unitMet si elle existait, et qu'elle est differente de la nouvelle (sinon on loop sans stop)

		if(moveMet != null && unitToReplace != null && !unitToReplace.getUnitUID().equals(moveMet.getUnitUID())){
			if(debug)Utils.print("--------------------------------");
			if(debug)if(debug)	Utils.print(" 7 : replace previous unitMet");
			
			refreshUnitMoves(unitToReplace);
		}
		
		//-------------------------------------------------------//

		MoveResult moveResult = new MoveResult();
		moveResult.setUnit(unit);
		moveResult.setCellDeparture(EntitiesConverter.convertCellDTO(gameDao.getCell(unit.getMoves().get(0).getCellUID())));

		return moveResult;
	}

	//==================================================================================================//

//	private void checkIfACityWithNoDefenseIsTaken(Unit unit, Move lastMove, DataContainer dataContainer) 
//	{
//		String allyUID = getAllyUID(unit);
//		boolean[] attackOrDefendACity = checkAttackOrDefendACity(lastMove.getCellUID(), dataContainer, allyUID);
//		boolean attackACity = attackOrDefendACity[0];
//		if(attackACity){
//			if(debug)Utils.print("attackACityWithNoArmyInDefense !!");
//			CellDTO cell = getCell(lastMove.getCellUID(), dataContainer);
//			
//			if(debug)Utils.print("getValue(unit) : " + getValue(unit));
//			if(debug)Utils.print("cell.getCity().getPopulation()/10 : " + cell.getCity().getPopulation()/10);
//			
//			Report report = new Report();
//			Unit dummyUnit = dummyUnitMet(cell.getCity(), dataContainer);
//			
//			initReport(report, lastMove.getCellUID(), unit, dummyUnit, REPORT_GROUND_FIGHT, true, false);
//			
//			if(getValue(unit) > getValue(dummyUnit)){
//				gameDao.cityIsTaken(cell.getCity().getCityUID(), unit.getPlayer().getUralysUID(), lastMove.getTimeFrom(), getValue(dummyUnit));
//			}
//			else{
//				
//			}
//			
//			//------------------------------------------------//
//			// envoi des rapports de combats
//			
//			unit.setMessageUID(gameDao.sendReport(report, unit.getPlayer().getUralysUID(), lastMove.getTimeFrom()));
//			gameDao.sendReport(report, dummyUnit.getPlayer().getUralysUID(), lastMove.getTimeFrom());
//		}
//	}
//
//	private Unit dummyUnitMet(CityDTO city, DataContainer dataContainer) 
//	{
//		Unit dummyUnit = new Unit();
//		Player cityOwner = getPlayer(city.getOwnerUID(), dataContainer);
//		
//		dummyUnit.setUnitUID(city.getCityUID());
//		dummyUnit.getPlayer().setUralysUID(cityOwner.getUralysUID());
//		dummyUnit.getPlayer().setName(cityOwner.getName());
//		
//		dummyUnit.setSize(city.getPopulation());
//		dummyUnit.setType(Unit.CITY);
//
//		return dummyUnit;
//	}

	//-----------------------------------------------------------------------------------//

	/**
	 * 	- updateUnit
	 *  - tryToSetChallenger
   	 *	- set des dataContainer.objectsAltered
	 */
	private void setChallengerAndUpdateUnit(Unit unit, Move moveMet, Move lastMove, DataContainer dataContainer) 
	{
		// on enregistre la derniere case où on tombe si il n'y a pas de meeting
		if(moveMet == null){
			unit.setCellUIDExpectedForLand(lastMove.getCellUID());

			// on set le nouveau challenger sur la case finale, si elle touche le royaume
			if(unit.getType() == Unit.ARMY)
				gameDao.tryToSetChallenger(unit, lastMove.getTimeFrom());
		}

		// on enregistre tous les parametres modifiés sur notre unitArriving
		gameDao.updateUnit(unit);
	}
	
	//-----------------------------------------------------------------------------------//
		private void resolveMeeting(Unit unit, Move moveMet, Move lastMove, DataContainer dataContainer) 
	{
		if(moveMet == null){		
			if(debug)Utils.print(" pas de meeting !");
//			checkIfACityWithNoDefenseIsTaken(unit, lastMove, dataContainer);
			return;
		}

		if(debug)Utils.print(" Meeting ! on calcule le resultat du meeting");
		
		Unit unitMet = getUnit(moveMet.getUnitUID(), dataContainer);
		Unit nextUnit;
		Object[] nextUnitAndReport;

		if(debug)Utils.print("meeting avec : " + moveMet.getMoveUID());
		
		if(isGathering(unit, unitMet)){
			if(debug)Utils.print("gathering");
			nextUnitAndReport = createNextUnitFromGathering(unit, unitMet, lastMove.getCellUID());
		}
		else{
			if(debug)Utils.print("conflict");
			nextUnitAndReport = createNextUnitFromConflict(unit, unitMet, lastMove, dataContainer);
		}

		nextUnit = (Unit) nextUnitAndReport[0];
		Report report = (Report) nextUnitAndReport[1];
		finalizeNextUnitCreation(unit, unitMet, nextUnit, report, lastMove, dataContainer);
		
		gameDao.updateUnit(unitMet);	
	}

	//-----------------------------------------------------------------------------------//
	
	private void finalizeNextUnitCreation(Unit unit, Unit unitMet, Unit nextUnit, Report report, Move lastMove, DataContainer dataContainer) 
	{
		if(nextUnit != null)
		{
			nextUnit.setUnitUID(nextUnit.getPlayer().getUralysUID()+"_"+Utils.generatePassword(3)+"_"+lastMove.getTimeFrom());
			nextUnit.setCellUIDExpectedForLand(lastMove.getCellUID());
			
			nextUnit.setBeginTime(lastMove.getTimeFrom());
			
			if(nextUnit.getSize() > 0)
				nextUnit.setEndTime(-1);
			else
				nextUnit.setEndTime(nextUnit.getBeginTime()); // draw
			
			unit.setUnitNextUID(nextUnit.getUnitUID());
			unitMet.setUnitNextUID(nextUnit.getUnitUID());
			
			//------------------------------------------------//
			
			ArrayList<Move> moves = new ArrayList<Move>();
			Move firstMoveForNextUnit = new Move();
			
			firstMoveForNextUnit.setMoveUID(lastMove.getTimeFrom()+"_"+TribesUtils.getX(lastMove.getCellUID())+"_"+TribesUtils.getY(lastMove.getCellUID())+"_"+nextUnit.getUnitUID());
			firstMoveForNextUnit.setUnitUID(nextUnit.getUnitUID());
			firstMoveForNextUnit.setCellUID(lastMove.getCellUID());
			firstMoveForNextUnit.setTimeFrom(lastMove.getTimeFrom());
			firstMoveForNextUnit.setTimeTo(-1);
			
			moves.add(firstMoveForNextUnit);
			nextUnit.setMoves(moves);
			
			//------------------------------------------------//
			
			gameDao.createUnit(nextUnit, null);
			gameDao.linkNewUnit(nextUnit.getPlayer().getUralysUID(), nextUnit.getUnitUID());
			gameDao.createMove(firstMoveForNextUnit, null, null);
			
		}
		// else : attaque/defense d'une cityUnit (ville sans defense) => pas de nextUnit
		
		//------------------------------------------------//

		unit.setEndTime(lastMove.getTimeFrom());
		unit.setUnitMetUID(unitMet.getUnitUID());

		unitMet.setEndTime(lastMove.getTimeFrom());
		unitMet.setUnitMetUID(unit.getUnitUID());

		//------------------------------------------------//
		// envoi des rapports de combats
		
		unit.setMessageUID(gameDao.sendReport(report, unit.getPlayer().getUralysUID(), lastMove.getTimeFrom()));

		if(!unit.getPlayer().getUralysUID().equals(unitMet.getPlayer().getUralysUID()))
			unitMet.setMessageUID(gameDao.sendReport(report, unitMet.getPlayer().getUralysUID(), lastMove.getTimeFrom()));
	}

	//-----------------------------------------------------------------------------------//
	
	private Object[] getMoveMetAndLastMove(Unit unit, DataContainer dataContainer) 
	{
		Move moveMet = null;
		Move lastMove = null;

		for(Move move : unit.getMoves())
		{
			CellDTO cell = getCell(move.getCellUID(), dataContainer);
			moveMet = getMoveMet(move, cell, unit, dataContainer);
			
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
			
			if(unit.getUnitNextUID() != null){
				Unit unitNextToDelete = getUnit(unit.getUnitNextUID(), dataContainer);
				
				if(debug)Utils.print("found a unitToReplace, deleting the previous nextUnit : " + unitNextToDelete.getUnitUID());
				deleteUnit(unitNextToDelete);
			}
			// else : annulation d'une attaque de cityUnit par exemple
			
			if(debug)Utils.print("deleting the messages");
			
			if(unit.getMessageUID() != null){
				List<String> unitMessage = new ArrayList<String>();
				unitMessage.add(unit.getMessageUID());
				gameDao.deleteMessages(unit.getPlayer().getUralysUID(), unitMessage);
			}
			
			// un messageUID peut etre null : dans le cas d'un gathering, on envoie un seul message, pas 2 au meme joueur.
			if(unitToReplace.getMessageUID() != null){
				List<String> unitToReplaceMessage = new ArrayList<String>();
				unitToReplaceMessage.add(unitToReplace.getMessageUID());
				gameDao.deleteMessages(unitToReplace.getPlayer().getUralysUID(), unitToReplaceMessage);
			}
			
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
	
	private Object[] createNextUnitFromConflict(Unit unit, Unit unitMet, Move lastMove, DataContainer dataContainer) 
	{
		Unit nextUnit = null;
		Report report =  new Report();
		
		//------------------------------------//

		String allyUID = getAllyUID(unit);
		boolean[] attackOrDefendACity = checkAttackOrDefendACity(lastMove.getCellUID(), dataContainer, allyUID);
		boolean attackACity = attackOrDefendACity[0];
		boolean defendACity = attackOrDefendACity[1] && unit.getType() == Unit.ARMY;
		if(debug)Utils.print("defendACity : " + defendACity);
		
		//------------------------------------//

		initReport(report, lastMove.getCellUID(), unit, unitMet, MovesManager.REPORT_GROUND_FIGHT, attackACity, defendACity);
		
		//------------------------------------//
		
		Object[] fightResult = fight(unit, unitMet, lastMove, attackACity, defendACity, dataContainer);
		Unit unitRemaining = (Unit) fightResult[0];
		double rateRemaining = (Double) fightResult[1];
		
		//------------------------------------//

		if(unit.getType() != Unit.CITY && unit.getType() != Unit.CITY)
		{
			nextUnit = new Unit();
			nextUnit.setSpeed(unitRemaining.getSpeed());
			nextUnit.setType(unitRemaining.getType());
			nextUnit.setSize((int)(unitRemaining.getSize()*rateRemaining));
			
			nextUnit.setArmors((int)((unit.getArmors() + unitMet.getArmors())*rateRemaining));
			nextUnit.setBows((int)((unit.getBows() + unitMet.getBows())*rateRemaining));
			nextUnit.setSwords((int)((unit.getSwords() + unitMet.getSwords())*rateRemaining));
			
			nextUnit.setWheat(unit.getWheat() + unitMet.getWheat());
			nextUnit.setWood(unit.getWood() + unitMet.getWood());
			nextUnit.setIron(unit.getIron() + unitMet.getIron());
			nextUnit.setGold(unit.getGold() + unitMet.getGold());
			
			nextUnit.setPlayer(unitRemaining.getPlayer());

			finalizeReport(report, nextUnit);
		}

		//------------------------------------//

		Object[] result = new Object[2];
		
		result[0] = nextUnit;
		result[1] = report;
		
		return result;
	}

	//----------------------------------------------------------------------------------------------------------------------------------//
	
	private Object[] fight(Unit unit, Unit unitMet, Move lastMove, boolean attackACity, boolean defendACity, DataContainer dataContainer) 
	{
		Unit unitRemaining;
		Double rateRemaining;
		
		CellDTO cell = getCell(lastMove.getCellUID(), dataContainer);
		CityDTO city = cell.getCity();
		
		int valueOfTheUnit = getValue(unit);
		int valueOfTheEnnemy = getValue(unitMet);

		boolean attackADefendedCity = attackACity && unitMet.getType() == Unit.ARMY;
		
		if(attackADefendedCity){
			if(debug)Utils.print(" - bonus pour l'ennemy dans sa ville " + unitMet.getUnitUID());	
			valueOfTheEnnemy += city.getPopulation()/10;
		}
		else if(defendACity){
			if(debug)Utils.print(" - bonus pour la unit dans sa ville " + unit.getUnitUID());	
			valueOfTheUnit += city.getPopulation()/10;
		}
		// sinon, si on attaque une ville, elle n'est pas defendue, c'est la cityUnit

		if(valueOfTheEnnemy > valueOfTheUnit)
		{
			if(debug)Utils.print(" Combat perdu par unit | valueOfTheUnit : " + valueOfTheUnit + " valueOfTheEnnemy | " + valueOfTheEnnemy);
			rateRemaining = (valueOfTheEnnemy - valueOfTheUnit)/(double)valueOfTheEnnemy;
			if(debug)Utils.print("rateRemaining : "+rateRemaining+" | valeur de l'unite restante : "+ ((int)(valueOfTheEnnemy*rateRemaining)));
			
			unitRemaining = unitMet;
			
			if(defendACity){
				if(debug)Utils.print("The city is lost...");
				gameDao.cityIsTaken(city.getCityUID(), unitMet.getPlayer().getUralysUID(), lastMove.getTimeFrom(), city.getPopulation()/10);
			}
		}
		else{
			if(debug)Utils.print(" Combat gagne par unit | valueOfTheUnit : " + valueOfTheUnit + " valueOfTheEnnemy | " + valueOfTheEnnemy);
			rateRemaining = (valueOfTheUnit - valueOfTheEnnemy)/(double)valueOfTheUnit;
			if(debug)Utils.print("rateRemaining : "+rateRemaining+" | valeur de l'unite restante : "+ ((int)(valueOfTheUnit*rateRemaining)));

			unitRemaining = unit;
		
			if(attackACity){
				if(debug)Utils.print("The city is won !");
				gameDao.cityIsTaken(city.getCityUID(), unit.getPlayer().getUralysUID(), lastMove.getTimeFrom(), city.getPopulation()/10);
			}
		}
		
		//------------------------------------//

		Object[] result = new Object[2];
		
		result[0] = unitRemaining;
		result[1] = rateRemaining;
		
		return result;
	}

	//-------------------------------------------------------------------------------------//
	
	private Object[] createNextUnitFromGathering(Unit unit, Unit unitMet, String cellUID) 
	{
		Unit nextUnit = new Unit();
		Report report =  new Report();

		//------------------------------------//

		initReport(report, cellUID, unit, unitMet, MovesManager.REPORT_GROUND_GATHERING, false, false);
		
		//------------------------------------//
		
		nextUnit.setSpeed(unit.getSpeed());
		nextUnit.setType(unit.getType());
		nextUnit.setSize(unit.getSize() + unitMet.getSize());

		nextUnit.setArmors(unit.getArmors() + unitMet.getArmors());
		nextUnit.setBows(unit.getBows() + unitMet.getBows());
		nextUnit.setSwords(unit.getSwords() + unitMet.getSwords());

		nextUnit.setWheat(unit.getWheat() + unitMet.getWheat());
		nextUnit.setWood(unit.getWood() + unitMet.getWood());
		nextUnit.setIron(unit.getIron() + unitMet.getIron());
		nextUnit.setGold(unit.getGold() + unitMet.getGold());
		
		if(unit.getPlayer().getAlly() == null){
			nextUnit.setPlayer(unit.getPlayer());
		}
		else{
			nextUnit.getPlayer().setUralysUID(getNewOwner(unitMet, unit));
		}

		//------------------------------------//

		finalizeReport(report, nextUnit);
		
		//------------------------------------//
		
		Object[] result = new Object[2];
		
		result[0] = nextUnit;
		result[1] = report;
		
		return result;
	}

	//-------------------------------------------------------------------------------------//

	private void initReport(Report report, String cellUID, Unit unit, Unit unitMet, int reportType, boolean attackACity, boolean defendACity) 
	{
		report.setReportType(reportType);
		report.setCellUID(cellUID);
		
		report.getUnit1().setUnitUID(unit.getUnitUID());
		report.getUnit1().setOwnerUID(unit.getPlayer().getUralysUID());
		report.getUnit1().setOwnerName(unit.getPlayer().getName());
		report.getUnit1().setSize(unit.getSize());
		report.getUnit1().setType(unit.getType());
		report.getUnit1().setBows(unit.getBows());
		report.getUnit1().setSwords(unit.getSwords());
		report.getUnit1().setArmors(unit.getArmors());
		report.getUnit1().setValue(getValue(unit));

		report.getUnit1().setWheat(unit.getWheat());
		report.getUnit1().setWood(unit.getWood());
		report.getUnit1().setIron(unit.getIron());
		report.getUnit1().setGold(unit.getGold());
		
		report.getUnit1().setAttackACity(attackACity);
		report.getUnit1().setDefendACity(defendACity);

		report.getUnit2().setUnitUID(unitMet.getUnitUID());
		report.getUnit2().setOwnerUID(unit.getPlayer().getUralysUID());
		report.getUnit2().setOwnerName(unitMet.getPlayer().getName());
		report.getUnit2().setSize(unitMet.getSize());
		report.getUnit2().setType(unitMet.getType());
		report.getUnit2().setBows(unitMet.getBows());
		report.getUnit2().setSwords(unitMet.getSwords());
		report.getUnit2().setArmors(unitMet.getArmors());
		report.getUnit2().setValue(getValue(unitMet));

		report.getUnit2().setWheat(unitMet.getWheat());
		report.getUnit2().setWood(unitMet.getWood());
		report.getUnit2().setIron(unitMet.getIron());
		report.getUnit2().setGold(unitMet.getGold());
		
	}
	
	//-------------------------------------------------------------------------------------//

	private void finalizeReport(Report report, Unit nextUnit) 
	{
		report.getNextUnit().setUnitUID(nextUnit.getUnitUID());
		report.getNextUnit().setOwnerUID(nextUnit.getPlayer().getUralysUID());
		report.getNextUnit().setOwnerName(nextUnit.getPlayer().getName());
		report.getNextUnit().setSize(nextUnit.getSize());
		report.getNextUnit().setType(nextUnit.getType());
		report.getNextUnit().setBows(nextUnit.getBows());
		report.getNextUnit().setSwords(nextUnit.getSwords());
		report.getNextUnit().setArmors(nextUnit.getArmors());
		report.getNextUnit().setValue(getValue(nextUnit));

		report.getNextUnit().setWheat(nextUnit.getWheat());
		report.getNextUnit().setWood(nextUnit.getWood());
		report.getNextUnit().setIron(nextUnit.getIron());
		report.getNextUnit().setGold(nextUnit.getGold());	
	}

	//-------------------------------------------------------------------------------------//

	private int getValue(Unit unit) {
		
		if(unit.getType() == Unit.CITY){
			return unit.getSize()/10;
		}
		else
			return unit.getSize() 
			+ unit.getBows()
			+ unit.getSwords()*2
			+ unit.getArmors()*3;
	}

	//-------------------------------------------------------------------------------------//
	
	private boolean isGathering(Unit unit, Unit unitMet) {
		return getAllyUID(unit).equals(getAllyUID(unitMet)); 
	}

	//-------------------------------------------------------------------------------------//
	
	// on doit choisir le plus grand timeFrom  avec timeFrom < celui auquel on arrive
	// et s'il n'y en a pas, le timeFrom le plus petit
	private Move getMoveMet(Move move, CellDTO cell, Unit unit, DataContainer dataContainer) 
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
		
		if(debug)Utils.print("----------");
		if(debug)Utils.print("====>  moveMet : " + (moveSelected == null ? "None" : moveSelected.getMoveUID()));
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
	
	private ArrayList<Move> getPossibleMoves(Move move, CellDTO cell, Unit unit, DataContainer dataContainer) 
	{
		if(debug)Utils.print("---------------------");
		if(debug)Utils.print("getPossibleMoves");
		ArrayList<Move> movesPossible = new ArrayList<Move>(); 
		
		for(MoveDTO recordedMoveDTO : cell.getMoves())
		{
			if(debug)Utils.print("-------");
			if(debug)Utils.print("checking : " + recordedMoveDTO.getMoveUID());
		
			Move recordedMove = EntitiesConverter.convertMoveDTO(recordedMoveDTO);
			Unit unitMet = getUnit(recordedMove.getUnitUID(), dataContainer);
			
			if(isHidden(recordedMove, unitMet.getEndTime())){
				if(debug)Utils.print("move hidden, continue");
				continue;
			}
			
			if(meetingExpected(move, recordedMove))
			{
				boolean sameAlly = areOnSameAlly(unit, unitMet);
				boolean sameType = areOfSameType(unit, unitMet);

				if(debug)Utils.print("sameAlly : " + sameAlly);
				if(debug)Utils.print("sameType : " + sameType);

				if(sameAlly && !sameType){
					if(debug)Utils.print("sameAlly + !sameType, continue");
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

	private boolean isHidden(Move move, long unitEndTime) {
		return unitEndTime != -1 && move.getTimeFrom() > unitEndTime;
	}

	//-------------------------------------------------------------------------------------//
	
	private boolean areOnSameAlly(Unit unit, Unit unitMet) 
	{
		if(unit.getPlayer().getAlly() == null){
			return unit.getPlayer().getUralysUID().equals(unitMet.getPlayer().getUralysUID());
		}
		else{
			if(unitMet.getPlayer().getAlly() == null) 
				return false;
			else
				return unitMet.getPlayer().getAlly().getAllyUID().equals(unit.getPlayer().getAlly().getAllyUID());
			
		}
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
	
	private String getNewOwner(Unit unitMet, Unit unit) 
	{
		AllyDTO ally = gameDao.getAlly(getAllyUID(unit));
		
		for(String playerUID : ally.getPlayerUIDs())
		{
			if(playerUID.equals(unitMet.getPlayer().getUralysUID()))
				return playerUID;
			else if(playerUID.equals(unit.getPlayer().getUralysUID()))
				return playerUID;
		}
		
		return null;
	}

	//-------------------------------------------------------------------------------------//
	
	private boolean[] checkAttackOrDefendACity(String cellUID, DataContainer dataContainer, String allyUID)
	{
		boolean[] result = new boolean[2];
		result[0] = false;
		result[1] = false;
		
		CellDTO cell = getCell(cellUID, dataContainer);
		CityDTO city = cell.getCity();
		
		if(city != null)
		{
			Player playerOfTheCity = getPlayer(city.getOwnerUID(), dataContainer);
			String allyUIDOfTheCity = playerOfTheCity.getAlly() == null ? playerOfTheCity.getUralysUID() : playerOfTheCity.getAlly().getAllyUID(); 
			
			if(!allyUIDOfTheCity.equals(allyUID))
			{
				result[0] = true;
				if(debug)Utils.print("attackACity !!");
			}
			else{
				result[1] = true;
				if(debug)Utils.print("may defendACity");
			}
		}
		if(!result[0])
			if(debug)Utils.print("NOT attacking a city");
		
		return result;
	}

	//-----------------------------------------------------------------------------------//	
	private Player getPlayer(String uralysUID, DataContainer dataContainer) 
	{
		if(dataContainer.playerAlreadyLoaded.get(uralysUID) == null){
			dataContainer.playerAlreadyLoaded.put(uralysUID, EntitiesConverter.convertPlayerDTO(gameDao.getPlayer(uralysUID), false));
		}
		
		return dataContainer.playerAlreadyLoaded.get(uralysUID);
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
	
	private CellDTO getCell(String cellUID, DataContainer dataContainer) 
	{
		if(dataContainer.cellsLoaded.get(cellUID) == null){
			dataContainer.cellsLoaded.put(cellUID, gameDao.getCell(cellUID));
		}
		
		return dataContainer.cellsLoaded.get(cellUID);
	}

	private Unit getUnit(String unitUID, DataContainer dataContainer) 
	{
		if(unitUID == null)
			return null;
		
		if(dataContainer.unitsLoaded.get(unitUID) == null){
			dataContainer.unitsLoaded.put(unitUID, EntitiesConverter.convertUnitDTO(gameDao.getUnit(unitUID)));
		}
		
		return dataContainer.unitsLoaded.get(unitUID);

	}

	private void deleteUnit(Unit unit) {
		gameDao.deleteUnit(unit.getPlayer().getUralysUID(), unit.getUnitUID());
	}
	
	//-----------------------------------------------------------------------------------//
	
	private class DataContainer{
		private HashMap<String, CellDTO> cellsLoaded = new HashMap<String, CellDTO>();
		private HashMap<String, Unit> unitsLoaded = new HashMap<String, Unit>();
		private HashMap<String, Player> playerAlreadyLoaded = new HashMap<String, Player>();
	}

}
