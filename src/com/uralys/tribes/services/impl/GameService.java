package com.uralys.tribes.services.impl;

import java.util.List;

import com.uralys.tribes.domain.IGameManager;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.services.IGameService;

public class GameService implements IGameService {

	//=========================================================================//
	
	private IGameManager gameManager;

	public GameService (IGameManager gameManager){
		this.gameManager = gameManager;
	}

	
	//=========================================================================//

	public String createPlayer(String uralysUID, String email) {
		return gameManager.createPlayer(uralysUID, email);
	}
	
	public Player getPlayer(String uralysUID) {
		try{
			return gameManager.getPlayer(uralysUID);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	//=========================================================================//
	
	public List<Item> loadItems() {
		return gameManager.loadItems();
	}
	
	//-----------------------------------------------------------------------------------//}
