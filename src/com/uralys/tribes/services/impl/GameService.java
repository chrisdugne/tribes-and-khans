package com.uralys.tribes.services.impl;

import com.uralys.tribes.domain.IGameManager;
import com.uralys.tribes.entities.Game;
import com.uralys.tribes.services.IGameService;

public class GameService implements IGameService {

	//=========================================================================//
	
	private IGameManager gameManager;

	public GameService (IGameManager gameManager){
		this.gameManager = gameManager;
	}

	//=========================================================================//

	public Game createGame(String playerUID, String gameName) {
		return gameManager.createGame(playerUID, gameName);
	}

}
