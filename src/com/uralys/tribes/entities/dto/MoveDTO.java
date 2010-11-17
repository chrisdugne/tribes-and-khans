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

	@Persistent private PlayerDTO player;
	@Persistent private int xFrom;
	@Persistent private int xTo;
	@Persistent private int yFrom;
	@Persistent private int yTo;
	
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
	public PlayerDTO getPlayer() {
		return player;
	}
	public void setPlayer(PlayerDTO player) {
		this.player = player;
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
