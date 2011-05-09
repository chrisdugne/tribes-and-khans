package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Meeting {
	
	//-----------------------------------------------------------------------------------//

	public static final int CONFLICT = 1;
	public static final int GATHERING = 2;
	
	//-----------------------------------------------------------------------------------//
	
	private String meetingUID;
	private String playerUID;
	private int type;

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
	public String getMeetingUID() {
		return meetingUID;
	}
	public void setMeetingUID(String meetingUID) {
		this.meetingUID = meetingUID;
	}
	public String getCaseUID() {
		return caseUID;
	}
	public void setCaseUID(String caseUID) {
		this.caseUID = caseUID;
	}
	public String getPlayerUID() {
		return playerUID;
	}
	public void setPlayerUID(String playerUID) {
		this.playerUID = playerUID;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
