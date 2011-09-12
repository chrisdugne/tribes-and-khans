package com.uralys.tribes.entities;

public class Stock {

	//-----------------------------------------------------------------------------------//

	private String stockUID;

	private Integer stockCapacity;

	private Integer peopleBuildingStock;
	private Long stockBeginTime;
	private Long stockEndTime;
	private Integer stockNextCapacity;

	private Integer smiths;
	private Integer itemsBeingBuilt;
	private Long itemsBeingBuiltBeginTime;
	private Long itemsBeingBuiltEndTime;
	
	//-----------------------------------------------------------------------------------//
	
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
	public Integer getSmiths() {
		return smiths;
	}
	public void setSmiths(Integer smiths) {
		this.smiths = smiths;
	}
}
