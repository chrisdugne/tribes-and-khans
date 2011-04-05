package com.uralys.tribes.entities.dto;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ServerDataDTO {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	protected String key;
    
	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String dataUID; 
	
	@Persistent	private int nbPlayers;
	
	@Persistent	private Boolean dataviewerVisible;

	//-----------------------------------------------------------------------------------//

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDataUID() {
		return dataUID;
	}

	public void setDataUID(String dataUID) {
		this.dataUID = dataUID;
	}

	public void setDataviewerVisible(Boolean dataviewerVisible) {
		this.dataviewerVisible = dataviewerVisible;
	}

	public Boolean getDataviewerVisible() {
		return dataviewerVisible;
	}

	public int getNbPlayers() {
		return nbPlayers;
	}

	public void setNbPlayers(int nbPlayers) {
		this.nbPlayers = nbPlayers;
	}
	
	
}
