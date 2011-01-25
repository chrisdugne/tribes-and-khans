package com.uralys.tribes.domain;

import java.util.List;

import com.uralys.tribes.entities.Game;

public interface IGameManager {

	public void createGame(String uralysUID, String gameName, String playerName, int nbMinByTurn);
	public void joinGame(String uralysUID, String gameUID, String playerName); 
	public List<Game> getCurrentGames(String uralysUID);
	public List<Game> getGamesToJoin();
}
