package com.uralys.tribes.services;

import java.util.List;

import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.DataContainer4UnitSaved;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Unit;

public interface IGameService {

	public void changeMusicOn(String uralysUID, boolean musicOn);
	public String createPlayer(String uralysUID, String email);
	public Player getPlayer(String uralysUID);
	public Player getPlayerInfo(String uralysUID);

	public void savePlayer(Player player);
	public String buildCity(City city, Unit merchant, String uralysUID);
	public void saveCity(City city);
	public void deleteUnit(String uralysUID, Unit unit);
	public void deleteUnits(String uralysUID, List<String> unitsUID);
	public void deleteMove(String moveUID);
	
	public DataContainer4UnitSaved createUnit(String uralysUID, Unit unit, String cityUID);
	public DataContainer4UnitSaved updateUnit(Unit unit, String cityUID);

	
	public List<Item> loadItems();
	public List<Case> loadCases(int[] groups, boolean refreshLandOwners);
	
	public void changeName(String uralysUID, String newName);
	public void changeCityName(String cityUID, String newName);
	
	public List<Player> getCitiesBoard();
	public List<Player> getLandsBoard();
	public List<Player> getPopulationBoard(); 
	public List<Player> getArmiesBoard();

	public void updatePlayerProfile(String playerUID, String profile);

	public void sendMessage(String senderUID, String recipientUID, String message);
	public void markAsRead(List<String> messageUIDs);
	public void archiveMessages(List<String> messageUIDs);
	public void deleteMessages(String uralysUID, List<String> messageUIDs);
}
