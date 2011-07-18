package com.uralys.tribes.entities;

import com.google.appengine.api.datastore.Text;

public class Message {
	
	//-----------------------------------------------------------------------------------//

	private String messageUID;
	private Text content;
	private String senderUID;
	private String senderName;
	private int status;

	//-----------------------------------------------------------------------------------//

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
	
	//-----------------------------------------------------------------------------------//
	
}
