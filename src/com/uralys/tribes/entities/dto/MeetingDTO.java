package com.uralys.tribes.entities.dto;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.uralys.tribes.dao.impl.UniversalDAO;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class MeetingDTO {
	
	//-----------------------------------------------------------------------------------//

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String meetingUID;
	
	@Persistent private List<String> gatheringUIDs = new ArrayList<String>();
	
	@Persistent private Integer x;
	@Persistent private Integer y;

	@Persistent private int type;
	@Persistent private int status;

	@Persistent private long timeFrom;
	@Persistent private long timeTo;
	
	//-----------------------------------------------------------------------------------//

	public String getKey() { 
		return key;
	}
	public void setKey(String key) {
		this.key = key; 
	}
	public String getMeetingUID() {
		return meetingUID;
	}
	public void setMeetingUID(String meetingUID) {
		this.meetingUID = meetingUID;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public List<String> getGatheringUIDs() {
		return gatheringUIDs;
	}
	public void setGatheringUIDs(List<String> gatheringUIDs) {
		this.gatheringUIDs = gatheringUIDs;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
	//-----------------------------------------------------------------------------------//
	
	public List<GatheringDTO> getGatherings() {
		return UniversalDAO.getInstance().getListDTO(gatheringUIDs, GatheringDTO.class);		
	}
	
}
