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
public class ProfilDTO {


    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String uralysUID;

	@Persistent	private List<String> playerUIDs = new ArrayList<String>();
	
	//-----------------------------------------------------------------------------------//

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getUralysUID() {
		return uralysUID;
	}
	public void setUralysUID(String uralysUID) {
		this.uralysUID = uralysUID;
	}

	//-----------------------------------------------------------------------------------//
	
	public List<PlayerDTO> getPlayers() {
		return UniversalDAO.getInstance().getListDTO(playerUIDs, PlayerDTO.class);
	}
	public List<String> getPlayerUIDs() {
		return playerUIDs;
	}
	public void setPlayerUIDs(List<String> playerUIDs) {
		this.playerUIDs = playerUIDs;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
