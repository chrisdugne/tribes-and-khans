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
	private List<String> unitUIDs = new ArrayList<String>();

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

	public List<String> getUnitUIDs() {
		return unitUIDs;
	}

	public void setUnitUIDs(List<String> unitUIDs) {
		this.unitUIDs = unitUIDs;
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
		for(String unitUIDInGathering : unitUIDs){
			indexToRemove ++;
			if(unitUIDInGathering.equals(unitUID)){
				break;
			}
		}
		
		unitUIDs.remove(indexToRemove);
	}

}
