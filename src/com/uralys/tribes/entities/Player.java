package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Player {

	//-----------------------------------------------------------------------------------//
	
	private String uralysUID;

	private String email;
	private String password;
	private String name;
	
	private List<Move> moves = new ArrayList<Move>();
	
	//-----------------------------------------------------------------------------------//

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Move> getMoves() {
		return moves;
	}
	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
