package com.uralys.tribes.domain.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.domain.IGameManager;
import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Conflict;
import com.uralys.tribes.entities.DataContainer4UnitSaved;
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

	//==================================================================================================//
	// game COEFF

	public final static int WHEAT_EARNING_COEFF = 5;
	public final static int WOOD_EARNING_COEFF = 3;
	public final static int IRON_EARNING_COEFF = 2;

	public final static boolean debug = true;
	public final static boolean topdebug = false;

	//==================================================================================================//

	private IGameDAO gameDao;

	public GameManager (IGameDAO gameDao){
		this.gameDao = gameDao;
	}

	//==================================================================================================//
	
	public String createPlayer(String uralysUID, String email) {
		return gameDao.createPlayer(uralysUID, email);			
	}
	
	//==================================================================================================//
	
	public String buildCity(City city, String uralysUID){
		return gameDao.createCity(city, uralysUID);
	}
	
	public void saveCity(City city){
		updateCity(city, false);
	}
	
	private void updateCity(City city, boolean saveResources)
	{
		if(city.getBeginTime() > new Date().getTime())
			return;
		
		gameDao.updateCityResources(city, saveResources);

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
	
	//==================================================================================================//

	public Player getPlayer(String uralysUID) {
		return getPlayer(uralysUID, null);
	}
	
	private Player getPlayer(String uralysUID, DataContainer datacontainer) 
	{
		if(datacontainer != null){
			if(datacontainer.playerAlreadyLoaded.get(uralysUID) == null){
				datacontainer.playerAlreadyLoaded.put(uralysUID, EntitiesConverter.convertPlayerDTO(gameDao.getPlayer(uralysUID), false));
			}
			
			return datacontainer.playerAlreadyLoaded.get(uralysUID);
		}
		else
			return EntitiesConverter.convertPlayerDTO(gameDao.getPlayer(uralysUID), true);
	}

	//==================================================================================================//

	public void savePlayer(Player player) {
		if(debug)Utils.print("-------------------------------------------------");
		if(debug)Utils.print("Save turn pour joueur : " + player.getName());

		//----------------------------------------------------------------//
		// Cities

		if(debug)Utils.print("--------------------");
		if(debug)Utils.print("Saving cities");

		for(City city : player.getCities())
		{
			updateCity(city, true);
		}
		
		//----------------------------------------------------------------//
		// Player
		
		gameDao.updatePlayer(player); // lastTurnPLayed, remove last Reports
	}
	
	//==================================================================================================//

	
	public DataContainer4UnitSaved createUnit(String uralysUID, Unit unit, String cityUID, boolean needReplacing){

		if(debug)Utils.print("-----------------------------------");
		if(debug)Utils.print("gamemanager createUnit : " + unit.getUnitUID() + " for uralysUID : " + uralysUID);
		
		gameDao.createUnit(unit, cityUID);
		gameDao.linkNewUnit(uralysUID, unit.getUnitUID());

		DataContainer datacontainer = new DataContainer();

		if(needReplacing){
			if(debug)Utils.print("placing the unit");
			placeUnit(unit, unit.getMoves(), new ArrayList<Unit>(), datacontainer);
		}
		
		if(debug)Utils.print("/gamemanager createUnit"); 
		if(debug)Utils.print("-----------------------------------");
		return datacontainer.objectsAltered;
	}
	
	public DataContainer4UnitSaved updateUnit(Unit unit, String cityUID, boolean needReplacing){

		if(debug)Utils.print("-----------------------------------");
		if(debug)Utils.print("gamemanager updateUnit : " + unit.getUnitUID());

		gameDao.updateUnit(unit, cityUID);
		
		DataContainer datacontainer = new DataContainer();

		if(unit.getEndTime() < new Date().getTime() && unit.getEndTime() != -1){
			needReplacing = false;
		}
		
		if(needReplacing){
			if(debug)Utils.print("placing the unit");
			placeUnit(unit, unit.getMoves(), new ArrayList<Unit>(), datacontainer);
		}

		if(debug)Utils.print("/gamemanager updateUnit"); 
		if(debug)Utils.print("-----------------------------------");
		return datacontainer.objectsAltered;
	}
	
	public void deleteUnit(String uralysUID, String unitUID){
		gameDao.deleteUnit(uralysUID, unitUID);
	}
	
	public void deleteUnits(String uralysUID, List<String> unitUIDs){
		gameDao.deleteUnits(uralysUID, unitUIDs);
	}

	//==================================================================================================//

	public void deleteMove(String moveUID) {
		deleteMove(moveUID, false);
	}
	
	private void deleteMove(String moveUID, boolean keepGatheringBecauseItIsLinkedWithAnotherMoveNow) {
		gameDao.deleteMove(moveUID, keepGatheringBecauseItIsLinkedWithAnotherMoveNow);
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

	public List<Case> loadCases(int[] groups) {
		List<Case> cases = new ArrayList<Case>();
	
		List<CaseDTO> casesLoaded = gameDao.loadCases(groups);
		for(CaseDTO caseDTO : casesLoaded){
			cases.add(EntitiesConverter.convertCaseDTO(caseDTO));
		}

		return cases;
	}

	//==================================================================================================//

	public void changeName(String uralysUID, String newName) {
		gameDao.changeName(uralysUID, newName);
	}
	
	public void changeCityName(String cityUID, String newName) {
		gameDao.changeCityName(cityUID, newName);
	}
	
	//==================================================================================================//
	
	/**
	 * 02 mai 2011
	 * HYPER IMPORTANT !!
	 * lorsqu'on fait les replaceUnit, on rentre dans de grosses recursions, il suffit de 12-15 pour deja perdre du temps, 
	 * donc un rassemblement de beauvcoup darmees ne peut pas marcher (et cest ce qui risque darriver tres vite )
	 * peut etre une solution : UNIFIER LES GROUPES DARMEES PAR ALLIANCE
	 * il faut une hierarchie d'alliance, pour determiner qui controle la nouvelle armee par defaut
	 * il faut plus tard pouvoir separer une armee en plusieurs si on les deplace (fixer chaque scission a 3 pour ne pas renouveler le probleme, comme ca larmee se divisera par 3, puis par 3 etc et ca sera accessible)
	 * il faut pouvoir donner le controle d'une armee à qqun de l'alliance.
	 * 
	 */
	// le premier Move est la case actuelle où est posée l'unité avant son départ
	private List<Unit> placeUnit(Unit unitArriving, List<Move> moves, List<Unit> unitsMakingThisReplacing, DataContainer datacontainer) {
		
		if(topdebug)Utils.print("entree dans placeUnit | " + new Date());
		
		if(debug)Utils.print("---------------------------------------");
		if(debug)Utils.print("place unit " + unitArriving.getUnitUID());
		
		int currentUnitValue = unitArriving.getValue();
		String allyUID = getPlayer(unitArriving.getPlayerUID(), datacontainer).getAllyUID();

		if(debug){
			Utils.print("initial value : " + currentUnitValue);
			Utils.print("ally : " + allyUID);
			Utils.print("moves to check and record");

			for(Move move : moves){
				int i = TribesUtils.getX(move.getCaseUID());
				int j = TribesUtils.getY(move.getCaseUID());
				
				Utils.print("["+i+"]["+j+"]");
			}

			Utils.print("unitsMakingThisReplacing :");
			
			for(Unit unitMakingThisReplacing : unitsMakingThisReplacing){
				Utils.print(unitMakingThisReplacing.getUnitUID());
			}
		}
		
		List<Unit> unitsReplacedByThisPlacement = new ArrayList<Unit>();

		if(debug)Utils.print("****");
		List<Unit> previousUnitsMet = resetPreviousMeetings(unitArriving, unitsMakingThisReplacing, datacontainer);
		if(debug)Utils.print("****");

		ArrayList<Unit> unitsToReplace = new ArrayList<Unit>();

		if(topdebug)Utils.print("debut du loop dans les moves  | " + new Date());

		boolean foundAGathering = false;
		String previousMoveUID = null;
		long timeFromChallenging = -1;
		
		for(Move arrivalOnThisCaseMove : moves)
		{
			boolean moveIsToBeCreated = true;
			
			if(foundAGathering){
				if(debug)Utils.print("foundAGathering yet => mouvement non appliqué");
				currentUnitValue = 0;
			}
			
			arrivalOnThisCaseMove.setValue(currentUnitValue);
			arrivalOnThisCaseMove.setUnitUID(unitArriving.getUnitUID());
			arrivalOnThisCaseMove.setGathering(new Gathering(allyUID));
			arrivalOnThisCaseMove.getGathering().getUnitUIDs().add(unitArriving.getUnitUID());

			// on ajoute le move dans unitArriving.moves, car unitArriving sera mise dans datacontainer.objectsAltered pour flex
			unitArriving.getMoves().add(arrivalOnThisCaseMove);
			
			if(debug){
				Utils.print("gathering : " + arrivalOnThisCaseMove.getGathering().getGatheringUID());
				int i = TribesUtils.getX(arrivalOnThisCaseMove.getCaseUID());
				int j = TribesUtils.getY(arrivalOnThisCaseMove.getCaseUID());
				Utils.print("passage par case ["+i+"]["+j+"], de ("+new Date(arrivalOnThisCaseMove.getTimeFrom())+") à ("+new Date(arrivalOnThisCaseMove.getTimeTo())+")");
				Utils.print("value : " + arrivalOnThisCaseMove.getValue());
			}

			Case _case = getCase(arrivalOnThisCaseMove.getCaseUID());
			if(!foundAGathering){
				unitArriving.setFinalCaseUIDExpected(_case.getCaseUID());
				timeFromChallenging = arrivalOnThisCaseMove.getTimeFrom();
			}
			
			//-----------------------------------------------------------------------------------//

			if(currentUnitValue == 0){
				if(debug)Utils.print("mouvement non appliqué, pas de calcul de croisement");
			}
			else if(_case.getRecordedMoves().size() == 0){
				if(debug)Utils.print("case libre");
			}

			if(currentUnitValue > 0 && _case.getRecordedMoves().size() > 0)
			{
				// le move est valable et qu'il y a des recordedMoves sur la case
				// loop sur les recordedMoves pour determiner s'ils ont lieu au meme moment
				// les recordedMoves passent dans l'ordre de leur creation 
				//	=> le premier qu'on trouve est celui du gathering
				//	   les autres auront lieu APRES et donc ne compte PAS
				//     donc on sort des qu'on a trouve un gathering
				for(Move recordedMove : _case.getRecordedMoves())
				{
					String unitRecordedUID = recordedMove.getUnitUID();
					
					if(debug)Utils.print("=============================================================");
					if(debug)Utils.print(" - trouvé un recordedMove : unit " + unitRecordedUID);

					if(recordedMove.getValue() == 0){
						if(debug)Utils.print("	recordedMove non appliqué (value = 0) : pas de croisement avec ce recordedMove");
						continue;
					}

					if(debug)Utils.print(" 	recordedMove de ["+new Date(recordedMove.getTimeFrom())+"] à ["+new Date(recordedMove.getTimeTo())+"]");
					
					//---------------------------------------------------------------------//
					// tests de croisement
					
					boolean meetingHappens = false;
					
					if(arrivalOnThisCaseMove.getTimeTo() == -1 && recordedMove.getTimeTo() == -1)
					{
						if(debug)Utils.print("	les 2 timeTo sont à -1 : croisement car les 2 unites s'arretent sur la meme case");
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

						if(move1.getTimeTo() > move2.getTimeFrom() 
								&& (move2.getTimeTo() == -1
										||
										move1.getTimeTo() < move2.getTimeTo()))
							
						{
							if(debug)Utils.print("	croisement lors d'un passage par la case");
							meetingHappens = true;
						}
					}

					
					//---------------------------------------------------------------------//
					
					if(meetingHappens)
					{
						if(debug)Utils.print(" - meetingHappens !");
						Gathering existingGathering = recordedMove.getGathering();

						if(existingGathering.getAllyUID().equals(allyUID))
						{
							if(debug)Utils.print(" - trouvé un ally : unit " + unitRecordedUID);
							Unit unitRecorded = getUnit(unitRecordedUID, datacontainer);
							
							if(unitRecorded.getType() != unitArriving.getType()){
								if(debug)Utils.print("  les unités sont de types differents : pas de gathering");
							}
							else{
								foundAGathering = true;
								boolean needReplaceNewArmy = false;
								
								if(debug)Utils.print("nbunits in existingGathering : " + existingGathering.getUnitUIDs().size());
								if(existingGathering.getUnitUIDs().size() == 2){
									/*
									 * au depart on a :
									 * A1
									 * n1 = A1+A2 (g1)
									 * n2 = n1+A3 (g2)
									 * 
									 * A4 arrive avant A2
									 * on croise A1 : gatheringE : g1 (existingGathering)
									 *  
									 *  recursion sur gi.ni si gi.ni a un gatheringExpected
									 *  => g1.n1 a un gatheringExpected : g2
									 *  	=> on set A3.gatheringExpected = null
									 *  	 + on set A3.endtime a 1 (pour le rendre perimé ce qui correspond à delete)
									 *  	 + on doit replacer A3
									 *  
									 *  => on cree n3 = A1+A4 et g3
									 *  
									 *  
									 *  au final on a :
									 *  A1
									 *  n3 = A1+A4 (g3)
									 * 	
									 *  replace A2 
									 *  n4 = n3 + A2 (g4)
									 *  
									 *  replace A3 
									 *  n5 = n4 + A3 (g5)
									 *  
									 */
									if(debug)Utils.print("  il est deja dans un gathering de 2 units");
									
									// recursion sur previousNewArmy.getGatheringUIDExpected() pour trouver toutes les unitToReplace
									// unitRecordedUID est celle qui reste (A1)
									// on doit replace la 2e unit du gathering (A2)
									// et ainsi de suite pour chacun des newArmy.gatheringExpected 
									recurringOnNextGatheringImpacted(existingGathering, unitRecordedUID, datacontainer, unitsToReplace);
									needReplaceNewArmy = false;
									if(debug)Utils.print("recurring done : nbunits in existingGathering : " + existingGathering.getUnitUIDs().size());
									
								}

								if(debug)Utils.print(" - attribution des endTime sur les units, et du timeTo sur le recordedMove");

								long timeOfTheMeeting; // c'est l'heure du croisement qui compte
								if(arrivalOnThisCaseMove.getTimeFrom() < recordedMove.getTimeFrom())
								{
									// dans ce cas, on intercepte un move (on arrive avant lui)
									// ce recorded move n'existe donc plus, puisque lorsquil arrivera on passera sur la newarmy
									if(debug)Utils.print("  on arrive sur la case AVANT celui qu'on croise");
									timeOfTheMeeting = recordedMove.getTimeFrom();
									arrivalOnThisCaseMove.setTimeTo(timeOfTheMeeting);
									previousMoveUID = getPreviousMove(recordedMove, datacontainer);
									
									deleteMove(recordedMove.getMoveUID(), true);
								}
								else
								{
									// dans ce cas on arrive apres le move intercepte
									// donc le recordedMove existe jusqu'a ce qu'on arrive
									// et le move ne doit surtout pas etre enregistre puisqu'on devient immediatement la newArmy avec son propre move. 
									if(debug)Utils.print("  on arrive sur la case APRES celui qu'on croise");
									moveIsToBeCreated = false;
									timeOfTheMeeting = arrivalOnThisCaseMove.getTimeFrom();
									gameDao.setTimeToForMove(recordedMove.getMoveUID(), timeOfTheMeeting);
								}

								if(previousMoveUID != null){
									if(debug)Utils.print(" - setGatheringForPreviousMove  " + previousMoveUID);
									gameDao.setNewGatheringForMoveAndDeletePreviousGathering(previousMoveUID, existingGathering.getGatheringUID());
								}
								
								unitRecorded.setEndTime(timeOfTheMeeting);
								unitArriving.setEndTime(timeOfTheMeeting);

								if(debug)Utils.print(" - creation de la nouvelle army");
								// le move qui cree le gathering (arrivalOnThisCaseMove) devient le firstMove de notre newUnit
								Unit newUnit = createNewArmy(unitArriving, unitRecorded, timeOfTheMeeting, arrivalOnThisCaseMove.getCaseUID(), allyUID, needReplaceNewArmy);


								if(debug)Utils.print(" - addUnitInGathering  " + unitArriving.getUnitUID());
								// Note tres importante pour bien comprendre comment ca marche
								// ici, existingGathering contient les 2 anciennes units du gathering qu'on est en train de defaire (3,1)
								// or, existingGathering a ete recupere par le getCase tout au depart
								// entre temps, on est passe par 'resetPreviousMeetings' qui fait un deleteMoves et donc qui a degage 
								// l'unite (unitToReplace)(3) de gathering.unitUIDs dans le datastore  (il n'y a plus que (1) dans le datastore)
								// donc cette appel avec gameDao va bien rajouter unitArriving dans gathering.unitUIDs avec unitRecorded seulement (2,1)
								gameDao.addUnitInGatheringAndSetNewArmy(existingGathering.getGatheringUID(), unitArriving.getUnitUID(), newUnit.getUnitUID());
								
								// les liens sur unit.move viennent detre fait par le createNewArmy.createUnit.placeUnit.createMove
								//requireLinks = false;
								
								//unitArriving.getMoves().add(arrivalOnThisCaseMove);
								unitArriving.setGatheringUIDExpected(existingGathering.getGatheringUID());
								unitRecorded.setGatheringUIDExpected(existingGathering.getGatheringUID());
								
								if(debug)Utils.print(" - annulation des moves prevus de unitRecorded");
								unitsToReplace.addAll(cancelRecordedMovesAndReturnAllUnitsToReplace(unitRecorded, timeOfTheMeeting, datacontainer));
								
								updateUnit(unitRecorded, null, false);

								datacontainer.objectsAltered.addUnitAltered(newUnit);
								datacontainer.objectsAltered.addUnitAltered(unitRecorded);
								
							}// endif croisement avec un ally de meme type
						
						} // endif croisement avec un ally
						else
						{
							// croisement avec un ennemi !
							// conflit
							if(debug)Utils.print(" Conflit !!!");
							Unit unitAlreadyOnTheCase = getUnit(existingGathering.getNewUnitUID(), datacontainer);	
							int valueOfTheUnitMet = recordedMove.getValue();
							if(debug)Utils.print("recordedMove.getValue() : " + recordedMove.getValue());
							
							String conflictUID = gameDao.createConflict(_case.getCaseUID(), unitArriving.getUnitUID(), unitAlreadyOnTheCase.getUnitUID());
							
							if(valueOfTheUnitMet > currentUnitValue)
							{
								if(debug)Utils.print(" Combat perdu par l'unite qui arrive");
								double rateRemaining = (valueOfTheUnitMet - currentUnitValue)/(double)valueOfTheUnitMet;
								if(debug)Utils.print(" valeur de l'unite restante : "+ ((int)(valueOfTheUnitMet*rateRemaining)));

								currentUnitValue = 0;
								arrivalOnThisCaseMove.setValue(currentUnitValue);
								unitArriving.setEndTime(arrivalOnThisCaseMove.getTimeFrom());
								gameDao.setValueForMove(recordedMove.getMoveUID(), (int)(valueOfTheUnitMet*rateRemaining));
							}
							else if(currentUnitValue > valueOfTheUnitMet)
							{
								if(debug)Utils.print(" Combat gagne par l'unite qui arrive");
								double rateRemaining = (currentUnitValue - valueOfTheUnitMet)/(double)currentUnitValue;
								if(debug)Utils.print(" valeur de l'unite restante : "+ ((int)(currentUnitValue*rateRemaining)));
								currentUnitValue = (int)(currentUnitValue*rateRemaining);
								
								gameDao.setValueForMove(recordedMove.getMoveUID(), 0);
								unitAlreadyOnTheCase.setEndTime(arrivalOnThisCaseMove.getTimeFrom());
								
							}

							unitArriving.setConflictUIDExpected(conflictUID);
							unitAlreadyOnTheCase.setConflictUIDExpected(conflictUID);
							updateUnit(unitAlreadyOnTheCase, null, false);
							
						} // endif croisement avec un ennemy
						
					}// endif croisement
					else{
						if(debug)Utils.print("	pas de croisement avec ce passage");
					}

					if(foundAGathering){
						if(debug)Utils.print("	FOUND A GATHERING : BREAK");
						break;
					}
				}// fin du for sur les recordedMoves
				
			}

			//-----------------------------------------------------------------------------------//
			// enregistrement du move dans les recordedMoves APRES check de ceux ci (sinon on va le checker avec lui meme)
		
			if(debug)Utils.print("recording move " + arrivalOnThisCaseMove.getMoveUID());
			if(debug)Utils.print("value : " + arrivalOnThisCaseMove.getValue());
			if(moveIsToBeCreated)
				gameDao.createMove(arrivalOnThisCaseMove);
			
			// le vieux move a ete vire de la case lors du resetPreviousMeetings qui a fait un deleteMoves 
			// le nouveau move a ete greffe sur la case et sur l'unite à linstant lors du createMove
			// donc on peut enregistrer la case dans les datacontainer.casesAltered
			if(debug)Utils.print("recording caseAltered " + _case.getCaseUID());
			datacontainer.objectsAltered.addCaseAltered(getCase(_case.getCaseUID()));
		
			previousMoveUID = arrivalOnThisCaseMove.getMoveUID();
		} // fin du loop sur les moves 

		// on enregistre tous les parametres modifiés sur notre unitArriving
		updateUnit(unitArriving, null, false);
		
		// on set le nouveau challenger sur la case finale, si elle touche le royaume
		gameDao.tryToSetChallenger(unitArriving, timeFromChallenging);

		// et enregistrer l'unite dans les datacontainer.unitsAltered
		datacontainer.objectsAltered.addUnitAltered(unitArriving);

		if(topdebug)Utils.print("fin du loop dans les moves  | " + new Date());
		
		//-----------------------------------------------------------------------------------//
		// ON REPLACE TOUTES LES UNITES IMPACTEES

		if(topdebug)Utils.print("debut du replacement  | " + new Date());
		unitsMakingThisReplacing.add(unitArriving);
		
		if(debug)Utils.print("----------------");
		if(debug)Utils.print("replacing new opponents : " + unitsToReplace.size());
		
		for(Unit unitToReplace : unitsToReplace){
			if(!contains(unitsReplacedByThisPlacement, unitToReplace.getUnitUID())){
				if(debug)Utils.print("replacing : " + unitToReplace.getUnitUID());
				List<Unit> unitsReplacedConsequently = placeUnit(unitToReplace, unitToReplace.getMoves(), unitsMakingThisReplacing, datacontainer);
				unitsReplacedByThisPlacement.add(unitToReplace);
				unitsReplacedByThisPlacement.addAll(unitsReplacedConsequently);				
			}
		}
		
		if(debug)Utils.print("----------------");

		if(debug)Utils.print("unitsReplacedByThisPlacement :");
		for(Unit unitReplacedByThisPlacement : unitsReplacedByThisPlacement){
			if(debug)Utils.print(unitReplacedByThisPlacement.getUnitUID());
		}
		
		if(debug)Utils.print("----------------");
		if(debug)Utils.print("replacing old opponents : " + previousUnitsMet.size());

		for(Unit previousOpponentToReplace : previousUnitsMet){
			if(!contains(unitsReplacedByThisPlacement, previousOpponentToReplace.getUnitUID())){
				if(debug)Utils.print("replacing : " + previousOpponentToReplace.getUnitUID());
				List<Unit> unitsReplacedConsequently = placeUnit(previousOpponentToReplace, previousOpponentToReplace.getMoves(), unitsMakingThisReplacing, datacontainer);
				unitsReplacedByThisPlacement.add(previousOpponentToReplace);
				unitsReplacedByThisPlacement.addAll(unitsReplacedConsequently);
			}
		}
		
		if(debug)Utils.print("----------------");
		if(debug)Utils.print("fin du placement");
		if(debug)Utils.print("----------------");

		if(topdebug)Utils.print("fin du replacement, et du placement  | " + new Date());
		return unitsReplacedByThisPlacement;

	}

	private String getPreviousMove(Move move, DataContainer datacontainer) 
	{
		if(debug)Utils.print("getPreviousMove to move " + move.getMoveUID());
		Unit unit = getUnit(move.getUnitUID(), datacontainer);
		
		String previousMoveUID = null;
		for(Move moveInUnit : unit.getMoves())
		{
			if(move.getTimeFrom() == moveInUnit.getTimeTo())
			{
				previousMoveUID = moveInUnit.getMoveUID();
				break;
			}
		}

		if(debug)Utils.print("previousMoveUID : " + previousMoveUID);
		return previousMoveUID;
	}

	/*
	 *  
	 * au depart on a :
	 * A1
	 * n1 = A1+A2 (g1)
	 * n2 = n1+A3 (g2)
	 * 
	 * A4 arrive avant A2
	 * on croise A1 : gatheringE : g1 (existingGathering)
	 *  
	 *  recursion sur gi.ni si gi.ni a un gatheringExpected
	 *  => g1.n1 a un gatheringExpected : g2
	 *  	=> on set A3.gatheringExpected = null
	 *  	 + on doit replacer A3
	 *  
	 *  => on supprime ni+gi
	 *  => on cree n3 = A1+A4 et g3
	 *  
	 *  DE PLUS en passant dans cette recurrence, on range les unitsToReplace dans l'ordre : elle seront replacees les unes apres les autres, de la premiere arrivee a la derniere 
	 */
	// g1, A1
	// g2, n1
	private void recurringOnNextGatheringImpacted(Gathering gathering, String unitNotToReplaceUID, DataContainer datacontainer, List<Unit> unitsToReplace) {

		if(debug)Utils.print("  recuringOnNextGatheringImpacted : " + gathering.getGatheringUID());
		if(debug)Utils.print("  unitNotToReplaceUID : " + unitNotToReplaceUID);

		// A2, A3
		Unit unitToReplace;
		if(gathering.getUnitUIDs().get(0).equals(unitNotToReplaceUID))
			unitToReplace = getUnit(gathering.getUnitUIDs().get(1), datacontainer, true);
		else
			unitToReplace = getUnit(gathering.getUnitUIDs().get(0), datacontainer, true);

		unitToReplace.setGatheringUIDExpected(null);
		unitToReplace.setConflictUIDExpected(null);
		unitToReplace.setEndTime(-1); // on va replacer cet unite, donc sont endTime est de nouveau indefini
		updateUnit(unitToReplace, null, false); // le replace se fera plus tard, tout sera enregistré
		
		if(debug)Utils.print("  unitToReplace : " + unitToReplace.getUnitUID());
		unitsToReplace.add(unitToReplace); // A2, A3
		
		// n1, n2
		Unit newArmy = getUnit(gathering.getNewUnitUID(), datacontainer);
		newArmy.setEndTime(1);
		updateUnit(newArmy, null, false);
		datacontainer.objectsAltered.addUnitAltered(newArmy);
		
		// g2
		if(newArmy.getGatheringUIDExpected() != null){
			Gathering nextGathering = EntitiesConverter.convertGatheringDTO(gameDao.getGathering(newArmy.getGatheringUIDExpected()));
			recurringOnNextGatheringImpacted(nextGathering, newArmy.getUnitUID(), datacontainer, unitsToReplace);
		}
		
	}

	private List<Unit> cancelRecordedMovesAndReturnAllUnitsToReplace(Unit unit, long timeFromWhichMovesMustBeCancelled, DataContainer datacontainer){ 
		if(debug)Utils.print("cancelRecordedMovesAndReturnAllUnitsToReplace");
		
		List<Unit> unitsToReplace = new ArrayList<Unit>();
		
		for(Move move : unit.getMoves()){
			if(debug)Utils.print("move " + move.getMoveUID());
			if(move.getTimeFrom() > timeFromWhichMovesMustBeCancelled){
				if(debug)Utils.print("isCancelled");
				gameDao.setValueForMove(move.getMoveUID(), 0);
				
				if(move.getGathering().getUnitUIDs().size() > 1)
				{
					Unit unitToReplace;
					if(move.getGathering().getUnitUIDs().get(0).equals(unit.getUnitUID()))
						unitToReplace = getUnit(move.getGathering().getUnitUIDs().get(1), datacontainer);
					else
						unitToReplace = getUnit(move.getGathering().getUnitUIDs().get(0), datacontainer);

					if(debug)Utils.print("found a unit to replace : " + unitToReplace.getUnitUID());
					unitsToReplace.add(unitToReplace);
				}
				
			}
			else
				if(debug)Utils.print("is still available");
		}

		return unitsToReplace;
	}

	private Unit createNewArmy(Unit unitArriving, Unit unitRecorded, long timeFrom, String caseUID, String allyUID, boolean needReplaceNewArmy) {
		
		// TODO : le ownerUID doit etre celui du plus haut placé des deux alliés
		String ownerUID = unitArriving.getPlayerUID();

		Unit newUnit = new Unit();
		newUnit.setUnitUID(ownerUID+"_"+Utils.generatePassword(3)+"_"+timeFrom);
		newUnit.setPlayerUID(ownerUID);
		newUnit.setBeginTime(timeFrom);
		newUnit.setEndTime(-1);
		newUnit.setStatus(unitArriving.getStatus());
		newUnit.setSize(unitArriving.getSize() + unitRecorded.getSize());
		newUnit.setValue(unitArriving.getValue() + unitRecorded.getValue());
	
		newUnit.setSpeed(unitArriving.getSpeed());
		newUnit.setType(unitArriving.getType());
		
		ArrayList<Move> moves = new ArrayList<Move>();
		Move firstMoveForNewUnit = new Move();
		
		firstMoveForNewUnit.setMoveUID(timeFrom+"_"+TribesUtils.getX(caseUID)+"_"+TribesUtils.getY(caseUID)+"_"+newUnit.getUnitUID());
		firstMoveForNewUnit.setUnitUID(newUnit.getUnitUID());
		firstMoveForNewUnit.setCaseUID(caseUID);
		firstMoveForNewUnit.setTimeFrom(timeFrom);
		firstMoveForNewUnit.setTimeTo(-1);
		firstMoveForNewUnit.setValue(newUnit.getValue());
		firstMoveForNewUnit.getGathering().setAllyUID(allyUID);
		firstMoveForNewUnit.getGathering().setUnitUIDs(new ArrayList<String>());
		firstMoveForNewUnit.getGathering().getUnitUIDs().add(newUnit.getUnitUID());
		
		
		moves.add(firstMoveForNewUnit);
		newUnit.setMoves(moves); // la newUnit recupere le nouveau move sur la case, avec gatheringNotCreatedYet
		
		List<Equipment> equipments = new ArrayList<Equipment>();
		
		for (Equipment equipment1 : unitArriving.getEquipments()) {
			for (Equipment equipment2 : unitRecorded.getEquipments()) {
				if(equipment1.getItem().getName().equals(equipment2.getItem().getName())){
					equipment1.setSize(equipment1.getSize() + equipment2.getSize());
					equipments.add(equipment1);
				}
			}
		}

		newUnit.setEquipments(equipments);
		
		createUnit(ownerUID, newUnit, null, needReplaceNewArmy);
		gameDao.createMove(firstMoveForNewUnit);
		
		return newUnit;
	}

	// - verifie s'il y avait un gathering en aval
	// et return la liste des armees à replacer
	private List<Unit> resetPreviousMeetings(Unit unit, List<Unit> unitsMakingThisReplacing, DataContainer datacontainer) 
	{
		if(debug)Utils.print("resetPreviousMeetings for unit " + unit.getUnitUID());
		List<Unit> unitsToReplace = new ArrayList<Unit>();

		// d'abord on regarde si il y avait un gathering de prevu
		if(unit.getGatheringUIDExpected() != null)
		{
			if(debug)Utils.print("found a gathering expected !");
			Gathering gatheringExpected = EntitiesConverter.convertGatheringDTO(gameDao.getGathering(unit.getGatheringUIDExpected()));
			if(debug)Utils.print("gathering : " + gatheringExpected.getGatheringUID());

			Unit unitToReplace;
			if(gatheringExpected.getUnitUIDs().get(0).equals(unit.getUnitUID()))
				unitToReplace = getUnit(gatheringExpected.getUnitUIDs().get(1), datacontainer, true);
			else
				unitToReplace = getUnit(gatheringExpected.getUnitUIDs().get(0), datacontainer, true);
			
			unitToReplace.setEndTime(-1);
			unitToReplace.setGatheringUIDExpected(null);
			unitToReplace.setConflictUIDExpected(null);
			updateUnit(unitToReplace, null, false);
			
			Unit newUnit = getUnit(gatheringExpected.getNewUnitUID(), datacontainer);
			newUnit.setEndTime(1);
			updateUnit(newUnit, null, false);
			
			// on supprime tous les moves (+ gathering, + case.recordedMove, + unit.move) de cette newUnit
			gameDao.deleteMoves(newUnit.getUnitUID());
			
			if(debug)Utils.print("unitToReplace : " + unitToReplace.getUnitUID());
			unitsToReplace.add(unitToReplace);
		}
		
		// ensuite on regarde si il y avait un conflit de prevu
		if(unit.getConflictUIDExpected() != null)
		{
			if(debug)Utils.print("found a confict expected !");
			Conflict conflictExpected = EntitiesConverter.convertConflictDTO(gameDao.getConflict(unit.getConflictUIDExpected()));
			if(debug)Utils.print("conflict : " + conflictExpected.getConflictUID());
		
			Unit unitToReplace;
			if(conflictExpected.getUnits().get(0).getUnitUID().equals(unit.getUnitUID()))
				unitToReplace = conflictExpected.getUnits().get(1);
			else
				unitToReplace = conflictExpected.getUnits().get(0);

			if(debug)Utils.print("unitToReplace : " + unitToReplace.getUnitUID());
			unitToReplace.setEndTime(-1);
			unitToReplace.setGatheringUIDExpected(null);
			unitToReplace.setConflictUIDExpected(null);
			updateUnit(unitToReplace, null, false);
			
		}
		
		// on degage le challenger(cette unité) de la case finale calculée precedemment pour cette unité
		gameDao.resetChallenger(unit.getFinalCaseUIDExpected());

		// on supprime tous les moves (+ gathering, + case.recordedMove, + unit.move)
		gameDao.deleteMoves(unit.getUnitUID());

		unit.setMoves(new ArrayList<Move>());
		unit.setEndTime(-1);
		unit.setGatheringUIDExpected(null);
		unit.setConflictUIDExpected(null);
		updateUnit(unit, null, false);

		return unitsToReplace;
	}


	private boolean contains(List<Unit> unitsMakingThisReplacing, String unitUID) {
		
		List<String> unitUIDsMakingThisReplacing = new ArrayList<String>();
				
		for(Unit unitMakingThisReplacing : unitsMakingThisReplacing){
			unitUIDsMakingThisReplacing.add(unitMakingThisReplacing.getUnitUID());
		}
		
		return unitUIDsMakingThisReplacing.contains(unitUID);
	}

	public Case getCase(int x, int y) {
		return EntitiesConverter.convertCaseDTO(gameDao.getCase(x,y));
	}
	
	private Case getCase(String caseUID) {
		return EntitiesConverter.convertCaseDTO(gameDao.getCase(TribesUtils.getX(caseUID),TribesUtils.getY(caseUID)));
	}

	private Unit getUnit(String unitUID, DataContainer datacontainer) {
		return getUnit(unitUID, datacontainer, false);
	}

	private Unit getUnit(String unitUID, DataContainer datacontainer, boolean requireLinkedMoveFromGathering) 
	{
		if(datacontainer != null){
			if(datacontainer.unitsLoaded.get(unitUID) == null){
				datacontainer.unitsLoaded.put(unitUID, EntitiesConverter.convertUnitDTO(gameDao.getUnit(unitUID), true, requireLinkedMoveFromGathering, true));
			}
			
			return datacontainer.unitsLoaded.get(unitUID);
		}
		else
			return EntitiesConverter.convertUnitDTO(gameDao.getUnit(unitUID), true, requireLinkedMoveFromGathering, true);

	}
	

	//==============================================================================================================//
	
	private class DataContainer{

			private HashMap<String, Unit> unitsLoaded = new HashMap<String, Unit>();
			private HashMap<String, Player> playerAlreadyLoaded = new HashMap<String, Player>();
			private DataContainer4UnitSaved objectsAltered = new DataContainer4UnitSaved();
	}

}
