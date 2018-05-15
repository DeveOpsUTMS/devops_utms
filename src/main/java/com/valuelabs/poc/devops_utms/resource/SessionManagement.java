package com.valuelabs.poc.devops_utms.resource;

import java.io.Serializable;

public class SessionManagement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Session session;

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	

}
