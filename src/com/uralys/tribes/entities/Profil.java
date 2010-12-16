package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Profil {

	private String uralysUID;
	private List<Player> players = new ArrayList<Player>();
	
	//-----------------------------------------------------------------------------------//

	public String getUralysUID() {
		return uralysUID;
	}
	public void setUralysUID(String uralysUID) {
		this.uralysUID = uralysUID;
	}
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
