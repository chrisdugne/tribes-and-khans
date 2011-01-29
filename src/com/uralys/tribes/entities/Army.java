package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Army {

	private String armyUID;
	
	private int size;
	private int speed;

	private int value;
	
	private int x;
	private int y;

	private List<Equipment> equipments = new ArrayList<Equipment>();
	
	//-----------------------------------------------------------------------------------//
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
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

	public String getArmyUID() {
		return armyUID;
	}

	public void setArmyUID(String armyUID) {
		this.armyUID = armyUID;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public List<Equipment> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<Equipment> equipments) {
		this.equipments = equipments;
	}

	//-----------------------------------------------------------------------------------//
	
}
