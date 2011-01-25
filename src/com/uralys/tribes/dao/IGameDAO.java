package com.uralys.tribes.dao;

import java.util.List;

import com.uralys.tribes.entities.dto.GameDTO;

public interface IGameDAO {

	public void createGame(String uralysUID, String gameName, String playerName, int nbMinByTurn) ;
	public List<GameDTO> getCurrentGames(String uralysUID);
	public List<GameDTO> getGamesToJoin();
}
