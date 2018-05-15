package com.valuelabs.poc.devops_utms.resource;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Nodes {
	
	private int id;
	private Boolean status;
	private List<String> failedNodes;
	private List<String> successfulNodes;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public List<String> getFailedNodes() {
		return failedNodes;
	}
	public void setFailedNodes(List<String> failedNodes) {
		this.failedNodes = failedNodes;
	}
	public List<String> getSuccessfulNodes() {
		return successfulNodes;
	}
	public void setSuccessfulNodes(List<String> successfulNodes) {
		this.successfulNodes = successfulNodes;
	}

}
