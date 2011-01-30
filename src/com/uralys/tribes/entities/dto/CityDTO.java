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
public class CityDTO {
	
	//-----------------------------------------------------------------------------------//

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String cityUID;
	
	@Persistent private String name;
	@Persistent private int population;
	
	@Persistent private int wheat;
	@Persistent private int peopleCreatingWheat;
	@Persistent private int wood;
	@Persistent private int peopleCreatingWood;
	@Persistent private int iron;
	@Persistent private int peopleCreatingIron;
	
	@Persistent private int gold;

	@Persistent private int x;
	@Persistent private int y;
	
	@Persistent private List<String> smithUIDs = new ArrayList<String>();
	@Persistent private List<String> equipmentStockUIDs = new ArrayList<String>();
	
	//-----------------------------------------------------------------------------------//

	public String getKey() { 
		return key;
	}
	public void setKey(String key) {
		this.key = key; 
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
	
	public String getCityUID() {
		return cityUID;
	}
	public void setCityUID(String cityUID) {
		this.cityUID = cityUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPopulation() {
		return population;
	}
	public void setPopulation(int population) {
		this.population = population;
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
	public int getPeopleCreatingWheat() {
		return peopleCreatingWheat;
	}
	public void setPeopleCreatingWheat(int peopleCreatingWheat) {
		this.peopleCreatingWheat = peopleCreatingWheat;
	}
	public int getPeopleCreatingWood() {
		return peopleCreatingWood;
	}
	public void setPeopleCreatingWood(int peopleCreatingWood) {
		this.peopleCreatingWood = peopleCreatingWood;
	}
	public int getPeopleCreatingIron() {
		return peopleCreatingIron;
	}
	public void setPeopleCreatingIron(int peopleCreatingIron) {
		this.peopleCreatingIron = peopleCreatingIron; 
	}
	
	//-----------------------------------------------------------------------------------//
	
	public List<SmithDTO> getSmiths() {
		return UniversalDAO.getInstance().getListDTO(smithUIDs, SmithDTO.class);
	}
	public List<String> getSmithUIDs() {
		return smithUIDs;
	}
	public void setSmithUIDs(List<String> smithUIDs) {
		this.smithUIDs = smithUIDs;
	}
	
	//-----------------------------------------------------------------------------------//
	
	public List<EquipmentDTO> getEquipmentStock() {
		return UniversalDAO.getInstance().getListDTO(equipmentStockUIDs, EquipmentDTO.class);
	}
	public List<String> getEquipmentStockUIDs() {
		return equipmentStockUIDs;
	}
	public void setEquipmentStockUIDs(List<String> equipmentStockUIDs) {
		this.equipmentStockUIDs = equipmentStockUIDs;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
