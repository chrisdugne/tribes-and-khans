package com.uralys.tribes.entities;

public class Message {
	
	//-----------------------------------------------------------------------------------//

	public static final int UNREAD = 1;
	public static final int READ = 2;
	public static final int ARCHIVED = 3;
	
	//-----------------------------------------------------------------------------------//

	private String messageUID;
	private String content;
	private String senderUID;
	private String senderName;
	private int status;
	private long time;

	//-----------------------------------------------------------------------------------//

	public String getMessageUID() {
		return messageUID;
	}
	public void setMessageUID(String messageUID) {
		this.messageUID = messageUID;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
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
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	//-----------------------------------------------------------------------------------//
	
}
