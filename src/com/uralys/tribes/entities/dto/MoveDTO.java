package com.uralys.tribes.entities.dto;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.uralys.tribes.dao.impl.UniversalDAO;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class MoveDTO {

	//-----------------------------------------------------------------------------------//

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String moveUID;

	@Persistent private String cellUID;
	@Persistent private long timeFrom;
	@Persistent private long timeTo;
	@Persistent private String unitUID;
	@Persistent private String nextMoveUID;
	@Persistent private boolean hidden;

	//-----------------------------------------------------------------------------------//

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getMoveUID() {
		return moveUID;
	}
	public void setMoveUID(String moveUID) {
		this.moveUID = moveUID;
	}
	public String getCellUID() {
		return cellUID;
	}
	public void setCellUID(String cellUID) {
		this.cellUID = cellUID;
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
	public String getUnitUID() {
		return unitUID;
	}
	public void setUnitUID(String unitUID) {
		this.unitUID = unitUID;
	}
	public String getNextMoveUID() {
		return nextMoveUID;
	}
	public void setNextMoveUID(String nextMoveUID) {
		this.nextMoveUID = nextMoveUID;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	//-----------------------------------------------------------------------------------//
	
	public UnitDTO getUnit() {
		return (UnitDTO) UniversalDAO.getInstance().getObjectDTO(unitUID, UnitDTO.class);
	}
	
	//-----------------------------------------------------------------------------------//
	
	public MoveDTO getNextMove() 
	{
		if(nextMoveUID == null)
			return null;
		
		return (MoveDTO) UniversalDAO.getInstance().getObjectDTO(nextMoveUID, MoveDTO.class);
	}

	//-----------------------------------------------------------------------------------//
}
