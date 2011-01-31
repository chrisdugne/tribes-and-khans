package com.uralys.tribes.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.entities.Game;
import com.uralys.tribes.entities.dto.CityDTO;
import com.uralys.tribes.entities.dto.GameDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.ProfilDTO;
import com.uralys.utils.Utils;

public class GameDAO  extends MainDAO implements IGameDAO {
	
	public void createGame(String uralysUID, String gameName, String playerName, int nbMinByTurn) {

		//-----------------------------------------------------------------------//

		String playerUID = Utils.generateUID();
		String gameUID = Utils.generateUID();

		//-----------------------------------------------------------------------//

		/*public var periods:ArrayCollection = new ArrayCollection(
				[   {periodLabel:"1 h", period:6},
					{periodLabel:"2 h", period:7},
					{periodLabel:"6 h", period:8},
					{periodLabel:"12 h", period:9},
					{periodLabel:"24 h", period:10},
					{periodLabel:"36 h", period:11},
					{periodLabel:"48 h", period:12},
					{periodLabel:"1 week", period:13} ]);
		*/
		
		switch (nbMinByTurn) {
			case 6:
				nbMinByTurn = 60;
				break;
			case 7:
				nbMinByTurn = 120;
				break;
			case 8:
				nbMinByTurn = 360;
				break;
			case 9:
				nbMinByTurn = 720;
				break;
			case 10:
				nbMinByTurn = 24*60;
				break;
			case 11:
				nbMinByTurn = 36*60;
				break;
			case 12:
				nbMinByTurn = 48*60;
				break;
			case 13:
				nbMinByTurn = 7*24*60;
				break;
	
			default:
				break;
		}

		//-----------------------------------------------------------------------//
		
		GameDTO gameDTO =  new GameDTO();
		
		Key key = KeyFactory.createKey(GameDTO.class.getSimpleName(), gameUID);
		
		gameDTO.setKey(KeyFactory.keyToString(key));
		gameDTO.setGameUID(gameUID);
		gameDTO.setCreatorUralysUID(uralysUID);
		gameDTO.setName(gameName);
		gameDTO.setStatus(Game.IN_CREATION);
		gameDTO.setCurrentTurn(0);
		gameDTO.setNbMinByTurn(nbMinByTurn);
		gameDTO.getPlayerUIDs().add(playerUID);
		
		persist(gameDTO);

		//-----------------------------------------------------------------------//
		
		PlayerDTO playerDTO = new PlayerDTO();
		key = KeyFactory.createKey(PlayerDTO.class.getSimpleName(), playerUID);
		
		playerDTO.setKey(KeyFactory.keyToString(key));
		playerDTO.setPlayerUID(playerUID);
		playerDTO.setGameUID(gameUID);
		playerDTO.setGameName(gameName);
		playerDTO.setName(playerName);
		
		persist(playerDTO);

		//-----------------------------------------------------------------------//
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ProfilDTO profil = pm.getObjectById(ProfilDTO.class, uralysUID);
		profil.getPlayerUIDs().add(playerUID);
		pm.close();

		//-----------------------------------------------------------------------//
	}
	
	public void joinGame(String uralysUID, String gameUID, String playerName) {
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();

		//-----------------------------------------------------------------------//

		String playerUID = Utils.generateUID();
		
		//-----------------------------------------------------------------------//
		
		GameDTO game = pm.getObjectById(GameDTO.class, gameUID);
		game.getPlayerUIDs().add(playerUID);
		
		//-----------------------------------------------------------------------//
		
		ProfilDTO profil = pm.getObjectById(ProfilDTO.class, uralysUID);
		profil.getPlayerUIDs().add(playerUID);
		
		//-----------------------------------------------------------------------//

		pm.close();

		//-----------------------------------------------------------------------//
		
		PlayerDTO playerDTO = new PlayerDTO();
		Key key = KeyFactory.createKey(PlayerDTO.class.getSimpleName(), playerUID);
		
		playerDTO.setKey(KeyFactory.keyToString(key));
		playerDTO.setPlayerUID(playerUID);
		playerDTO.setGameUID(gameUID);
		playerDTO.setGameName(game.getName());
		playerDTO.setName(playerName);
		
		persist(playerDTO);
		
		//-----------------------------------------------------------------------//

	}
	
	public List<GameDTO> getCurrentGames(String uralysUID) {

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		ProfilDTO profil = pm.getObjectById(ProfilDTO.class, uralysUID);

		List<String> gameUIDs = new ArrayList<String>();
		
		for(PlayerDTO player : profil.getPlayers())
			gameUIDs.add(player.getGameUID());
		

		return UniversalDAO.getInstance().getListDTO(gameUIDs, GameDTO.class);
		
	}

	
	@SuppressWarnings("unchecked")
	public List<GameDTO> getGamesToJoin(){
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query query = pm.newQuery("select from " + GameDTO.class.getName());
		query.setFilter("status == :status");
		
		return (List<GameDTO>) query.execute(Game.IN_CREATION);
	}


	//==================================================================================================//	
	
	public boolean launchGame(String gameUID) {

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		GameDTO game = pm.getObjectById(GameDTO.class, gameUID);
		
		game.setBeginTurnTimeMillis(new Date().getTime());
		game.setStatus(Game.RUNNING);
		game.setCurrentTurn(1);
		
		
		//-----------------------------------------------------------------------------------//
		for (String playerUID : game.getPlayerUIDs()){
			PlayerDTO player = pm.getObjectById(PlayerDTO.class, playerUID);
			
			CityDTO city = new CityDTO();

			String cityUID = Utils.generateUID();

			Key key = KeyFactory.createKey(CityDTO.class.getSimpleName(), cityUID);

			city.setKey(KeyFactory.keyToString(key));
			city.setCityUID(cityUID);
			city.setName("Ville de " + player.getName());
			city.setPopulation(1000);
			city.setWheat(400);
			city.setWood(200);
			city.setIron(200);
			city.setPeopleCreatingWheat(0);
			city.setPeopleCreatingWood(0);
			city.setPeopleCreatingIron(0);
			city.setGold(100);

			city.setX(Utils.random(2700));
			city.setY(Utils.random(2700));
			
			pm.makePersistent(city);
			
			player.setLastTurnPlayed(0);
			player.getCityUIDs().add(cityUID);
			
		}
		
		//-----------------------------------------------------------------------------------//
		
		pm.close();
		
		return true;
	}

	//==================================================================================================//
	
	public List<ItemDTO> loadItems() {
		return UniversalDAO.getInstance().getListDTO(ItemDTO.class, 1, 100);
	}

}
