package com.uralys.tribes.entities;

public class Cell {

	//-----------------------------------------------------------------------------------//

	public final static int FOREST = 0;
	public final static int CITY = 1;
	public final static int LAKE = 2;
	public final static int ROCK = 3;
	
	//-----------------------------------------------------------------------------------//

	private String cellUID;

	private int group;
	private int x;
	private int y;
	
	private int type;
	private City city;
	
	private Player landOwner;
	private Player challenger;
	private long timeFromChallenging;
	
	private Unit army;
	private Unit caravan;
	
	private long timeToChangeUnit;
	private String nextCellUID;

	//-----------------------------------------------------------------------------------//

	public Cell() {}

	public Cell(int i, int j) {
		x = i;
		y = j;
	}

	//-----------------------------------------------------------------------------------//

	public int getX() {
		return x;
	}

	public String getCellUID() {
		return cellUID;
	}

	public void setCellUID(String cellUID) {
		this.cellUID = cellUID;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Player getLandOwner() {
		return landOwner;
	}

	public void setLandOwner(Player landOwner) {
		this.landOwner = landOwner;
	}

	public Player getChallenger() {
		return challenger;
	}

	public void setChallenger(Player challenger) {
		this.challenger = challenger;
	}

	public long getTimeFromChallenging() {
		return timeFromChallenging;
	}

	public void setTimeFromChallenging(long timeFromChallenging) {
		this.timeFromChallenging = timeFromChallenging;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public Unit getArmy() {
		return army;
	}

	public void setArmy(Unit army) {
		this.army = army;
	}

	public Unit getCaravan() {
		return caravan;
	}

	public void setCaravan(Unit caravan) {
		this.caravan = caravan;
	}

	public long getTimeToChangeUnit() {
		return timeToChangeUnit;
	}

	public void setTimeToChangeUnit(long timeToChangeUnit) {
		this.timeToChangeUnit = timeToChangeUnit;
	}

	public String getNextCellUID() {
		return nextCellUID;
	}

	public void setNextCellUID(String nextCellUID) {
		this.nextCellUID = nextCellUID;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
