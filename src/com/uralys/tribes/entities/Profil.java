package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Profil {

	private String uralysUID;
	private String email;
	private List<Player> players = new ArrayList<Player>();
	
	//-----------------------------------------------------------------------------------//

	public String getUralysUID() {
		return uralysUID;
	}
	public void setUralysUID(String uralysUID) {
		this.uralysUID = uralysUID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
