package com.uralys.tribes.entities.converters;

import java.util.ArrayList;
import java.util.List;

import com.uralys.tribes.entities.Army;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Equipment;
import com.uralys.tribes.entities.Smith;
import com.uralys.tribes.entities.Game;
import com.uralys.tribes.entities.Merchant;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Profil;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.dto.ArmyDTO;
import com.uralys.tribes.entities.dto.CityDTO;
import com.uralys.tribes.entities.dto.EquipmentDTO;
import com.uralys.tribes.entities.dto.SmithDTO;
import com.uralys.tribes.entities.dto.GameDTO;
import com.uralys.tribes.entities.dto.MerchantDTO;
import com.uralys.tribes.entities.dto.MoveDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.ProfilDTO;
import com.uralys.tribes.entities.dto.ItemDTO;

public class EntitiesConverter {
	
	//-----------------------------------------------------------------------------------//
	
	public static Move convertMoveDTO(MoveDTO moveDTO) {
		
		if(moveDTO == null)
			return null;
		
		Move move = new Move();
		
		move.setMoveUID(moveDTO.getMoveUID());
		move.setxFrom(moveDTO.getxFrom());
		move.setxTo(moveDTO.getxTo());
		move.setyFrom(moveDTO.getyFrom());
		move.setyTo(moveDTO.getyTo());
		move.setMovingUID(moveDTO.getMovingUID());
		move.setType(moveDTO.getType());
		
		return move;
	}
	
	//-----------------------------------------------------------------------------------//
	
	public static Player convertPlayerDTO(PlayerDTO playerDTO) {
		
		if(playerDTO == null)
			return null;
		
		Player player = new Player();
		
		player.setPlayerUID(playerDTO.getPlayerUID());
		player.setName(playerDTO.getName());
		player.setGameName(playerDTO.getGameName());
		player.setGameUID(playerDTO.getGameUID());
		player.setLastTurnPlayed(playerDTO.getLastTurnPlayed());
		
		//-----------------------------------------------------------------------------------//
		List<City> cities = new ArrayList<City>();
		
		for(CityDTO cityDTO : playerDTO.getCities()){
			cities.add(convertCityDTO(cityDTO));
		}
		
		player.setCities(cities);
		
		//-----------------------------------------------------------------------------------//
		List<Move> moves = new ArrayList<Move>();
		
		for(MoveDTO moveDTO : playerDTO.getMoves()){
			moves.add(convertMoveDTO(moveDTO));
		}
		
		player.setMoves(moves);
		
		//-----------------------------------------------------------------------------------//		
		List<Army> armies = new ArrayList<Army>();
		
		for(ArmyDTO armyDTO : playerDTO.getArmies()){
			armies.add(convertArmyDTO(armyDTO));
		}
		
		player.setArmies(armies);
		
		//-----------------------------------------------------------------------------------//		
		List<Merchant> merchants = new ArrayList<Merchant>();
		
		for(MerchantDTO merchantDTO : playerDTO.getMerchants()){
			merchants.add(convertMerchantDTO(merchantDTO));
		}
		
		player.setMerchants(merchants);
		
		//-----------------------------------------------------------------------------------//
		return player;
	}
	
	
	//-----------------------------------------------------------------------------------//
	
	public static Game convertGameDTO(GameDTO gameDTO) {

		if(gameDTO == null)
			return null;
		
		Game game = new Game();
		
		game.setCreatorUralysUID(gameDTO.getCreatorUralysUID());
		game.setGameUID(gameDTO.getGameUID());
		game.setName(gameDTO.getName());
		game.setStatus(gameDTO.getStatus());
		game.setCurrentTurn(gameDTO.getCurrentTurn());
		game.setBeginTurnTimeMillis(gameDTO.getBeginTurnTimeMillis());
		game.setNbMinByTurn(gameDTO.getNbMinByTurn());
		
		List<Player> players = new ArrayList<Player>();
		
		for(PlayerDTO playerDTO : gameDTO.getPlayers()){
			players.add(convertPlayerDTO(playerDTO));
		}
		
		game.setPlayers(players);

		return game;
	}

	
	//-----------------------------------------------------------------------------------//
	
