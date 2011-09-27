package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Unit {

	//-----------------------------------------------------------------------------------//
	
	public final static int ARMY = 1;
	public final static int CARAVAN = 2;
	
	//-----------------------------------------------------------------------------------//
	
	public Unit(){
		player = new Player();
	}
	
	//-----------------------------------------------------------------------------------//
	
	private String unitUID;
	private int type;
	private Player player;

	private int size;
	private int speed;

	private int wheat;
	private int wood;
	private int iron;
	private int gold;
	
	private int bows;
	private int swords;
	private int armors;
	
	private long beginTime;
	private long endTime;
	
	private String cellUIDExpectedForLand;
	private String unitMetUID;
	private String unitNextUID;
	private String messageUID;

	private List<Move> moves = new ArrayList<Move>();
	
	//-----------------------------------------------------------------------------------//
	
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public String getUnitUID() {
		return unitUID;
	}

	public void setUnitUID(String unitUID) {
		this.unitUID = unitUID;
	}

	public List<Move> getMoves() {
		return moves;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getCellUIDExpectedForLand() {
		return cellUIDExpectedForLand;
	}

	public void setCellUIDExpectedForLand(String cellUIDExpectedForLand) {
		this.cellUIDExpectedForLand = cellUIDExpectedForLand;
	}

	public String getUnitMetUID() {
		return unitMetUID;
	}

	public void setUnitMetUID(String unitMetUID) {
		this.unitMetUID = unitMetUID;
	}

	public String getUnitNextUID() {
		return unitNextUID;
	}

	public void setUnitNextUID(String unitNextUID) {
		this.unitNextUID = unitNextUID;
	}

	public String getMessageUID() {
		return messageUID;
	}
	
	public void setMessageUID(String messageUID) {
		this.messageUID = messageUID;
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

	public boolean equals(Unit unit){
		return unit.getUnitUID().equals(unitUID);
	}

}
