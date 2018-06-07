package com.valuelabs.poc.devops_utms.resource.appdynamics;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class HealthRuleViolation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int id;
	private String name;
	private String sverity;
	
	@SerializedName("triggeredEntityDefinition")
	private SeverityCause severityCause;
	
	@SerializedName("deepLinkUrl")
	private String incidentUrl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSverity() {
		return sverity;
	}

	public void setSverity(String sverity) {
		this.sverity = sverity;
	}

	public SeverityCause getSeverityCause() {
		return severityCause;
	}

	public void setSeverityCause(SeverityCause severityCause) {
		this.severityCause = severityCause;
	}

	public String getIncidentUrl() {
		return incidentUrl;
	}

	public void setIncidentUrl(String incidentUrl) {
		this.incidentUrl = incidentUrl;
	}
	

}
