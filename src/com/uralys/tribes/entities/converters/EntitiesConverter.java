package com.uralys.tribes.entities.converters;

import java.util.ArrayList;
import java.util.List;

import com.uralys.tribes.dao.impl.UniversalDAO;
import com.uralys.tribes.entities.Case;
import com.uralys.tribes.entities.City;
import com.uralys.tribes.entities.Conflict;
import com.uralys.tribes.entities.Equipment;
import com.uralys.tribes.entities.Gathering;
import com.uralys.tribes.entities.Item;
import com.uralys.tribes.entities.Message;
import com.uralys.tribes.entities.Move;
import com.uralys.tribes.entities.MoveConflict;
import com.uralys.tribes.entities.Player;
import com.uralys.tribes.entities.Smith;
import com.uralys.tribes.entities.Unit;
import com.uralys.tribes.entities.dto.CaseDTO;
import com.uralys.tribes.entities.dto.CityDTO;
import com.uralys.tribes.entities.dto.ConflictDTO;
import com.uralys.tribes.entities.dto.EquipmentDTO;
import com.uralys.tribes.entities.dto.GatheringDTO;
import com.uralys.tribes.entities.dto.ItemDTO;
import com.uralys.tribes.entities.dto.MessageDTO;
import com.uralys.tribes.entities.dto.MoveConflictDTO;
import com.uralys.tribes.entities.dto.MoveDTO;
import com.uralys.tribes.entities.dto.PlayerDTO;
import com.uralys.tribes.entities.dto.SmithDTO;
import com.uralys.tribes.entities.dto.UnitDTO;
import com.uralys.utils.Utils;

public class EntitiesConverter {

	// -----------------------------------------------------------------------------------//

	public static Move convertMoveDTO(MoveDTO moveDTO, boolean requireLinkedGathering) 
	{
		if (moveDTO == null)
			return null;

		Move move = new Move();

		move.setMoveUID(moveDTO.getMoveUID());
		move.setCaseUID(moveDTO.getCaseUID());
		move.setTimeFrom(moveDTO.getTimeFrom());
		move.setTimeTo(moveDTO.getTimeTo());
		move.setUnitUID(moveDTO.getUnitUID());
		move.setValue(moveDTO.getValue());

		if(requireLinkedGathering){
			try {
				move.setGathering(convertGatheringDTO(moveDTO.getGathering()));
			} catch (Exception e) {
				Utils.print("LOADCASES : move.getGathering DOES NOT EXIST | gathering " + moveDTO.getGatheringUID());
			}
			
		}

		return move;
	}

	// -----------------------------------------------------------------------------------//

	public static Player convertPlayerDTO(PlayerDTO playerDTO, boolean requireFullData) 
	{
		if(playerDTO == null)
			return null;

		Player player = new Player();

		player.setUralysUID(playerDTO.getUralysUID());
		player.setAllyUID(playerDTO.getAllyUID());
		player.setName(playerDTO.getName());
		player.setLastStep(playerDTO.getLastStep());
		player.setNbLands(playerDTO.getNbLands());
		player.setNbArmies(playerDTO.getNbArmies());
		player.setNbCities(playerDTO.getNbCities());
		player.setNbPopulation(playerDTO.getNbPopulation());
		
		if(!requireFullData)
			return player;

		//player.setProfile(playerDTO.getProfile().toString());
			
		// -----------------------------------------------------------------------------------//

		List<City> cities = new ArrayList<City>();
		
		for (CityDTO cityDTO : playerDTO.getCities()) {
			cities.add(convertCityDTO(cityDTO, true));
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
			units.add(convertUnitDTO(unitDTO, true, true, false));
		}

		player.setUnits(units);

		// -----------------------------------------------------------------------------------//

		List<String> readMessages = new ArrayList<String>();
		
		for (String message : playerDTO.getReadMessages()) {
			readMessages.add(message);
		}
		
		player.setReadMessages(readMessages);
		
		// -----------------------------------------------------------------------------------//

		List<String> newMessages = new ArrayList<String>();
		
		for (String message : playerDTO.getNewMessages()) {
			newMessages.add(message);
		}
		
		player.setNewMessages(newMessages);
		
		// -----------------------------------------------------------------------------------//

		return player;

		// -----------------------------------------------------------------------------------//
	}

	// -----------------------------------------------------------------------------------//

	public static Message convertMessageDTO(MessageDTO messageDTO)
	{
		Message message = new Message();
		
		message.setMessageUID(messageDTO.getMessageUID());
		message.setContent(messageDTO.getContent());
		message.setSenderUID(messageDTO.getSenderUID());
		message.setSenderName(messageDTO.getSenderName());
		message.setStatus(messageDTO.getStatus());
		
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
		city.setBeginTime(cityDTO.getBeginTime());
		city.setEndTime(cityDTO.getEndTime());

		if(requireFullData){
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
		}

		return city;
	}

	// -----------------------------------------------------------------------------------//

