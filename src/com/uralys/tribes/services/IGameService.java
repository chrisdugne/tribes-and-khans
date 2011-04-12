package com.uralys.tribes.services;

import java.util.List;

import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Unit;

public interface IGameService {

	public String createPlayer(String uralysUID, String email);
	public Player getPlayer(String uralysUID);

	public List<Unit> savePlayer(Player player);
	
	public List<Item> loadItems();
	public List<Case> loadCases(List<String> caseUIDs);
}
