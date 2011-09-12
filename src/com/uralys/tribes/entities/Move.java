package com.uralys.tribes.entities;

public class Move {
	
	//-----------------------------------------------------------------------------------//
	
	private String moveUID;

	private String cellUID;
	private long timeFrom;
	private long timeTo;
	private String unitUID;
	private boolean hidden;
	
	//-----------------------------------------------------------------------------------//

	public Move(){}
	
	public Move(int i, int j, long timeFrom, long timeTo) 
	{
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.cellUID = "cell_"+i+"_"+j;
	}

	//-----------------------------------------------------------------------------------//
	
	public String getMoveUID() {
		return moveUID;
	}
	public void setMoveUID(String moveUID) {
		this.moveUID = moveUID;
	}
	public String getCellUID() {
		return cellUID;
	}
	public void setCellUID(String cellUID) {
		this.cellUID = cellUID;
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
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	//-----------------------------------------------------------------------------------//
	
	
}
