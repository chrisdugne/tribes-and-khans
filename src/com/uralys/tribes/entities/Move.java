package com.uralys.tribes.entities;

public class Move {

	private String moveUID;

	private int xFrom;
	private int xTo;
	private int yFrom;
	private int yTo;

	//-----------------------------------------------------------------------------------//

	public String getMoveUID() {
		return moveUID;
	}
	public void setMoveUID(String moveUID) {
		this.moveUID = moveUID;
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
