package com.uralys.tribes.domain;

import com.uralys.tribes.entities.Player;

public interface IPlayerManager {

	public Player createPlayer(String uralysUID);
	public Player getPlayer(String uralysUID);
	
}
