package com.uralys.tribes.entities.dto;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.uralys.tribes.dao.impl.UniversalDAO;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SmithDTO {
	
	//-----------------------------------------------------------------------------------//

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")	
    protected String key;

	@Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
    private String smithUID;
	
	@Persistent private String itemUID;
	@Persistent private int people;
	
	//-----------------------------------------------------------------------------------//

	public String getKey() { 
		return key;
	}
	public void setKey(String key) {
		this.key = key; 
	}
	public String getSmithUID() {
		return smithUID;
	}
	public void setSmithUID(String forgeStockUID) {
		this.smithUID = forgeStockUID;
	}
	public int getPeople() {
		return people;
	}
	public void setPeople(int people) {
		this.people = people;
	}
	
	//-----------------------------------------------------------------------------------//
	
	public ItemDTO getItem() {
		return (ItemDTO) UniversalDAO.getInstance().getObjectDTO(itemUID, ItemDTO.class);
	}
	
	public String getItemUID() {
		return itemUID;
	}
	public void setItemUID(String itemUID) {
		this.itemUID = itemUID;
	}
}
