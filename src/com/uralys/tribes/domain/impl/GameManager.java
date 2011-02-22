package com.uralys.tribes.domain.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.domain.IGameManager;
import com.uralys.tribes.entities.Army;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Equipment;
import com.uralys.tribes.entities.Game;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Smith;
import com.uralys.tribes.entities.converters.EntitiesConverter;
import com.uralys.tribes.entities.dto.GameDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.utils.Utils;

public class GameManager implements IGameManager {
	
	//==================================================================================================//
	// game COEFF
	
	public final static int WHEAT_EARNING_COEFF = 5;
	public final static int WOOD_EARNING_COEFF = 3;
	public final static int IRON_EARNING_COEFF = 2;

	//==================================================================================================//
	
	private IGameDAO gameDao;

	public GameManager (IGameDAO gameDao){
		this.gameDao = gameDao;
	}

	//==================================================================================================//
	
	public void createGame(String uralysUID, String gameName, String playerName, int nbMinByTurn)  {
		gameDao.createGame(uralysUID, gameName, playerName, nbMinByTurn);
	}

	public void joinGame(String uralysUID, String gameUID, String playerName)  {
		gameDao.joinGame(uralysUID, gameUID, playerName);
	}

	//==================================================================================================//

	public List<Game> getCurrentGames(String uralysUID) {
		
		List<Game> games = new ArrayList<Game>();
		
		for(GameDTO gameDTO : gameDao.getCurrentGames(uralysUID)){
			games.add(EntitiesConverter.convertGameDTO(gameDTO));
		}
		
		calculateTurnsNotPlayed(games);
		
		return games;
	}
	

	public List<Game> getGamesToJoin(){
		
		List<Game> games = new ArrayList<Game>();
		
		for(GameDTO gameDTO : gameDao.getGamesToJoin()){
			games.add(EntitiesConverter.convertGameDTO(gameDTO));
		}
		
		return games;
	}

	//==================================================================================================//

	public boolean launchGame(String gameUID) {
		return gameDao.launchGame(gameUID);
	}
	
	//==================================================================================================//
	
	public List<Item> loadItems() {
		List<Item> items = new ArrayList<Item>();
		
		for(ItemDTO itemDTO : gameDao.loadItems()){
			items.add(EntitiesConverter.convertItemDTO(itemDTO));
		}
		
		return items;
	}

	//==================================================================================================//
	
	public void saveTurn(Player player, boolean fromForceTurn){

		System.out.println("-------------------------------------------------");
		System.out.println("Save turn pour joueur : " + player.getName());

		//----------------------------------------------------------------//
		// Cities

		for(City city : player.getCities()){

			if(city.getCityUID().equals("new")){
				System.out.println("Create a new city");
				gameDao.createCity(city, player.getPlayerUID());
			}
			else{
				System.out.println("saving city : " + city.getName());
				
				//----------------------------------------//
				// Resources
				// wheat : si < 0 : gens meurent de faim : calcul de pop en consequence
				
				boolean starvation = false;
				
				if(city.getWheat() < 0){
					starvation = true;
					city.setWheat(0);
				}

				if(!fromForceTurn){ // else la pop a deja ete calculee
					//----------------------------------------//
					// Population
					
					int armyRaised = 0;
					
					// calcul de armyRaised
					for(Army army : player.getArmies()){
						if(army.getArmyUID().equals("new") && army.getX() == city.getX() && army.getY() == city.getY())
							armyRaised += army.getSize();
					}
					
					
					System.out.println("update population (armyRaised : " + armyRaised + ")");
					city.setPopulation(calculatePopulation(city.getPopulation(), starvation, armyRaised));
				}
				
				System.out.println("updateCityResources");
				gameDao.updateCityResources(city);

				//----------------------------------------//
				// Forge
				
				for(Smith smith : city.getSmiths()){
					System.out.println("update smith " + smith.getItem().getName());
					gameDao.updateSmith(smith.getSmithUID(), smith.getPeople());				
				}

				//----------------------------------------//
				// Equipment stock
				
				for(Equipment stock : city.getEquipmentStock()){
					System.out.println("update stock " + stock.getItem().getName());
					gameDao.updateStock(stock.getEquimentUID(), stock.getSize());	
				}
			}
		}
		
		//----------------------------------------------------------------//
		// Armies

		List<String> createdArmyUIDs = new ArrayList<String>();
		List<String> editedArmyUIDs = new ArrayList<String>();
		List<String> toDeleteArmyUIDs = new ArrayList<String>();
//		int armyRaised = 0;
		
		for(Army army : player.getArmies()){
			
			if(army.getMoves().size() > 0){
				String moveUID = gameDao.saveMove(army.getMoves().get(0));
				army.getMoves().get(0).setMoveUID(moveUID);
			}
			
			if(army.getArmyUID().equals("new")){
				createdArmyUIDs.add(gameDao.createArmy(army));
//				armyRaised += army.getSize();
			}
			else{
				gameDao.updateArmy(army);
				editedArmyUIDs.add(army.getArmyUID());
			}

		}

		List<String> existingArmyUIDs = gameDao.linkNewArmiesAndGetPreviousArmyUIDs(player.getPlayerUID(), createdArmyUIDs);

		for(String existingArmyUID : existingArmyUIDs){
			System.out.println("existingArmyUID : " + existingArmyUID);
			boolean found = false;
			
			for(String editedArmyUID : editedArmyUIDs){
				if(editedArmyUID.equals(existingArmyUID)){
					found = true;
					break;
				}
			}
			
			if(!found)
				toDeleteArmyUIDs.add(existingArmyUID);
		}
		
		
		//----------------------------------------------------------------//
		// Merchants
		
		List<String> createdMerchantUIDs = new ArrayList<String>();
		List<String> editedMerchantUIDs = new ArrayList<String>();
		
		for(Army merchant : player.getMerchants()){
			
			if(merchant.getMoves().size() > 0){
				String moveUID = gameDao.saveMove(merchant.getMoves().get(0));
				merchant.getMoves().get(0).setMoveUID(moveUID);
			}
			
			if(merchant.getArmyUID().equals("new")){
				createdMerchantUIDs.add(gameDao.createArmy(merchant));
//				armyRaised += merchant.getSize();
			}
			else{
				gameDao.updateArmy(merchant);
				editedMerchantUIDs.add(merchant.getArmyUID());
			}
			
		}
		
		List<String> existingMerchantUIDs = gameDao.linkNewMerchantsAndGetPreviousMerchantUIDs(player.getPlayerUID(), createdMerchantUIDs);
		
		for(String existingMerchantUID : existingMerchantUIDs){
			System.out.println("existingMerchantUID : " + existingMerchantUID);
			boolean found = false;
			
			for(String editedMerchantUID : editedMerchantUIDs){
				if(editedMerchantUID.equals(existingMerchantUID)){
					found = true;
					break;
				}
			}
			
			if(!found)
				toDeleteArmyUIDs.add(existingMerchantUID);
		}
		
		//----------------------------------------------------------------//
		// delete armies ans merchants to be deleted

		gameDao.deleteArmies(toDeleteArmyUIDs, player.getPlayerUID());

		//----------------------------------------------------------------//
		// Player
		
		gameDao.updatePlayer(player); // lastTurnPLayed, remove last Reports
		
		//----------------------------------------------------------------//
		// verifying if new turn is ready
		
		gameDao.checkEndTurn(player.getGameUID());
	}
	
