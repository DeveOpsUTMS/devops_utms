package com.valuelabs.poc.devops_utms.resource;

import java.io.Serializable;

public class AnalysisReport implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userStoryIds;
	
	private String commits;
	
	private String build;
	
	private String deployAndQA;
	
	public AnalysisReport(){
		
	}

	public String getUserStoryIds() {
		return userStoryIds;
	}

	public void setUserStoryIds(String userStoryIds) {
		this.userStoryIds = userStoryIds;
	}

	public String getCommits() {
		return commits;
	}

	public void setCommits(String commits) {
		this.commits = commits;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public String getDeployAndQA() {
		return deployAndQA;
	}

	public void setDeployAndQA(String deployAndQA) {
		this.deployAndQA = deployAndQA;
	}

	@Override
	public String toString() {
		return "AnalysisReport [userStoryIds=" + userStoryIds + ", commits=" + commits + ", build=" + build
				+ ", deployAndQA=" + deployAndQA + "]";
	}

}
