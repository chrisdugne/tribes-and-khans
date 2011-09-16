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
public class UnitDTO {
	 
	//-----------------------------------------------------------------------------------//

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String unitUID;
	
	@Persistent private long beginTime;
	@Persistent private long endTime;

	@Persistent private int wheat;
	@Persistent private int wood;
	@Persistent private int iron;
	@Persistent private int gold;
	
	@Persistent private int bows;
	@Persistent private int swords;
	@Persistent private int armors;

	// la case en fin de chemin qui est sera peut etre colonisee
	@Persistent private String caseUIDExpectedForLand;

	// l'armee avec laquelle il y aura soit un conflit soit un gathering, si elle existe sur le chemin
	@Persistent private String unitMetUID;

	// l'armee qui va etre crŽŽe suite ˆ ce croisement, si elle existe.
	@Persistent private String unitNextUID;

	@Persistent private int type;
	@Persistent private int speed;
	@Persistent private int size;
	@Persistent private String playerUID;
	
	@Persistent private List<String> moveUIDs = new ArrayList<String>();
	
	//-----------------------------------------------------------------------------------//

	public String getKey() { 
		return key;
	}
	public void setKey(String key) {
		this.key = key; 
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
	public String getUnitUID() {
		return unitUID;
	}
	public void setUnitUID(String armyUID) {
		this.unitUID = armyUID;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	public String getPlayerUID() {
		return playerUID;
	}
	public void setPlayerUID(String playerUID) {
		this.playerUID = playerUID;
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
	public String getCaseUIDExpectedForLand() {
		return caseUIDExpectedForLand;
	}
	public void setCaseUIDExpectedForLand(String caseUIDExpectedForLand) {
		this.caseUIDExpectedForLand = caseUIDExpectedForLand;
	}
	
	//-----------------------------------------------------------------------------------//
	
	public PlayerDTO getPlayer() {
		return (PlayerDTO) UniversalDAO.getInstance().getObjectDTO(playerUID, PlayerDTO.class);
	}
	
	//-----------------------------------------------------------------------------------//
	
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
