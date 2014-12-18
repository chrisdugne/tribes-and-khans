package com.uralys.tribes.entities;

public class UnitReport {

	private String unitUID;
	private String ownerUID;
	private String ownerName;
	private int type;
	private int size;
	private int bows;
	private int armors;
	private int swords;
	private int value;
	private int wheat;
	private int wood;
	private int iron;

	private boolean attackACity;
	private boolean defendACity;
	
	//-----------------------------------------------------------------------------------//	
	public String getUnitUID() {
		return unitUID;
	}
	public void setUnitUID(String unitUID) {
		this.unitUID = unitUID;
	}
	public boolean isDefendACity() {
		return defendACity;
	}
	public void setDefendACity(boolean defendACity) {
		this.defendACity = defendACity;
	}
	public boolean isAttackACity() {
		return attackACity;
	}
	public void setAttackACity(boolean attackACity) {
		this.attackACity = attackACity;
	}
	public String getOwnerUID() {
		return ownerUID;
	}
	public void setOwnerUID(String ownerUID) {
		this.ownerUID = ownerUID;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getBows() {
		return bows;
	}
	public void setBows(int bows) {
		this.bows = bows;
	}
	public int getArmors() {
		return armors;
	}
	public void setArmors(int armors) {
		this.armors = armors;
	}
	public int getSwords() {
		return swords;
	}
	public void setSwords(int swords) {
		this.swords = swords;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
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
	private int gold;

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
