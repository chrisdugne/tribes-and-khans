package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Player {

	private String playerUID;

	private String name;
	private String gameName;
	private String gameUID;
	private Integer lastTurnPlayed;
	
	private List<City> cities = new ArrayList<City>();
	private List<Army> armies = new ArrayList<Army>();
	private List<Army> merchants = new ArrayList<Army>();
	private List<String> allies = new ArrayList<String>();
	private List<Integer> lands = new ArrayList<Integer>();
	private List<Conflict> conflicts = new ArrayList<Conflict>();

	
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
	public List<Army> getMerchants() {
		return merchants;
	}
	public void setMerchants(List<Army> merchants) {
		this.merchants = merchants;
	}
	public Integer getLastTurnPlayed() {
		return lastTurnPlayed;
	}
	public void setLastTurnPlayed(Integer lastTurnPlayed) {
		this.lastTurnPlayed = lastTurnPlayed;
	}
	public List<Integer> getLands() {
		return lands;
	}
	public void setLands(List<Integer> lands) {
		this.lands = lands;
	}
	public List<String> getAllies() {
		return allies;
	}
	public void setAllies(List<String> allies) {
		this.allies = allies;
	}
	public List<Conflict> getConflicts() {
		return conflicts;
	}
	public void setConflicts(List<Conflict> conflicts) {
		this.conflicts = conflicts;
	}
	
	
	//-----------------------------------------------------------------------------------//
	
}
