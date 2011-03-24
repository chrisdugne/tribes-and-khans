package com.uralys.tribes.entities.dto;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class MoveConflictDTO {
	
	//-----------------------------------------------------------------------------------//

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String moveConflictUID;
	
	@Persistent private Integer armySize;
	@Persistent private Integer armyBows;
	@Persistent private Integer armySwords;
	@Persistent private Integer armyArmors;
	@Persistent private Integer xFrom;
	@Persistent private Integer yFrom;

	//-----------------------------------------------------------------------------------//

	public String getKey() { 
		return key;
	}
	public void setKey(String key) {
		this.key = key; 
	}
	public String getMoveConflictUID() {
		return moveConflictUID;
	}
	public void setMoveConflictUID(String moveConflictUID) {
		this.moveConflictUID = moveConflictUID;
	}
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
	
	//-----------------------------------------------------------------------------------//
	
}
