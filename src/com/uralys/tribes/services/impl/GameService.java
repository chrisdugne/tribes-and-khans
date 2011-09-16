package com.uralys.tribes.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.uralys.tribes.domain.IGameManager;
import com.uralys.tribes.entities.Ally;
import com.uralys.tribes.entities.Cell;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.ObjectsAltered;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.services.IGameService;

public class GameService implements IGameService {

	// =========================================================================//

	private IGameManager gameManager;

	public GameService(IGameManager gameManager) {
		this.gameManager = gameManager;
	}

	// =========================================================================//

	public void changeMusicOn(String uralysUID, boolean musicOn) {
		gameManager.changeMusicOn(uralysUID, musicOn);
	}

	//==================================================================================================//
	

	public String createPlayer(String uralysUID, String email) {
		try {
			return gameManager.createPlayer(uralysUID, email);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public Player getPlayer(String uralysUID, boolean newConnection) {
		try {
			return gameManager.getPlayer(uralysUID, newConnection);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public Player getPlayerInfo(String uralysUID) {
		try {
			return gameManager.getPlayerInfo(uralysUID);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public void savePlayer(Player player) {
		try {
			gameManager.savePlayer(player);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	public void saveCity(City city) {
		try {
			gameManager.saveCity(city);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public String buildCity(City city, Unit merchant, String uralysUID)
	{
		try {
			String cityUID = gameManager.buildCity(city, uralysUID);
			deleteUnit(uralysUID, merchant);
			return cityUID;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return "";
		}
		
	}

	public ObjectsAltered createUnit(String uralysUID, Unit unit,
			String cityUID)
	{
		try {
			return gameManager.createUnit(uralysUID, unit, cityUID);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ObjectsAltered updateUnit(Unit unit, String cityUID)
	{
		try {
			return gameManager.updateUnit(unit, cityUID, true);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void deleteUnit(String uralysUID, Unit unit) {
		try {
			gameManager.deleteUnit(uralysUID, unit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteUnits(String uralysUID, List<String> unitUIDs) {
		try {
			gameManager.deleteUnits(uralysUID, unitUIDs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteMove(String moveUID) {
		try {
			gameManager.deleteMove(moveUID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// =========================================================================//

	public List<Item> loadItems() {
		return gameManager.loadItems();
	}

	public List<Cell> loadCells(int[] groups, boolean refreshLandOwners) {
		try {
			return gameManager.loadCells(groups, refreshLandOwners);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Cell>();
		}
	}

	// -----------------------------------------------------------------------------------//

	public void changeName(String uralysUID, String newName) {
		gameManager.changeName(uralysUID, newName);
	}

	public void changeCityName(String cityUID, String newName) {
		gameManager.changeCityName(cityUID, newName);
	}

	// -----------------------------------------------------------------------------------//
	
	public List<Player> getCitiesBoard(){
		return gameManager.getCitiesBoard();
	}
	
	public List<Player> getLandsBoard(){
		return gameManager.getLandsBoard();
	}
	
	public List<Player> getPopulationBoard(){
		return gameManager.getPopulationBoard();
	}
	
	public List<Player> getArmiesBoard(){
		return gameManager.getArmiesBoard();
	}

	// -----------------------------------------------------------------------------------//
	
	public void updatePlayerProfile(String playerUID, String profile) {
		gameManager.updatePlayerProfile(playerUID, profile);
	}
	
	public void updateAllyProfile(String allyUID, String profile) {
		gameManager.updateAllyProfile(allyUID, profile);
	}

	// -----------------------------------------------------------------------------------//

	public void sendMessage(String senderUID, String recipientUID, String message){
		gameManager.sendMessage(senderUID, recipientUID, message);
	}

	public void markAsRead(List<String> messageUIDs) {
		gameManager.markAsRead(messageUIDs);
	}

	public void archiveMessages(List<String> messageUIDs) {
		gameManager.archiveMessages(messageUIDs);
	}

	public void deleteMessages(String uralysUID, List<String> messageUIDs) {
		gameManager.deleteMessages(uralysUID, messageUIDs);
	}

	// -----------------------------------------------------------------------------------//

	public Ally createAlly(String uralysUID, String allyName) {
		return gameManager.createAlly(uralysUID, allyName);
	}

	public Ally getAlly(String allyUID) {
		return gameManager.getAlly(allyUID);
	}

	public void joinAlly(String uralysUID, String allyUID) {
		gameManager.joinAlly(uralysUID, allyUID);
	}

	public List<Ally> getTopAlliesByCities() {
		return gameManager.getTopAlliesByCities();
	}

	public List<Ally> getTopAlliesByArmies() {
		return gameManager.getTopAlliesByArmies();
	}

	public List<Ally> getTopAlliesByPopulation() {
		return gameManager.getTopAlliesByPopulation();
	}

	public List<Ally> getTopAlliesByLands() {
		return gameManager.getTopAlliesByLands();
	}

	public void inviteInAlly(String uralysUID, String allyUID) {
		gameManager.inviteInAlly(uralysUID, allyUID);
	}

	public void removeFromAlly(String uralysUID, String allyUID) {
		gameManager.removeFromAlly(uralysUID, allyUID);
	}

	public void saveAllyHierarchy(Ally ally) {
		gameManager.saveAllyHierarchy(ally);
	}
	
}
