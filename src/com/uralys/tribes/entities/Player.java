package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Player 
{
	private String uralysUID;

	private String allyUID;
	private String name;
	
	private List<City> cities = new ArrayList<City>();
	private List<Unit> units = new ArrayList<Unit>();
	private int nbLands;
	private List<Conflict> conflicts = new ArrayList<Conflict>();
	private List<String> newMessages = new ArrayList<String>();
	private List<String> readMessages = new ArrayList<String>();

	private long lastStep;
	
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
	public List<String> getNewMessages() {
		return newMessages;
	}
	public void setNewMessages(List<String> newMessages) {
		this.newMessages = newMessages;
	}
	public List<String> getReadMessages() {
		return readMessages;
	}
	public void setReadMessages(List<String> oldMessages) {
		this.readMessages = oldMessages;
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
	public int getNbLands() {
		return nbLands;
	}
	public void setNbLands(int nbLands) {
		this.nbLands = nbLands;
	}
	public List<Conflict> getConflicts() {
		return conflicts;
	}
	public void setConflicts(List<Conflict> conflicts) {
		this.conflicts = conflicts;
	}
	public String getAllyUID() {
		return allyUID;
	}
	public void setAllyUID(String allyUID) {
		this.allyUID = allyUID;
	}
	public List<Unit> getUnits() {
		return units;
	}
	public void setUnits(List<Unit> units) {
		this.units = units;
	}
	public long getLastStep() {
		return lastStep;
	}
	public void setLastStep(long lastStep) {
		this.lastStep = lastStep;
	}
	
	
	//-----------------------------------------------------------------------------------//
	
}
