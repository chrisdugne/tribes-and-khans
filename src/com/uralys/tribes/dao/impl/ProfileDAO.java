package com.uralys.tribes.dao.impl;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.uralys.tribes.dao.IProfileDAO;
import com.uralys.tribes.entities.dto.UralysProfileDTO;
import com.uralys.utils.Utils;


public class ProfileDAO extends MainDAO implements IProfileDAO{
	
	public boolean existFacebookPlayer(String facebookUID) {
		return getProfileByFacebookUID(facebookUID) != null;
	}
	
	public void linkFacebookUID(String uralysUID, String facebookUID) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		UralysProfileDTO profile = pm.getObjectById(UralysProfileDTO.class, uralysUID);
		
		profile.setFacebookUID(facebookUID);
		pm.close();
	}
	
	public UralysProfileDTO createUralysAccount(String email, String shaPwd) {
		
		if(exists(email))
			return new UralysProfileDTO("EMAIL_EXISTS");
		
		String uralysUID = Utils.generateUID();
		
		UralysProfileDTO profile = new UralysProfileDTO();
		Key key = KeyFactory.createKey(UralysProfileDTO.class.getSimpleName(), uralysUID);
		
		profile.setKey(KeyFactory.keyToString(key));
		profile.setUralysUID(uralysUID);
		profile.setFacebookUID(Utils.checkEmail(email) ? null : email);
		profile.setEmail(email);
		profile.setShaPwd(shaPwd);

		profile.setLanguage(0);
		
		profile.setSurname("New Player");
		
		profile.setLastLog(new Date().getTime());

		persist(profile);
		
		return profile;
	}

	public UralysProfileDTO getProfile(String playerUID) {
		return super.getProfile(playerUID);
	}


	public void refreshLastLog(String playerUID) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		UralysProfileDTO player =  pm.getObjectById(UralysProfileDTO.class, playerUID);
		
		player.setLastLog(new Date().getTime());
		
		pm.close();
		
	}

	public UralysProfileDTO login(String email, String shaPwd) 
	{
		Utils.print("--------------------");
		Utils.print("login : " + email);
	
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query q = pm.newQuery("select from " + UralysProfileDTO.class.getName() + " where email == :email");
		q.setUnique(true);

		UralysProfileDTO profile = (UralysProfileDTO) q.execute(email);
		
		if(profile == null || !profile.getShaPwd().equals(shaPwd)){
			profile = new UralysProfileDTO("WRONG_PWD");
		}
		else
			profile.setLastLog(new Date().getTime());
		
		pm.close();
		
		Utils.print("--------------------");
		Utils.print("profile : " + profile.getUralysUID());
		
		return profile;
	}

	public boolean changeLanguage(String uralysUID, int language) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		UralysProfileDTO profile = pm.getObjectById(UralysProfileDTO.class, uralysUID);
		
		profile.setLanguage(language);
		
		pm.close();
		return true;
	}

	public void setTransactionMillis(String playerUID, Long dateMillis) {
		// TODO Auto-generated method stub
		
	}

	//===========================================================================//
	
	private boolean exists(String email) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query q = pm.newQuery("select from " + UralysProfileDTO.class.getName() + " where email == :email");
		q.setUnique(true);
		
		return q.execute(email) != null;
	}
}
