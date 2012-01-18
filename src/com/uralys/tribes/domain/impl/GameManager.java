package com.uralys.tribes.domain.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.uralys.tribes.commons.Constants;
import com.uralys.tribes.dao.IGameDAO;
import com.uralys.tribes.dao.impl.PMF;
import com.uralys.tribes.domain.IGameManager;
import com.uralys.tribes.domain.IMovesManager;
import com.uralys.tribes.entities.Ally;
import com.uralys.tribes.entities.Cell;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.MoveResult;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Report;
import com.uralys.tribes.entities.Stock;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.converters.EntitiesConverter;
import com.uralys.tribes.entities.dto.AllyDTO;
import com.uralys.tribes.entities.dto.CellDTO;
import com.uralys.tribes.entities.dto.CityDTO;
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
		return gameDao.buildCity(city, uralysUID);
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
		
		checkCitiesBeingOwned(uralysUID);
		checkCitiesBeingLost(uralysUID);
		
		//refresh du player apres les changements de propriete des villes
		playerDTO = gameDao.getPlayer(uralysUID);
		
		Player player = EntitiesConverter.convertPlayerDTO(playerDTO, true);

		if(player.getCities().size() == 0){
			if(debug)Utils.print("Creation de la nouvelle premier ville");
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
		
		if(player.getAlly() != null)
		{
			player.getAlly().setNbArmies(player.getAlly().getNbArmies() + player.getNbArmies());
			player.getAlly().setNbCities(player.getAlly().getNbCities() + player.getNbCities());
			player.getAlly().setNbPopulation(player.getAlly().getNbPopulation() + player.getNbPopulation());
			
			gameDao.updateAllyPoints(player.getAlly());
		}

		//------------------------------------//

		if(debug)Utils.print("player ready | cities size : " + player.getCities().size());
		if(debug)Utils.print("player ready | nb cities : " + player.getNbCities());
		return player;
	}

	//==================================================================================================//

	private void checkCitiesBeingOwned(String uralysUID) 
	{
		if(debug)Utils.print("-------------------------------------------------");
		if(debug)Utils.print("verif des villes que "+uralysUID+" va gagner");
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, uralysUID);
		long now = new Date().getTime();
		
		List<String> citiesToRemoveFromBeingOwned = new ArrayList<String>();
		
		for(int i = 0; i < playerDTO.getCityBeingOwnedUIDs().size(); i++)
		{
			String[] cityBeingOwnedData = playerDTO.getCityBeingOwnedUIDs().get(i).split("_");
			String cityUID = cityBeingOwnedData[0];
			long timeToChangeOwner = Long.parseLong(cityBeingOwnedData[1]);
			int populationLost = Integer.parseInt(cityBeingOwnedData[2]);
			
			if(debug)Utils.print("city : " + cityUID);
			if(debug)Utils.print("timeToChangeOwner : " + timeToChangeOwner + " | now : " + now);
			if(debug)Utils.print("populationLost : " + populationLost);
			
			if(timeToChangeOwner != -1 && timeToChangeOwner < now){
				gameDao.changeCityOwner(cityUID, playerDTO.getUralysUID(), populationLost, pm);
				citiesToRemoveFromBeingOwned.add(playerDTO.getCityBeingOwnedUIDs().get(i));
			}
		}
		
		for(String cityToRemove : citiesToRemoveFromBeingOwned){
			playerDTO.getCityBeingOwnedUIDs().remove(cityToRemove);
		}

		
		pm.close();
	}	
	
	private void checkCitiesBeingLost(String uralysUID) 
	{
		if(debug)Utils.print("-------------------------------------------------");
		if(debug)Utils.print("verif des villes que "+uralysUID+" va perdre");
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		PlayerDTO playerDTO = pm.getObjectById(PlayerDTO.class, uralysUID);
		
		for(CityDTO city : playerDTO.getCities()){
			for(String nextOwnerUID : city.getNextOwnerUIDs()){
				checkCitiesBeingOwned(nextOwnerUID);
			}
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

	public MoveResult createUnit(String uralysUID, Unit unit)
	{
		if(debug)Utils.print("-----------------------------------");
		if(debug)Utils.print("gamemanager createUnit : " + unit.getUnitUID() + " for uralysUID : " + uralysUID);
		
		gameDao.createUnit(unit, null);
		gameDao.linkNewUnit(uralysUID, unit.getUnitUID());

		return movesManager.refreshUnitMoves(unit, true);
	}

	public MoveResult updateUnit(Unit unit)
	{
		if(debug)Utils.print("-----------------------------------");
		if(debug)Utils.print("gamemanager updateUnit : " + unit.getUnitUID());

		try{
			gameDao.updateUnit(unit);
		}
		catch(Exception e){
			return null;
		}
		
		if(debug)Utils.print("refreshUnitWay for the unit");
		return movesManager.refreshUnitMoves(unit, false);
	}
	
	public void deleteUnit(String uralysUID, Unit unit)
	{
		Utils.print("manager deleteUnit");
		if(unit.getMoves().size() != 0){
			ArrayList<Move> dummyMoves = new ArrayList<Move>();
			dummyMoves.add(unit.getMoves().get(0));			
			movesManager.refreshUnitMoves(unit, false);
		}
		
		gameDao.deleteUnit(uralysUID, unit.getUnitUID());
	}
	
	public void deleteUnits(String uralysUID, List<String> unitUIDs){
		gameDao.deleteUnits(uralysUID, unitUIDs);
	}

	//==================================================================================================//

	public void shoot(Unit shooter, Unit target, String cellUID)
	{
		if(debug)Utils.print("----------------------------");
		if(debug)Utils.print("shoot");
		if(debug)Utils.print("----------------------------");
		if(debug)Utils.print("shooter : " + shooter.getUnitUID());
		if(debug)Utils.print("target : " + target.getUnitUID());
		if(debug)Utils.print("cellUID : " + cellUID);
		if(debug)Utils.print("----------------------------");
		
		int previousSize = target.getSize();
		int deads = shooter.getBows()/Constants.BOW_SHOT_COEFF;

		if(debug)Utils.print("deads : " + deads);
		if(debug)Utils.print("----------------------------");
		
		target.setSize(previousSize - deads);
		
		int newSize = target.getSize();

		if(debug)Utils.print("newSize : " + newSize);
		if(debug)Utils.print("----------------------------");
		
		if(newSize <= 0){
			newSize = 0;
			movesManager.cancelAllUnitMoves(target);
			deleteUnit(target.getPlayer().getUralysUID(), target);
		}
		else{
			movesManager.refreshUnitMoves(target, false);
		}

		shooter.setLastShotTime(new Date().getTime());
		gameDao.updateUnit(shooter);
		
		sendReportForBowShot(cellUID, shooter, target, previousSize, newSize, deads);
	}

	private void sendReportForBowShot(String cellUID, Unit shooter, Unit target, int previousSize, int newSize, int deads) 
	{
		Report report = new Report();
		
		report.setReportType(MovesManager.REPORT_BOW_SHOT);
		report.setCellUID(cellUID);
		
		report.getUnit1().setUnitUID(shooter.getUnitUID());
		report.getUnit1().setOwnerUID(shooter.getPlayer().getUralysUID());
		report.getUnit1().setOwnerName(shooter.getPlayer().getName());
		report.getUnit1().setType(shooter.getType());
		report.getUnit1().setBows(shooter.getBows());
		report.getUnit1().setValue(deads);

		report.getUnit1().setAttackACity(false);
		report.getUnit1().setDefendACity(false);

		report.getUnit2().setUnitUID(target.getUnitUID());
		report.getUnit2().setOwnerUID(target.getPlayer().getUralysUID());
		report.getUnit2().setOwnerName(target.getPlayer().getName());
		report.getUnit2().setSize(previousSize);
		report.getUnit2().setType(target.getType());

		report.getNextUnit().setUnitUID(target.getUnitUID());
		report.getNextUnit().setOwnerUID(target.getPlayer().getUralysUID());
		report.getNextUnit().setOwnerName(target.getPlayer().getName());
		report.getNextUnit().setSize(newSize);
		
		gameDao.sendReport(report, target.getPlayer().getUralysUID(), new Date().getTime());
	}
	//==================================================================================================//

	public void deleteMove(String moveUID) {
		gameDao.deleteMove(moveUID);
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
		List<Cell> cells = new ArrayList<Cell>();
	
		List<CellDTO> casesLoaded = gameDao.loadCells(groups, refreshLandOwners);
		for(CellDTO cellDTO : casesLoaded){
			cells.add(EntitiesConverter.convertCellDTO(cellDTO));
		}

		return cells;
	}

	public List<Cell> getNewCells(Cell cell) 
	{
		List<Cell> cells = new ArrayList<Cell>();
		
		cells.add(EntitiesConverter.convertCellDTO(gameDao.getCell(cell.getX(), cell.getY())));
		
		if(cell.getNextCellUID() != null)
			cells.add(EntitiesConverter.convertCellDTO(gameDao.getCell(cell.getNextCellUID())));

		return cells;
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

	//==============================================================================================================//
	
}
