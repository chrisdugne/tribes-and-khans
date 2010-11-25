package com.uralys.tribes.services.impl;

import java.util.List;

import com.uralys.tribes.domain.IPlayerManager;
import com.uralys.tribes.entities.Game;
import com.uralys.tribes.services.IPlayerService;

public class PlayerService implements IPlayerService {

	//=========================================================================//
	
	private IPlayerManager playerManager;

	public PlayerService (IPlayerManager playerManager){
		this.playerManager = playerManager;
	}

	//=========================================================================//

	public List<Game> getCurrentGames(String playerUID) {
		// TODO Auto-generated method stub
		return null;
	}
}
