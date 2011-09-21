package com.uralys.tribes.domain.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.domain.IGameManager;
import com.uralys.tribes.domain.IMovesManager;
import com.uralys.tribes.entities.Ally;
import com.uralys.tribes.entities.Cell;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.ObjectsAltered;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Stock;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.converters.EntitiesConverter;
import com.uralys.tribes.entities.dto.AllyDTO;
import com.uralys.tribes.entities.dto.CellDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.utils.Utils;

public class GameManager implements IGameManager {

	
	/*
	 * 1 case 15min
	 * 4 cases 1h
	 * 96 cases/24h
	 * plateau : 96h : 400x400 = 160000 tuiles
	 * 
	 * pour commencer
	 * joueur i, place en (200 + 10i)(200 + 10i)
	 * 
	 * 
	 * tile : height = 86
	 * 		  width = 50+25+25 = 100
	 * 
	 * 50^2 = 25^2+ x^2 => x=sqrt(2500-625)
	 * sqrt(1875) = 43
	 * 
	 * plateau height : 86 * 400 = 
	 * plateau width  : 100 * 400 = 40000px
	 * 
	 */

	//==================================================================================================//
	// game COEFF

	public final static int WHEAT_EARNING_COEFF = 5;
	public final static int WOOD_EARNING_COEFF = 3;
	public final static int IRON_EARNING_COEFF = 2;

	public final static boolean debug = true;
	public final static boolean topdebug = true;

	//==================================================================================================//

	private IGameDAO gameDao;
	private IMovesManager movesManager;

	public GameManager (IGameDAO gameDao, IMovesManager movesManager){
		this.gameDao = gameDao;
		this.movesManager = movesManager;
	}

	//==================================================================================================//
	
	public String createPlayer(String uralysUID, String email) {
		return gameDao.createPlayer(uralysUID, email);			
	}

	// =========================================================================//

	public void changeMusicOn(String uralysUID, boolean musicOn) {
		gameDao.changeMusicOn(uralysUID, musicOn);
	}
	
	//==================================================================================================//
	
	public String buildCity(City city, String uralysUID){
		return gameDao.createCity(city, uralysUID);
	}
	
	public void saveCity(City city){
		updateCity(city, false);
	}
	
	private void updateCity(City city, boolean newStep)
	{
		Utils.print("updateCity");
		if(city.getBeginTime() > new Date().getTime())
			return;
		
		//----------------------------------------//

		gameDao.updateCityResources(city, newStep);

		//----------------------------------------//
		// Stocks

		for(Stock stock : city.getStocks()){
			gameDao.updateStock(stock);	
		}
	}
	
	//==================================================================================================//

	public Player getPlayerInfo(String uralysUID) {
		return EntitiesConverter.convertPlayerDTO(gameDao.getPlayer(uralysUID), false);
	}

	public Player getPlayer(String uralysUID, boolean newConnection)
	{
		PlayerDTO playerDTO = gameDao.getPlayer(uralysUID, newConnection);
		
		if(playerDTO == null)
			return null;
		
		// on reattribue les villes 
		List<String> cityUIDs = new ArrayList<String>();
		cityUIDs.addAll(playerDTO.getCityUIDs());
		cityUIDs.addAll(playerDTO.getCityBeingOwnedUIDs());
			
		checkCityOwners(cityUIDs);
		
		//refresh du player
		playerDTO = gameDao.getPlayer(uralysUID);
		
		Player player = EntitiesConverter.convertPlayerDTO(playerDTO, true);

		if(player.getCities().size() == 0){
			player.getCities().add(EntitiesConverter.convertCityDTO(gameDao.createNewFirstCity(uralysUID), true));
		}
		
		//------------------------------------//
		// MAJ des points des l'alliance : on enleve les anciennes donnees
		
		if(player.getAlly() != null){
			player.getAlly().setNbArmies(player.getAlly().getNbArmies() - player.getNbArmies());
			player.getAlly().setNbCities(player.getAlly().getNbCities() - player.getNbCities());
			player.getAlly().setNbPopulation(player.getAlly().getNbPopulation() - player.getNbPopulation());
		}
		
		//------------------------------------//
		// on calcule les nouvelles donnees
		
		player.setNbCities(player.getCities().size());
		
		player.setNbPopulation(0);
		for(City city : player.getCities()){
			player.setNbPopulation(player.getNbPopulation() + city.getPopulation());
		}

		player.setNbArmies(0);
		for(Unit unit : player.getUnits()){
			if(unit.getType() == Unit.ARMY)
				player.setNbArmies(player.getNbArmies() + unit.getSize());
		}
		
		gameDao.updatePlayerPoints(player);
		
		//------------------------------------//
		// MAJ des points des l'alliance : on ajoute les nouvelles donnees
		
		if(player.getAlly() != null){
			player.getAlly().setNbArmies(player.getAlly().getNbArmies() + player.getNbArmies());
			player.getAlly().setNbCities(player.getAlly().getNbCities() + player.getNbCities());
			player.getAlly().setNbPopulation(player.getAlly().getNbPopulation() + player.getNbPopulation());
			

			gameDao.updateAllyPoints(player.getAlly());
		}

		//------------------------------------//

		return player;
	}

