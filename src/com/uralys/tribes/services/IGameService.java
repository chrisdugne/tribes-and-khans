package com.uralys.tribes.services;

import java.util.List;

import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Unit;

public interface IGameService {

	public String createPlayer(String uralysUID, String email);
	public Player getPlayer(String uralysUID);

	public void savePlayer(Player player);
	public void saveUnit(String uralysUID, Unit unit);
	public void deleteUnit(String uralysUID, String unitUID);
	
	public List<Item> loadItems();
	public List<Case> loadCases(List<String> caseUIDs);
}
