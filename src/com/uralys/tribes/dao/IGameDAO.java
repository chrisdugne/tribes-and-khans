package com.uralys.tribes.dao;

import java.util.List;

import com.uralys.tribes.entities.Ally;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Stock;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.dto.AllyDTO;
import com.uralys.tribes.entities.dto.CellDTO;
import com.uralys.tribes.entities.dto.CityDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.UnitDTO;

public interface IGameDAO {

	//-----------------------------------------------------------------------------------//
	
	public String createPlayer(String uralysUID, String email);
	public PlayerDTO getPlayer(String uralysUID);
	public PlayerDTO getPlayer(String uralysUID, boolean newConnection);

	//-----------------------------------------------------------------------------------//
	
	public List<ItemDTO> loadItems();
	public List<CellDTO> loadCells(int[] groups, boolean refreshLandOwners);
	public CellDTO getCell(int i, int j);
	public CellDTO getCell(String cellUID);

	//-----------------------------------------------------------------------------------//
	
	public void updatePlayer(Player player);
	public void updatePlayerPoints(Player player);
	public void updateAllyPoints(Ally ally);
	public void updateCityResources(City city, boolean newStep);
	public void updateStock(Stock stock);

	//-----------------------------------------------------------------------------------//
	
	public void createUnit(Unit unit);
	public void updateUnit(Unit unit);

	//-----------------------------------------------------------------------------------//
	
	public List<String> linkNewUnitsAndGetPreviousUnitUIDs(String uralysUID, List<String> createdUnitUIDs);
	public void linkNewUnit(String uralysUID, String unitUID);

	//-----------------------------------------------------------------------------------//
	
	public void deleteUnits(String uralysUID, List<String> toDeleteUnitUIDs);
	public void deleteUnit(String uralysUID, String unitUID);
	public UnitDTO getUnit(String unitUID);

	//-----------------------------------------------------------------------------------//
	
	public String createCity(City city, String playerUID);
	public CityDTO createNewFirstCity(String playerUID);
	public void cityIsTaken(String cityUID, String newOwnerUID, long timeToChangeOwner, int populationLost);
	public void checkCityOwner(String cityUID);

	//-----------------------------------------------------------------------------------//
	
	public String createMove(Move move, String nextMoveUID);
	public void setTimeToForMove(String moveUID, long timeTo);
	public void setNewGatheringForMoveAndDeletePreviousGathering(String moveUID, String gatheringUID);
	public void setHiddenForMove(String moveUID, boolean hidden);
	public void deleteMove(String moveUID, boolean keepGatheringBecauseItIsLinkedWithAnotherMoveNow);
	public void deleteMoves(String unitUID);

	//-----------------------------------------------------------------------------------//
	
	public void resetChallenger(String caseUID);
	public void tryToSetChallenger(Unit unitArriving, long timeFromChallenging);

	//-----------------------------------------------------------------------------------//
	
	public void changeName(String uralysUID, String newName);
	public void changeCityName(String cityUID, String newName);
	public void changeMusicOn(String uralysUID, boolean musicOn);

	//-----------------------------------------------------------------------------------//
	
	public List<PlayerDTO> getCitiesBoard();
	public List<PlayerDTO> getLandsBoard();
	public List<PlayerDTO> getPopulationBoard(); 
	public List<PlayerDTO> getArmiesBoard();

	//-----------------------------------------------------------------------------------//
	
	public void updatePlayerProfile(String playerUID, String profile);
	public void updateAllyProfile(String allyUID, String profile);

	//-----------------------------------------------------------------------------------//
	
	public String sendMessage(String string, String uralysUID, String report, long time);
	public void markAsRead(List<String> messageUIDs);
	public void archiveMessages(List<String> messageUIDs);
	public void deleteMessages(String uralysUID, List<String> messageUIDs);
	
	//-----------------------------------------------------------------------------------//
	
	public AllyDTO createAlly(String uralysUID, String allyName);
	public AllyDTO getAlly(String allyUID);
	public void inviteInAlly(String uralysUID, String allyUID, String inviteInAllyMessage);
	public void joinAlly(String uralysUID, String allyUID);
	public void removeFromAlly(String uralysUID, String allyUID);
	public void saveAllyPlayers(String allyUID, List<String> playerUIDs);

	public List<AllyDTO> getTopAlliesByCities();
	public List<AllyDTO> getTopAlliesByArmies();
	public List<AllyDTO> getTopAlliesByPopulation();
	public List<AllyDTO> getTopAlliesByLands();
	
	//-----------------------------------------------------------------------------------//
	
}