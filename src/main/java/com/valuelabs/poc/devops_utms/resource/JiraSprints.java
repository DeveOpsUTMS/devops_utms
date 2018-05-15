package com.valuelabs.poc.devops_utms.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)

public class JiraSprints  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int maxResults;
	
	private int startAt;
	
	
	@SerializedName("values")
	private List<Sprint> sprintDetails = new ArrayList<>();

	
	public List<Sprint> getSprintDetails() {
		return sprintDetails;
	}

	public void setSprintDetails(List<Sprint> sprintDetails) {
		this.sprintDetails = sprintDetails;
	}

	public void setStartAt(int startAt) {
		this.startAt = startAt;
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

	
	
}

