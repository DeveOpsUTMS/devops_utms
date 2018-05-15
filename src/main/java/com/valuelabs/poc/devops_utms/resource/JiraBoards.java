package com.valuelabs.poc.devops_utms.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document
public class JiraBoards implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int maxResults;
	
	private int startAt;
	
	
	@SerializedName("values")
	private List<JiraBoard> jiraBoard = new ArrayList<>();

	public List<JiraBoard> getJiraBoard() {
		return jiraBoard;
	}

	public void setJiraBoard(List<JiraBoard> jiraBoard) {
		this.jiraBoard = jiraBoard;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public Integer getStartAt() {
		return startAt;
	}

	public void setStartAt(Integer startAt) {
		this.startAt = startAt;
	}

	

	@Override
	public String toString() {
		return "JiraBorads [maxResults=" + maxResults + ", startAt=" + startAt + ", jiraBorad=" + jiraBoard + "]";
	}
	
}

