package com.uralys.tribes.entities;


public class Report {

	private String reportUID;
	private int status;

	private String report;
	
	//-----------------------------------------------------------------------------------//

	public String getReportUID() {
		return reportUID;
	}

	public void setReportUID(String reportUID) {
		this.reportUID = reportUID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}
	

	//-----------------------------------------------------------------------------------//
	
}
