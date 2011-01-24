package com.uralys.tribes.services;

import java.util.List;
import com.uralys.tribes.entities.Game;

public interface IGameService {

	public List<Game> createGame(String uralysUID, String gameName, int nbMinByTurn) ;
	public Game joinGame(String uralysUID, String gameUID);

	public List<Game> getGamesToJoin();
	public List<Game> getCurrentGames(String uralysUID);
	
}
