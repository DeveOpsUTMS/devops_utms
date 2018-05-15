package com.valuelabs.poc.devops_utms.resource;

import java.io.Serializable;
import java.util.List;

public class Portfolio implements Serializable {
	private static final long serialVersionUID = 1L;

	private int portfolioId;

	private String portfolio;

	private List<Project> projects;

	public Portfolio() {
	}

	public int getPortfolioId() {
		return this.portfolioId;
	}

	public void setPortfolioId(int portfolioId) {
		this.portfolioId = portfolioId;
	}

	public String getPortfolio() {
		return this.portfolio;
	}

	public void setPortfolio(String portfolio) {
		this.portfolio = portfolio;
	}

	public List<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

}
