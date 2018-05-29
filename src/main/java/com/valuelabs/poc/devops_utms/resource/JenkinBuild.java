package com.valuelabs.poc.devops_utms.resource;

public class JenkinBuild {

	/*
	private int buildId;
	private String buildResult;
	private long timeStamp;
	*/
	private String commitNumber;
	private int buildNumber;
	private String status;
	//private long date;
	private String date;
	private long buildDuration;
	
	private int totalRegressionCount;
	private int failedRegressionCount;
	private int passedRegressionCount;
	
	private int totalUnitTestCasesCount;
	private int failedUnitTestCasesCount;
	private int passedUnitTestCasesCount;
	
	/*
	public int getBuildId() {
		return buildId;
	}
	public void setBuildId(int buildId) {
		this.buildId = buildId;
	}

	public String getBuildResult() {
		return buildResult;
	}
	public void setBuildResult(String buildResult) {
		this.buildResult = buildResult;
	}

	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	*/

	public int getTotalUnitTestCasesCount() {
		return totalUnitTestCasesCount;
	}
	public void setTotalUnitTestCasesCount(int totalUnitTestCasesCount) {
		this.totalUnitTestCasesCount = totalUnitTestCasesCount;
	}
	public int getFailedUnitTestCasesCount() {
		return failedUnitTestCasesCount;
	}
	public void setFailedUnitTestCasesCount(int failedUnitTestCasesCount) {
		this.failedUnitTestCasesCount = failedUnitTestCasesCount;
	}
	public int getPassedUnitTestCasesCount() {
		return passedUnitTestCasesCount;
	}
	public void setPassedUnitTestCasesCount(int passedUnitTestCasesCount) {
		this.passedUnitTestCasesCount = passedUnitTestCasesCount;
	}
	public int getTotalRegressionCount() {
		return totalRegressionCount;
	}
	public void setTotalRegressionCount(int totalRegressionCount) {
		this.totalRegressionCount = totalRegressionCount;
	}
	public int getFailedRegressionCount() {
		return failedRegressionCount;
	}
	public void setFailedRegressionCount(int failedRegressionCount) {
		this.failedRegressionCount = failedRegressionCount;
	}
	public int getPassedRegressionCount() {
		return passedRegressionCount;
	}
	public void setPassedRegressionCount(int passedRegressionCount) {
		this.passedRegressionCount = passedRegressionCount;
	}
	public String getCommitNumber() {
		return commitNumber;
	}
	public void setCommitNumber(String commitNumber) {
		this.commitNumber = commitNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getBuildNumber() {
	return buildNumber;
	}
	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}

	/*public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}*/
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public long getBuildDuration() {
		return buildDuration;
	}
	public void setBuildDuration(long buildDuration) {
		this.buildDuration = buildDuration;
	}
	@Override
	public String toString() {
		return "JenkinBuild [commitNumber=" + commitNumber + ", buildNumber=" + buildNumber + ", status=" + status
				+ ", date=" + date + ", buildDuration=" + buildDuration + "]";
	}
		
	
}
