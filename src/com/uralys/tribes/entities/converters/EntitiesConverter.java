package com.uralys.tribes.entities.converters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.uralys.tribes.entities.Ally;
import com.uralys.tribes.entities.Cell;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Message;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Report;
import com.uralys.tribes.entities.Stock;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.dto.AllyDTO;
import com.uralys.tribes.entities.dto.CellDTO;
import com.uralys.tribes.entities.dto.CityDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.entities.dto.MessageDTO;
import com.uralys.tribes.entities.dto.MoveDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.StockDTO;
import com.uralys.tribes.entities.dto.UnitDTO;
import com.uralys.utils.Utils;

public class EntitiesConverter {

	// -----------------------------------------------------------------------------------//

	public static Move convertMoveDTO(MoveDTO moveDTO) 
	{
		if (moveDTO == null)
			return null;

		Move move = new Move();

		move.setMoveUID(moveDTO.getMoveUID());
		move.setCellUID(moveDTO.getCellUID());
		move.setTimeFrom(moveDTO.getTimeFrom());
		move.setTimeTo(moveDTO.getTimeTo());
		move.setUnitUID(moveDTO.getUnitUID());

		return move;
	}

	// -----------------------------------------------------------------------------------//

	public static Player convertPlayerDTO(PlayerDTO playerDTO, boolean requireFullData) 
	{
		if(playerDTO == null)
			return null;

		Player player = new Player();

		player.setUralysUID(playerDTO.getUralysUID());
		player.setName(playerDTO.getName());
		player.setLastStep(playerDTO.getLastStep());
		player.setNbLands(playerDTO.getNbLands());
		player.setNbArmies(playerDTO.getNbArmies());
		player.setNbCities(playerDTO.getNbCities());
		player.setNbPopulation(playerDTO.getNbPopulation());
		player.setProfile(playerDTO.getProfile() == null ? "" : playerDTO.getProfile());

		player.setAlly(convertAllyDTO(playerDTO.getAlly(), requireFullData));
		
		if(!requireFullData)
			return player;

			
		// -----------------------------------------------------------------------------------//

		List<City> cities = new ArrayList<City>();
		
		for (CityDTO cityDTO : playerDTO.getCities()) {
			cities.add(convertCityDTO(cityDTO, true));
		}

		player.setCities(cities);

		// -----------------------------------------------------------------------------------//

		List<Unit> units = new ArrayList<Unit>();

		for (UnitDTO unitDTO : playerDTO.getUnits()) {
			units.add(convertUnitDTO(unitDTO));
		}

		player.setUnits(units);

		// -----------------------------------------------------------------------------------//

		List<Message> readMessages = new ArrayList<Message>();
		List<Message> newMessages = new ArrayList<Message>();
		List<Message> archivedMessages = new ArrayList<Message>();
		
		for (MessageDTO message : playerDTO.getMessages()) 
		{
			if(message.getTime() > new Date().getTime())
				continue;
			
			switch (message.getStatus()) 
			{
				case Message.UNREAD:
					newMessages.add(convertMessageDTO(message));
					break;
				case Message.READ:
					readMessages.add(convertMessageDTO(message));
					break;
				case Message.ARCHIVED:
					archivedMessages.add(convertMessageDTO(message));
					break;

				default:
					break;
			}
		}
		
		player.setReadMessages(readMessages);
		player.setNewMessages(newMessages);
		player.setArchivedMessages(archivedMessages);
		
		// -----------------------------------------------------------------------------------//

		return player;

		// -----------------------------------------------------------------------------------//
	}

	// -----------------------------------------------------------------------------------//

