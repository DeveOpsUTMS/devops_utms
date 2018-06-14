package com.valuelabs.poc.devops_utms.resource.appdynamics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Applications implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<Application> application = new ArrayList<>();

	public List<Application> getApplication() {
		return application;
	}

	public void setApplication(List<Application> application) {
		this.application = application;
	}
	
	

}
