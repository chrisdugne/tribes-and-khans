package com.uralys.tribes.services;

import java.util.List;

import com.uralys.tribes.entities.Game;

public interface IPlayerService {

	public List<Game> getCurrentGames(String playerUID);
}
