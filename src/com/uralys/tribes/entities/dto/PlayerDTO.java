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
public class PlayerDTO {


    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String playerUID;

	@Persistent private String name;
	@Persistent private String gameUID;
	@Persistent private String gameName;
	@Persistent private List<String> cityUIDs = new ArrayList<String>();
	@Persistent private List<String> armyUIDs = new ArrayList<String>();
	@Persistent private List<String> merchantUIDs = new ArrayList<String>();
	@Persistent private List<String> moveUIDs = new ArrayList<String>();
	
	//-----------------------------------------------------------------------------------//

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getPlayerUID() {
		return playerUID;
	}
	public void setPlayerUID(String playerUID) {
		this.playerUID = playerUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGameUID() {
		return gameUID;
	}
	public void setGameUID(String gameUID) {
		this.gameUID = gameUID;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public List<String> getMoveUIDs() {
		return moveUIDs;
	}
	public void setMoveUIDs(List<String> moves) {
		this.moveUIDs = moves;
	}
	public List<String> getCityUIDs() {
		return cityUIDs;
	}
	public void setCityUIDs(List<String> cityUIDs) {
		this.cityUIDs = cityUIDs;
	}
	public List<String> getArmyUIDs() {
		return armyUIDs;
	}
	public void setArmyUIDs(List<String> armyUIDs) {
		this.armyUIDs = armyUIDs;
	}
	public List<String> getMerchantUIDs() {
		return merchantUIDs;
	}
	public void setMerchantUIDs(List<String> merchantUIDs) {
		this.merchantUIDs = merchantUIDs;
	}
	
	//-----------------------------------------------------------------------------------//

	public List<MoveDTO> getMoves() {
		return UniversalDAO.getInstance().getListDTO(moveUIDs, MoveDTO.class);		
	}
	
	public List<CityDTO> getCities() {
		return UniversalDAO.getInstance().getListDTO(cityUIDs, CityDTO.class);		
	}
	
	public List<ArmyDTO> getArmies() {
		return UniversalDAO.getInstance().getListDTO(armyUIDs, ArmyDTO.class);		
	}
	
	public List<MerchantDTO> getMerchants() {
		return UniversalDAO.getInstance().getListDTO(merchantUIDs, MerchantDTO.class);		
	}
	
	//-----------------------------------------------------------------------------------//
	
}

