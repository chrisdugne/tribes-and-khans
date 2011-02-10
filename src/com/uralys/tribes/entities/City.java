package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class City {

	private String cityUID;
	private String name;
	
	private int population;
	
	private int wheat;
	private int peopleCreatingWheat;
	private int wood;
	private int peopleCreatingWood;
	private int iron;
	private int peopleCreatingIron;

	private int x;
	private int y;

	private List<Equipment> equipmentStock = new ArrayList<Equipment>();
	private List<Smith> smiths = new ArrayList<Smith>();
	
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

	public List<Equipment> getEquipmentStock() {
		return equipmentStock;
	}

	public void setEquipmentStock(List<Equipment> equipmentStock) {
		this.equipmentStock = equipmentStock;
	}

	public List<Smith> getSmiths() {
		return smiths;
	}

	public void setSmiths(List<Smith> smiths) {
		this.smiths = smiths;
	}

	//-----------------------------------------------------------------------------------//
	
}
