package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Game {

	private String gameUID;

	private String name;
	private List<Player> players = new ArrayList<Player>();
	
	//-----------------------------------------------------------------------------------//

	public String getGameUID() {
		return gameUID;
	}
	public void setGameUID(String gameUID) {
		this.gameUID = gameUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
