package com.uralys.tribes.domain.impl;

import com.uralys.tribes.dao.IPlayerDAO;
import com.uralys.tribes.domain.IPlayerManager;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.converters.EntitiesConverter;

public class PlayerManager implements IPlayerManager {
	
	//==================================================================================================//
	
	private IPlayerDAO playerDao;

	public PlayerManager (IPlayerDAO playerDao){
		this.playerDao = playerDao;
	}

	//==================================================================================================//
	
	public Player createPlayer(String uralysUID) {
		return EntitiesConverter.convertPlayerDTO(playerDao.createPlayer(uralysUID));
	}
	
	public Player getPlayer(String uralysUID) {
		return EntitiesConverter.convertPlayerDTO(playerDao.getPlayer(uralysUID));
	}

}
