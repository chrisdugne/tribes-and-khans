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

	// -----------------------------------------------------------------------------------//

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	protected String key;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.pk-name", value = "true")
	private String conflictUID;

	private String caseUID;
	private List<String> unitUIDs = new ArrayList<String>();

	// -----------------------------------------------------------------------------------//

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

	public String getCaseUID() {
		return caseUID;
	}

	public void setCaseUID(String caseUID) {
		this.caseUID = caseUID;
	}

	public List<String> getUnitUIDs() {
		return unitUIDs;
	}

	public void setUnitUIDs(List<String> unitUIDs) {
		this.unitUIDs = unitUIDs;
	}

	// -----------------------------------------------------------------------------------//

	public List<UnitDTO> getUnits() {
		return UniversalDAO.getInstance().getListDTO(unitUIDs, UnitDTO.class);
	}

}
