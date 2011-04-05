package com.uralys.tribes.services;

import java.util.List;

import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Player;

public interface IGameService {

	public String createPlayer(String uralysUID, String email);
	public Player getPlayer(String uralysUID);
	
	public List<Item> loadItems();
}
