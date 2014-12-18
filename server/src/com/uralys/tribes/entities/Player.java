package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Player 
{
	//-----------------------------------------------------------------------------------//
	
	private String uralysUID;

	private String name;
	private Ally ally;
	
	private List<City> cities = new ArrayList<City>();
	private List<Unit> units = new ArrayList<Unit>();
	private List<Message> newMessages = new ArrayList<Message>();
	private List<Message> readMessages = new ArrayList<Message>();
	private List<Message> archivedMessages = new ArrayList<Message>();

	private long lastStep;

	private Integer nbLands;
	private Integer nbPopulation;
	private Integer nbCities;
	private Integer nbArmies;

	private String profile;
	
	//-----------------------------------------------------------------------------------//

	public String getUralysUID() {
		return uralysUID;
	}
	public void setUralysUID(String uralysUID) {
		this.uralysUID = uralysUID;
	}
	public String getName() {
		return name;
	}
	public List<Message> getNewMessages() {
		return newMessages;
	}
	public void setNewMessages(List<Message> newMessages) {
		this.newMessages = newMessages;
	}
	public List<Message> getReadMessages() {
		return readMessages;
	}
	public void setReadMessages(List<Message> oldMessages) {
		this.readMessages = oldMessages;
	}
	public List<Message> getArchivedMessages() {
		return archivedMessages;
	}
	public void setArchivedMessages(List<Message> archivedMessages) {
		this.archivedMessages = archivedMessages;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	public Integer getNbLands() {
		return nbLands;
	}
	public void setNbLands(Integer nbLands) {
		this.nbLands = nbLands;
	}
	public Ally getAlly() {
		return ally;
	}
	public void setAlly(Ally ally) {
		this.ally = ally;
	}
	public List<Unit> getUnits() {
		return units;
	}
	public void setUnits(List<Unit> units) {
		this.units = units;
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
	public long getLastStep() {
		return lastStep;
	}
	public void setLastStep(long lastStep) {
		this.lastStep = lastStep;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getProfile() {
		return profile;
	}
	
	
	//-----------------------------------------------------------------------------------//
	
}
