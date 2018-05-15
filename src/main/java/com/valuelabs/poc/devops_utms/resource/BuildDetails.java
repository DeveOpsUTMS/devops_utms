package com.valuelabs.poc.devops_utms.resource;

import java.util.Date;

public class BuildDetails {
	private int buildNumber;
	private String commitNumber;
	//private long date;
	public int getBuildNumber() {
		return buildNumber;
	}
	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}
	public String getCommitNumber() {
		return commitNumber;
	}
	public void setCommitNumber(String commitNumber) {
		this.commitNumber = commitNumber;
	}
	/*public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}*/
	@Override
	public String toString() {
		return "BuildDetails [buildNumber=" + buildNumber + ", commitNumber=" + commitNumber + "]";
	}
		
}
