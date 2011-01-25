package com.uralys.tribes.entities;


public class Weapon {

	private String weaponUID;
	private int wood;
	private int iron;
	private int goldPrice;
	private int value;

	//-----------------------------------------------------------------------------------//
	
	public String getWeaponUID() {
		return weaponUID;
	}
	public void setWeaponUID(String weaponUID) {
		this.weaponUID = weaponUID;
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
	public int getGoldPrice() {
		return goldPrice;
	}
	public void setGoldPrice(int goldPrice) {
		this.goldPrice = goldPrice;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

	//-----------------------------------------------------------------------------------//

	
}
