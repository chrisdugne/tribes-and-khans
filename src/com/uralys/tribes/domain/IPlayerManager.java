package com.uralys.tribes.domain;

import com.uralys.tribes.entities.Profil;

public interface IPlayerManager {

	public Profil createProfil(String uralysUID, String email);
	public Profil getProfil(String uralysUID);
	
}
