package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Case {

	//-----------------------------------------------------------------------------------//

	public final static int FOREST = 0;
	public final static int CITY = 1;
	public final static int LAKE = 2;
	public final static int ROCK = 3;
	
	//-----------------------------------------------------------------------------------//

	private String caseUID;

	private int x;
	private int y;
	
	private List<Move> recordedMoves;
	private int type;
	private City city;
	private Player landOwner;

	//-----------------------------------------------------------------------------------//

	public Case() {}

	public Case(int i, int j) {
		x = i;
		y = j;
		recordedMoves = new ArrayList<Move>();
	}

	//-----------------------------------------------------------------------------------//

	public int getX() {
		return x;
	}

	public String getCaseUID() {
		return caseUID;
	}

	public void setCaseUID(String caseUID) {
		this.caseUID = caseUID;
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

	public List<Move> getRecordedMoves() {
		return recordedMoves;
	}

	public void setRecordedMoves(List<Move> moves) {
		this.recordedMoves = moves;
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
	
	//-----------------------------------------------------------------------------------//
	
}
