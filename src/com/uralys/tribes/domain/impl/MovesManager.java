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
		//-------------------------------------------------------//

		Utils.print("--------------------");
		Utils.print("refreshUnitMoves");
		DataContainer dataContainer = new DataContainer();
		Player player = getPlayer(unit.getPlayer().getUralysUID(), dataContainer);
		String allyUIDOfUnitArriving = player.getAlly() == null ? player.getUralysUID() : player.getAlly().getAllyUID();
		
		//-------------------------------------------------------//
		// - 1 : on verifie si on a croisé une unitMet avant
		//		 on fera un refreshUnitWay de cet unitMet
		
		Unit unitToReplace = null;
		if(unit.getUnitMetUID() != null){
			unitToReplace = getUnit(unit.getUnitMetUID(), dataContainer);
			unitToReplace.setUnitMetUID(null);
		}

		//-------------------------------------------------------//
		// - 2 : on verifie si on croise une unit sur le trajet
		
		boolean foundAMeeting = false;
		for(Move move : unit.getMoves())
		{
			Cell cell = getCell(move.getCellUID());

			//-----------------------------------------------------------------------------------//

			boolean[] attackOrDefendACity = checkAttackOrDefendACity(cell, dataContainer, allyUIDOfUnitArriving);
			boolean attackACity = attackOrDefendACity[0];
			boolean defendACity = attackOrDefendACity[1];
			
			//-----------------------------------------------------------------------------------//
			
			if(cell.getRecordedMoves().size() == 0 && attackACity)
				;//attackACityWithNoArmyInDefense
				
			// on doit choisir le plus grand timeFrom  avec timeFrom < celui ou on arrive
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
					
					foundAMeeting = true;
				}
			}
		}
		
		
		return dataContainer.objectsAltered;
	}

	//-------------------------------------------------------------------------------------//

	private boolean areOnSameAlly(Unit unit, Unit unitMet) {
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
		
		if(!meetingHappens)
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
	
	private boolean[] checkAttackOrDefendACity(Cell cell, DataContainer dataContainer, String allyUIDOfUnitArriving) 
	{
		boolean[] result = new boolean[2];
		result[0] = false;
		result[1] = false;
		       
		if(cell.getCity() != null)
		{
			Player playerOfTheCity = getPlayer(cell.getCity().getOwnerUID(), dataContainer);
			String allyUIDOfTheCity = playerOfTheCity.getAlly() == null ? playerOfTheCity.getUralysUID() : playerOfTheCity.getAlly().getAllyUID(); 
			
			if(!allyUIDOfTheCity.equals(allyUIDOfUnitArriving))
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
		return EntitiesConverter.convertCaseDTO(gameDao.getCase(TribesUtils.getX(caseUID),TribesUtils.getY(caseUID)));
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
	
	//-----------------------------------------------------------------------------------//
	
	private class DataContainer{
		private HashMap<String, Unit> unitsLoaded = new HashMap<String, Unit>();
		private HashMap<String, Player> playerAlreadyLoaded = new HashMap<String, Player>();
		private ObjectsAltered objectsAltered = new ObjectsAltered();
	}

}
