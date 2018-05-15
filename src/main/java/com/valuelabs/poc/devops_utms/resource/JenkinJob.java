package com.valuelabs.poc.devops_utms.resource;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class JenkinJob {

	@Id
	private String jobName;
	private String jobUrl;
	private String sourceCodeRepo;
	private List<JenkinBuild> allBuilds;
	private JenkinBuild lastBuild;
	private JenkinBuild lastFailedBuild;
	private JenkinBuild lastSuccessfulBuild;
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobUrl() {
		return jobUrl;
	}
	public void setJobUrl(String jobUrl) {
		this.jobUrl = jobUrl;
	}
	public String getSourceCodeRepo() {
		return sourceCodeRepo;
	}
	public void setSourceCodeRepo(String sourceCodeRepo) {
		this.sourceCodeRepo = sourceCodeRepo;
	}
	public List<JenkinBuild> getAllBuilds() {
		return allBuilds;
	}
	public void setAllBuilds(List<JenkinBuild> allBuilds) {
		this.allBuilds = allBuilds;
	}
	public JenkinBuild getLastBuild() {
		return lastBuild;
	}
	public void setLastBuild(JenkinBuild lastBuild) {
		this.lastBuild = lastBuild;
	}
	public JenkinBuild getLastFailedBuild() {
		return lastFailedBuild;
	}
	public void setLastFailedBuild(JenkinBuild lastFailedBuild) {
		this.lastFailedBuild = lastFailedBuild;
	}
	public JenkinBuild getLastSuccessfulBuild() {
		return lastSuccessfulBuild;
	}
	public void setLastSuccessfulBuild(JenkinBuild lastSuccessfulBuild) {
		this.lastSuccessfulBuild = lastSuccessfulBuild;
	}
	@Override
	public String toString() {
		return "JenkinJob [jobName=" + jobName + ", jobUrl=" + jobUrl + ", sourceCodeRepo=" + sourceCodeRepo
				+ ", allBuilds=" + allBuilds + ", lastBuild=" + lastBuild + ", lastFailedBuild=" + lastFailedBuild
				+ ", lastSuccessfulBuild=" + lastSuccessfulBuild + "]";
	}
	
}
