package com.valuelabs.poc.devops_utms.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPOJOBuilder
public class JiraBoardsResource {
	
	private int maxResults;
	
	private int startAt;
	
	private JiraBoardResource jiraBoard;

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

	public JiraBoardResource getJiraBoard() {
		return jiraBoard;
	}

	public void setJiraBoard(JiraBoardResource jiraBoard) {
		this.jiraBoard = jiraBoard;
	}

}
