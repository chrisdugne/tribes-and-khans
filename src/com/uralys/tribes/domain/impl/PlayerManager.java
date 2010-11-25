package com.uralys.tribes.domain.impl;

import com.uralys.tribes.dao.IPlayerDAO;
import com.uralys.tribes.domain.IPlayerManager;

public class PlayerManager implements IPlayerManager {
	
	//==================================================================================================//
	
	private IPlayerDAO playerDao;

	public PlayerManager (IPlayerDAO playerDao){
		this.playerDao = playerDao;
	}

	//==================================================================================================//

}