	public static Profil convertProfilDTO(ProfilDTO profilDTO) {
		
		if(profilDTO == null)
			return null;
		
		Profil profil = new Profil();
		
		profil.setUralysUID(profilDTO.getUralysUID());
		
		List<Player> players = new ArrayList<Player>();
		
		for(PlayerDTO playerDTO : profilDTO.getPlayers()){
			players.add(convertPlayerDTO(playerDTO));
		}
		
		profil.setPlayers(players);
		
		return profil;
	}
	

	//-----------------------------------------------------------------------------------//	
	public static City convertCityDTO(CityDTO cityDTO) {
		
		if(cityDTO == null)
			return null;
		
		City city = new City();

		city.setCityUID(cityDTO.getCityUID());
		city.setGold(cityDTO.getGold());
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
		
		//---------------------------------//
		
		List<Equipment> stock = new ArrayList<Equipment>();
		
		for(EquipmentDTO equipmentDTO : cityDTO.getEquipmentStock()){
			stock.add(convertEquipmentDTO(equipmentDTO));
		}
		
		city.setEquipmentStock(stock);

		//---------------------------------//

		List<Smith> smiths = new ArrayList<Smith>();
		
		for(SmithDTO smithDTO : cityDTO.getSmiths()){
			smiths.add(convertSmithDTO(smithDTO));
		}
		
		city.setSmiths(smiths);

		//---------------------------------//
		
		return city;
	}
	
	
	//-----------------------------------------------------------------------------------//
	
	public static Army convertArmyDTO(ArmyDTO armyDTO) {
		
		if(armyDTO == null)
			return null;
		
		Army army = new Army();
		
		army.setArmyUID(armyDTO.getArmyUID());
		army.setSize(armyDTO.getSize());
		army.setSpeed(armyDTO.getSpeed());
		army.setValue(armyDTO.getValue());
		army.setX(armyDTO.getX());
		army.setY(armyDTO.getY());
		
		List<Equipment> equipments = new ArrayList<Equipment>();
		
		for(EquipmentDTO equipmentDTO : armyDTO.getEquipments()){
			equipments.add(convertEquipmentDTO(equipmentDTO));
		}
		
		army.setEquipments(equipments);
		
		return army;
	}
	
	
	//-----------------------------------------------------------------------------------//
	
	public static Merchant convertMerchantDTO(MerchantDTO merchantDTO) {
		
		if(merchantDTO == null)
			return null;
		
		Merchant merchant = new Merchant();
		
		merchant.setGold(merchantDTO.getGold());
		merchant.setIron(merchantDTO.getIron());
		merchant.setMerchantUID(merchantDTO.getMerchantUID());
		merchant.setSize(merchantDTO.getSize());
		merchant.setSpeed(merchantDTO.getSpeed());
		merchant.setWheat(merchantDTO.getWheat());
		merchant.setWood(merchantDTO.getWood());
		merchant.setX(merchantDTO.getX());
		merchant.setY(merchantDTO.getY());

		return merchant;
	}
	
	
	//-----------------------------------------------------------------------------------//
	
	public static Item convertItemDTO(ItemDTO itemDTO) {
		
		if(itemDTO == null)
			return null;
		
		Item item = new Item();
		
		item.setName(itemDTO.getName());
		item.setPeopleRequired(itemDTO.getPeopleRequired());
		item.setGoldPrice(itemDTO.getGoldPrice());
		item.setIron(itemDTO.getIron());
		item.setValue(itemDTO.getValue());
		item.setItemUID(itemDTO.getItemUID());
		item.setWood(itemDTO.getWood());

		return item;
	}
	
	//-----------------------------------------------------------------------------------//
	
	public static Equipment convertEquipmentDTO(EquipmentDTO equipmentDTO) {
		
		if(equipmentDTO == null)
			return null;
		
		Equipment equipment = new Equipment();

		equipment.setEquimentUID(equipmentDTO.getEquimentUID());
		equipment.setSize(equipmentDTO.getSize());
		
		equipment.setItem(convertItemDTO(equipmentDTO.getItem()));
		
		return equipment;
	}

	//-----------------------------------------------------------------------------------//	
	
	public static Smith convertSmithDTO(SmithDTO smithDTO) {
		
		if(smithDTO == null)
			return null;
		
		Smith smith = new Smith();
		
		smith.setSmithUID(smithDTO.getSmithUID());
		smith.setPeople(smithDTO.getPeople());
		
		smith.setItem(convertItemDTO(smithDTO.getItem()));
		
		return smith;
	}
}
