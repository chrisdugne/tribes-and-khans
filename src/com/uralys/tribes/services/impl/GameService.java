package com.uralys.tribes.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.uralys.tribes.domain.IGameManager;
import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.DataContainer4UnitSaved;
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

	public Player getPlayer(String uralysUID) {
		try {
			return gameManager.getPlayer(uralysUID);
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

	public DataContainer4UnitSaved buildCity(City city, Unit merchant, String uralysUID){
		try {
			String cityUID = gameManager.buildCity(city, uralysUID);
			deleteUnit(uralysUID, merchant.getUnitUID());
			DataContainer4UnitSaved container = new DataContainer4UnitSaved();
			container.setCityUID(cityUID);
			container.addCaseAltered(gameManager.getCase(city.getX(), city.getY()));
			return container;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public DataContainer4UnitSaved createUnit(String uralysUID, Unit unit,
			String cityUID) {
		try {
			return gameManager.createUnit(uralysUID, unit, cityUID, true);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public DataContainer4UnitSaved updateUnit(Unit unit, String cityUID) {
		try {
			return gameManager.updateUnit(unit, cityUID, true);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void deleteUnit(String uralysUID, String unitUID) {
		try {
			gameManager.deleteUnit(uralysUID, unitUID);
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

	public List<Case> loadCases(int[] groups) {
		try {
			return gameManager.loadCases(groups);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Case>();
		}
	}

	// -----------------------------------------------------------------------------------//

	public void changeName(String uralysUID, String newName) {
		gameManager.changeName(uralysUID, newName);
	}

	public void changeCityName(String cityUID, String newName) {
		gameManager.changeCityName(cityUID, newName);
	}

}
