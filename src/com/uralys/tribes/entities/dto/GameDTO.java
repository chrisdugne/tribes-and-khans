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
public class GameDTO {

	//-----------------------------------------------------------------------------------//

	public final static int IN_CREATION = 1;
	public final static int RUNNING = 2;
	
	//-----------------------------------------------------------------------------------//

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String gameUID;

	@Persistent	private String name;
	@Persistent	private int status;
	@Persistent	private int currentTurn;
	@Persistent	private int autoEndTurnPeriod;	
	@Persistent	private List<String> playerUIDs = new ArrayList<String>();
	
	//-----------------------------------------------------------------------------------//

	public String getKey() { 
		return key;
	}
	public void setKey(String key) {
		this.key = key; 
	}
	public String getGameUID() { 
		return gameUID;
	}
	public void setGameUID(String gameUID) {
		this.gameUID = gameUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCurrentTurn() {
		return currentTurn;
	}
	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}
	public int getAutoEndTurnPeriod() {
		return autoEndTurnPeriod;
	}
	public void setAutoEndTurnPeriod(int autoEndTurnPeriod) {
		this.autoEndTurnPeriod = autoEndTurnPeriod;
	}
	
	//-----------------------------------------------------------------------------------//
	
	public List<PlayerDTO> getPlayers() {
		return UniversalDAO.getInstance().getListDTO(playerUIDs, PlayerDTO.class, "playerUID");
	}
	public List<String> getPlayerUIDs() {
		return playerUIDs;
	}
	public void setPlayerUIDs(List<String> playerUIDs) {
		this.playerUIDs = playerUIDs;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
