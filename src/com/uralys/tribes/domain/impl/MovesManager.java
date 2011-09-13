package com.uralys.tribes.domain.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.domain.IMovesManager;
import com.uralys.tribes.entities.Cell;
import com.uralys.tribes.entities.ObjectsAltered;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.converters.EntitiesConverter;
import com.uralys.tribes.utils.TribesUtils;
import com.uralys.utils.Utils;

public class MovesManager implements IMovesManager{

	//==================================================================================================//

	private IGameDAO gameDao;

	public MovesManager (IGameDAO gameDao){
		this.gameDao = gameDao;
	}

	//==================================================================================================//

	public ObjectsAltered refreshUnitWay(Unit unit) 
	{
		Utils.print("--------------------");
		Utils.print("refreshUnitWay");
		DataContainer dataContainer = new DataContainer();
		return dataContainer.objectsAltered;
	}

	//-----------------------------------------------------------------------------------//	
	@SuppressWarnings("unused")
	private Player getPlayer(String uralysUID, DataContainer datacontainer, boolean newConnection) 
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
	
	@SuppressWarnings("unused")
	private Cell getCase(String caseUID) {
		return EntitiesConverter.convertCaseDTO(gameDao.getCase(TribesUtils.getX(caseUID),TribesUtils.getY(caseUID)));
	}

	@SuppressWarnings("unused")
	private Unit getUnit(String unitUID, DataContainer datacontainer) {
		return getUnit(unitUID, datacontainer, false);
	}
	
	private Unit getUnit(String unitUID, DataContainer datacontainer, boolean requireLinkedMoveFromGathering) 
	{
		if(unitUID == null)
			return null;
		
		if(datacontainer != null){
			if(datacontainer.unitsLoaded.get(unitUID) == null){
				datacontainer.unitsLoaded.put(unitUID, EntitiesConverter.convertUnitDTO(gameDao.getUnit(unitUID), true, requireLinkedMoveFromGathering, true));
			}
			
			return datacontainer.unitsLoaded.get(unitUID);
		}
		else
			return EntitiesConverter.convertUnitDTO(gameDao.getUnit(unitUID), true, requireLinkedMoveFromGathering, true);

	}
	
	//-----------------------------------------------------------------------------------//
	private class DataContainer{
		private HashMap<String, Unit> unitsLoaded = new HashMap<String, Unit>();
		private HashMap<String, Player> playerAlreadyLoaded = new HashMap<String, Player>();
		private ObjectsAltered objectsAltered = new ObjectsAltered();
	}

}
