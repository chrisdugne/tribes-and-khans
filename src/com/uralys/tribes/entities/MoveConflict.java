package com.uralys.tribes.entities;

public class MoveConflict {
	
	//-----------------------------------------------------------------------------------//

	private Integer armySize;
	private Integer armyBows;
	private Integer armySwords;
	private Integer armyArmors;
	private Integer xFrom;
	private Integer yFrom;

	private Boolean armyStanding;

	//-----------------------------------------------------------------------------------//

	public Integer getArmySize() {
		return armySize;
	}
	public void setArmySize(Integer armySize) {
		this.armySize = armySize;
	}
	public Integer getArmyBows() {
		return armyBows;
	}
	public void setArmyBows(Integer armyBows) {
		this.armyBows = armyBows;
	}
	public Integer getArmySwords() {
		return armySwords;
	}
	public void setArmySwords(Integer armySwords) {
		this.armySwords = armySwords;
	}
	public Integer getArmyArmors() {
		return armyArmors;
	}
	public void setArmyArmors(Integer armyArmors) {
		this.armyArmors = armyArmors;
	}
	public Integer getxFrom() {
		return xFrom;
	}
	public void setxFrom(Integer xFrom) {
		this.xFrom = xFrom;
	}
	public Integer getyFrom() {
		return yFrom;
	}
	public void setyFrom(Integer yFrom) {
		this.yFrom = yFrom;
	}
	public Boolean getArmyStanding() {
		return armyStanding;
	}
	public void setArmyStanding(Boolean armyStanding) {
		this.armyStanding = armyStanding;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
