package com.uralys.tribes.entities.converters;

import java.util.ArrayList;
import java.util.List;

import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Conflict;
import com.uralys.tribes.entities.Equipment;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.MoveConflict;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Smith;
import com.uralys.tribes.entities.dto.CaseDTO;
import com.uralys.tribes.entities.dto.UnitDTO;
import com.uralys.tribes.entities.dto.CityDTO;
import com.uralys.tribes.entities.dto.ConflictDTO;
import com.uralys.tribes.entities.dto.EquipmentDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.entities.dto.MoveConflictDTO;
import com.uralys.tribes.entities.dto.MoveDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.SmithDTO;

public class EntitiesConverter {
	
	//-----------------------------------------------------------------------------------//
	
	public static Move convertMoveDTO(MoveDTO moveDTO) {
		
		if(moveDTO == null)
			return null;
		
		Move move = new Move();
		
		move.setMoveUID(moveDTO.getMoveUID());
		
		return move;
	}
	
	//-----------------------------------------------------------------------------------//
	
	public static Player convertPlayerDTO(PlayerDTO playerDTO) {
		
		if(playerDTO == null)
			return null;
		
		Player player = new Player();
		
		player.setUralysUID(playerDTO.getUralysUID());
		player.setName(playerDTO.getName());
		player.setNbLands(playerDTO.getNbLands());
		
		//-----------------------------------------------------------------------------------//
		List<City> cities = new ArrayList<City>();
		
		for(CityDTO cityDTO : playerDTO.getCities()){
			System.out.println("city : " + cityDTO.getCityUID());
			cities.add(convertCityDTO(cityDTO));
		}
		
		player.setCities(cities);

		//-----------------------------------------------------------------------------------//		
		List<Conflict> conflicts = new ArrayList<Conflict>();
		
		for(ConflictDTO conflictDTO : playerDTO.getConflicts()){
			conflicts.add(convertConflictDTO(conflictDTO));
		}
		
		player.setConflicts(conflicts);
		
		
		//-----------------------------------------------------------------------------------//		
		List<Unit> units = new ArrayList<Unit>();
		
		for(UnitDTO unitDTO : playerDTO.getUnits()){
			units.add(convertUnitDTO(unitDTO));
		}
		
		player.setUnits(units);
				//-----------------------------------------------------------------------------------//

		return player;

		//-----------------------------------------------------------------------------------//
	}
	
	
	//-----------------------------------------------------------------------------------//	
	public static City convertCityDTO(CityDTO cityDTO) {
		
		if(cityDTO == null)
			return null;
		
		City city = new City();

		city.setCityUID(cityDTO.getCityUID());
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
	
	public static Unit convertUnitDTO(UnitDTO unitDTO) {
		
		if(unitDTO == null)
			return null;
		
		Unit unit = new Unit();
		
		unit.setUnitUID(unitDTO.getUnitUID());
		unit.setSize(unitDTO.getSize());
		unit.setSpeed(unitDTO.getSpeed());
		unit.setValue(unitDTO.getValue());
		unit.setGold(unitDTO.getGold());
		unit.setIron(unitDTO.getIron());
		unit.setWheat(unitDTO.getWheat());
		unit.setWood(unitDTO.getWood());
		
		unit.setType(unitDTO.getType());
		unit.setStatus(unitDTO.getStatus());
		unit.setCurrentCase(convertCaseDTO(unitDTO.getCurrentCase()));
		unit.setPlayerUID(unitDTO.getPlayerUID());

		//-----------------------------------------------------------------------------------//
		
		List<Equipment> equipments = new ArrayList<Equipment>();
		
		for(EquipmentDTO equipmentDTO : unitDTO.getEquipments()){
			equipments.add(convertEquipmentDTO(equipmentDTO));
		}
		
		unit.setEquipments(equipments);

		//-----------------------------------------------------------------------------------//

		List<Move> moves = new ArrayList<Move>();
		
		for(MoveDTO moveDTO : unitDTO.getMoves()){
			moves.add(convertMoveDTO(moveDTO));
		}
		
		unit.setMoves(moves);
		
		return unit;
	}
	
	//-----------------------------------------------------------------------------------//

	
	public static Case convertCaseDTO(CaseDTO caseDTO) {
		
		if(caseDTO == null)
			return null;
		
		Case _case = new Case();
		
		_case.setX(caseDTO.getX());
		_case.setY(caseDTO.getY());
		_case.setType(caseDTO.getType());

		_case.setCity(convertCityDTO(caseDTO.getCity()));
		_case.setLandOwner(convertPlayerDTO(caseDTO.getLandOwner()));

		//-----------------------------------------------------------------------------------//

		List<Move> moves = new ArrayList<Move>();
		
		for(MoveDTO moveDTO : caseDTO.getMoves()){
			moves.add(convertMoveDTO(moveDTO));
		}
		
		_case.setRecordedMoves(moves);
		
		return _case;
	}
	
	
	//-----------------------------------------------------------------------------------//
	
	public static Item convertItemDTO(ItemDTO itemDTO) {
		
		if(itemDTO == null)
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
	
	//-----------------------------------------------------------------------------------//
	
	public static Conflict convertConflictDTO(ConflictDTO conflictDTO) {
		
		if(conflictDTO == null)
			return null;
		
		Conflict conflict = new Conflict();

		conflict.setConflictUID(conflictDTO.getConflictUID());
		
		//------------------------------------------------------------------------------------//
		
		return conflict;
	}
	//-----------------------------------------------------------------------------------//
	
	public static MoveConflict convertMoveConflictDTO(MoveConflictDTO moveConflictDTO) {
		if(moveConflictDTO == null)
			return null;
		
		MoveConflict moveConflict = new MoveConflict();

		moveConflict.setArmyArmors(moveConflictDTO.getArmyArmors());
		moveConflict.setArmyBows(moveConflictDTO.getArmyBows());
		moveConflict.setArmySize(moveConflictDTO.getArmySize());
		moveConflict.setArmySwords(moveConflictDTO.getArmySwords());
		moveConflict.setxFrom(moveConflictDTO.getxFrom());
		moveConflict.setyFrom(moveConflictDTO.getyFrom());
		moveConflict.setArmyStanding(moveConflictDTO.getArmyStanding());
		
		return moveConflict;
	}
		
}
