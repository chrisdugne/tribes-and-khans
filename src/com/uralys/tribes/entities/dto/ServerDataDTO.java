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
	
	@Persistent	private Boolean dataviewerVisible;
	@Persistent	private String dataviewerPassword;

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

	public String getDataviewerPassword() {
		return dataviewerPassword;
	}

	public void setDataviewerPassword(String dataviewerPassword) {
		this.dataviewerPassword = dataviewerPassword;
	}

	public Boolean getDataviewerVisible() {
		return dataviewerVisible;
	}
	
	
}
