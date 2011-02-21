package com.uralys.tribes.entities.dto;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ReportDTO {
	
	//-----------------------------------------------------------------------------------//

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String reportUID;
	
	@Persistent private int status;

	@Persistent private String report;
	
	//-----------------------------------------------------------------------------------//

	public String getKey() { 
		return key;
	}
	public void setKey(String key) {
		this.key = key; 
	}
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
