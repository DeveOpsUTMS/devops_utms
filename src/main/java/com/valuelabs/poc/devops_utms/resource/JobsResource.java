package com.valuelabs.poc.devops_utms.resource;

public class JobsResource {
private String id;
	
	private String name;
	private String project;
	private String group;
	private Boolean scheduled;
	private String href;
	private String revisionID;
	private String deployedBy;
	private String env;
	
	private Executions executionsList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Boolean getScheduled() {
		return scheduled;
	}

	public void setScheduled(Boolean scheduled) {
		this.scheduled = scheduled;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getRevisionID() {
		return revisionID;
	}

	public void setRevisionID(String revisionID) {
		this.revisionID = revisionID;
	}

	public String getDeployedBy() {
		return deployedBy;
	}

	public void setDeployedBy(String deployedBy) {
		this.deployedBy = deployedBy;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public Executions getExecutionsList() {
		return executionsList;
	}

	public void setExecutionsList(Executions executionsList) {
		this.executionsList = executionsList;
	}
}
