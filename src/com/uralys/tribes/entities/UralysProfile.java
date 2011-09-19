package com.uralys.tribes.entities;

public class UralysProfile {

	private String uralysUID;
	private String facebookUID;
	private String email;
	private String surname;

	private Long lastLog;
	private Integer language;

	//-----------------------------------------------------------------------------------//

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
	
}
