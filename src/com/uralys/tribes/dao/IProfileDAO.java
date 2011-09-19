package com.uralys.tribes.dao;

import com.uralys.tribes.entities.dto.UralysProfileDTO;

public interface IProfileDAO {

	public UralysProfileDTO login(String email, String shaPwd);
	public UralysProfileDTO createUralysAccount(String email, String shaPwd);
	
	public void refreshLastLog(String uralysUID);
	public boolean existFacebookPlayer(String facebookUID);
	public void linkFacebookUID(String uralysUID, String facebookUID);
	
	public UralysProfileDTO getProfile(String uralysUID);
	public UralysProfileDTO getProfileByFacebookUID(String facebookUID);
	
	public boolean changeLanguage(String uralysUID, int language);
	public void setTransactionMillis(String uralysUID, Long dateMillis);
	 
}
