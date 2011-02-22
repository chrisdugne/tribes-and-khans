package com.uralys.tribes.dao;

import java.util.List;

import com.uralys.tribes.entities.Army;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Game;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.dto.GameDTO;
import com.uralys.tribes.entities.dto.ItemDTO;

public interface IGameDAO {

	public void createGame(String uralysUID, String gameName, String playerName, int nbMinByTurn) ;
	public void joinGame(String uralysUID, String gameUID, String playerName);
	public List<GameDTO> getCurrentGames(String uralysUID);
	public List<GameDTO> getGamesToJoin();
	
	public boolean launchGame(String gameUID);
	
	public List<ItemDTO> loadItems();
	
	public void updateCityResources(City city);
	public void updateSmith(String smithUID, int people);
	public void updateStock(String equipmentUID, int size);


	public List<String> linkNewArmiesAndGetPreviousArmyUIDs(String playerUID, List<String> newArmyUIDs);
	public List<String> linkNewMerchantsAndGetPreviousMerchantUIDs(String playerUID, List<String> newMerchantUIDs);
	public String createArmy(Army army);
	public void createCity(City city, String playerUID);
	public void updateArmy(Army army);
	public String saveMove(Move move);
	public void updatePlayer(Player player);
	public void updateGame(Game game);
	public void deleteArmies(List<String> toDeleteArmyUIDs, String playerUID);
	public void checkEndTurn(String gameUID);
}