package com.uralys.tribes.domain;

import java.util.List;

import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Player;

public interface IGameManager {

	public String createPlayer(String uralysUID, String email);
	public Player getPlayer(String uralysUID);
	public List<Item> loadItems();
	
}
