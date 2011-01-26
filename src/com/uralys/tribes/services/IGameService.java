package com.uralys.tribes.services;

import java.util.List;
import com.uralys.tribes.entities.Game;

public interface IGameService {

	public List<Game> createGame(String uralysUID, String gameName, String playerName, int nbMinByTurn) ;
	public List<Game> joinGame(String uralysUID, String gameUID, String playerName);

	public List<Game> getGamesToJoin();
	public List<Game> getCurrentGames(String uralysUID);

	public boolean launchGame(String gameUID);
	
}
