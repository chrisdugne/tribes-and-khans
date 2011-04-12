package com.uralys.tribes.dao;

import java.util.List;

import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.dto.CaseDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.UnitDTO;

public interface IGameDAO {

	//==================================================================================================//

	public String createPlayer(String uralysUID, String email);
	public PlayerDTO getPlayer(String uralysUID);
	
	//==================================================================================================//
	public List<ItemDTO> loadItems();
	public List<CaseDTO> loadCases(List<String> caseUIDs);
	public CaseDTO getCase(int i, int j);
	
	public void updatePlayer(Player player);
	public void updateCityResources(City city);
	public void updateSmith(String smithUID, int people);
	public void updateStock(String equipmentUID, int size);

	public void createUnit(Unit unit);
	public void updateUnit(Unit unit);
	public List<String> linkNewUnitsAndGetPreviousUnitUIDs(String uralysUID, List<String> createdUnitUIDs);
	public void deleteUnits(List<String> toDeleteUnitUIDs, String uralysUID);
	public UnitDTO getUnit(String unitUID);
	
	public void createCity(City city, String playerUID);
	
	public String saveMove(Move move, String unitUID);
	public void deleteMove(String moveUID, String caseUID, String unitUID);
	
}