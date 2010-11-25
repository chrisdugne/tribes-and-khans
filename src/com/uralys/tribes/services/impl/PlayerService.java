package com.uralys.tribes.services.impl;

import com.uralys.tribes.domain.IPlayerManager;
import com.uralys.tribes.services.IPlayerService;

public class PlayerService implements IPlayerService {

	//=========================================================================//
	
	private IPlayerManager playerManager;

	public PlayerService (IPlayerManager playerManager){
		this.playerManager = playerManager;
	}

	//=========================================================================//

}
