package com.valuelabs.poc.devops_utms.resource;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Execution {
	
	private Paging paging;
	private List<Executions> executions = new ArrayList<>();
	
	public Paging getPaging() {
		return paging;
	}
	public void setPaging(Paging paging) {
		this.paging = paging;
	}
	public List<Executions> getExecutions() {
		return executions;
	}
	public void setExecutions(List<Executions> executions) {
		this.executions = executions;
	}
	

}
