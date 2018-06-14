package com.valuelabs.poc.devops_utms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPOJOBuilder
@Document
public class MetricData {
	
	@Id
	private int applicationId;
	
	private String description;
	private String name;
	
	private TiersResource tiers;

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TiersResource getTiers() {
		return tiers;
	}

	public void setTiers(TiersResource tiers) {
		this.tiers = tiers;
	}
	

}
