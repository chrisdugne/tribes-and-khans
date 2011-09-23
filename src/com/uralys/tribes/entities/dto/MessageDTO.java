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
	
	//-----------------------------------------------------------------------------------//
	
}
