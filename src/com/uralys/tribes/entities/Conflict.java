package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Conflict {
	
	//-----------------------------------------------------------------------------------//

	private String conflictUID;

	private long timeFrom;
	private long timeTo;

	private String caseUID;
	private List<Gathering> gatherings = new ArrayList<Gathering>();
	
	//-----------------------------------------------------------------------------------//


	public List<Gathering> getGatherings() {
		return gatherings;
	}
	public void setGatherings(List<Gathering> gatherings) {
		this.gatherings = gatherings;
	}
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
	
}
