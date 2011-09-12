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
 
	//-----------------------------------------------------------------------------------//

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String uralysUID;
	
	@Persistent private String name;
	@Persistent private String allyUID; 

	@Persistent private Integer nbConnections;
	@Persistent private boolean musicOn; 

	@Persistent private List<String> cityUIDs = new ArrayList<String>();
	@Persistent private List<String> unitUIDs = new ArrayList<String>();
	@Persistent private List<String> cityBeingOwnedUIDs = new ArrayList<String>();
	@Persistent private List<String> messageUIDs = new ArrayList<String>();
	@Persistent private List<String> meetingsUIDs = new ArrayList<String>();

	@Persistent private Long lastStep;

	@Persistent private Integer nbLands; 
	@Persistent private Integer nbPopulation;
	@Persistent private Integer nbCities;
	@Persistent private Integer nbArmies;

	@Persistent private String profile;
	
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
	public List<String> getCityBeingOwnedUIDs() {
		return cityBeingOwnedUIDs;
	}
	public void setCityBeingOwnedUIDs(List<String> cityBeingOwnedUIDs) {
		this.cityBeingOwnedUIDs = cityBeingOwnedUIDs;
	}
	public List<String> getMeetingsUIDs() {
		return meetingsUIDs;
	}
	public void setMeetingsUIDs(List<String> meetingsUIDs) {
		this.meetingsUIDs = meetingsUIDs;
	}
	public Integer getNbLands() {
		return nbLands;
	}
	public void setNbLands(Integer nbLands) {
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
	public Integer getNbConnections() {
		return nbConnections;
	}
	public void setNbConnections(Integer nbConnections) {
		this.nbConnections = nbConnections;
	}
	public Long getLastStep() {
		return lastStep;
	}
	public void setLastStep(Long lastStep) {
		this.lastStep = lastStep;
	}
	public List<String> getMessageUIDs() {
		return messageUIDs;
	}
	public void setMessageUIDs(List<String> messages) {
		this.messageUIDs = messages;
	}
	public Integer getNbPopulation() {
		return nbPopulation;
	}
	public void setNbPopulation(Integer nbPopulation) {
		this.nbPopulation = nbPopulation;
	}
	public Integer getNbCities() {
		return nbCities;
	}
	public void setNbCities(Integer nbCities) {
		this.nbCities = nbCities;
	}
	public Integer getNbArmies() {
		return nbArmies;
	}
	public void setNbArmies(Integer nbArmies) {
		this.nbArmies = nbArmies;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	//-----------------------------------------------------------------------------------//
	
	public List<MessageDTO> getMessages() {
		return UniversalDAO.getInstance().getListDTO(messageUIDs, MessageDTO.class);		
	}
	
	public List<UnitDTO> getUnits() {
		return UniversalDAO.getInstance().getListDTO(unitUIDs, UnitDTO.class);		
	}
	
	public List<CityDTO> getCities() {
		return UniversalDAO.getInstance().getListDTO(cityUIDs, CityDTO.class);		
	}

	public AllyDTO getAlly() {
		if(allyUID.equals(uralysUID))
			return null;
		else
			return (AllyDTO) UniversalDAO.getInstance().getObjectDTO(allyUID, AllyDTO.class);		
	}
	
	//-----------------------------------------------------------------------------------//
	
}

