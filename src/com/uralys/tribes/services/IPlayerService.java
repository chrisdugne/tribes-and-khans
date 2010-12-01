package com.uralys.tribes.services;

import com.uralys.tribes.entities.Profil;

public interface IPlayerService {

	public Profil createProfil(String uralysUID, String email);
	public Profil getProfil(String uralysUID);
	
}
