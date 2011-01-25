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

	public List<Game> createGame(String uralysUID, String gameName, String playerName, int nbMinByTurn)  {
		gameManager.createGame(uralysUID, gameName, playerName, nbMinByTurn);
		return getCurrentGames(uralysUID);
	}

	public List<Game> joinGame(String uralysUID, String gameUID, String playerName) {
		gameManager.joinGame(uralysUID, gameUID, playerName);
		return getCurrentGames(uralysUID);
	}

	public List<Game> getGamesToJoin() {
		try{
			return gameManager.getGamesToJoin();
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public List<Game> getCurrentGames(String uralysUID) {
		try{
			return gameManager.getCurrentGames(uralysUID);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
