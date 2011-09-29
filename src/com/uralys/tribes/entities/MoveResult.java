package com.uralys.tribes.entities;

public class MoveResult {

	public MoveResult(){};
	
	//------------------------------------------------------------------------------------------//

	private Cell cellDeparture;
	private Unit unit;

	//------------------------------------------------------------------------------------------//

	public Cell getCellDeparture() {
		return cellDeparture;
	}

	public void setCellDeparture(Cell cellDeparture) {
		this.cellDeparture = cellDeparture;
	}
	
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	
}
