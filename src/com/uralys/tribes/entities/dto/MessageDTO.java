package com.uralys.tribes.entities.dto;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class MessageDTO {
	
	//-----------------------------------------------------------------------------------//

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String messageUID;
	
	@Persistent private Text content;
	@Persistent private String senderUID;
	@Persistent private String senderName;
	@Persistent private int status;
	@Persistent private long time;

	//-----------------------------------------------------------------------------------//

	@Persistent private boolean isReport;
	@Persistent private int reportType;
	@Persistent private String reportCellUID;
	
	@Persistent private String unit1_unitUID;
	@Persistent private String unit1_ownerUID;
	@Persistent private String unit1_ownerName;
	@Persistent private int unit1_size;
	@Persistent private int unit1_type;
	@Persistent private int unit1_bows;
	@Persistent private int unit1_armors;
	@Persistent private int unit1_swords;
	@Persistent private int unit1_value;
	@Persistent private int unit1_wheat;
	@Persistent private int unit1_wood;
	@Persistent private int unit1_iron;
	@Persistent private int unit1_gold;

	@Persistent private boolean unit1_attackACity;
	@Persistent private boolean unit1_defendACity;

	@Persistent private String unit2_unitUID;
	@Persistent private String unit2_ownerUID;
	@Persistent private String unit2_ownerName;
	@Persistent private int unit2_size;
	@Persistent private int unit2_type;
	@Persistent private int unit2_bows;
	@Persistent private int unit2_armors;
	@Persistent private int unit2_swords;
	@Persistent private int unit2_value;
	@Persistent private int unit2_wheat;
	@Persistent private int unit2_wood;
	@Persistent private int unit2_iron;
	@Persistent private int unit2_gold;

	@Persistent private String nextUnit_unitUID;
	@Persistent private String nextUnit_ownerUID;
	@Persistent private String nextUnit_ownerName;
	@Persistent private int nextUnit_size;
	@Persistent private int nextUnit_type;
	@Persistent private int nextUnit_bows;
	@Persistent private int nextUnit_armors;
	@Persistent private int nextUnit_swords;
	@Persistent private int nextUnit_value;
	@Persistent private int nextUnit_wheat;
	@Persistent private int nextUnit_wood;
	@Persistent private int nextUnit_iron;
	@Persistent private int nextUnit_gold;
	
	//-----------------------------------------------------------------------------------//

	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessageUID() {
		return messageUID;
	}
	public void setMessageUID(String messageUID) {
		this.messageUID = messageUID;
	}
	public Text getContent() {
		return content;
	}
	public void setContent(Text content) {
		this.content = content;
	}
	public String getSenderUID() {
		return senderUID;
	}
	public void setSenderUID(String senderUID) {
		this.senderUID = senderUID;
	}
	public String getKey() { 
		return key;
	}
	public void setKey(String key) {
		this.key = key; 
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public boolean isReport() {
		return isReport;
	}
	public void setReport(boolean isReport) {
		this.isReport = isReport;
	}
	public String getUnit1_unitUID() {
		return unit1_unitUID;
	}
	public void setUnit1_unitUID(String unit1_unitUID) {
		this.unit1_unitUID = unit1_unitUID;
	}
	public String getUnit1_ownerUID() {
		return unit1_ownerUID;
	}
	public void setUnit1_ownerUID(String unit1_ownerUID) {
		this.unit1_ownerUID = unit1_ownerUID;
	}
	public String getUnit1_ownerName() {
		return unit1_ownerName;
	}
	public void setUnit1_ownerName(String unit1_ownerName) {
		this.unit1_ownerName = unit1_ownerName;
	}
	public int getUnit1_size() {
		return unit1_size;
	}
	public void setUnit1_size(int unit1_size) {
		this.unit1_size = unit1_size;
	}
	public int getUnit1_bows() {
		return unit1_bows;
	}
	public void setUnit1_bows(int unit1_bows) {
		this.unit1_bows = unit1_bows;
	}
	public int getUnit1_armors() {
		return unit1_armors;
	}
	public void setUnit1_armors(int unit1_armors) {
		this.unit1_armors = unit1_armors;
	}
	public int getUnit1_swords() {
		return unit1_swords;
	}
	public void setUnit1_swords(int unit1_swords) {
		this.unit1_swords = unit1_swords;
	}
	public int getUnit1_value() {
		return unit1_value;
	}
	public void setUnit1_value(int unit1_value) {
		this.unit1_value = unit1_value;
	}
	public int getUnit1_wheat() {
		return unit1_wheat;
	}
	public void setUnit1_wheat(int unit1_wheat) {
		this.unit1_wheat = unit1_wheat;
	}
	public int getUnit1_wood() {
		return unit1_wood;
	}
	public void setUnit1_wood(int unit1_wood) {
		this.unit1_wood = unit1_wood;
	}
	public int getUnit1_iron() {
		return unit1_iron;
	}
	public void setUnit1_iron(int unit1_iron) {
		this.unit1_iron = unit1_iron;
	}
	public boolean isUnit1_attackACity() {
		return unit1_attackACity;
	}
	public void setUnit1_attackACity(boolean unit1_attackACity) {
		this.unit1_attackACity = unit1_attackACity;
	}
	public boolean isUnit1_defendACity() {
		return unit1_defendACity;
	}
	public void setUnit1_defendACity(boolean unit1_defendACity) {
		this.unit1_defendACity = unit1_defendACity;
	}
	public String getUnit2_unitUID() {
		return unit2_unitUID;
	}
	public void setUnit2_unitUID(String unit2_unitUID) {
		this.unit2_unitUID = unit2_unitUID;
	}
	public String getUnit2_ownerUID() {
		return unit2_ownerUID;
	}
	public void setUnit2_ownerUID(String unit2_ownerUID) {
		this.unit2_ownerUID = unit2_ownerUID;
	}
	public String getUnit2_ownerName() {
		return unit2_ownerName;
	}
	public void setUnit2_ownerName(String unit_ownerName) {
		this.unit2_ownerName = unit_ownerName;
	}
	public int getUnit2_size() {
		return unit2_size;
	}
	public void setUnit2_size(int unit2_size) {
		this.unit2_size = unit2_size;
	}
	public int getUnit2_bows() {
		return unit2_bows;
	}
	public void setUnit2_bows(int unit2_bows) {
		this.unit2_bows = unit2_bows;
	}
	public int getUnit2_armors() {
		return unit2_armors;
	}
	public void setUnit2_armors(int unit2_armors) {
		this.unit2_armors = unit2_armors;
	}
	public int getUnit2_swords() {
		return unit2_swords;
	}
	public void setUnit2_swords(int unit2_swords) {
		this.unit2_swords = unit2_swords;
	}
	public int getUnit2_value() {
		return unit2_value;
	}
	public void setUnit2_value(int unit2_value) {
		this.unit2_value = unit2_value;
	}
	public int getUnit2_wheat() {
		return unit2_wheat;
	}
	public void setUnit2_wheat(int unit2_wheat) {
		this.unit2_wheat = unit2_wheat;
	}
	public int getUnit2_wood() {
		return unit2_wood;
	}
	public void setUnit2_wood(int unit2_wood) {
		this.unit2_wood = unit2_wood;
	}
	public int getUnit2_iron() {
		return unit2_iron;
	}
	public void setUnit2_iron(int unit2_iron) {
		this.unit2_iron = unit2_iron;
	}
	public String getNextUnit_unitUID() {
		return nextUnit_unitUID;
	}
	public void setNextUnit_unitUID(String nextUnit_unitUID) {
		this.nextUnit_unitUID = nextUnit_unitUID;
	}
	public String getNextUnit_ownerUID() {
		return nextUnit_ownerUID;
	}
	public void setNextUnit_ownerUID(String nextUnit_ownerUID) {
		this.nextUnit_ownerUID = nextUnit_ownerUID;
	}
	public String getNextUnit_ownerName() {
		return nextUnit_ownerName;
	}
	public void setNextUnit_ownerName(String nextUnit_ownerName) {
		this.nextUnit_ownerName = nextUnit_ownerName;
	}
	public int getNextUnit_size() {
		return nextUnit_size;
	}
	public void setNextUnit_size(int nextUnit_size) {
		this.nextUnit_size = nextUnit_size;
	}
	public int getNextUnit_bows() {
		return nextUnit_bows;
	}
	public void setNextUnit_bows(int nextUnit_bows) {
		this.nextUnit_bows = nextUnit_bows;
	}
	public int getNextUnit_armors() {
		return nextUnit_armors;
	}
	public void setNextUnit_armors(int nextUnit_armors) {
		this.nextUnit_armors = nextUnit_armors;
	}
	public int getNextUnit_swords() {
		return nextUnit_swords;
	}
	public void setNextUnit_swords(int nextUnit_swords) {
		this.nextUnit_swords = nextUnit_swords;
	}
	public int getNextUnit_value() {
		return nextUnit_value;
	}
	public void setNextUnit_value(int nextUnit_value) {
		this.nextUnit_value = nextUnit_value;
	}
	public int getNextUnit_wheat() {
		return nextUnit_wheat;
	}
	public void setNextUnit_wheat(int nextUnit_wheat) {
		this.nextUnit_wheat = nextUnit_wheat;
	}
	public int getNextUnit_wood() {
		return nextUnit_wood;
	}
	public void setNextUnit_wood(int nextUnit_wood) {
		this.nextUnit_wood = nextUnit_wood;
	}
	public int getNextUnit_iron() {
		return nextUnit_iron;
	}
	public void setNextUnit_iron(int nextUnit_iron) {
		this.nextUnit_iron = nextUnit_iron;
	}
	public int getReportType() {
		return reportType;
	}
	public void setReportType(int reportType) {
		this.reportType = reportType;
	}
	public int getUnit1_gold() {
		return unit1_gold;
	}
	public void setUnit1_gold(int unit1_gold) {
		this.unit1_gold = unit1_gold;
	}
	public int getUnit2_gold() {
		return unit2_gold;
	}
	public void setUnit2_gold(int unit2_gold) {
		this.unit2_gold = unit2_gold;
	}
	public int getNextUnit_gold() {
		return nextUnit_gold;
	}
	public void setNextUnit_gold(int nextUnit_gold) {
		this.nextUnit_gold = nextUnit_gold;
	}
	public String getReportCellUID() {
		return reportCellUID;
	}
	public void setReportCellUID(String reportCellUID) {
		this.reportCellUID = reportCellUID;
	}
	public int getUnit1_type() {
		return unit1_type;
	}
	public void setUnit1_type(int unit1_type) {
		this.unit1_type = unit1_type;
	}
	public int getUnit2_type() {
		return unit2_type;
	}
	public void setUnit2_type(int unit2_type) {
		this.unit2_type = unit2_type;
	}
	public int getNextUnit_type() {
		return nextUnit_type;
	}
	public void setNextUnit_type(int nextUnit_type) {
		this.nextUnit_type = nextUnit_type;
	}
	
	
	//-----------------------------------------------------------------------------------//
	
}
