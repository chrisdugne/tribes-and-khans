package com.uralys.tribes.services;

import java.util.List;

import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.DataContainer4UnitSaved;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Unit;

public interface IGameService {

	public String createPlayer(String uralysUID, String email);
	public Player getPlayer(String uralysUID);

	public void savePlayer(Player player);
	public void deleteUnit(String uralysUID, String unitUID);
	public void deleteUnits(String uralysUID, List<String> unitsUID);
	public void deleteMove(String moveUID);
	
	public DataContainer4UnitSaved createUnit(String uralysUID, Unit unit, String cityUID);
	public DataContainer4UnitSaved updateUnit(Unit unit, String cityUID);

	
	public List<Item> loadItems();
	public List<Case> loadCases(List<String> caseUIDs);
	
	public void changeName(String uralysUID, String newName);
	public void changeCityName(String cityUID, String newName);
}