	public static Message convertMessageDTO(MessageDTO messageDTO)
	{
		Message message = new Message();
		
		message.setMessageUID(messageDTO.getMessageUID());
		message.setSenderUID(messageDTO.getSenderUID());
		message.setSenderName(messageDTO.getSenderName());
		message.setStatus(messageDTO.getStatus());
		message.setTime(messageDTO.getTime());
		
		if(messageDTO.isReport()){

			Report report = new Report();
			
			report.setReportType(messageDTO.getReportType());
			report.setCellUID(messageDTO.getReportCellUID());
			
			report.getUnit1().setUnitUID(messageDTO.getUnit1_unitUID());
			report.getUnit1().setOwnerUID(messageDTO.getUnit1_ownerUID());
			report.getUnit1().setOwnerName(messageDTO.getUnit1_ownerName());
			report.getUnit1().setSize(messageDTO.getUnit1_size());
			report.getUnit1().setType(messageDTO.getUnit1_type());
			report.getUnit1().setBows(messageDTO.getUnit1_bows());
			report.getUnit1().setSwords(messageDTO.getUnit1_swords());
			report.getUnit1().setArmors(messageDTO.getUnit1_armors());
			report.getUnit1().setValue(messageDTO.getUnit1_value());

			report.getUnit1().setWheat(messageDTO.getUnit1_wheat());
			report.getUnit1().setWood(messageDTO.getUnit1_wood());
			report.getUnit1().setIron(messageDTO.getUnit1_iron());
			report.getUnit1().setGold(messageDTO.getUnit1_gold());
			
			report.getUnit1().setAttackACity(messageDTO.isUnit1_defendACity());
			report.getUnit1().setDefendACity(messageDTO.isUnit1_attackACity());


			report.getUnit2().setUnitUID(messageDTO.getUnit2_unitUID());
			report.getUnit2().setOwnerUID(messageDTO.getUnit2_ownerUID());
			report.getUnit2().setOwnerName(messageDTO.getUnit2_ownerName());
			report.getUnit2().setSize(messageDTO.getUnit2_size());
			report.getUnit2().setType(messageDTO.getUnit2_type());
			report.getUnit2().setBows(messageDTO.getUnit2_bows());
			report.getUnit2().setSwords(messageDTO.getUnit2_swords());
			report.getUnit2().setArmors(messageDTO.getUnit2_armors());
			report.getUnit2().setValue(messageDTO.getUnit2_value());

			report.getUnit2().setWheat(messageDTO.getUnit2_wheat());
			report.getUnit2().setWood(messageDTO.getUnit2_wood());
			report.getUnit2().setIron(messageDTO.getUnit2_iron());
			report.getUnit2().setGold(messageDTO.getUnit2_gold());
			
			report.getNextUnit().setUnitUID(messageDTO.getNextUnit_unitUID());
			report.getNextUnit().setOwnerUID(messageDTO.getNextUnit_ownerUID());
			report.getNextUnit().setOwnerName(messageDTO.getNextUnit_ownerName());
			report.getNextUnit().setSize(messageDTO.getNextUnit_size());
			report.getNextUnit().setType(messageDTO.getNextUnit_type());
			report.getNextUnit().setBows(messageDTO.getNextUnit_bows());
			report.getNextUnit().setSwords(messageDTO.getNextUnit_swords());
			report.getNextUnit().setArmors(messageDTO.getNextUnit_armors());
			report.getNextUnit().setValue(messageDTO.getNextUnit_value());
			
			report.getNextUnit().setWheat(messageDTO.getNextUnit_wheat());
			report.getNextUnit().setWood(messageDTO.getNextUnit_wood());
			report.getNextUnit().setIron(messageDTO.getNextUnit_iron());
			report.getNextUnit().setGold(messageDTO.getNextUnit_gold());
			
			message.setReport(report);
		}
		else{
			message.setContent(messageDTO.getContent().getValue());
		}
		
		return message;
	}
	
	// -----------------------------------------------------------------------------------//

