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
public class ConflictDTO {
	
	//-----------------------------------------------------------------------------------//

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String conflictUID;
	
	@Persistent private List<String> moveAlliesUIDs = new ArrayList<String>();
	@Persistent private List<String> moveEnnemiesUIDs = new ArrayList<String>();
	
	@Persistent private Integer x;
	@Persistent private Integer y;

	@Persistent private int status;
	@Persistent private String report;
	
	//-----------------------------------------------------------------------------------//

	public String getKey() { 
		return key;
	}
	public void setKey(String key) {
		this.key = key; 
	}
	public String getConflictUID() {
		return conflictUID;
	}
	public void setConflictUID(String conflictUID) {
		this.conflictUID = conflictUID;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	public List<String> getMoveAlliesUIDs() {
		return moveAlliesUIDs;
	}
	public void setMoveAlliesUIDs(List<String> moveAlliesUIDs) {
		this.moveAlliesUIDs = moveAlliesUIDs;
	}
	public List<String> getMoveEnnemiesUIDs() {
		return moveEnnemiesUIDs;
	}
	public void setMoveEnnemiesUIDs(List<String> moveEnnemiesUIDs) {
		this.moveEnnemiesUIDs = moveEnnemiesUIDs;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	
	
	//-----------------------------------------------------------------------------------//
	
	public List<MoveConflictDTO> getMoveAllies() {
		return UniversalDAO.getInstance().getListDTO(moveAlliesUIDs, MoveConflictDTO.class);		
	}

	public List<MoveConflictDTO> getMoveEnnemies() {
		return UniversalDAO.getInstance().getListDTO(moveEnnemiesUIDs, MoveConflictDTO.class);		
	}
	
}
