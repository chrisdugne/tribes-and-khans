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
public class CaseDTO {
	
	//-----------------------------------------------------------------------------------//

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String caseUID;
	
	@Persistent private int groupeCase;
	@Persistent private int x;
	@Persistent private int y;
	
	@Persistent private Integer type;
	@Persistent private String cityUID;
	@Persistent private String landOwnerUID;
	
	@Persistent private List<String> moveUIDs = new ArrayList<String>();
	
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
	public String getLandOwnerUID() {
		return landOwnerUID;
	}
	public void setLandOwnerUID(String landOwnerUID) {
		this.landOwnerUID = landOwnerUID;
	}
	public String getCaseUID() {
		return caseUID;
	}
	public void setCaseUID(String caseUID) {
		this.caseUID = caseUID;
	}
	public int getGroupCase() {
		return groupeCase;
	}
	public void setGroupCase(int groupeCase) {
		this.groupeCase = groupeCase;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
	//-----------------------------------------------------------------------------------//
	
	public List<MoveDTO> getMoves() {
		return UniversalDAO.getInstance().getListDTO(moveUIDs, MoveDTO.class);
	}

	public List<String> getMoveUIDs() {
		return moveUIDs;
	}
	public void setMoveUIDs(List<String> moveUIDs) {
		this.moveUIDs = moveUIDs;
	}
	
	//-----------------------------------------------------------------------------------//
	
	public CityDTO getCity() {
		return (CityDTO) UniversalDAO.getInstance().getObjectDTO(cityUID, CityDTO.class);
	}

	public PlayerDTO getLandOwner() {
		return (PlayerDTO) UniversalDAO.getInstance().getObjectDTO(landOwnerUID, PlayerDTO.class);
	}
		
	//-----------------------------------------------------------------------------------//
	
}
