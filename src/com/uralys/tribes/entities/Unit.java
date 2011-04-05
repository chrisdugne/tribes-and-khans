package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Unit {

	//-----------------------------------------------------------------------------------//
	
	public final static int INTERCEPTED = 1;
	public final static int FREE = 2;
	public final static int DESTROYED = 3;

	//-----------------------------------------------------------------------------------//

	public final static int ARMY = 1;
	public final static int MERCHANT = 2;
	
	//-----------------------------------------------------------------------------------//

	private String unitUID;
	private int value;
	private int status = FREE;
	private int type;
	private Case currentCase;
	private String playerUID;

	private int size;
	private int speed;

	private int wheat;
	private int wood;
	private int iron;
	private int gold;
	
	private List<Move> moves = new ArrayList<Move>();
	private List<Equipment> equipments = new ArrayList<Equipment>();
	private List<Conflict> conflicts = new ArrayList<Conflict>();

	//-----------------------------------------------------------------------------------//
	
	
	public String getUnitUID() {
		return unitUID;
	}

	public void setUnitUID(String unitUID) {
		this.unitUID = unitUID;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Case getCurrentCase() {
		return currentCase;
	}

	public void setCurrentCase(Case currentCase) {
		this.currentCase = currentCase;
	}

	public List<Move> getMoves() {
		return moves;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPlayerUID() {
		return playerUID;
	}

	public void setPlayerUID(String playerUID) {
		this.playerUID = playerUID;
	}

	public List<Conflict> getConflicts() {
		return conflicts;
	}

	public void setConflicts(List<Conflict> conflicts) {
		this.conflicts = conflicts;
	}

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

	public List<Equipment> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<Equipment> equipments) {
		this.equipments = equipments;
	}

	//-----------------------------------------------------------------------------------//
	
	public boolean equals(Unit unit){
		return unit.getUnitUID().equals(unitUID);
	}

}
