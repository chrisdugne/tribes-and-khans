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
	private String uralysUID;
	
	@Persistent private String name;
	@Persistent private int nbConnections;
	
	@Persistent private String allyUID; 
	@Persistent private int nbLands; 
	@Persistent private boolean musicOn; 
	@Persistent private List<String> cityUIDs = new ArrayList<String>();
	@Persistent private List<String> unitUIDs = new ArrayList<String>();
	@Persistent private List<String> meetingsUIDs = new ArrayList<String>();

	@Persistent private Long lastStep;
	
	//-----------------------------------------------------------------------------------//

	public String getUralysUID() {
		return uralysUID;
	}
	public void setUralysUID(String uralysUID) {
		this.uralysUID = uralysUID;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public boolean isMusicOn() {
		return musicOn;
	}
	public void setMusicOn(boolean musicOn) {
		this.musicOn = musicOn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getCityUIDs() {
		return cityUIDs;
	}
	public void setCityUIDs(List<String> cityUIDs) {
		this.cityUIDs = cityUIDs;
	}
	public List<String> getMeetingsUIDs() {
		return meetingsUIDs;
	}
	public void setMeetingsUIDs(List<String> meetingsUIDs) {
		this.meetingsUIDs = meetingsUIDs;
	}
	public int getNbLands() {
		return nbLands;
	}
	public void setNbLands(int nbLands) {
		this.nbLands = nbLands;
	}
	public String getAllyUID() {
		return allyUID;
	}
	public void setAllyUID(String allyUID) {
		this.allyUID = allyUID;
	}
	public List<String> getUnitUIDs() {
		return unitUIDs;
	}
	public void setUnitUIDs(List<String> unitUIDs) {
		this.unitUIDs = unitUIDs;
	}
	public int getNbConnections() {
		return nbConnections;
	}
	public void setNbConnections(int nbConnections) {
		this.nbConnections = nbConnections;
	}
	public Long getLastStep() {
		return lastStep;
	}
	public void setLastStep(Long lastStep) {
		this.lastStep = lastStep;
	}
	
	//-----------------------------------------------------------------------------------//
	
	public List<UnitDTO> getUnits() {
		return UniversalDAO.getInstance().getListDTO(unitUIDs, UnitDTO.class);		
	}
	
	public List<CityDTO> getCities() {
		return UniversalDAO.getInstance().getListDTO(cityUIDs, CityDTO.class);		
	}
	
	public List<ConflictDTO> getMeetings() {
		return UniversalDAO.getInstance().getListDTO(meetingsUIDs, ConflictDTO.class);		
	}
	
	//-----------------------------------------------------------------------------------//
	
}

