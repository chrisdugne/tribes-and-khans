package com.uralys.tribes.entities;

public class Merchant {

	private String merchantUID;
	
	private int size;
	private int speed;
	
	private int wheat;
	private int wood;
	private int iron;
	private int gold;

	private int x;
	private int y;
	private int radius;
	
	
	//-----------------------------------------------------------------------------------//
	
	public int getWheat() {
		return wheat;
	}

	public String getMerchantUID() {
		return merchantUID;
	}

	public void setMerchantUID(String merchantUID) {
		this.merchantUID = merchantUID;
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

	//-----------------------------------------------------------------------------------//
	
}
