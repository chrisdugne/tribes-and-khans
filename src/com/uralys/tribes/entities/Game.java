package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Game {

	//-----------------------------------------------------------------------------------//

	public final static int IN_CREATION = 1;
	public final static int RUNNING = 2;
	
	public final static int H00 = 1;
	public final static int H06 = 2;
	public final static int H12 = 3;
	public final static int H18 = 4;
	
	//-----------------------------------------------------------------------------------//

	private String gameUID;

	private String name;
	private int status;
	private int autoEndTurnTime;	
	private int currentTurn;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCurrentTurn() {
		return currentTurn;
	}
	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}
	public int getAutoEndTurnTime() {
		return autoEndTurnTime;
	}
	public void setAutoEndTurnTime(int autoEndTurnTime) {
		this.autoEndTurnTime = autoEndTurnTime;
	}
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
