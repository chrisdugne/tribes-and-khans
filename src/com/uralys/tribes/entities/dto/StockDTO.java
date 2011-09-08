package com.uralys.tribes.entities.dto;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class StockDTO {
	
	//-----------------------------------------------------------------------------------//

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String stockUID;

	@Persistent private long stockCapacity;

	@Persistent private int peopleBuildingStock;
	@Persistent private long stockBeginTime;
	@Persistent private long stockEndTime;
	@Persistent private long stockNextCapacity;

	@Persistent private Integer itemsBeingBuilt;
	@Persistent private Long itemsBeingBuiltBeginTime;
	@Persistent private Long itemsBeingBuiltEndTime;
	
	//-----------------------------------------------------------------------------------//

	public String getKey() { 
		return key;
	}
	public void setKey(String key) {
		this.key = key; 
	}
	public String getStockUID() {
		return stockUID;
	}
	public void setStockUID(String equimentUID) {
		this.stockUID = equimentUID;
	}
	public long getStockCapacity() {
		return stockCapacity;
	}
	public void setStockCapacity(long stockCapacity) {
		this.stockCapacity = stockCapacity;
	}
	public int getPeopleBuildingStock() {
		return peopleBuildingStock;
	}
	public void setPeopleBuildingStock(int peopleCreatingBuilding) {
		this.peopleBuildingStock = peopleCreatingBuilding;
	}
	public long getStockBeginTime() {
		return stockBeginTime;
	}
	public void setStockBeginTime(long stockBeginTime) {
		this.stockBeginTime = stockBeginTime;
	}
	public long getStockEndTime() {
		return stockEndTime;
	}
	public void setStockEndTime(long stockEndTime) {
		this.stockEndTime = stockEndTime;
	}
	public long getStockNextCapacity() {
		return stockNextCapacity;
	}
	public void setStockNextCapacity(long stockNextCapacity) {
		this.stockNextCapacity = stockNextCapacity;
	}
	public Integer getItemsBeingBuilt() {
		return itemsBeingBuilt;
	}
	public void setItemsBeingBuilt(Integer itemsBeingBuilt) {
		this.itemsBeingBuilt = itemsBeingBuilt;
	}
	public Long getItemsBeingBuiltBeginTime() {
		return itemsBeingBuiltBeginTime;
	}
	public void setItemsBeingBuiltBeginTime(Long itemsBeingBuiltBeginTime) {
		this.itemsBeingBuiltBeginTime = itemsBeingBuiltBeginTime;
	}
	public Long getItemsBeingBuiltEndTime() {
		return itemsBeingBuiltEndTime;
	}
	public void setItemsBeingBuiltEndTime(Long itemsBeingBuiltEndTime) {
		this.itemsBeingBuiltEndTime = itemsBeingBuiltEndTime;
	}
}
