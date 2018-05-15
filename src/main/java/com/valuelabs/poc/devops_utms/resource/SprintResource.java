package com.valuelabs.poc.devops_utms.resource;

public class SprintResource {

	private int maxResults;
	
	private int startAt;
	
	//private Sprint sprintDetails;
	
	private SprintDetailsResource sprintDetails;

	public SprintDetailsResource getSprintDetails() {
		return sprintDetails;
	}

	public void setSprintDetails(SprintDetailsResource sprintDetails) {
		this.sprintDetails = sprintDetails;
	}

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

	
	
}
