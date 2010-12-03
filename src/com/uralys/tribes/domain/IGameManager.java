package com.uralys.tribes.domain;

import com.uralys.tribes.entities.Game;

public interface IGameManager {

	public Game createGame(String uralysUID, String gameName, int autoEndTurnPeriod);
}
