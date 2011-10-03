package com.uralys.tribes.entities;

public class Report {

	private int reportType;
	private String cellUID;
	private UnitReport unit1 = new UnitReport();
	private UnitReport unit2 = new UnitReport();
	private UnitReport nextUnit = new UnitReport();
	
	//-----------------------------------------------------------------------------------//
	
	public int getReportType() {
		return reportType;
	}
	public void setReportType(int reportType) {
		this.reportType = reportType;
	}
	public UnitReport getUnit1() {
		return unit1;
	}
	public void setUnit1(UnitReport unit1) {
		this.unit1 = unit1;
	}
	public UnitReport getUnit2() {
		return unit2;
	}
	public void setUnit2(UnitReport unit2) {
		this.unit2 = unit2;
	}
	public UnitReport getNextUnit() {
		return nextUnit;
	}
	public void setNextUnit(UnitReport nextUnit) {
		this.nextUnit = nextUnit;
	}
	public String getCellUID() {
		return cellUID;
	}
	public void setCellUID(String cellUID) {
		this.cellUID = cellUID;
	}
}
