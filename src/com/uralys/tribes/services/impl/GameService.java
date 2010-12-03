package com.uralys.tribes.services.impl;

import java.util.List;

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

	public Game createGame(String uralysUID, String gameName, int autoEndTurnPeriod) {
		return gameManager.createGame(uralysUID, gameName, autoEndTurnPeriod);
	}

	public Game joinGame(String uralysUID, String gameUID) {
		return null;
	}

	public List<Game> getGamesToJoin() {
		return null;
	}

}
