package com.uralys.tribes.dao;

import java.util.List;

import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.dto.CaseDTO;
import com.uralys.tribes.entities.dto.CityDTO;
import com.uralys.tribes.entities.dto.ConflictDTO;
import com.uralys.tribes.entities.dto.GatheringDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.UnitDTO;

public interface IGameDAO {

	//==================================================================================================//

	public String createPlayer(String uralysUID, String email);
	public PlayerDTO getPlayer(String uralysUID);
	
	//==================================================================================================//
	public List<ItemDTO> loadItems();
	public List<CaseDTO> loadCases(int[] groups);
	public CaseDTO getCase(int i, int j);
	
	public void updatePlayer(Player player);
	public void updateCityResources(City city, boolean saveResources);
	public void updateSmith(String smithUID, int people);
	public void updateStock(String equipmentUID, int size);

	public void createUnit(Unit unit, String cityUID);
	public void updateUnit(Unit unit, String cityUID);
	
	public List<String> linkNewUnitsAndGetPreviousUnitUIDs(String uralysUID, List<String> createdUnitUIDs);
	public void linkNewUnit(String uralysUID, String unitUID);
	
	public void deleteUnits(String uralysUID, List<String> toDeleteUnitUIDs);
	public void deleteUnit(String uralysUID, String unitUID);
	public UnitDTO getUnit(String unitUID);
	
	public String createCity(City city, String playerUID);
	public CityDTO createNewFirstCity(String playerUID);
	public void setNewCityOwner(String cityUID, String newOwnerUID, long timeToChangeOwner);
	public void checkCityOwner(String cityUID);
	
	public String createMove(Move move);
	public void setTimeToForMove(String moveUID, long timeTo);
	public void setNewGatheringForMoveAndDeletePreviousGathering(String moveUID, String gatheringUID);
	public void setValueForMove(String moveUID, int value);
	public void deleteMove(String moveUID, boolean keepGatheringBecauseItIsLinkedWithAnotherMoveNow);
	public void deleteMoves(String unitUID);

	public void resetChallenger(String caseUID);
	public CaseDTO tryToSetChallenger(Unit unitArriving, long timeFromChallenging);
	
	public void addUnitInGatheringAndSetNewArmy(String gatheringUID, String unitUID, String newUnitUID);
	
	public ConflictDTO getConflict(String conflictUID);
	public GatheringDTO getGathering(String gatheringUID);
	
	public String createConflict(String caseUID, String unitUID, String unitUID2);
	
	public void changeName(String uralysUID, String newName);
	public void changeCityName(String cityUID, String newName);
	public void changeMusicOn(String uralysUID, boolean musicOn);
	
}