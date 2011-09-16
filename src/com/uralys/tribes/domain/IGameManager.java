package com.uralys.tribes.domain;

import java.util.List;

import com.uralys.tribes.entities.Ally;
import com.uralys.tribes.entities.Cell;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.ObjectsAltered;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Unit;

public interface IGameManager {

	//-----------------------------------------------------------------------------------//
	
	public String createPlayer(String uralysUID, String email);
	public Player getPlayer(String uralysUID, boolean newConnection);
	public Player getPlayerInfo(String uralysUID);

	//-----------------------------------------------------------------------------------//
	
	public void savePlayer(Player player);
	public String buildCity(City city, String uralysUID);	
	public void saveCity(City city);	

	//-----------------------------------------------------------------------------------//
	
	public ObjectsAltered createUnit(String uralysUID, Unit unit, String cityUID);
	public ObjectsAltered updateUnit(Unit unit, String cityUID, boolean needReplacing);

	//-----------------------------------------------------------------------------------//
	
	public void deleteUnit(String uralysUID, Unit unit);
	public void deleteUnits(String uralysUID, List<String> unitUIDs);
	public void deleteMove(String moveUID) ;

	//-----------------------------------------------------------------------------------//
	
	public List<Item> loadItems();
	public List<Cell> loadCells(int[] groups, boolean refreshLandOwners);
//	public Cell getCase(int x, int y);

	//-----------------------------------------------------------------------------------//
	
	public void changeName(String uralysUID, String newName);
	public void changeCityName(String cityUID, String newName);
	public void changeMusicOn(String uralysUID, boolean musicOn);

	//-----------------------------------------------------------------------------------//
	
	public List<Player> getCitiesBoard();
	public List<Player> getLandsBoard();
	public List<Player> getPopulationBoard(); 
	public List<Player> getArmiesBoard();

	//-----------------------------------------------------------------------------------//
	
	public void updatePlayerProfile(String playerUID, String profile);
	public void updateAllyProfile(String allyUID, String profile);

	//-----------------------------------------------------------------------------------//
	
	public void sendMessage(String senderUID, String recipientUID, String message);
	public void markAsRead(List<String> messageUIDs);
	public void archiveMessages(List<String> messageUIDs);
	public void deleteMessages(String uralysUID, List<String> messageUIDs);
	
	//-----------------------------------------------------------------------------------//
	
	public Ally createAlly(String uralysUID, String allyName);
	public Ally getAlly(String allyUID);
	public void inviteInAlly(String uralysUID, String allyUID);
	public void joinAlly(String uralysUID, String allyUID);
	public void removeFromAlly(String uralysUID, String allyUID);
	public void saveAllyHierarchy(Ally ally);

	public List<Ally> getTopAlliesByCities();
	public List<Ally> getTopAlliesByArmies();
	public List<Ally> getTopAlliesByPopulation();
	public List<Ally> getTopAlliesByLands();
	
	//-----------------------------------------------------------------------------------//
	
}
