package com.valuelabs.poc.devops_utms.resource.appdynamics;

import com.valuelabs.poc.devops_utms.model.TiersResource;

public class MetricResource {

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
