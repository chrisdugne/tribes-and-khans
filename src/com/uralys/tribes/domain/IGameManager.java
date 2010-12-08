package com.uralys.tribes.domain;

import java.util.List;

import com.uralys.tribes.entities.Game;

public interface IGameManager {

	public void createGame(String uralysUID, String gameName, int autoEndTurnPeriod);
	public List<Game> getCurrentGames(String uralysUID);
	public List<Game> getGamesToJoin(); 
}
