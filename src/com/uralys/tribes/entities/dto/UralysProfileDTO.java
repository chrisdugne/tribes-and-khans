package com.uralys.tribes.entities.dto;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UralysProfileDTO {


    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String uralysUID;

	@Persistent private String facebookUID;
	@Persistent private String email;
	@Persistent private String surname;
	@Persistent private String shaPwd;
	@Persistent private Long lastLog;
	@Persistent private Long lastTransactionMillis;
	@Persistent private Integer language; 
	
	public UralysProfileDTO(){}
	
	// used whenever a dummy Profile with an error message as uralysUID is needed (login.wrongPwd, createAccount.emailExist)
	public UralysProfileDTO(String dummyUID){
		uralysUID = dummyUID;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getUralysUID() {
		return uralysUID;
	}
	public void setUralysUID(String uralysUID) {
		this.uralysUID = uralysUID;
	}
	public String getFacebookUID() {
		return facebookUID;
	}

	public void setFacebookUID(String facebookUID) {
		this.facebookUID = facebookUID;
	}
	public String getShaPwd() {
		return shaPwd;
	}
	public void setShaPwd(String shaPwd) {
		this.shaPwd = shaPwd;
	}
	public Long getLastLog() {
		return lastLog;
	}
	public void setLastLog(Long lastLog) {
		this.lastLog = lastLog;
	}
	public Integer getLanguage() {
		return language;
	}
	public void setLanguage(Integer language) {
		this.language = language;
	}
	public Long getLastTransactionMillis() {
		return lastTransactionMillis;
	}
	public void setLastTransactionMillis(Long lastTransactionMillis) {
		this.lastTransactionMillis = lastTransactionMillis;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
