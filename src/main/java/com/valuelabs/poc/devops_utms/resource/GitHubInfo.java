package com.valuelabs.poc.devops_utms.resource;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;

public class GitHubInfo implements Serializable{
	
	@Id
	private String name;
	
	private Date lastRunDateTime;
	
	public GitHubInfo() {
		
	}
	
	

	public GitHubInfo(String name, Date lastRunDateTime) {
		super();
		this.name = name;
		this.lastRunDateTime = lastRunDateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLastRunDateTime() {
		return lastRunDateTime;
	}

	public void setLastRunDateTime(Date lastRunDateTime) {
		this.lastRunDateTime = lastRunDateTime;
	}

	@Override
	public String toString() {
		return "GitHubInfo [name=" + name + ", lastRunDateTime=" + lastRunDateTime + "]";
	}
}
