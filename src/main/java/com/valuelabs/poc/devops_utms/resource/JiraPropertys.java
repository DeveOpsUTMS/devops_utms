package com.valuelabs.poc.devops_utms.resource;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class JiraPropertys {
	
	@Id
	private String id;
	private String summery;
	private String name;
	private String priority;
	private String projectName;
	private String projectKey;
	private Date createdDate;
	private Date lastviewedDate;
	private Date duedateDate;
	private String statusDescription;
	private String reporterFirstName;
	private String reporterLastName;
	private String reporterEmailID;
	private String assigneeFirstname;
	private String assigneeLastName;
	private String assigneeemailAddress;
	
	public String getAssigneeFirstname() {
		return assigneeFirstname;
	}
	public void setAssigneeFirstname(String assigneeFirstname) {
		this.assigneeFirstname = assigneeFirstname;
	}
	public String getAssigneeLastName() {
		return assigneeLastName;
	}
	public void setAssigneeLastName(String assigneeLastName) {
		this.assigneeLastName = assigneeLastName;
	}
	public String getAssigneeemailAddress() {
		return assigneeemailAddress;
	}
	public void setAssigneeemailAddress(String assigneeemailAddress) {
		this.assigneeemailAddress = assigneeemailAddress;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSummery() {
		return summery;
	}
	public void setSummery(String summery) {
		this.summery = summery;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectKey() {
		return projectKey;
	}
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getLastviewedDate() {
		return lastviewedDate;
	}
	public void setLastviewedDate(Date lastviewedDate) {
		this.lastviewedDate = lastviewedDate;
	}
	public Date getDuedateDate() {
		return duedateDate;
	}
	public void setDuedateDate(Date duedateDate) {
		this.duedateDate = duedateDate;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	public String getReporterFirstName() {
		return reporterFirstName;
	}
	public void setReporterFirstName(String reporterFirstName) {
		this.reporterFirstName = reporterFirstName;
	}
	public String getReporterLastName() {
		return reporterLastName;
	}
	public void setReporterLastName(String reporterLastName) {
		this.reporterLastName = reporterLastName;
	}
	public String getReporterEmailID() {
		return reporterEmailID;
	}
	public void setReporterEmailID(String reporterEmailID) {
		this.reporterEmailID = reporterEmailID;
	}
	}
