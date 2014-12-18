package com.uralys.tribes.entities;


public class Item {

	private String itemUID;
	private String name;
	private int peopleRequired;
	private int wood;
	private int iron;
	private Float goldPriceBase;
	private Float goldPriceCurrent;
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
	public Float getGoldPriceBase() {
		return goldPriceBase;
	}
	public void setGoldPriceBase(Float goldPriceBase) {
		this.goldPriceBase = goldPriceBase;
	}
	public Float getGoldPriceCurrent() {
		return goldPriceCurrent;
	}
	public void setGoldPriceCurrent(Float goldPriceCurrent) {
		this.goldPriceCurrent = goldPriceCurrent;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

	//-----------------------------------------------------------------------------------//

	
}
