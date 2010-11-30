package com.uralys.tribes.domain.impl;

import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.domain.IGameManager;
import com.uralys.tribes.entities.Game;
import com.uralys.tribes.entities.converters.EntitiesConverter;

public class GameManager implements IGameManager {
	
	//==================================================================================================//
	
	private IGameDAO gameDao;

	public GameManager (IGameDAO gameDao){
		this.gameDao = gameDao;
	}

	//==================================================================================================//
	
	public Game createGame(String playerUID, String gameName) {
		return EntitiesConverter.convertGameDTO(gameDao.createGame(playerUID, gameName));
	}

}
