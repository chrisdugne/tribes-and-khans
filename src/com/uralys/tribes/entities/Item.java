package com.uralys.tribes.entities;


public class Item {

	private String itemUID;
	private String name;
	private int peopleRequired;
	private int wood;
	private int iron;
	private int goldPrice;
	private int value;

	//-----------------------------------------------------------------------------------//
	
	public String getItemUID() {
		return itemUID;
	}
	public void setItemUID(String itemUID) {
		this.itemUID = itemUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPeopleRequired() {
		return peopleRequired;
	}
	public void setPeopleRequired(int peopleRequired) {
		this.peopleRequired = peopleRequired;
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
