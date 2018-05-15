package com.valuelabs.poc.devops_utms.resource;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPOJOBuilder
@Document
public class RundeckResource {
	private String url;
	@Id
	private String name;
	private String description;
	private JobsResource jobsList;
	
	public JobsResource getJobsList() {
		return jobsList;
	}
	public void setJobsList(JobsResource jobsList) {
		this.jobsList = jobsList;
	}
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
	
}
