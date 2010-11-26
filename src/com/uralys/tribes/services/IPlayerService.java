package com.uralys.tribes.services;

import com.uralys.tribes.entities.Player;

public interface IPlayerService {

	public Player createPlayer(String uralysUID);
	public Player getPlayer(String uralysUID);
	
}
