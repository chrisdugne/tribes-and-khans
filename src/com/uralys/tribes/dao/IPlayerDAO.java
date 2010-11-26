package com.uralys.tribes.dao;

import com.uralys.tribes.entities.dto.PlayerDTO;

public interface IPlayerDAO {
	
	public PlayerDTO createPlayer(String uralysUID);
	public PlayerDTO getPlayer(String uralysUID);
	
}
