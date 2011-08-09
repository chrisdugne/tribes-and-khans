package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;


public class Ally {
	
	//-----------------------------------------------------------------------------------//
	private String allyUID;
	
	private String name;
	private List<Player> players = new ArrayList<Player>();

	private Integer nbPlayers; 
	private Integer nbLands; 
	private Integer nbPopulation;
	private Integer nbCities;
	private Integer nbArmies;

	private String profile;
	
	//-----------------------------------------------------------------------------------//

	public String getAllyUID() {
		return allyUID;
	}

	public void setAllyUID(String allyUID) {
		this.allyUID = allyUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNbPlayers() {
		return nbPlayers;
	}

	public void setNbPlayers(Integer nbPlayers) {
		this.nbPlayers = nbPlayers;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Integer getNbLands() {
		return nbLands;
	}

	public void setNbLands(Integer nbLands) {
		this.nbLands = nbLands;
	}

	public Integer getNbPopulation() {
		return nbPopulation;
	}

	public void setNbPopulation(Integer nbPopulation) {
		this.nbPopulation = nbPopulation;
	}

	public Integer getNbCities() {
		return nbCities;
	}

	public void setNbCities(Integer nbCities) {
		this.nbCities = nbCities;
	}

	public Integer getNbArmies() {
		return nbArmies;
	}

	public void setNbArmies(Integer nbArmies) {
		this.nbArmies = nbArmies;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
