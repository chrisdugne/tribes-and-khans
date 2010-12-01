package com.uralys.tribes.dao;

import com.uralys.tribes.entities.dto.ProfilDTO;

public interface IPlayerDAO {
	
	public ProfilDTO createProfil(String uralysUID, String email);
	public ProfilDTO getProfil(String uralysUID);
	
}
