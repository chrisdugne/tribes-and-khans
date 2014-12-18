package com.uralys.tribes.domain.impl;

import com.uralys.tribes.dao.IProfileDAO;
import com.uralys.tribes.domain.IProfileManager;
import com.uralys.tribes.entities.UralysProfile;
import com.uralys.tribes.entities.dto.UralysProfileDTO;
import com.uralys.utils.Utils;

public class ProfileManager implements IProfileManager{
	
	//==================================================================================================//
	
	private IProfileDAO profileDao;

	public ProfileManager (IProfileDAO profileDao){
		this.profileDao = profileDao;
	}

	//==================================================================================================//

	public boolean existFacebookPlayer(String facebookUID) {
		return profileDao.existFacebookPlayer(facebookUID);
	}

	public void linkFacebookUID(String uralysUID, String facebookUID) {
		profileDao.linkFacebookUID(uralysUID, facebookUID);
	}

	public UralysProfile createUralysAccount(String email, String shaPwd) {
		return convertProfileDTO(profileDao.createUralysAccount(email, shaPwd));
	}

	public UralysProfile login(String email, String shaPwd) {
		return convertProfileDTO(profileDao.login(email, shaPwd));
	}

	/**
	 * if profile exists yet 		: set lastLog=0 (AccountManager redirects to resultLogin)
	 * if profile is just created 	: set lastLog=1 (AccountManager redirects to register)
	 */
	public UralysProfile loginFromFacebook(String facebookUID) {
		if(existFacebookPlayer(facebookUID)){
			UralysProfile profileRetrieved = getProfileByFacebookUID(facebookUID);
			profileRetrieved.setLastLog(0L);
			return profileRetrieved;
		}
		else{
			UralysProfile profileCreated = createUralysAccount(facebookUID, "");
			profileCreated.setLastLog(1L);
			return profileCreated;
		}
	}
	
	public UralysProfile getProfile(String uralysUID) {
		return convertProfileDTO(profileDao.getProfile(uralysUID));
	}

	public UralysProfile getProfileByFacebookUID(String facebookUID) {
		return convertProfileDTO(profileDao.getProfileByFacebookUID(facebookUID));
	}
	
	public boolean changeLanguage(String uralysUID, int language) {
		return profileDao.changeLanguage(uralysUID, language);
	}
	
	public void refreshLastLog(String uralysUID) {
		profileDao.refreshLastLog(uralysUID);
	}

	public void setTransactionMillis(String uralysUID, Long dateMillis) {
		profileDao.setTransactionMillis(uralysUID, dateMillis);
	}


	//==================================================================================================//
	
	public static UralysProfile convertProfileDTO(UralysProfileDTO profileDTO) {

		
		UralysProfile profile = new UralysProfile();
		
		profile.setUralysUID(profileDTO.getUralysUID());
		profile.setSurname(profileDTO.getSurname());
		
		profile.setEmail(profileDTO.getEmail());
		profile.setFacebookUID(profileDTO.getFacebookUID());

		profile.setLanguage(profileDTO.getLanguage());
		profile.setLastLog(profileDTO.getLastLog());
		
		
		Utils.print("--------------------");
		Utils.print("profile : " + profile.getUralysUID());
		
		return profile;
	}
}
