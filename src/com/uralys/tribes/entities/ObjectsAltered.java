package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class ObjectsAltered {

	public ObjectsAltered(){};
	
	//------------------------------------------------------------------------------------------//

	private Cell cellDeparture;
	private List<Unit> unitsAltered = new ArrayList<Unit>();
	private String cityUID;

	//------------------------------------------------------------------------------------------//

	public List<Unit> getUnitsAltered() {
		return unitsAltered;
	}

	public void setUnitsAltered(List<Unit> unitsAltered) {
		this.unitsAltered = unitsAltered;
	}

	public String getCityUID() {
		return cityUID;
	}
	
	public void setCityUID(String cityBuildUID) {
		this.cityUID = cityBuildUID;
	}
	
	public Cell getCellDeparture() {
		return cellDeparture;
	}

	public void setCellDeparture(Cell cellDeparture) {
		this.cellDeparture = cellDeparture;
	}
	
	//------------------------------------------------------------------------------------------//
	
	public void addUnitAltered(Unit unit){
		
		int indexAlreadyrecorded = -1;
		for(Unit unitRecorded : unitsAltered)
		{
			if(unitRecorded.getUnitUID().equals(unit.getUnitUID()))
			{
				indexAlreadyrecorded = unitsAltered.indexOf(unitRecorded);
				break;
			}
		}
		
		if(indexAlreadyrecorded >= 0)
			unitsAltered.remove(indexAlreadyrecorded);
		
		unitsAltered.add(unit);
	}
}
