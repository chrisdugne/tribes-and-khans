package com.uralys.tribes.dao.impl;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;


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

}
