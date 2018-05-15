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
public class UserStoryFields implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private String assigneeName;

	@Id
	private String userStoryType;

	@SerializedName("summary")
	private String userStoryTitle;

	@SerializedName("issuetype")
	private JiraIssueType issueType;

	@SerializedName("description")
	private String description;

	@SerializedName("updated")
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date updatedDate;

	@SerializedName("created")
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date createdDate;

	///private String updatedBy;

	//private String projectName;

	//private String issueStatus;

	//private String timeSpent;

	@SerializedName("project")
	private Project project;

	@SerializedName("status")
	private Status userStoryStatus;

	@SerializedName("creator")
	private Creator creator;

	@SerializedName("assignee")
	private Assignee assignee;
	
	@SerializedName("subtasks")
	private List<Subtasks> subtasks = new ArrayList<>();
	
	@SerializedName("parent")
	private Parent parent;
	
	@SerializedName("timeestimate")
	private int timeestimate;

	public int getTimeestimate() {
		return timeestimate;
	}

	public void setTimeestimate(int timeestimate) {
		this.timeestimate = timeestimate;
	}

	public Parent getParent() {
		return parent;
	}

	public void setParent(Parent parent) {
		this.parent = parent;
	}

	public List<Subtasks> getSubtasks() {
		return subtasks;
	}

	public void setSubtasks(List<Subtasks> subtasks) {
		this.subtasks = subtasks;
	}

	@SerializedName("worklog")
	private Worklog worklog;

	

	public Worklog getWorklog() {
		return worklog;
	}

	public void setWorklog(Worklog worklog) {
		this.worklog = worklog;
	}

	public Assignee getAssignee() {
		return assignee;
	}

	public void setAssignee(Assignee assignee) {
		this.assignee = assignee;
	}

	public Creator getCreator() {
		return creator;
	}

	public void setCreator(Creator creator) {
		this.creator = creator;
	}

	public Status getUserStoryStatus() {
		return userStoryStatus;
	}

	public void setUserStoryStatus(Status userStoryStatus) {
		this.userStoryStatus = userStoryStatus;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	

	public String getUserStoryType() {
		return userStoryType;
	}

	public void setUserStoryType(String userStoryType) {
		this.userStoryType = userStoryType;
	}

	public JiraIssueType getIssueType() {
		return issueType;
	}

	public void setIssueType(JiraIssueType issueType) {
		this.issueType = issueType;
	}


	public String getUserStoryTitle() {
		return this.userStoryTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUserStoryTitle(String userStoryTitle) {
		this.userStoryTitle = userStoryTitle;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	

}
