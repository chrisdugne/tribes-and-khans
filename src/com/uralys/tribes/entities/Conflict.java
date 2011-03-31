package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Conflict {
	
	//-----------------------------------------------------------------------------------//

	private String conflictUID;
	
	private String playerUID;
	private List<MoveConflict> moveAllies = new ArrayList<MoveConflict>();
	private List<MoveConflict> moveEnnemies = new ArrayList<MoveConflict>();
	
	private Integer x;
	private Integer y;

	private int status;
	private String report;
	
	//-----------------------------------------------------------------------------------//

	public String getConflictUID() {
		return conflictUID;
	}
	public void setConflictUID(String conflictUID) {
		this.conflictUID = conflictUID;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	public String getPlayerUID() {
		return playerUID;
	}
	public void setPlayerUID(String playerUID) {
		this.playerUID = playerUID;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public List<MoveConflict> getMoveAllies() {
		return moveAllies;
	}
	public void setMoveAllies(List<MoveConflict> moveAllies) {
		this.moveAllies = moveAllies;
	}
	public List<MoveConflict> getMoveEnnemies() {
		return moveEnnemies;
	}
	public void setMoveEnnemies(List<MoveConflict> moveEnnemies) {
		this.moveEnnemies = moveEnnemies;
	}
	
	
	//-----------------------------------------------------------------------------------//
	
}
