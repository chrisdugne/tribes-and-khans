package com.uralys.tribes.entities.dto;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.uralys.tribes.dao.impl.UniversalDAO;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UnitDTO {
	 
	//-----------------------------------------------------------------------------------//

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String unitUID;
	
	@Persistent private int value;
	
	@Persistent private long beginTime;
	@Persistent private long endTime;

	@Persistent private String gatheringUIDExpected;
	@Persistent private String conflictUIDExpected;

	@Persistent private int type;
	@Persistent private String playerUID;
	
	@Persistent private int size;
	@Persistent private int speed;
	
	@Persistent private int wheat;
	@Persistent private int wood;
	@Persistent private int iron;
	@Persistent private int gold;
	
	@Persistent private List<String> equipmentUIDs = new ArrayList<String>();
	@Persistent private List<String> moveUIDs = new ArrayList<String>();
	
	//-----------------------------------------------------------------------------------//

	public String getKey() { 
		return key;
	}
	public void setKey(String key) {
		this.key = key; 
	}
	
	public String getUnitUID() {
		return unitUID;
	}
	public void setUnitUID(String armyUID) {
		this.unitUID = armyUID;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) { 
		this.value = value;
	}
	public int getWheat() {
		return wheat;
	}
	public void setWheat(int wheat) {
		this.wheat = wheat;
	}
	public int getWood() {
		return wood;
	}
	public void setWood(int wood) {
		this.wood = wood;
	}
	public int getIron() {
		return iron;
	}
	public void setIron(int iron) {
		this.iron = iron;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getGatheringUIDExpected() {
		return gatheringUIDExpected;
	}
	public void setGatheringUIDExpected(String gatheringUIDExpected) {
		this.gatheringUIDExpected = gatheringUIDExpected;
	}
	public String getConflictUIDExpected() {
		return conflictUIDExpected;
	}
	public void setConflictUIDExpected(String conflictUIDExpected) {
		this.conflictUIDExpected = conflictUIDExpected;
	}
	public String getPlayerUID() {
		return playerUID;
	}
	public void setPlayerUID(String playerUID) {
		this.playerUID = playerUID;
	}

	//-----------------------------------------------------------------------------------//

	public List<EquipmentDTO> getEquipments() {
		return UniversalDAO.getInstance().getListDTO(equipmentUIDs, EquipmentDTO.class);
	}
	public List<String> getEquipmentUIDs() {
		return equipmentUIDs;
	}
	public void setEquipmentUIDs(List<String> equipmentUIDs) {
		this.equipmentUIDs = equipmentUIDs;
	}	

	//-----------------------------------------------------------------------------------//
	
	
	public List<MoveDTO> getMoves() {
		return UniversalDAO.getInstance().getListDTO(moveUIDs, MoveDTO.class);
	}
	public List<String> getMoveUIDs() {
		return moveUIDs;
	}
	public void setMoveUIDs(List<String> moves) {
		this.moveUIDs = moves;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
