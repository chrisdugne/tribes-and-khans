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
public class AllyDTO {
 
	//-----------------------------------------------------------------------------------//

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String allyUID;
	
	@Persistent private String name;
	@Persistent private List<String> playerUIDs = new ArrayList<String>();

	@Persistent private Integer nbLands; 
	@Persistent private Integer nbPopulation;
	@Persistent private Integer nbCities;
	@Persistent private Integer nbArmies;

	@Persistent private String profile;

	//-----------------------------------------------------------------------------------//
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAllyUID() {
		return allyUID;
	}

	public void setAllyUID(String allyUID) {
		this.allyUID = allyUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPlayerUIDs() {
		return playerUIDs;
	}

	public void setPlayerUIDs(List<String> playerUIDs) {
		this.playerUIDs = playerUIDs;
	}

	public Integer getNbLands() {
		return nbLands;
	}

	public void setNbLands(Integer nbLands) {
		this.nbLands = nbLands;
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
	
	public List<PlayerDTO> getPlayers() {
		return (List<PlayerDTO>) UniversalDAO.getInstance().getListDTO(playerUIDs, PlayerDTO.class);
	}

	//-----------------------------------------------------------------------------------//
}

