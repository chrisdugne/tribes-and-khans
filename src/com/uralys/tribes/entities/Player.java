package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Player {

	private String playerUID;

	private String name;
	private String gameName;
	private String gameUID;
	
	private List<City> cities = new ArrayList<City>();
	private List<Army> armies = new ArrayList<Army>();
	private List<Merchant> merchants = new ArrayList<Merchant>();
	private List<Move> moves = new ArrayList<Move>();
	
	//-----------------------------------------------------------------------------------//

	public String getPlayerUID() {
		return playerUID;
	}
	public void setPlayerUID(String playerUID) {
		this.playerUID = playerUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGameUID() {
		return gameUID;
	}
	public void setGameUID(String gameUID) {
		this.gameUID = gameUID;
	}
	public List<Move> getMoves() {
		return moves;
	}
	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	public List<Army> getArmies() {
		return armies;
	}
	public void setArmies(List<Army> armies) {
		this.armies = armies;
	}
	public List<Merchant> getMerchants() {
		return merchants;
	}
	public void setMerchants(List<Merchant> merchants) {
		this.merchants = merchants;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
