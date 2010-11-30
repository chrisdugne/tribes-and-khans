package com.uralys.tribes.dao.impl;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.entities.dto.GameDTO;
import com.uralys.utils.Utils;

public class GameDAO  extends MainDAO implements IGameDAO {
	
	public GameDTO createGame(String playerUID, String gameName) {
		
		GameDTO gameDTO =  new GameDTO();
		
		String gameUID = Utils.generateUID();
		Key key = KeyFactory.createKey(GameDTO.class.getSimpleName(), gameUID);
		
		gameDTO.setKey(KeyFactory.keyToString(key));
		gameDTO.setGameUID(gameUID);
		gameDTO.setName(gameName);
		gameDTO.getPlayerUIDs().add(playerUID);
		

		try{
			System.out.println(gameUID);
			System.out.println("persist");
			persist(gameDTO);
			System.out.println("ok");
			System.out.println(gameUID);
		}
		catch(Exception e){
			System.out.println("boum");
			e.printStackTrace();
		}
		

		System.out.println(gameUID);
		return gameDTO;
	}

}