	public static City convertCityDTO(CityDTO cityDTO, boolean requireFullData) 
	{
		if (cityDTO == null)
			return null;

		City city = new City();

		city.setCityUID(cityDTO.getCityUID());
		city.setOwnerUID(cityDTO.getOwnerUID());
		city.setIron(cityDTO.getIron());
		city.setPeopleCreatingIron(cityDTO.getPeopleCreatingIron());
		city.setName(cityDTO.getName());
		city.setPopulation(cityDTO.getPopulation());
		city.setWheat(cityDTO.getWheat());
		city.setPeopleCreatingWheat(cityDTO.getPeopleCreatingWheat());
		city.setWood(cityDTO.getWood());
		city.setPeopleCreatingWood(cityDTO.getPeopleCreatingWood());
		city.setX(cityDTO.getX());
		city.setY(cityDTO.getY());
		city.setGold(cityDTO.getGold());
		city.setBows(cityDTO.getBows());
		city.setSwords(cityDTO.getSwords());
		city.setArmors(cityDTO.getArmors());
		city.setBeginTime(cityDTO.getBeginTime());
		city.setEndTime(cityDTO.getEndTime());

		if(requireFullData){
			// ---------------------------------//
			
			List<Stock> stocks = new ArrayList<Stock>();
			
			for (StockDTO stockDTO : cityDTO.getStocks()) {
				stocks.add(convertStockDTO(stockDTO));
			}
			
			city.setStocks(stocks);
			
			// ---------------------------------//
		}

		return city;
	}

	// -----------------------------------------------------------------------------------//

	public static Unit convertUnitDTO(UnitDTO unitDTO) 
	{
		if (unitDTO == null)
			return null;

		Unit unit = new Unit();

		unit.setUnitUID(unitDTO.getUnitUID());
		unit.setSize(unitDTO.getSize());
		unit.setSpeed(unitDTO.getSpeed());

		unit.setWheat(unitDTO.getWheat());
		unit.setWood(unitDTO.getWood());
		unit.setIron(unitDTO.getIron());
		unit.setGold(unitDTO.getGold());
		unit.setBows(unitDTO.getBows());
		unit.setSwords(unitDTO.getSwords());
		unit.setArmors(unitDTO.getArmors());

		unit.setType(unitDTO.getType());
		unit.setPlayer(convertPlayerDTO(unitDTO.getPlayer(), false));

		unit.setBeginTime(unitDTO.getBeginTime());
		unit.setEndTime(unitDTO.getEndTime());

		unit.setUnitMetUID(unitDTO.getUnitMetUID());
		unit.setUnitNextUID(unitDTO.getUnitNextUID());
		unit.setMessageUID(unitDTO.getMessageUID());
		unit.setCellUIDExpectedForLand(unitDTO.getCellUIDExpectedForLand());

		// -----------------------------------------------------------------------------------//

		List<Move> moves = new ArrayList<Move>();

		for (MoveDTO moveDTO : unitDTO.getMoves()) {
			Move move = convertMoveDTO(moveDTO);
			moves.add(move);
		}

		unit.setMoves(moves);

		// -----------------------------------------------------------------------------------//

		return unit;
	}

	// -----------------------------------------------------------------------------------//

	public static Cell convertCellDTO(CellDTO cellDTO) {

		if (cellDTO == null)
			return null;

		Cell cell = new Cell();

		cell.setCellUID(cellDTO.getCellUID());
		cell.setX(cellDTO.getX());
		cell.setGroup(cellDTO.getGroupCell());
		cell.setY(cellDTO.getY());
		cell.setType(cellDTO.getType());

		cell.setCity(convertCityDTO(cellDTO.getCity(), false));

		cell.setLandOwner(convertPlayerDTO(cellDTO.getLandOwner(), false));
		cell.setChallenger(convertPlayerDTO(cellDTO.getChallenger(), false));
		cell.setTimeFromChallenging(cellDTO.getTimeFromChallenging());

		// -----------------------------------------------------------------------------------//

		cell.setArmy(null);
		cell.setCaravan(null);
		cell.setTimeToChangeUnit(-1);
		cell.setNextCellUID(null);
		
		long now = new Date().getTime();
		
		for(MoveDTO move : cellDTO.getMoves())
		{
			if(move.getTimeFrom() <= now && (move.getTimeTo() > now || move.getTimeTo() == -1))
			{
				UnitDTO unit = move.getUnit();
				if(unit.getEndTime() != -1 && unit.getEndTime() < new Date().getTime())
					continue; // on ne prend pas cette unite, puisqu'elle est perimee (par exemple dans le cas d'un gathering). Elle sera detruite au prochain getPlayer
				
				switch(unit.getType()){
					case Unit.ARMY :
						cell.setArmy(convertUnitDTO(unit));
						break;
					case Unit.CARAVAN :
						cell.setCaravan(convertUnitDTO(unit));
						break;
				}
				
				// on va ecouter la premiere des unitŽs qui va se dŽplacer 
				if(move.getTimeTo() != -1 && cell.getTimeToChangeUnit() == -1 || cell.getTimeToChangeUnit() > move.getTimeTo())
				{
					cell.setTimeToChangeUnit(move.getTimeTo());
					cell.setNextCellUID(move.getNextMove() != null ? move.getNextMove().getCellUID() : null);
				}
			}
		}

		// -----------------------------------------------------------------------------------//

		return cell;
	}

