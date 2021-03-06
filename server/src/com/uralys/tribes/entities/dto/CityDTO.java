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

	@Persistent private String ownerUID;


	@Persistent private int wheat;
	@Persistent private int peopleCreatingWheat;
	@Persistent private int wood;
	@Persistent private int peopleCreatingWood;
	@Persistent private int iron;
	@Persistent private int peopleCreatingIron;

	@Persistent private int bows;
	@Persistent private int swords;
	@Persistent private int armors;

	@Persistent private Integer gold;

	@Persistent private int x;
	@Persistent private int y;
	
	@Persistent private List<String> nextOwnerUIDs = new ArrayList<String>();
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
	public String getOwnerUID() {
		return ownerUID;
	}
	public void setOwnerUID(String ownerUID) {
		this.ownerUID = ownerUID;
	}
	public int getBows() {
		return bows;
	}
	public void setBows(int bows) {
		this.bows = bows;
	}
	public int getSwords() {
		return swords;
	}
	public void setSwords(int swords) {
		this.swords = swords;
	}
	public int getArmors() {
		return armors;
	}
	public void setArmors(int armors) {
		this.armors = armors;
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
	public List<String> getNextOwnerUIDs() {
		return nextOwnerUIDs;
	}
	public void setNextOwnerUIDs(List<String> nextOwnerUIDs) {
		this.nextOwnerUIDs = nextOwnerUIDs;
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
