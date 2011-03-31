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
public class ArmyDTO {
	 
	//-----------------------------------------------------------------------------------//

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String armyUID;
	
	@Persistent private int size;
	@Persistent private int speed;

	@Persistent private int value;
	
	@Persistent private int wheat;
	@Persistent private int wood;
	@Persistent private int iron;
	@Persistent private int gold;
	
	@Persistent private int x;
	@Persistent private int y;

	@Persistent private List<String> equipmentUIDs = new ArrayList<String>();
	@Persistent private List<String> moveUIDs = new ArrayList<String>();
	
	//-----------------------------------------------------------------------------------//

	public String getKey() { 
		return key;
	}
	public void setKey(String key) {
		this.key = key; 
	}
	
	public String getArmyUID() {
		return armyUID;
	}
	public void setArmyUID(String armyUID) {
		this.armyUID = armyUID;
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
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
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
