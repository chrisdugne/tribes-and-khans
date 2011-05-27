package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class DataContainer4UnitSaved {

	public DataContainer4UnitSaved(){};
	
	//------------------------------------------------------------------------------------------//

	private List<Case> casesAltered = new ArrayList<Case>();
	private List<Unit> unitsAltered = new ArrayList<Unit>();
	private String cityUID;

	//------------------------------------------------------------------------------------------//

	public List<Case> getCasesAltered() {
		return casesAltered;
	}
	
	public List<Unit> getUnitsAltered() {
		return unitsAltered;
	}

	public void setUnitsAltered(List<Unit> unitsAltered) {
		this.unitsAltered = unitsAltered;
	}

	public void setCasesAltered(List<Case> casesAltered) {
		this.casesAltered = casesAltered;
	}
	
	public String getCityUID() {
		return cityUID;
	}
	
	public void setCityUID(String cityBuildUID) {
		this.cityUID = cityBuildUID;
	}
	
	
	//------------------------------------------------------------------------------------------//
	
	public void addCaseAltered(Case caseToRecord){

		int indexAlreadyrecorded = -1;
		for(Case caseRecorded : casesAltered)
		{
			if(caseRecorded.getCaseUID().equals(caseToRecord))
			{
				indexAlreadyrecorded = casesAltered.indexOf(caseRecorded);
				break;
			}
		}
		
		if(indexAlreadyrecorded >= 0)
			casesAltered.remove(indexAlreadyrecorded);
		
		casesAltered.add(caseToRecord);
	}
	
	//------------------------------------------------------------------------------------------//
	
	public void addUnitAltered(Unit unitToRecord){
		
		int indexAlreadyrecorded = -1;
		for(Unit unitRecorded : unitsAltered)
		{
			if(unitRecorded.getUnitUID().equals(unitToRecord))
			{
				indexAlreadyrecorded = unitsAltered.indexOf(unitRecorded);
				break;
			}
		}
		
		if(indexAlreadyrecorded >= 0)
			unitsAltered.remove(indexAlreadyrecorded);
		
		unitsAltered.add(unitToRecord);
	}
}
