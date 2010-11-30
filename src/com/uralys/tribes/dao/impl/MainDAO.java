package com.uralys.tribes.dao.impl;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;


public class MainDAO {

	protected static final Logger log = Logger.getLogger(MainDAO.class.getName());
	
	//==================================================================================================//
	
	protected void persist(Object o) {

		
		try {
			System.out.println("4345");
			PersistenceManager pm = PMF.getInstance().getPersistenceManager();
			System.out.println("7777");
			Transaction tx = pm.currentTransaction();
		
			System.out.println("2");
			tx.begin();
			System.out.println("2");
			pm.makePersistent(o);
			System.out.println("2");
			tx.commit();
			System.out.println("2");
		}
		catch(Exception e){
			System.out.println("craack");
			e.printStackTrace();
		}
		finally {        
			System.out.println("fianlly");
//			if (tx.isActive()) 
//				tx.rollback();
//
//			System.out.println("f");
//			pm.close();
//			System.out.println("g");
		}
	}

	//===========================================================//

}
