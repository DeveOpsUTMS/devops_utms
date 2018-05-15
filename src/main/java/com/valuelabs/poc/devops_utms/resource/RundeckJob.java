package com.valuelabs.poc.devops_utms.resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPOJOBuilder
@Document
public class RundeckJob {

	private String url;
	@Id
	private String name;
	private String description;
	private List<Jobs> jobsList = new ArrayList<>();
	// private List<Execution> executionsList = new ArrayList<>();
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Jobs> getJobsList() {
		return jobsList;
	}

	public void setJobsList(List<Jobs> jobsList) {
		this.jobsList = jobsList;
	}
}