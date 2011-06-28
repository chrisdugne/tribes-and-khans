package com.uralys.tribes.domain;

import java.util.List;

import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.DataContainer4UnitSaved;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Unit;

public interface IGameManager {

	public String createPlayer(String uralysUID, String email);
	public Player getPlayer(String uralysUID);

	public void savePlayer(Player player);
	public String buildCity(City city, String uralysUID);	
	public void saveCity(City city);	

	public DataContainer4UnitSaved createUnit(String uralysUID, Unit unit, String cityUID, boolean needReplacing);
	public DataContainer4UnitSaved updateUnit(Unit unit, String cityUID, boolean needReplacing);
	
	public void deleteUnit(String uralysUID, Unit unit);
	public void deleteUnits(String uralysUID, List<String> unitUIDs);
	public void deleteMove(String moveUID) ;
	
	public List<Item> loadItems();
	public List<Case> loadCases(int[] groups);
	public Case getCase(int x, int y);
	
	public void changeName(String uralysUID, String newName);
	public void changeCityName(String cityUID, String newName);
	public void changeMusicOn(String uralysUID, boolean musicOn);
	
}
