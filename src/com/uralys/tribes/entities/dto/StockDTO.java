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

	@Persistent private Integer stockCapacity;

	@Persistent private Integer peopleBuildingStock;
	@Persistent private Long stockBeginTime;
	@Persistent private Long stockEndTime;
	@Persistent private Integer stockNextCapacity;

	@Persistent private Integer smiths;
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
	public void setStockUID(String stockUID) {
		this.stockUID = stockUID;
	}
	public Integer getStockCapacity() {
		return stockCapacity;
	}
	public void setStockCapacity(Integer stockCapacity) {
		this.stockCapacity = stockCapacity;
	}
	public Integer getPeopleBuildingStock() {
		return peopleBuildingStock;
	}
	public void setPeopleBuildingStock(Integer peopleBuildingStock) {
		this.peopleBuildingStock = peopleBuildingStock;
	}
	public Long getStockBeginTime() {
		return stockBeginTime;
	}
	public void setStockBeginTime(Long stockBeginTime) {
		this.stockBeginTime = stockBeginTime;
	}
	public Long getStockEndTime() {
		return stockEndTime;
	}
	public void setStockEndTime(Long stockEndTime) {
		this.stockEndTime = stockEndTime;
	}
	public Integer getStockNextCapacity() {
		return stockNextCapacity;
	}
	public void setStockNextCapacity(Integer stockNextCapacity) {
		this.stockNextCapacity = stockNextCapacity;
	}
	public Integer getSmiths() {
		return smiths;
	}
	public void setSmiths(Integer smiths) {
		this.smiths = smiths;
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
	
	//-----------------------------------------------------------------------------------//


}
