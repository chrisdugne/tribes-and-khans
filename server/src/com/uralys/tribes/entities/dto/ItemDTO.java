package com.uralys.tribes.entities.dto;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ItemDTO {
	
	//-----------------------------------------------------------------------------------//

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String itemUID;
	
	@Persistent private String name;
	@Persistent private int peopleRequired;
	@Persistent private int wood;
	@Persistent private int iron;
	@Persistent private Float goldPriceBase;
	@Persistent private Float goldPriceCurrent;
	@Persistent private int value;
	
	//-----------------------------------------------------------------------------------//

	public String getKey() { 
		return key;
	}
	public void setKey(String key) {
		this.key = key; 
	}
	public String getItemUID() {
		return itemUID;
	}
	public void setItemUID(String itemUID) {
		this.itemUID = itemUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPeopleRequired() {
		return peopleRequired;
	}
	public void setPeopleRequired(int peopleRequired) {
		this.peopleRequired = peopleRequired;
	}
	public int getWood() {
		return wood;
	}
	public void setWood(int wood) {
		this.wood = wood;
	}
	public int getIron() {
		return iron;
	}
	public void setIron(int iron) {
		this.iron = iron;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Float getGoldPriceBase() {
		return goldPriceBase;
	}
	public void setGoldPriceBase(Float goldPriceBase) {
		this.goldPriceBase = goldPriceBase;
	}
	public Float getGoldPriceCurrent() {
		return goldPriceCurrent;
	}
	public void setGoldPriceCurrent(Float goldPriceCurrent) {
		this.goldPriceCurrent = goldPriceCurrent;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
