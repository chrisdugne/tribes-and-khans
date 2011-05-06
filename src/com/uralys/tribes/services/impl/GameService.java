package com.uralys.tribes.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.uralys.tribes.domain.IGameManager;
import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.services.IGameService;

public class GameService implements IGameService {

	//=========================================================================//
	
	private IGameManager gameManager;

	public GameService (IGameManager gameManager){
		this.gameManager = gameManager;
	}

	
	//=========================================================================//

	public String createPlayer(String uralysUID, String email) {
		return gameManager.createPlayer(uralysUID, email);
	}
	
	public Player getPlayer(String uralysUID) {
		try{
			return gameManager.getPlayer(uralysUID, false);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public void savePlayer(Player player) {
		try{
			gameManager.savePlayer(player);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public void saveUnit(String uralysUID, Unit unit) {
		try{
			gameManager.saveUnit(uralysUID, unit);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void deleteUnit(String uralysUID, String unitUID) {
		try{
			gameManager.deleteUnit(uralysUID, unitUID);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void deleteMove(String moveUID, String caseUID, String unitUID) {
		try{
			gameManager.deleteMove(moveUID, caseUID, unitUID);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	//=========================================================================//
	
	public List<Item> loadItems() {
		return gameManager.loadItems();
	}
	
	
	public List<Case> loadCases(List<String> caseUIDs) {
		try{
			return gameManager.loadCases(caseUIDs);
		}
		catch(Exception e){
			e.printStackTrace();
			return new ArrayList<Case>();
		}
	}
	
	//-----------------------------------------------------------------------------------//}