	// -----------------------------------------------------------------------------------//

	public static Item convertItemDTO(ItemDTO itemDTO) {

		if (itemDTO == null)
			return null;

		Item item = new Item();

		item.setName(itemDTO.getName());
		item.setPeopleRequired(itemDTO.getPeopleRequired());
		item.setGoldPriceBase(itemDTO.getGoldPriceBase());
		item.setGoldPriceCurrent(itemDTO.getGoldPriceCurrent());
		item.setIron(itemDTO.getIron());
		item.setValue(itemDTO.getValue());
		item.setItemUID(itemDTO.getItemUID());
		item.setWood(itemDTO.getWood());

		return item;
	}

	// -----------------------------------------------------------------------------------//
	
	public static Stock convertStockDTO(StockDTO stockDTO) {
		
		if (stockDTO == null)
			return null;
		
		Stock stock = new Stock();
		
		stock.setStockUID(stockDTO.getStockUID());
		stock.setStockCapacity(stockDTO.getStockCapacity());
		stock.setStockNextCapacity(stockDTO.getStockNextCapacity());
		stock.setPeopleBuildingStock(stockDTO.getPeopleBuildingStock());
		stock.setStockBeginTime(stockDTO.getStockBeginTime());
		stock.setStockEndTime(stockDTO.getStockEndTime());

		stock.setSmiths(stockDTO.getSmiths());
		stock.setItemsBeingBuilt(stockDTO.getItemsBeingBuilt());
		stock.setItemsBeingBuiltBeginTime(stockDTO.getItemsBeingBuiltBeginTime());
		stock.setItemsBeingBuiltEndTime(stockDTO.getItemsBeingBuiltEndTime());
		
		return stock;
	}
	
	// -----------------------------------------------------------------------------------//
	
	public static Ally convertAllyDTO(AllyDTO allyDTO, boolean requirePlayerList) 
	{
		if (allyDTO == null)
			return null;
		
		Ally ally = new Ally();

		ally.setAllyUID(allyDTO.getAllyUID());
		ally.setName(allyDTO.getName());
		ally.setNbLands(allyDTO.getNbLands());
		ally.setNbArmies(allyDTO.getNbArmies());
		ally.setNbCities(allyDTO.getNbCities());
		ally.setNbPopulation(allyDTO.getNbPopulation());
		ally.setProfile(allyDTO.getProfile() == null ? "" : allyDTO.getProfile());
		
		ally.setNbPlayers(allyDTO.getPlayerUIDs().size());

		// -----------------------------------------------------------------------------------//
		
		if(requirePlayerList)
		{
			List<Player> players = new ArrayList<Player>();
			
			for (PlayerDTO playerDTO : allyDTO.getPlayers()) {
				Utils.print(playerDTO.getName());
				players.add(convertPlayerDTO(playerDTO, false));
			}
			
			ally.setPlayers(players);

			List<Player> inviteds = new ArrayList<Player>();
			
			for (PlayerDTO playerDTO : allyDTO.getInviteds()) {
				inviteds.add(convertPlayerDTO(playerDTO, false));
			}
			
			ally.setInviteds(inviteds);
		}
		
		// -----------------------------------------------------------------------------------//

		return ally;
	}
		
	// -----------------------------------------------------------------------------------//

}
