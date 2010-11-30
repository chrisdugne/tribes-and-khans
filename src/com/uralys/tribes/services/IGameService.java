package com.uralys.tribes.services;

import com.uralys.tribes.entities.Game;

public interface IGameService {

	public Game createGame(String playerUID, String gameName);
}
