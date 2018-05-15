package com.valuelabs.poc.devops_utms.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Executions {
	
	private int id;
	private String href;
	private Boolean status;
	private String project;
	
	@SerializedName("date-started")
	private ExecutionStart dateStarted;
	@SerializedName("date-ended")
	private ExecutionEnd dateEended;
	
	private String user;
	
	//private Jobs job;
	private Nodes nodes;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Nodes getNodes() {
		return nodes;
	}
	public void setNodes(Nodes nodes) {
		this.nodes = nodes;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public ExecutionStart getDateStarted() {
		return dateStarted;
	}
	public void setDateStarted(ExecutionStart dateStarted) {
		this.dateStarted = dateStarted;
	}
	public ExecutionEnd getDateEended() {
		return dateEended;
	}
	public void setDateEended(ExecutionEnd dateEended) {
		this.dateEended = dateEended;
	}
	

}
