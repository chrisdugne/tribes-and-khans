package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class City {

	private String cityUID;
	private String name;
	
	private int population;
	
	private int wheat;
	private int wood;
	private int iron;
	private int gold;

	private int x;
	private int y;
	private int radius;
	
	private List<Equipment> equipmentStock = new ArrayList<Equipment>();
	
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

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
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

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public List<Equipment> getEquipmentStock() {
		return equipmentStock;
	}

	public void setEquipmentStock(List<Equipment> equipmentStock) {
		this.equipmentStock = equipmentStock;
	}

	//-----------------------------------------------------------------------------------//
	
}
