package com.valuelabs.poc.devops_utms.resource;

public class JiraSprintResource {
	
	private int maxResults;
	
	private int startAt;
	
	//private Sprint sprintDetails;
	
	private JiraSprintDetailsResource sprintDetails;

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public int getStartAt() {
		return startAt;
	}

	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	public JiraSprintDetailsResource getSprintDetails() {
		return sprintDetails;
	}

	public void setSprintDetails(JiraSprintDetailsResource sprintDetails) {
		this.sprintDetails = sprintDetails;
	}

}