	//==================================================================================================//


	private void calculateTurnsNotPlayed(List<Game> games) {
		System.out.println("==============================================================");
		System.out.println("calculateTurnsNotPlayed (games.size : "+games.size()+")");
		for(Game game : games){
			
			if(game.getCurrentTurn() == 0)
				break;

			System.out.println("-----------------------------------");
			System.out.println("game : " + game.getName());
			

			for(Player player : game.getPlayers()){
				
				int nbTurnPlayedByOthersWithoutPlayer = game.getCurrentTurn() - player.getLastTurnPlayed() - 1;
				
				if(nbTurnPlayedByOthersWithoutPlayer == -1)
					nbTurnPlayedByOthersWithoutPlayer = 0; //le joueur avait bien joue son dernier tour a l'heure : game.currentTurn = player.lastTurnPlayed

				long millisSinceLastTurnBegining = new Date().getTime() - game.getBeginTurnTimeMillis();
				
				int nbTurnPlayedWithoutEveryone = (int) (millisSinceLastTurnBegining/(game.getNbMinByTurn()*60*1000));

				System.out.println("-----------------------------");
				System.out.println("player : " + player.getName());
				System.out.println("nbTurnPlayedByOthersWithoutPlayer : " + nbTurnPlayedByOthersWithoutPlayer);
				System.out.println("nbTurnPlayedWithoutEveryone : " + nbTurnPlayedWithoutEveryone);
				
				if(nbTurnPlayedByOthersWithoutPlayer + nbTurnPlayedWithoutEveryone > 0){
					System.out.println("----");
					System.out.println("force play");

					game.setCurrentTurn(game.getCurrentTurn() + nbTurnPlayedWithoutEveryone);
					game.setBeginTurnTimeMillis(game.getBeginTurnTimeMillis() + game.getNbMinByTurn()*60*1000*nbTurnPlayedWithoutEveryone);					
					
					
					for(int i = 0; i < nbTurnPlayedByOthersWithoutPlayer + nbTurnPlayedWithoutEveryone; i++){
						forcePlayTurn(player);
					}
					
					System.out.println("game.currentTurn set to : " + game.getCurrentTurn());
					System.out.println("beginTurnTimeMillis : " + new Date(game.getBeginTurnTimeMillis()));
					
					gameDao.updateGame(game); // set currentTurn and beginTurnTimeMillis
					
					// on sauvegarde le dernier
					System.out.println("----------");
					System.out.println("saveTurn");
					saveTurn(player, true);
				}
			}
		}
	}

