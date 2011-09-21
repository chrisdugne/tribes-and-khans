package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class ObjectsAltered {

	public ObjectsAltered(){};
	
	//------------------------------------------------------------------------------------------//

	private List<Cell> casesAltered = new ArrayList<Cell>();
	private List<Unit> unitsAltered = new ArrayList<Unit>();
	private String cityUID;

	//------------------------------------------------------------------------------------------//

	public List<Cell> getCasesAltered() {
		return casesAltered;
	}
	
	public List<Unit> getUnitsAltered() {
		return unitsAltered;
	}

	public void setUnitsAltered(List<Unit> unitsAltered) {
		this.unitsAltered = unitsAltered;
	}

	public void setCasesAltered(List<Cell> casesAltered) {
		this.casesAltered = casesAltered;
	}
	
	public String getCityUID() {
		return cityUID;
	}
	
	public void setCityUID(String cityBuildUID) {
		this.cityUID = cityBuildUID;
	}
	
	
	//------------------------------------------------------------------------------------------//
	
	public void addCellAltered(Cell cell){

		int indexAlreadyrecorded = -1;
		for(Cell cellRecorded : casesAltered)
		{
			if(cellRecorded.getCellUID().equals(cell))
			{
				indexAlreadyrecorded = casesAltered.indexOf(cellRecorded);
				break;
			}
		}
		
		if(indexAlreadyrecorded >= 0)
			casesAltered.remove(indexAlreadyrecorded);
		
		casesAltered.add(cell);
	}
	
	//------------------------------------------------------------------------------------------//
	
	public void addUnitAltered(Unit unit){
		
		int indexAlreadyrecorded = -1;
		for(Unit unitRecorded : unitsAltered)
		{
			if(unitRecorded.getUnitUID().equals(unit))
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
