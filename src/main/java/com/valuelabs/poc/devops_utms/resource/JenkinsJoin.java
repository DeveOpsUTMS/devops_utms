package com.valuelabs.poc.devops_utms.resource;

import java.util.List;

import org.springframework.data.annotation.Id;

public class JenkinsJoin {

	@Id
	private String jobName;
	private String commitNumber;
	private List<JenkinBuildC> allBuilds;
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getCommitNumber() {
		return commitNumber;
	}
	public void setCommitNumber(String commitNumber) {
		this.commitNumber = commitNumber;
	}
	public List<JenkinBuildC> getAllBuilds() {
		return allBuilds;
	}
	public void setAllBuilds(List<JenkinBuildC> allBuilds) {
		this.allBuilds = allBuilds;
	}
	@Override
	public String toString() {
		return "JenkinsJoin [jobName=" + jobName + ", allBuilds=" + allBuilds + ",commitNumber="+commitNumber+ "]";
	}
	
}
