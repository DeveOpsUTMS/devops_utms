package com.valuelabs.poc.devops_utms.resource;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Worklog {
	
	@SerializedName("worklogs")
	private List<Worklogs> logs;

	public List<Worklogs> getLogs() {
		return logs;
	}

	public void setLogs(List<Worklogs> logs) {
		this.logs = logs;
	}
	
}
