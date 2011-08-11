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
	
	@Persistent private long beginTime;
	@Persistent private long endTime;

	@Persistent private Long timeToChangeOwner;
	@Persistent private String nextOwnerUID;
	@Persistent private String ownerUID;
	@Persistent private Integer populationLost;
	
	@Persistent private int wheat;
	@Persistent private int peopleCreatingWheat;
	@Persistent private int wood;
	@Persistent private int peopleCreatingWood;
	@Persistent private int iron;
	@Persistent private int peopleCreatingIron;

	@Persistent private Integer gold;

	@Persistent private int x;
	@Persistent private int y;
	
	@Persistent private List<String> smithUIDs = new ArrayList<String>();
	@Persistent private List<String> equipmentStockUIDs = new ArrayList<String>();
	@Persistent private List<String> stockUIDs = new ArrayList<String>();
	
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
	public Integer getGold() {
		return gold;
	}
	public void setGold(Integer gold) {
		this.gold = gold;
	}
	public Long getTimeToChangeOwner() {
		return timeToChangeOwner;
	}
	public void setTimeToChangeOwner(Long timeToChangeOwner) {
		this.timeToChangeOwner = timeToChangeOwner;
	}
	public String getNextOwnerUID() {
		return nextOwnerUID;
	}
	public void setNextOwnerUID(String nextOwnerUID) {
		this.nextOwnerUID = nextOwnerUID;
	}
	public String getOwnerUID() {
		return ownerUID;
	}
	public void setOwnerUID(String ownerUID) {
		this.ownerUID = ownerUID;
	}
	public Integer getPopulationLost() {
		return populationLost;
	}
	public void setPopulationLost(Integer populationLost) {
		this.populationLost = populationLost;
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
	
	public List<StockDTO> getStocks() {
		return UniversalDAO.getInstance().getListDTO(stockUIDs, StockDTO.class);
	}
	public List<String> getStockUIDs() {
		return stockUIDs;
	}
	public void setStockUIDs(List<String> stockUIDs) {
		this.stockUIDs = stockUIDs;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
