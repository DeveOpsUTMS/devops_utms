package com.valuelabs.poc.devops_utms.resource;

public class BoardsResource {
	
	private int maxResults;
	
	private int startAt;
	
	private BoardResource jiraBoard;

	public BoardResource getJiraBoard() {
		return jiraBoard;
	}

	public void setJiraBoard(BoardResource jiraBoard) {
		this.jiraBoard = jiraBoard;
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
