package com.valuelabs.poc.devops_utms.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;

@Document
public class GitProperties {
	
	@Id
	private String commitId;
	private String commitMessage;
	private String branch;
	private String userName;
	private String userEmail;
	private String commitTime;
	private String buildUserName;
	private String buildUserEmail;
	private String buildTime;
	private String buildHost;
	private String buildVersion;
	private String commitIdDescribeShort;
	private String dirty;
	private String remoteOriginUrl;
	private String closestTagName;
	private String commitIdAbbrev;
	private String closestTagCommitCount;
	private String tags;
	public String getCommitId() {
		return commitId;
	}
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
	public String getCommitMessage() {
		return commitMessage;
	}
	public void setCommitMessage(String commitMessage) {
		this.commitMessage = commitMessage;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getCommitTime() {
		return commitTime;
	}
	public void setCommitTime(String commitTime) {
		this.commitTime = commitTime;
	}
	public String getBuildUserName() {
		return buildUserName;
	}
	public void setBuildUserName(String buildUserName) {
		this.buildUserName = buildUserName;
	}
	public String getBuildUserEmail() {
		return buildUserEmail;
	}
	public void setBuildUserEmail(String buildUserEmail) {
		this.buildUserEmail = buildUserEmail;
	}
	public String getBuildTime() {
		return buildTime;
	}
	public void setBuildTime(String buildTime) {
		this.buildTime = buildTime;
	}
	public String getBuildHost() {
		return buildHost;
	}
	public void setBuildHost(String buildHost) {
		this.buildHost = buildHost;
	}
	public String getBuildVersion() {
		return buildVersion;
	}
	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}
	public String getCommitIdDescribeShort() {
		return commitIdDescribeShort;
	}
	public void setCommitIdDescribeShort(String commitIdDescribeShort) {
		this.commitIdDescribeShort = commitIdDescribeShort;
	}
	public String getDirty() {
		return dirty;
	}
	public void setDirty(String dirty) {
		this.dirty = dirty;
	}
	public String getRemoteOriginUrl() {
		return remoteOriginUrl;
	}
	public void setRemoteOriginUrl(String remoteOriginUrl) {
		this.remoteOriginUrl = remoteOriginUrl;
	}
	public String getClosestTagName() {
		return closestTagName;
	}
	public void setClosestTagName(String closestTagName) {
		this.closestTagName = closestTagName;
	}
	public String getCommitIdAbbrev() {
		return commitIdAbbrev;
	}
	public void setCommitIdAbbrev(String commitIdAbbrev) {
		this.commitIdAbbrev = commitIdAbbrev;
	}
	public String getClosestTagCommitCount() {
		return closestTagCommitCount;
	}
	public void setClosestTagCommitCount(String closestTagCommitCount) {
		this.closestTagCommitCount = closestTagCommitCount;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	
	


}
