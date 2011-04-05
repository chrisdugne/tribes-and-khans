package com.uralys.tribes.dao;

import java.util.List;

import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;

public interface IGameDAO {

	//==================================================================================================//

	public String createPlayer(String uralysUID, String email);
	public PlayerDTO getPlayer(String uralysUID);
	
	//==================================================================================================//
	public List<ItemDTO> loadItems();
	
	public void updateCityResources(City city);
	public void updateSmith(String smithUID, int people);
	public void updateStock(String equipmentUID, int size);

	public String createArmy(Unit army);
	public void createCity(City city, String playerUID);
	public void deleteArmies(List<String> toDeleteArmyUIDs, String playerUID);
	
}