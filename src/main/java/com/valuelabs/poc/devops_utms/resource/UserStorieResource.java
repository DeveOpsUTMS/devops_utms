package com.valuelabs.poc.devops_utms.resource;

public class UserStorieResource {
	
	private int startAt;
	
	private int maxResults;
	
	private int total;
	
	//private List<UserStory> issues = new ArrayList<>();
	
	private UserStory issues ;

	

	public UserStory getIssues() {
		return issues;
	}

	public void setIssues(UserStory issues) {
		this.issues = issues;
	}

	public int getStartAt() {
		return startAt;
	}

	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}


}
