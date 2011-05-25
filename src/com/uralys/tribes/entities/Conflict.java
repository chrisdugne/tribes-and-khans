package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Conflict {
	
	//-----------------------------------------------------------------------------------//

	private String conflictUID;

	private String caseUID;
	private List<Unit> units = new ArrayList<Unit>();
	
	//-----------------------------------------------------------------------------------//

	public String getConflictUID() {
		return conflictUID;
	}
	public void setConflictUID(String meetingUID) {
		this.conflictUID = meetingUID;
	}
	public String getCaseUID() {
		return caseUID;
	}
	public void setCaseUID(String caseUID) {
		this.caseUID = caseUID;
	}
	public List<Unit> getUnits() {
		return units;
	}
	public void setUnits(List<Unit> units) {
		this.units = units;
	}
	
}
