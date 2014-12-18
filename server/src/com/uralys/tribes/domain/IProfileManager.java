package com.uralys.tribes.domain;

import com.uralys.tribes.entities.UralysProfile;


public interface IProfileManager {

	public UralysProfile createUralysAccount(String email, String shaPwd);
	public UralysProfile login(String email, String shaPwd);
	public UralysProfile loginFromFacebook(String facebookUID);

	public void refreshLastLog(String uralysUID);
	public void setTransactionMillis(String uralysUID, Long dateMillis);

	public boolean existFacebookPlayer(String facebookUID);
	public void linkFacebookUID(String uralysUID, String facebookUID);
	
	public UralysProfile getProfile(String uralysUID);
	public UralysProfile getProfileByFacebookUID(String facebookUID);
	
	public boolean changeLanguage(String uralysUID, int language);
	

}
