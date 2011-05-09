package com.uralys.tribes.entities.converters;

import java.util.ArrayList;
import java.util.List;

import com.uralys.tribes.dao.impl.UniversalDAO;
import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.Gathering;
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
import com.uralys.tribes.entities.dto.GatheringDTO;
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

	// -----------------------------------------------------------------------------------//

	public static Move convertMoveDTO(MoveDTO moveDTO) {

		if (moveDTO == null)
			return null;

		Move move = new Move();

		move.setMoveUID(moveDTO.getMoveUID());
		move.setCaseUID(moveDTO.getCaseUID());
		move.setTimeFrom(moveDTO.getTimeFrom());
		move.setTimeTo(moveDTO.getTimeTo());
		move.setUnitUID(moveDTO.getUnitUID());
		move.setValue(moveDTO.getValue());

		move.setGathering(convertGatheringDTO(moveDTO.getGathering()));

		return move;
	}

	// -----------------------------------------------------------------------------------//

	public static Player convertPlayerDTO(PlayerDTO playerDTO) {

		if (playerDTO == null)
			return null;

		Player player = new Player();

		player.setUralysUID(playerDTO.getUralysUID());
		player.setAllyUID(playerDTO.getAllyUID());
		player.setName(playerDTO.getName());
		player.setNbLands(playerDTO.getNbLands());
		player.setLastStep(playerDTO.getLastStep());

		// -----------------------------------------------------------------------------------//

		List<City> cities = new ArrayList<City>();

		for (CityDTO cityDTO : playerDTO.getCities()) {
			System.out.println("city : " + cityDTO.getCityUID());
			cities.add(convertCityDTO(cityDTO));
		}

		player.setCities(cities);

		// -----------------------------------------------------------------------------------//

		List<Conflict> conflicts = new ArrayList<Conflict>();

		for (ConflictDTO conflictDTO : playerDTO.getMeetings()) {
			conflicts.add(convertConflictDTO(conflictDTO));
		}

		player.setConflicts(conflicts);

		// -----------------------------------------------------------------------------------//

		List<Unit> units = new ArrayList<Unit>();

		for (UnitDTO unitDTO : playerDTO.getUnits()) {
			units.add(convertUnitDTO(unitDTO, true));
		}

		player.setUnits(units);

		// -----------------------------------------------------------------------------------//

		return player;

		// -----------------------------------------------------------------------------------//
	}

	// -----------------------------------------------------------------------------------//

	public static City convertCityDTO(CityDTO cityDTO) {

		if (cityDTO == null)
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

		// ---------------------------------//

		List<Equipment> stock = new ArrayList<Equipment>();

		for (EquipmentDTO equipmentDTO : cityDTO.getEquipmentStock()) {
			stock.add(convertEquipmentDTO(equipmentDTO));
		}

		city.setEquipmentStock(stock);

		// ---------------------------------//

		List<Smith> smiths = new ArrayList<Smith>();

		for (SmithDTO smithDTO : cityDTO.getSmiths()) {
			smiths.add(convertSmithDTO(smithDTO));
		}

		city.setSmiths(smiths);

		// ---------------------------------//

		return city;
	}

	// -----------------------------------------------------------------------------------//

	public static Unit convertUnitDTO(UnitDTO unitDTO, boolean requireMoves) {

		if (unitDTO == null)
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
		unit.setPlayerUID(unitDTO.getPlayerUID());

		unit.setBeginTime(unitDTO.getBeginTime());
		unit.setEndTime(unitDTO.getEndTime());
		unit.setGatheringUIDExpected(unitDTO.getGatheringUIDExpected());
		unit.setConflictUIDExpected(unitDTO.getConflictUIDExpected());

		// -----------------------------------------------------------------------------------//

		List<Equipment> equipments = new ArrayList<Equipment>();

		for (EquipmentDTO equipmentDTO : unitDTO.getEquipments()) {
			equipments.add(convertEquipmentDTO(equipmentDTO));
		}

		unit.setEquipments(equipments);

		// -----------------------------------------------------------------------------------//

		// pour eviter le loop infini : pas besoin des moves pour les units dans
		// le gathering
		if (requireMoves) {
			List<Move> moves = new ArrayList<Move>();

			for (MoveDTO moveDTO : unitDTO.getMoves()) {
				Move move = convertMoveDTO(moveDTO);
				moves.add(move);
			}

			unit.setMoves(moves);
		}

		// -----------------------------------------------------------------------------------//

		return unit;
	}

	// -----------------------------------------------------------------------------------//

	public static Case convertCaseDTO(CaseDTO caseDTO) {

		if (caseDTO == null)
			return null;

		Case _case = new Case();

		_case.setCaseUID(caseDTO.getCaseUID());
		_case.setX(caseDTO.getX());
		_case.setY(caseDTO.getY());
		_case.setType(caseDTO.getType());

		_case.setCity(convertCityDTO(caseDTO.getCity()));
		_case.setLandOwner(convertPlayerDTO(caseDTO.getLandOwner()));

		// -----------------------------------------------------------------------------------//

		List<Move> moves = new ArrayList<Move>();
		List<String> unitUIDs = new ArrayList<String>();

		for (MoveDTO moveDTO : caseDTO.getMoves()) {
			Move move = convertMoveDTO(moveDTO);
			moves.add(move);
			unitUIDs.add(move.getUnitUID());
		}

		_case.setRecordedMoves(moves);

		// -----------------------------------------------------------------------------------//

		List<Unit> units = new ArrayList<Unit>();

		for (UnitDTO unitDTO : UniversalDAO.getInstance().getListDTO(unitUIDs,
				UnitDTO.class)) {
			units.add(convertUnitDTO(unitDTO, true));
		}

		_case.setUnits(units);

		// -----------------------------------------------------------------------------------//

		return _case;
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

	public static Equipment convertEquipmentDTO(EquipmentDTO equipmentDTO) {

		if (equipmentDTO == null)
			return null;

		Equipment equipment = new Equipment();

		equipment.setEquimentUID(equipmentDTO.getEquimentUID());
		equipment.setSize(equipmentDTO.getSize());

		equipment.setItem(convertItemDTO(equipmentDTO.getItem()));

		return equipment;
	}

	// -----------------------------------------------------------------------------------//

	public static Smith convertSmithDTO(SmithDTO smithDTO) {

		if (smithDTO == null)
			return null;

		Smith smith = new Smith();

		smith.setSmithUID(smithDTO.getSmithUID());
		smith.setPeople(smithDTO.getPeople());

		smith.setItem(convertItemDTO(smithDTO.getItem()));

		return smith;
	}

	// -----------------------------------------------------------------------------------//

	public static Conflict convertConflictDTO(ConflictDTO conflictDTO) {

		if (conflictDTO == null)
			return null;

		Conflict conflict = new Conflict();
		conflict.setConflictUID(conflictDTO.getConflictUID());
		conflict.setCaseUID(conflictDTO.getCaseUID());
		conflict.setTimeFrom(conflictDTO.getTimeFrom());
		conflict.setTimeTo(conflictDTO.getTimeTo());

		// -----------------------------------------------------------------------------------//

		List<Gathering> gatherings = new ArrayList<Gathering>();

		for (GatheringDTO gatheringDTO : conflictDTO.getGatherings()) {
			gatherings.add(convertGatheringDTO(gatheringDTO));
		}

		conflict.setGatherings(gatherings);

		// ------------------------------------------------------------------------------------//

		return conflict;
	}

	// -----------------------------------------------------------------------------------//

	public static Gathering convertGatheringDTO(GatheringDTO gatheringDTO) {
		if (gatheringDTO == null)
			return null;

		Gathering gathering = new Gathering();

		gathering.setAllyUID(gatheringDTO.getAllyUID());
		gathering.setGatheringUID(gatheringDTO.getGatheringUID());

		// -----------------------------------------------------------------------------------//

		List<Unit> units = new ArrayList<Unit>();

		for (UnitDTO unitDTO : gatheringDTO.getUnits()) {
			units.add(convertUnitDTO(unitDTO, false));
		}

		gathering.setUnits(units);

		return gathering;
	}

	// -----------------------------------------------------------------------------------//

	public static MoveConflict convertMoveConflictDTO(MoveConflictDTO moveConflictDTO) 
	{
		if (moveConflictDTO == null)
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
