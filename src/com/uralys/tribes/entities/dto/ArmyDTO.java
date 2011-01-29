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
	
	@Persistent private int x;
	@Persistent private int y;

	@Persistent private List<String> equipmentUIDs = new ArrayList<String>();
	
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
	
}
