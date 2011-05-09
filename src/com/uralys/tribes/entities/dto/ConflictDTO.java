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

	private long timeFrom;
	private long timeTo;

	private String caseUID;
	private List<String> gatheringUIDs = new ArrayList<String>();

	// -----------------------------------------------------------------------------------//

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(long timeFrom) {
		this.timeFrom = timeFrom;
	}

	public long getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(long timeTo) {
		this.timeTo = timeTo;
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

	public List<String> getGatheringUIDs() {
		return gatheringUIDs;
	}

	public void setGatheringUIDs(List<String> gatheringUIDs) {
		this.gatheringUIDs = gatheringUIDs;
	}

	// -----------------------------------------------------------------------------------//

	public List<GatheringDTO> getGatherings() {
		return UniversalDAO.getInstance().getListDTO(gatheringUIDs,
				GatheringDTO.class);
	}

}
