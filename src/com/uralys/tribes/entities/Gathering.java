package com.uralys.tribes.entities;

import java.util.ArrayList;
import java.util.List;

public class Gathering {

	//-----------------------------------------------------------------------------------//
	
	public Gathering(){}

	public Gathering(String allyUID){
		this.allyUID = allyUID;
	}
	
	//-----------------------------------------------------------------------------------//

	private String gatheringUID = "notcreatedyet";
	private String allyUID;
	private List<Unit> units = new ArrayList<Unit>();
	
	//-----------------------------------------------------------------------------------//
	
	public String getGatheringUID() {
		return gatheringUID;
	}
	public void setGatheringUID(String gatheringUID) {
		this.gatheringUID = gatheringUID;
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
	

	//-----------------------------------------------------------------------------------//
	
}
