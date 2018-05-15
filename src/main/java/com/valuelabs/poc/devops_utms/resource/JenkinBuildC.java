package com.valuelabs.poc.devops_utms.resource;

public class JenkinBuildC {
	private String commitNumber;
	private int buildNumber;
	public String getCommitNumber() {
		return commitNumber;
	}
	public void setCommitNumber(String commitNumber) {
		this.commitNumber = commitNumber;
	}
	public int getBuildNumber() {
		return buildNumber;
	}
	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}
	
	public String toString() {
		return "JenkinBuildC [buildNumber=" + buildNumber + ", commitNumber=" + commitNumber + "]";
	}

}
