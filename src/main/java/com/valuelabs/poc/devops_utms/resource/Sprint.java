package com.valuelabs.poc.devops_utms.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sprint implements Serializable {
	private static final long serialVersionUID = 1L;

	@SerializedName("id")
	private int sprintId;

	@SerializedName("name")
	private String sprintName;
	
	private String state;
	@Id
	private Integer originBoardId;
	
	//private Project project;
	
	
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date startDate;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date endDate;
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	List<String> userStoryList = new ArrayList<>();
	
	JiraUserStories userStories;
	
	
	
	public List<String> getUserStoryList() {
		return userStoryList;
	}

	public void setUserStoryList(List<String> userStoryList) {
		this.userStoryList = userStoryList;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Sprint() {
	}

	public int getSprintId() {
		return this.sprintId;
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

	/*public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}*/

	public JiraUserStories getUserStories() {
		return userStories;
	}

	public void setUserStories(JiraUserStories userStories) {
		this.userStories = userStories;
	}

	@Override
	public String toString() {
		return "Sprint [sprintId=" + sprintId + ", sprintName=" + sprintName + ", state=" + state + ", originBoardId="
				+ originBoardId +  "]";
	}

}
