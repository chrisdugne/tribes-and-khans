package com.uralys.tribes.dao.impl;

import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.uralys.tribes.entities.dto.UralysProfileDTO;


public class MainDAO {

	protected static final Logger log = Logger.getLogger(MainDAO.class.getName());
	
	//==================================================================================================//
	
	protected void persist(Object o) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		
		try {
			tx.begin();
			pm.makePersistent(o);
			tx.commit();
		} 
		finally {        
			if (tx.isActive()) 
				tx.rollback();
			
			pm.close();
		}
	}

	//===========================================================//
	// from UralysLogger

	public UralysProfileDTO getProfile(String uralysUID) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		return pm.getObjectById(UralysProfileDTO.class, uralysUID);
	}

	public UralysProfileDTO getProfileByFacebookUID(String facebookUID)  {

		System.out.println("getProfileByFacebookUID");
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query q = pm.newQuery("select from " + UralysProfileDTO.class.getName() + " where facebookUID == :fbUID");
		q.setUnique(true);
		
		System.out.println("ready to execute");
		UralysProfileDTO profile = (UralysProfileDTO) q.execute(facebookUID);
		
		System.out.println(profile == null);
		if(profile != null)
			profile.setLastLog(new Date().getTime());
		
		pm.close();
		
		System.out.println("return profile");
		return profile;
	}
}
