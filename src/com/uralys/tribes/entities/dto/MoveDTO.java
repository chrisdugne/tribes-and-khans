package com.uralys.tribes.entities.dto;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class MoveDTO {


    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String moveUID;


	@Persistent private String caseUID;
	@Persistent private long timeFrom;
	@Persistent private long timeTo;
	@Persistent private String unitUID;
	@Persistent private int value;

	//-----------------------------------------------------------------------------------//

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getMoveUID() {
		return moveUID;
	}
	public void setMoveUID(String moveUID) {
		this.moveUID = moveUID;
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
	public String getUnitUID() {
		return unitUID;
	}
	public void setUnitUID(String unitUID) {
		this.unitUID = unitUID;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
