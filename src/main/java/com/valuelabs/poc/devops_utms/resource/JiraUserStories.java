package com.valuelabs.poc.devops_utms.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties( ignoreUnknown = true )
public class JiraUserStories implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//@JsonProperty("startAt")
	private int startAt;
	
	//@JsonProperty("maxResults")
	private int maxResults;
	
	//@JsonProperty("total")
	private int total;
	
	@SerializedName("issues")
	private List<UserStory> issues = new ArrayList<>();

	public Integer getStartAt() {
		return startAt;
	}

	public void setStartAt(Integer startAt) {
		this.startAt = startAt;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<UserStory> getIssues() {
		return issues;
	}

	public void setIssues(List<UserStory> issues) {
		this.issues = issues;
	}
	
}

