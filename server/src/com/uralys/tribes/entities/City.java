package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class City {
	
	//-----------------------------------------------------------------------------------//

	private String cityUID;
	private String ownerUID;
	private String name;
	
	private long beginTime;
	private long endTime;

	private int population;
	
	private int wheat;
	private int peopleCreatingWheat;
	private int wood;
	private int peopleCreatingWood;
	private int iron;
	private int peopleCreatingIron;

	private int x;
	private int y;
	private int gold;

	private int bows;
	private int swords;
	private int armors;

	private List<Stock> stocks = new ArrayList<Stock>();
	
	//-----------------------------------------------------------------------------------//
	
	public String getCityUID() {
		return cityUID;
	}

	public void setCityUID(String cityUID) {
		this.cityUID = cityUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public int getWheat() {
		return wheat;
	}

	public void setWheat(int wheat) {
		this.wheat = wheat;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public int getIron() {
		return iron;
	}

	public void setIron(int iron) {
		this.iron = iron;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getPeopleCreatingWheat() {
		return peopleCreatingWheat;
	}

	public void setPeopleCreatingWheat(int peopleCreatingWheat) {
		this.peopleCreatingWheat = peopleCreatingWheat;
	}

	public int getPeopleCreatingWood() {
		return peopleCreatingWood;
	}

	public void setPeopleCreatingWood(int peopleCreatingWood) {
		this.peopleCreatingWood = peopleCreatingWood;
	}

	public int getPeopleCreatingIron() {
		return peopleCreatingIron;
	}

	public void setPeopleCreatingIron(int peopleCreatingIron) {
		this.peopleCreatingIron = peopleCreatingIron;
	}

	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getOwnerUID() {
		return ownerUID;
	}

	public void setOwnerUID(String ownerUID) {
		this.ownerUID = ownerUID;
	}

	public List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}

	public int getBows() {
		return bows;
	}

	public void setBows(int bows) {
		this.bows = bows;
	}

	public int getSwords() {
		return swords;
	}

	public void setSwords(int swords) {
		this.swords = swords;
	}

	public int getArmors() {
		return armors;
	}

	public void setArmors(int armors) {
		this.armors = armors;
	}

	
	//-----------------------------------------------------------------------------------//
	
}
