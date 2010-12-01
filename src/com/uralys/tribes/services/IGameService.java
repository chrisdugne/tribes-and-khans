package com.uralys.tribes.services;

import java.util.List;
import com.uralys.tribes.entities.Game;

public interface IGameService {

	public Game createGame(String uralysUID, String gameName);
	public Game joinGame(String uralysUID, String gameUID);

	public List<Game> getGamesToJoin();
	
}
