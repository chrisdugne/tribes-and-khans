package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Conflict {
	
	//-----------------------------------------------------------------------------------//
	
	private String conflictUID;
	private String playerUID;

	private Case _case;
	private List<Unit> units = new ArrayList<Unit>();
	
	//-----------------------------------------------------------------------------------//


	public String getConflictUID() {
		return conflictUID;
	}
	public void setConflictUID(String conflictUID) {
		this.conflictUID = conflictUID;
	}
	public Case getCase() {
		return _case;
	}
	public void setCase(Case _case) {
		this._case = _case;
	}
	public List<Unit> getUnits() {
		return units;
	}
	public void setUnits(List<Unit> units) {
		this.units = units;
	}
	public String getPlayerUID() {
		return playerUID;
	}
	public void setPlayerUID(String playerUID) {
		this.playerUID = playerUID;
	}
	

	
}
