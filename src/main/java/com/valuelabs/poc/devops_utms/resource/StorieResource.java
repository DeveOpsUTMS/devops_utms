package com.valuelabs.poc.devops_utms.resource;

import java.util.ArrayList;
import java.util.List;

public class StorieResource {

private int startAt;
	
	private int maxResults;
	
	private int total;
	
	//private List<UserStory> issues = new ArrayList<>();
	
	private List<UserStory> issues = new ArrayList<>();

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

	public List<UserStory> getIssues() {
		return issues;
	}

	public void setIssues(List<UserStory> issues) {
		this.issues = issues;
	}
	
}
