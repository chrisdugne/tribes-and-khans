package com.uralys.tribes.entities;

public class Move {
	
	//-----------------------------------------------------------------------------------//
	
	private String moveUID;

	private Case _case;
	private long timeFrom;
	private long timeTo;
	private String unitUID;
	private int value;
	
	//-----------------------------------------------------------------------------------//

	public Move(){}
	
	public Move(int i, int j, long timeFrom, long timeTo) {
		_case = new Case(i, j);
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
	}

	//-----------------------------------------------------------------------------------//
	
	public String getMoveUID() {
		return moveUID;
	}
	public void setMoveUID(String moveUID) {
		this.moveUID = moveUID;
	}
	public Case getCase() {
		return _case;
	}
	public void setCase(Case _case) {
		this._case = _case;
	}
	public long getTimeFrom() {
		return timeFrom;
	}
	public void setTimeFrom(long timeFrom) {
		this.timeFrom = timeFrom;
	}
	public long getTimeTo() {
		return timeTo;
	}
	public void setTimeTo(long timeTo) {
		this.timeTo = timeTo;
	}
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
	
	//-----------------------------------------------------------------------------------//
	
}
