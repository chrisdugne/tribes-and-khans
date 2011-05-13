package com.uralys.tribes.domain;

import java.util.List;

import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Unit;

public interface IGameManager {

	public String createPlayer(String uralysUID, String email);
	public Player getPlayer(String uralysUID);

	public void savePlayer(Player player);

	public void createUnit(String uralysUID, Unit unit, String cityUID, boolean needReplacing);
	public List<Case> updateUnit(Unit unit, String cityUID, boolean needReplacing);
	
	public void deleteUnit(String uralysUID, String unitUID);
	public void deleteMove(String moveUID) ;
	
	public List<Item> loadItems();
	public List<Case> loadCases(List<String> caseUIDs);
	
}
