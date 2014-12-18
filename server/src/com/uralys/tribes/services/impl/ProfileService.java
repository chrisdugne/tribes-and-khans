package com.uralys.tribes.services.impl;

import com.uralys.tribes.domain.IProfileManager;
import com.uralys.tribes.entities.UralysProfile;
import com.uralys.tribes.services.IProfileService;


public class ProfileService implements IProfileService{

	//=========================================================================//
	
	private IProfileManager profileManager;

	public ProfileService (IProfileManager playerManager){
		this.profileManager = playerManager;
	}

	//=========================================================================//

	public boolean existFacebookPlayer(String facebookUID) {
		try{
			return profileManager.existFacebookPlayer(facebookUID);
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public void linkFacebookUID(String uralysUID, String facebookUID) {
		profileManager.linkFacebookUID(uralysUID, facebookUID);
	}
	
	public UralysProfile createUralysAccount(String email, String shaPwd) {
		return profileManager.createUralysAccount(email, shaPwd);
	}

	public UralysProfile login(String email, String shaPwd) {
		return profileManager.login(email, shaPwd);
	}
	
	public UralysProfile loginFromFacebook(String facebookUID) {
		try{
			return profileManager.loginFromFacebook(facebookUID);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public UralysProfile getProfileByFacebookUID(String facebookUID) {
		return profileManager.getProfileByFacebookUID(facebookUID);
	}
	
	public UralysProfile getProfile(String uralysUID) {
		return profileManager.getProfile(uralysUID);
	}

	public boolean changeLanguage(String uralysUID, int language) {
		return profileManager.changeLanguage(uralysUID, language);
	}

	//=========================================================================//

	public void refreshLastLog(String uralysUID) {
		System.out.println("refreshLastLog " + uralysUID);
		profileManager.refreshLastLog(uralysUID);		
	}

	public void setTransactionMillis(String uralysUID, Long dateMillis) {
		profileManager.setTransactionMillis(uralysUID, dateMillis);
	}

	//==================================================================================================//

}
