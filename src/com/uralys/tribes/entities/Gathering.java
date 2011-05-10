package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Gathering {

	// -----------------------------------------------------------------------------------//

	public Gathering() {
	}

	public Gathering(String allyUID) {
		this.allyUID = allyUID;
	}

	// -----------------------------------------------------------------------------------//

	private String gatheringUID = "notcreatedyet";
	private String allyUID;
	private String newArmyUID;
	private List<Unit> units = new ArrayList<Unit>();

	// -----------------------------------------------------------------------------------//

	public String getGatheringUID() {
		return gatheringUID;
	}

	public void setGatheringUID(String gatheringUID) {
		this.gatheringUID = gatheringUID;
	}

	public String getNewArmyUID() {
		return newArmyUID;
	}

	public void setNewArmyUID(String newArmyUID) {
		this.newArmyUID = newArmyUID;
	}

	public List<Unit> getUnits() {
		return units;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}

	public String getAllyUID() {
		return allyUID;
	}

	public void setAllyUID(String allyUID) {
		this.allyUID = allyUID;
	}

	// -----------------------------------------------------------------------------------//
	
	public void remove(String unitUID) {
		int indexToRemove = -1;
		for(Unit unitInGathering : units){
			indexToRemove ++;
			if(unitInGathering.getUnitUID().equals(unitUID)){
				break;
			}
		}
		
		units.remove(indexToRemove);
	}

}