	/*
	 * requireMoves : pour greffer les moves dans Unit.moves
	 * requireLinkedGatherings : pour coller le Gathering dans chaque Move
	 * requireLinkedMoveFromGathering : pour coller dans les moves le Move supplementaire qui correspond a celui du gathering
	 * si requireLinkedMoveFromGathering est false, on recupere uniquement les moves enregistres par le joueur
	 * si requireLinkedMoveFromGathering est true, on recupere tous les moves prevus pour cette unit, gathering/conflit compris
	 * 
	 * 
	 * requireLinkedGatherings est forcement false si requireMoves est false
	 * 
	 */
	public static Unit convertUnitDTO(UnitDTO unitDTO, boolean requireMoves, boolean requireLinkedMoveFromGathering, boolean requireLinkedGatherings) 
	{
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
		unit.setPlayer(convertPlayerDTO(unitDTO.getPlayer(), false));

		unit.setBeginTime(unitDTO.getBeginTime());
		unit.setEndTime(unitDTO.getEndTime());
		unit.setGatheringUIDExpected(unitDTO.getGatheringUIDExpected());
		unit.setFinalCaseUIDExpected(unitDTO.getFinalCaseUIDExpected());

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
				Move move = convertMoveDTO(moveDTO, requireLinkedGatherings);
				moves.add(move);
			}
			
			if(requireLinkedMoveFromGathering && unit.getGatheringUIDExpected() != null)
			{
				GatheringDTO gathering = null;
				
				try{
					gathering = (GatheringDTO) UniversalDAO.getInstance().getObjectDTO(unit.getGatheringUIDExpected(), GatheringDTO.class);
				}
				catch (Exception e) {
					// ce cas semble survenir pour certaines unites endTime<now qui ont ete en conflit
					// elles ont un getGatheringUIDExpected, mais le gathering a ŽtŽ supprimŽ ?
					// bref comme elles sont perimes de toute facon, on n'a pas besoin du 'moveToAdd' (qui n'existe pas de toute maniere)
				}
				
				if(gathering != null)
				{
					UnitDTO newUnit = (UnitDTO) UniversalDAO.getInstance().getObjectDTO(gathering.getNewUnitUID(), UnitDTO.class);
					
					if(newUnit != null){
						// il arrive qu'il y ait des unit qui nont pas de move...
						// dans ce cas bug : l'armee sera supprimee par flex lors de la prochaine connexion du joueur.
						if(newUnit.getMoves().size() != 0){
							Move moveToAdd = convertMoveDTO(newUnit.getMoves().get(0), requireLinkedGatherings);
							
							int indexOfTheMoveToAdd = 0;
							for(Move moveRecorded : moves){
								indexOfTheMoveToAdd++;
								if(moveRecorded.getTimeTo() == moveToAdd.getTimeFrom()){
									break;
								}
							}
							
							if(indexOfTheMoveToAdd < moves.size())
								moveToAdd.setTimeTo(moves.get(indexOfTheMoveToAdd).getTimeFrom());
							
							moves.add(indexOfTheMoveToAdd, moveToAdd);					
						}
					}
					//else : pas de newUnit pour ce gathering => il y a eu un conflit qui se termine en DRAW : autodestruction des 2 armees
				}
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
		_case.setGroup(caseDTO.getGroupCase());
		_case.setY(caseDTO.getY());
		_case.setType(caseDTO.getType());

		_case.setCity(convertCityDTO(caseDTO.getCity(), false));

		_case.setLandOwner(convertPlayerDTO(caseDTO.getLandOwner(), false));
		_case.setChallenger(convertPlayerDTO(caseDTO.getChallenger(), false));
		_case.setTimeFromChallenging(caseDTO.getTimeFromChallenging());

		// -----------------------------------------------------------------------------------//

		List<Move> moves = new ArrayList<Move>();
		List<String> unitUIDs = new ArrayList<String>();

		for (MoveDTO moveDTO : caseDTO.getMoves()) {
			Move move = convertMoveDTO(moveDTO, true);
			moves.add(move);
			unitUIDs.add(move.getUnitUID());
		}

		_case.setRecordedMoves(moves);

		// -----------------------------------------------------------------------------------//

		List<Unit> units = new ArrayList<Unit>();

		for (UnitDTO unitDTO : UniversalDAO.getInstance().getListDTO(unitUIDs, UnitDTO.class)) {
			units.add(convertUnitDTO(unitDTO, true, false, false));
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

		// -----------------------------------------------------------------------------------//

		List<Unit> units = new ArrayList<Unit>();

		for (UnitDTO unitDTO : conflictDTO.getUnits()) {
			units.add(convertUnitDTO(unitDTO, true, false, false));
		}

		conflict.setUnits(units);

		// ------------------------------------------------------------------------------------//

		return conflict;
	}

	// -----------------------------------------------------------------------------------//

	public static Gathering convertGatheringDTO(GatheringDTO gatheringDTO) {
		if (gatheringDTO == null)
			return null;

		Gathering gathering = new Gathering();

		gathering.setNewUnitUID(gatheringDTO.getNewUnitUID());
		gathering.setAllyUID(gatheringDTO.getAllyUID());
		gathering.setGatheringUID(gatheringDTO.getGatheringUID());
		gathering.setUnitUIDs(gatheringDTO.getUnitUIDs());

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
