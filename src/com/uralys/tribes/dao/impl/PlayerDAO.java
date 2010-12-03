package com.uralys.tribes.dao.impl;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.uralys.tribes.dao.IPlayerDAO;
import com.uralys.tribes.entities.dto.ProfilDTO;

public class PlayerDAO  extends MainDAO implements IPlayerDAO {
	
	public ProfilDTO createProfil(String uralysUID, String email) {
		
		ProfilDTO profilDTO =  new ProfilDTO();
		
		Key key = KeyFactory.createKey(ProfilDTO.class.getSimpleName(), uralysUID);
		
		profilDTO.setKey(KeyFactory.keyToString(key));
		profilDTO.setUralysUID(uralysUID);
		profilDTO.setEmail(email);
		
		persist(profilDTO);

		return profilDTO;
	}
	
	public ProfilDTO getProfil(String uralysUID) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		try{
			return pm.getObjectById(ProfilDTO.class, uralysUID);
		}
		catch(Exception e){
			// no Profil !!
			return null;
		}
	}

}
