package com.valuelabs.poc.devops_utms.resource;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

@Document
public class SVNProperties {
	
	@Id
	private long revisionId;
	private String author;
	private Date commitedDate;
	private String message;
	private List<Map<String, String>> modifiedFilesList;

	public long getRevisionId() {
		return revisionId;
	}
	public void setRevisionId(long revisionId) {
		this.revisionId = revisionId;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getCommitedDate() {
		return commitedDate;
	}
	public void setCommitedDate(Date commitedDate) {
		this.commitedDate = commitedDate;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Map<String, String>> getModifiedFilesList() {
		return modifiedFilesList;
	}
	public void setModifiedFilesList(List<Map<String, String>> modifiedFilesList) {
		this.modifiedFilesList = modifiedFilesList;
	}
	
}
