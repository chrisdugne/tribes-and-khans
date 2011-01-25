package com.uralys.tribes.entities;

public class Move {

	private String moveUID;

	private int xFrom;
	private int xTo;
	private int yFrom;
	private int yTo;

	private int type; // 1:Army | 2:Merchants
	private int movingUID; // the uid of the entity moving (1 or 2)
	
	//-----------------------------------------------------------------------------------//

	public String getMoveUID() {
		return moveUID;
	}
	public void setMoveUID(String moveUID) {
		this.moveUID = moveUID;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getMovingUID() {
		return movingUID;
	}
	public void setMovingUID(int movingUID) {
		this.movingUID = movingUID;
	}
	public int getxFrom() {
		return xFrom;
	}
	public void setxFrom(int xFrom) {
		this.xFrom = xFrom;
	}
	public int getxTo() {
		return xTo;
	}
	public void setxTo(int xTo) {
		this.xTo = xTo;
	}
	public int getyFrom() {
		return yFrom;
	}
	public void setyFrom(int yFrom) {
		this.yFrom = yFrom;
	}
	public int getyTo() {
		return yTo;
	}
	public void setyTo(int yTo) {
		this.yTo = yTo;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
