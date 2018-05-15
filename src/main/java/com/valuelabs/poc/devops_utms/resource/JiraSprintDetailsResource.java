package com.valuelabs.poc.devops_utms.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class JiraSprintDetailsResource {
	
	private int sprintId;

	private String sprintName;
	
	private String state;
	
	private Integer originBoardId;
	
	//private Project project;
	
	
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endDate;
	
	private List<String> userStoryList = new ArrayList<>();
	
	//JiraUserStories userStories;
	
	private UserStorieResource userStories;
	
	public int getSprintId() {
		return sprintId;
	}

	public void setSprintId(int sprintId) {
		this.sprintId = sprintId;
	}

	public String getSprintName() {
		return sprintName;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getOriginBoardId() {
		return originBoardId;
	}

	public void setOriginBoardId(Integer originBoardId) {
		this.originBoardId = originBoardId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<String> getUserStoryList() {
		return userStoryList;
	}

	public void setUserStoryList(List<String> userStoryList) {
		this.userStoryList = userStoryList;
	}

	public UserStorieResource getUserStories() {
		return userStories;
	}

	public void setUserStories(UserStorieResource userStories) {
		this.userStories = userStories;
	}

}
