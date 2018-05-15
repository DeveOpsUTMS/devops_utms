package com.valuelabs.poc.devops_utms.resource;

import java.util.Date;

public class UserStoryResource {

	private String userStoryId;
	
	private String userStoryType;
	
	private String userStoryTitle;
	
	private String userStoryDescrtption;
	
	private String updatedDate;
	
	private String createdDate;
	
	private String productName;
	
	private String userStoryStatus;
	
	private String createdBy;
	
	private String assignedTo;
	
	private int storyPoints;
	
	private int efforts;
	
	private String sprintId;

	

	public String getSprintId() {
		return sprintId;
	}

	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}

	private String lastUpdatedTimeStamp;

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	public void setLastUpdatedTimeStamp(String lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	public String getUserStoryId() {
		return userStoryId;
	}

	public void setUserStoryId(String userStoryId) {
		this.userStoryId = userStoryId;
	}

	public String getUserStoryType() {
		return userStoryType;
	}

	public void setUserStoryType(String userStoryType) {
		this.userStoryType = userStoryType;
	}

	public String getUserStoryTitle() {
		return userStoryTitle;
	}

	public void setUserStoryTitle(String userStoryTitle) {
		this.userStoryTitle = userStoryTitle;
	}

	public String getUserStoryDescrtption() {
		return userStoryDescrtption;
	}

	public void setUserStoryDescrtption(String userStoryDescrtption) {
		this.userStoryDescrtption = userStoryDescrtption;
	}

	

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUserStoryStatus() {
		return userStoryStatus;
	}

	public void setUserStoryStatus(String userStoryStatus) {
		this.userStoryStatus = userStoryStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public int getStoryPoints() {
		return storyPoints;
	}

	public void setStoryPoints(int storyPoints) {
		this.storyPoints = storyPoints;
	}

	public int getEfforts() {
		return efforts;
	}

	public void setEfforts(int efforts) {
		this.efforts = efforts;
	}

	
	
	
	
	
}