	private void forcePlayTurn(Player player) {

		System.out.println("-------------------------------------------------");
		System.out.println("Tour force pour joueur : " + player.getName());
		
		player.setLastTurnPlayed(player.getLastTurnPlayed()+1);
		
		for(City city : player.getCities()){
			System.out.println("-------------------------------------------------");
			System.out.println("ville : " + city.getName());
			System.out.println("---------------------");
			System.out.println("Forge : ");
			
			//----------------------------------------//
			// Forge
			
			for(Smith smith : city.getSmiths()){
				System.out.println(smith.getItem().getName() + " : " + smith.getPeople());
				
				int ironNeeded = smith.getPeople()*smith.getItem().getIron();
				int woodNeeded = smith.getPeople()*smith.getItem().getWood();

				System.out.println("ironNeeded : " + ironNeeded + ", iron in stock : " + city.getIron());
				System.out.println("woodNeeded : " + woodNeeded + ", wood in stock : " + city.getWood());
				
				// penalite : si on a pas assez de stock quand on rate un tour, on ne construit rien du tout, on recolte a la place.
				if(ironNeeded > city.getIron()){
					System.out.println("stock de fer non suffisant : les workers passent sur la creation de fer");
					city.setPeopleCreatingIron(city.getPeopleCreatingIron()+smith.getPeople());
					smith.setPeople(0);
				}
				else if(woodNeeded > city.getWood()){
					System.out.println("stock de bois non suffisant : les workers passent sur la creation de bois");
					city.setPeopleCreatingWood(city.getPeopleCreatingWood()+smith.getPeople());
					smith.setPeople(0);
				}
				// sinon ok, on construit les armes
				else{
					System.out.println("ressources suffisantes");
					// consommation des ressources
					city.setIron(city.getIron()-ironNeeded);
					city.setWood(city.getWood()-woodNeeded);
					
					// augmentation du stock
					for(Equipment equipment : city.getEquipmentStock()){
						if(equipment.getItem().getName().equals(smith.getItem().getName())){
							equipment.setSize(equipment.getSize() + smith.getPeople());
							System.out.println("stock de " + equipment.getItem().getName() + " final : " + equipment.getSize() + "(+ "+smith.getPeople()+")");
							break;
						}
					}
				}
			}
				
			//----------------------------------------//
			// Resources
			// wheat : si < 0 : gens meurent de faim : calcul de pop en consequence
			
			System.out.println("---------------------");
			System.out.println("Resources : ");
			System.out.println("wheat : + " + (city.getPeopleCreatingWheat()*WHEAT_EARNING_COEFF - city.getPopulation()));
			System.out.println("wood : + " + (city.getPeopleCreatingWood()*WOOD_EARNING_COEFF));
			System.out.println("iron : + " + (city.getPeopleCreatingIron()*IRON_EARNING_COEFF));
			
			city.setWheat(city.getWheat() + city.getPeopleCreatingWheat()*WHEAT_EARNING_COEFF - city.getPopulation());
			city.setWood(city.getWood() + city.getPeopleCreatingWood()*WOOD_EARNING_COEFF);
			city.setIron(city.getIron() + city.getPeopleCreatingIron()*IRON_EARNING_COEFF);
			
			boolean starvation = false;
			
			if(city.getWheat() < 0){
				System.out.println("Starvation !");
				starvation = true;
				city.setWheat(0);
				
				// penalite : revolte : tout le monde fait du ble.
				for(Smith smithStarving : city.getSmiths()){
					smithStarving.setPeople(0);
				}
				
				city.setPeopleCreatingWood(0);
				city.setPeopleCreatingIron(0);
				
				city.setPeopleCreatingWheat(city.getPopulation());
			}
			
			//----------------------------------------------------//
			// Population : new workers are put in wheat creation
			
			int oldPopulation = city.getPopulation();	
			city.setPopulation(calculatePopulation(city.getPopulation(), starvation, 0));
			
			int newComers = city.getPopulation() - oldPopulation;
			city.setPeopleCreatingWheat(city.getPeopleCreatingWheat() + newComers);
			
		}
	}

	private int calculatePopulation(int population, boolean starvation, int armyRaised) {
		
		int naturalEvolutionPercentage = Utils.random(5) + 5;
		int armyPercentage = 100*armyRaised/population;
		
		System.out.println("naturalEvolutionPercentage : " + naturalEvolutionPercentage);
		System.out.println("armyPercentage : " + armyPercentage);
		
		int evolutionPercentage = naturalEvolutionPercentage - (int)Math.sqrt(armyPercentage);

		System.out.println("evolutionPercentage : " + evolutionPercentage);
		
		if(starvation)
			evolutionPercentage -= 25;

		System.out.println("final evolutionPercentage : " + evolutionPercentage);

		
		int populationEvolution =  population*evolutionPercentage/100;
		System.out.println("populationEvolution : " + populationEvolution);
		
		return population + populationEvolution;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
