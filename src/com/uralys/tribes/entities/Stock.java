package com.uralys.tribes.entities;

public class Stock {

	//-----------------------------------------------------------------------------------//

	private String stockUID;

	private long stockCapacity;

	private int peopleBuildingStock;
	private long stockBeginTime;
	private long stockEndTime;
	private long stockNextCapacity;
	
	//-----------------------------------------------------------------------------------//
	
	public String getStockUID() {
		return stockUID;
	}
	public void setStockUID(String stockUID) {
		this.stockUID = stockUID;
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
	public void setPeopleBuildingStock(int peopleBuildingStock) {
		this.peopleBuildingStock = peopleBuildingStock;
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

	
}
