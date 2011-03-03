package com.uralys.tribes.dao.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.spi.PersistenceCapable;
import javax.jdo.spi.StateManager;

public class UniversalDAO{

	//-----------------------------------------------------------------------------------//

	private static UniversalDAO instance = new UniversalDAO();

	public static UniversalDAO getInstance() {
		return instance;
	}
	
	//-----------------------------------------------------------------------------------//

	public <E> Object getObjectDTO(String uid, Class<E> objectClass) {
		
		if(uid == null)
			return null;
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		return pm.getObjectById(objectClass, uid);
	}

	//-----------------------------------------------------------------------------------//

	@SuppressWarnings("unchecked")
	public <E> List<E> getListDTO(Class<E> objectClass, int from, int to) {
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query query = pm.newQuery(objectClass);
		query.setRange(from-1, to);
		
		return (List<E>) query.execute();
	}

	//-----------------------------------------------------------------------------------//

	@SuppressWarnings("unchecked")
	public <E> List<E> getListDTO(List<String> uids, Class<E> objectClass) {
		
		if(uids == null || uids.size() == 0)
			return new ArrayList<E>();
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query query = pm.newQuery("select from " + objectClass.getName() + " where :uids.contains(key)");
		query.setOrdering("key");
		
		return (List<E>) query.execute(uids);
	}

	//-----------------------------------------------------------------------------------//

//	@SuppressWarnings("unchecked")
//	public <E> List<E> getListDTO(String uid, String keyName, Class<E> objectClass) {
//		
//		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
//		Query query = pm.newQuery("select from " + objectClass.getName() + " where :uid == "+keyName);
//
//		System.out.println(query.toString());
//		return (List<E>) query.execute(uid);
//	}

	//-----------------------------------------------------------------------------------//
	
	public <E> void delete(Class<E> objectClass, String uid) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		E o = pm.getObjectById(objectClass, uid);

		pm.deletePersistent(o);
		pm.close();
	}

	//-----------------------------------------------------------------------------------//

	public void makePersistent(Object newDTO) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		pm.makePersistent(newDTO);
		pm.close();
	}

	//-----------------------------------------------------------------------------------//

	@SuppressWarnings("unchecked")
	public <E> void update(Object updatedDTO, String uid) {
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		E o = (E) pm.getObjectById(updatedDTO.getClass(), uid);

		for(Field fieldEdited : updatedDTO.getClass().getDeclaredFields()){

			if(fieldEdited.getName().startsWith("key") || fieldEdited.getName().startsWith("jdo"))
				continue;
			
			try {
				
				Field fieldToSave = o.getClass().getDeclaredField(fieldEdited.getName());
				
				fieldToSave.setAccessible(true);
				fieldEdited.setAccessible(true);
				
				Object valueEdited = fieldEdited.get(updatedDTO);
				fieldToSave.set(o, valueEdited);

				//-----------------------------------------------------------------------------------//
				//make dirty for jdo refreshing
				
				Field jdoStateManager = o.getClass().getDeclaredField("jdoStateManager");
				jdoStateManager.setAccessible(true);
				StateManager stateManager = (StateManager)jdoStateManager.get(o);
				stateManager.makeDirty((PersistenceCapable) o, fieldToSave.getName());
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		
		try {
			pm.refresh(o);
		} 
		finally {        
			pm.close();
		}
	}

}