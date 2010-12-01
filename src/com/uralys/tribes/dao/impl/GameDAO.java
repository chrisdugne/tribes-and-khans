package com.uralys.tribes.dao.impl;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.entities.dto.GameDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.ProfilDTO;
import com.uralys.utils.Utils;

public class GameDAO  extends MainDAO implements IGameDAO {
	
	public GameDTO createGame(String uralysUID, String gameName) {

		//-----------------------------------------------------------------------//

		String playerUID = Utils.generateUID();
		String gameUID = Utils.generateUID();

		//-----------------------------------------------------------------------//
		
		GameDTO gameDTO =  new GameDTO();
		
		Key key = KeyFactory.createKey(GameDTO.class.getSimpleName(), gameUID);
		
		gameDTO.setKey(KeyFactory.keyToString(key));
		gameDTO.setGameUID(gameUID);
		gameDTO.setName(gameName);
		gameDTO.getPlayerUIDs().add(playerUID);
		
		persist(gameDTO);

		//-----------------------------------------------------------------------//
		
		PlayerDTO playerDTO = new PlayerDTO();
		key = KeyFactory.createKey(PlayerDTO.class.getSimpleName(), playerUID);
		
		playerDTO.setKey(KeyFactory.keyToString(key));
		playerDTO.setPlayerUID(playerUID);
		playerDTO.setGameUID(gameUID);
		playerDTO.setGameName(gameName);
		playerDTO.setName("Creator");
		
		persist(playerDTO);

		//-----------------------------------------------------------------------//
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ProfilDTO profil = pm.getObjectById(ProfilDTO.class, uralysUID);
		profil.getPlayerUIDs().add(playerUID);

		//-----------------------------------------------------------------------//
		
		return gameDTO;
	}

}
