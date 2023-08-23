package com.greenelegentfarmer.model;

public class EmailDetails {

	private String recipient;
	private String msgBody;
	private String subject;
	private String attachment;
	
	private EmailDetails() {}
	
	public EmailDetails(String recipient) {
		this.recipient=recipient;
	}
	
	public String getRecipient() {
		return recipient;
	}
	public EmailDetails setRecipient(String recipient) {
		this.recipient = recipient;
		return this;
	}
	public String getMsgBody() {
		return msgBody;
	}
	public EmailDetails setMsgBody(String msgBody) {
		this.msgBody = msgBody;
		return this;
	}
	public String getSubject() {
		return subject;
	}
	public EmailDetails setSubject(String subject) {
		this.subject = subject;
		return this;
	}
	public String getAttachment() {
		return attachment;
	}
	public EmailDetails setAttachment(String attachment) {
		this.attachment = attachment;
		return this;
	}
	
	


}