	private void checkCityOwners(List<String> cityUIDs) 
	{
		if(debug)Utils.print("-------------------------------------------------");
		if(debug)Utils.print("checkCityOwners");
		for(String cityUID : cityUIDs){
			if(debug)Utils.print("city : " + cityUID);
			gameDao.checkCityOwner(cityUID);
		}
	}

	//==================================================================================================//

	public void savePlayer(Player player) {
		if(debug)Utils.print("-------------------------------------------------");
		if(debug)Utils.print("Save turn pour joueur : " + player.getName());

		//----------------------------------------------------------------//
		// Cities

		if(debug)Utils.print("--------------------");
		if(debug)Utils.print("Saving cities");

		for(City city : player.getCities())
		{
			updateCity(city, true);
		}
		
		//----------------------------------------------------------------//
		// Player
		
		gameDao.updatePlayer(player); // lastTurnPLayed, remove last Reports
	}
	
	//==================================================================================================//

	public ObjectsAltered createUnit(String uralysUID, Unit unit, String cityUID)
	{
		if(debug)Utils.print("-----------------------------------");
		if(debug)Utils.print("gamemanager createUnit : " + unit.getUnitUID() + " for uralysUID : " + uralysUID);
		
		gameDao.createUnit(unit, cityUID);
		gameDao.linkNewUnit(uralysUID, unit.getUnitUID());

		return movesManager.refreshUnitMoves(unit);
	}

	public ObjectsAltered updateUnit(Unit unit, boolean needReplacing)
	{
		if(debug)Utils.print("-----------------------------------");
		if(debug)Utils.print("gamemanager updateUnit : " + unit.getUnitUID());

		gameDao.updateUnit(unit);
		
		if(unit.getEndTime() < new Date().getTime() && unit.getEndTime() != -1){
			needReplacing = false;
		}
		
		if(needReplacing){
			if(debug)Utils.print("refreshUnitWay for the unit");
			return movesManager.refreshUnitMoves(unit);
		}

		return null;
	}
	
	public void deleteUnit(String uralysUID, Unit unit)
	{
		Utils.print("manager deleteUnit");
		if(unit.getMoves().size() != 0){
			ArrayList<Move> dummyMoves = new ArrayList<Move>();
			dummyMoves.add(unit.getMoves().get(0));			
			movesManager.refreshUnitMoves(unit);
		}
		
		gameDao.deleteUnit(uralysUID, unit.getUnitUID());
	}
	
	public void deleteUnits(String uralysUID, List<String> unitUIDs){
		gameDao.deleteUnits(uralysUID, unitUIDs);
	}

	//==================================================================================================//

	public void deleteMove(String moveUID) {
		deleteMove(moveUID, false);
	}
	
	private void deleteMove(String moveUID, boolean keepGatheringBecauseItIsLinkedWithAnotherMoveNow) {
		gameDao.deleteMove(moveUID, keepGatheringBecauseItIsLinkedWithAnotherMoveNow);
	}
	
	//==================================================================================================//

	public List<Item> loadItems() 
	{
		List<Item> items = new ArrayList<Item>();

		for(ItemDTO itemDTO : gameDao.loadItems()){
			items.add(EntitiesConverter.convertItemDTO(itemDTO));
		}

		return items;
	}

	//==================================================================================================//

	public List<Cell> loadCells(int[] groups, boolean refreshLandOwners) 
	{
		List<Cell> cases = new ArrayList<Cell>();
	
		List<CellDTO> casesLoaded = gameDao.loadCells(groups, refreshLandOwners);
		for(CellDTO caseDTO : casesLoaded){
			cases.add(EntitiesConverter.convertCellDTO(caseDTO));
		}

		return cases;
	}

	//==================================================================================================//

	public void changeName(String uralysUID, String newName) {
		gameDao.changeName(uralysUID, newName);
	}
	
	public void changeCityName(String cityUID, String newName) {
		gameDao.changeCityName(cityUID, newName);
	}
	
	//==================================================================================================//

	public List<Player> getCitiesBoard()
	{
		List<Player> players = new ArrayList<Player>();
		
		List<PlayerDTO> playersDTO = gameDao.getCitiesBoard();
		for(PlayerDTO playerDTO : playersDTO){
			Utils.print(playerDTO.getName());
			players.add(EntitiesConverter.convertPlayerDTO(playerDTO, false));
		}

		Utils.print(players.size() + " players au final");
		return players;
	}
	
	public List<Player> getLandsBoard()
	{
		List<Player> players = new ArrayList<Player>();
		
		List<PlayerDTO> playersDTO = gameDao.getLandsBoard();
		for(PlayerDTO playerDTO : playersDTO){
			players.add(EntitiesConverter.convertPlayerDTO(playerDTO, false));
		}

		return players;
	}
	
