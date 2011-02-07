package com.uralys.tribes.services.impl;

import com.uralys.tribes.domain.IPlayerManager;
import com.uralys.tribes.entities.Profil;
import com.uralys.tribes.services.IPlayerService;

public class PlayerService implements IPlayerService {

	//=========================================================================//
	
	private IPlayerManager playerManager;

	public PlayerService (IPlayerManager playerManager){
		this.playerManager = playerManager;
	}

	//=========================================================================//

	public Profil createProfil(String uralysUID, String email) {
		return playerManager.createProfil(uralysUID, email);
	}
	
	public Profil getProfil(String uralysUID) {
		try{
			return playerManager.getProfil(uralysUID);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
