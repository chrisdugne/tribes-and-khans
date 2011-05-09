package com.uralys.tribes.entities;

public class Move {
	
	//-----------------------------------------------------------------------------------//
	
	private String moveUID;

	private String caseUID;
	private long timeFrom;
	private long timeTo;
	private String unitUID;
	private Gathering gathering;
	private int value;
	
	//-----------------------------------------------------------------------------------//

	public Move(){}
	
	public Move(int i, int j, long timeFrom, long timeTo) {
		
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.caseUID = "case_"+i+"_"+j;
	}

	//-----------------------------------------------------------------------------------//
	
	public String getMoveUID() {
		return moveUID;
	}
	public void setMoveUID(String moveUID) {
		this.moveUID = moveUID;
	}
	public String getCaseUID() {
		return caseUID;
	}
	public void setCaseUID(String caseUID) {
		this.caseUID = caseUID;
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
	public Gathering getGathering() {
		return gathering;
	}
	public void setGathering(Gathering gathering) {
		this.gathering = gathering;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	//-----------------------------------------------------------------------------------//
	
	// caseUID : case_i_j
	public int getX(){
		String[] split = caseUID.split("_");
		return Integer.parseInt(split[1]);
	}

	public int getY(){
		String[] split = caseUID.split("_");
		return Integer.parseInt(split[2]);
	}
	
}
