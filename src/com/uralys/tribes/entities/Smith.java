package com.uralys.tribes.entities;


public class Smith {
	
	//-----------------------------------------------------------------------------------//

	private String smithUID;
	private Item item;
	private int people;
	
	//-----------------------------------------------------------------------------------//

	public int getPeople() {
		return people;
	}
	public String getSmithUID() {
		return smithUID;
	}
	public void setSmithUID(String smithUID) {
		this.smithUID = smithUID;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public void setPeople(int people) {
		this.people = people;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
