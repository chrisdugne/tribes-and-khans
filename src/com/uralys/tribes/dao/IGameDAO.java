package com.uralys.tribes.dao;

import java.util.List;

import com.uralys.tribes.entities.dto.GameDTO;

public interface IGameDAO {

	public GameDTO createGame(String uralysUID, String gameName, int autoEndTurnPeriod);
	public List<GameDTO> getCurrentGames(String uralysUID);
	public List<GameDTO> getGamesToJoin();
}