	public List<Player> getPopulationBoard()
	{
		List<Player> players = new ArrayList<Player>();
		
		List<PlayerDTO> playersDTO = gameDao.getPopulationBoard();
		for(PlayerDTO playerDTO : playersDTO){
			players.add(EntitiesConverter.convertPlayerDTO(playerDTO, false));
		}

		return players;
		
	}
	
	public List<Player> getArmiesBoard()
	{
		List<Player> players = new ArrayList<Player>();
		
		List<PlayerDTO> playersDTO = gameDao.getArmiesBoard();
		for(PlayerDTO playerDTO : playersDTO){
			players.add(EntitiesConverter.convertPlayerDTO(playerDTO, false));
		}

		return players;
		
	}
	
	//==================================================================================================//

	public void updatePlayerProfile(String playerUID, String profile) {
		gameDao.updatePlayerProfile(playerUID, profile);
	}
	
	public void updateAllyProfile(String allyUID, String profile) {
		gameDao.updateAllyProfile(allyUID, profile);
	}
	
	//==================================================================================================//

	public void sendMessage(String senderUID, String recipientUID, String message){
		gameDao.sendMessage(senderUID, recipientUID, message);
	}

	public void markAsRead(List<String> messageUIDs) {
		gameDao.markAsRead(messageUIDs);
	}

	public void archiveMessages(List<String> messageUIDs) {
		gameDao.archiveMessages(messageUIDs);
	}

	public void deleteMessages(String uralysUID, List<String> messageUIDs) {
		gameDao.deleteMessages(uralysUID, messageUIDs);
	}
	
	//==================================================================================================//

	public Ally createAlly(String uralysUID, String allyName) {
		return EntitiesConverter.convertAllyDTO(gameDao.createAlly(uralysUID, allyName), true);
	}

	public Ally getAlly(String allyUID) {
		return EntitiesConverter.convertAllyDTO(gameDao.getAlly(allyUID), true);
	}

	public void joinAlly(String uralysUID, String allyUID) {
		gameDao.joinAlly(uralysUID, allyUID);
	}

	public void inviteInAlly(String uralysUID, String allyUID) 
	{
		AllyDTO ally = gameDao.getAlly(allyUID);
		gameDao.inviteInAlly(uralysUID, allyUID, createInviteInAllyMessage(allyUID, ally.getName()));
	}

	public void removeFromAlly(String uralysUID, String allyUID){ 
		gameDao.removeFromAlly(uralysUID, allyUID);
	}

	public void saveAllyHierarchy(Ally ally) {
		
		List<String> playerUIDs = new ArrayList<String>();
		
		for(Player player : ally.getPlayers()){
			playerUIDs.add(player.getUralysUID());
		}
		
		gameDao.saveAllyPlayers(ally.getAllyUID(), playerUIDs);
	}
	
	private String createInviteInAllyMessage(String allyUID, String allyName) {
		return "____allyInvitation|"+allyUID+"|"+allyName;
	}
	
	//-----------------------------------------------------------------------------------//
	public List<Ally> getTopAlliesByCities() 
	{
		List<Ally> allies = new ArrayList<Ally>();
		
		List<AllyDTO> alliesDTO = gameDao.getTopAlliesByCities();
		for(AllyDTO allyDTO : alliesDTO){
			allies.add(EntitiesConverter.convertAllyDTO(allyDTO, false));
		}

		return allies;
	}

	public List<Ally> getTopAlliesByArmies() {

		List<Ally> allies = new ArrayList<Ally>();
		
		List<AllyDTO> alliesDTO = gameDao.getTopAlliesByArmies();
		for(AllyDTO allyDTO : alliesDTO){
			allies.add(EntitiesConverter.convertAllyDTO(allyDTO, false));
		}

		return allies;
	}

	public List<Ally> getTopAlliesByPopulation() {

		List<Ally> allies = new ArrayList<Ally>();
		
		List<AllyDTO> alliesDTO = gameDao.getTopAlliesByPopulation();
		for(AllyDTO allyDTO : alliesDTO){
			allies.add(EntitiesConverter.convertAllyDTO(allyDTO, false));
		}

		return allies;
	}

	public List<Ally> getTopAlliesByLands() {

		List<Ally> allies = new ArrayList<Ally>();
		
		List<AllyDTO> alliesDTO = gameDao.getTopAlliesByLands();
		for(AllyDTO allyDTO : alliesDTO){
			allies.add(EntitiesConverter.convertAllyDTO(allyDTO, false));
		}

		return allies;
	}
	
	
	//==================================================================================================//

	public static void main(String[] args){
		
	}
//
//	public Cell getCase(int x, int y) {
//		return EntitiesConverter.convertCaseDTO(gameDao.getCase(x,y));
//	}

	//==============================================================================================================//
	
}
