package com.uralys.tribes.dao;

import com.uralys.tribes.entities.dto.GameDTO;

public interface IGameDAO {

	public GameDTO createGame(String uralysUID, String gameName);
}
