package com.uralys.tribes.domain.impl;

import com.uralys.tribes.dao.IPlayerDAO;
import com.uralys.tribes.domain.IPlayerManager;
import com.uralys.tribes.entities.Profil;
import com.uralys.tribes.entities.converters.EntitiesConverter;

public class PlayerManager implements IPlayerManager {
	
	//==================================================================================================//
	
	private IPlayerDAO playerDao;

	public PlayerManager (IPlayerDAO playerDao){
		this.playerDao = playerDao;
	}

	//==================================================================================================//
	
	public Profil createProfil(String uralysUID, String email) {
		return EntitiesConverter.convertProfilDTO(playerDao.createProfil(uralysUID, email));			
	}
	
	public Profil getProfil(String uralysUID) {
		try{
			return EntitiesConverter.convertProfilDTO(playerDao.getProfil(uralysUID));
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
