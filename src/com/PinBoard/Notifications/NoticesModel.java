package com.PinBoard.Notifications;
public class NoticesModel {
	String heading;
	String content;
	String objectId;
	
	public NoticesModel(String heading, String content, String objectId) {
		// TODO Auto-generated constructor stub
		this.heading=heading;
		this.content=content;
		this.objectId=objectId;
	}
	
	public String getHeading()
	{
		return heading;
		
	}
	
	public String getContent() {
		
		return content;
	}
	
	public String getObjectId() {
		
		return objectId;
	}
	

}
