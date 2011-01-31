package com.uralys.tribes.domain.impl;

import java.util.ArrayList;
import java.util.List;

import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.domain.IGameManager;
import com.uralys.tribes.entities.Game;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.converters.EntitiesConverter;
import com.uralys.tribes.entities.dto.GameDTO;
import com.uralys.tribes.entities.dto.ItemDTO;

public class GameManager implements IGameManager {
	
	//==================================================================================================//
	
	private IGameDAO gameDao;

	public GameManager (IGameDAO gameDao){
		this.gameDao = gameDao;
	}

	//==================================================================================================//
	
	public void createGame(String uralysUID, String gameName, String playerName, int nbMinByTurn)  {
		gameDao.createGame(uralysUID, gameName, playerName, nbMinByTurn);
	}

	public void joinGame(String uralysUID, String gameUID, String playerName)  {
		gameDao.joinGame(uralysUID, gameUID, playerName);
	}

	//==================================================================================================//

	public List<Game> getCurrentGames(String uralysUID) {
		
		List<Game> games = new ArrayList<Game>();
		
		for(GameDTO gameDTO : gameDao.getCurrentGames(uralysUID)){
			games.add(EntitiesConverter.convertGameDTO(gameDTO));
		}
		
		return games;
	}
	
	public List<Game> getGamesToJoin(){
		
		List<Game> games = new ArrayList<Game>();
		
		for(GameDTO gameDTO : gameDao.getGamesToJoin()){
			games.add(EntitiesConverter.convertGameDTO(gameDTO));
		}
		
		return games;
	}

	//==================================================================================================//

	public boolean launchGame(String gameUID) {
		return gameDao.launchGame(gameUID);
	}
	
	//==================================================================================================//
	
	public List<Item> loadItems() {
		List<Item> items = new ArrayList<Item>();
		
		for(ItemDTO itemDTO : gameDao.loadItems()){
			items.add(EntitiesConverter.convertItemDTO(itemDTO));
		}
		
		return items;
	}
}
