package com.valuelabs.poc.devops_utms.resource;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document
public class Commit {

	@Id
	private String commitId;

	private Date commitedDate;

	private String message;

	private String author;

	private String repo;

	private String userStoryId;

	private String url;

	private String commitNumber;

	private String project;

	private List<Map<String, String>> modifiedFilesList;

	private String committedBy;

	private String gitPath;
	
	private int linesAdded;
	
	private int linesRemoved;
	
	private int totalLinesChanged;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private String lastUpdatedDate;

	

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public int getTotalLinesChanged() {
		return totalLinesChanged;
	}

	public void setTotalLinesChanged(int totalLinesChanged) {
		this.totalLinesChanged = totalLinesChanged;
	}

	public int getLinesAdded() {
		return linesAdded;
	}

	public void setLinesAdded(int linesAdded) {
		this.linesAdded = linesAdded;
	}

	public int getLinesRemoved() {
		return linesRemoved;
	}

	public void setLinesRemoved(int linesRemoved) {
		this.linesRemoved = linesRemoved;
	}

	public String getGitPath() {
		return gitPath;
	}

	public void setGitPath(String gitPath) {
		this.gitPath = gitPath;
	}

	public String getCommittedBy() {
		return committedBy;
	}

	public void setCommittedBy(String committedBy) {
		this.committedBy = committedBy;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getCommitNumber() {
		return commitNumber;
	}

	public void setCommitNumber(String commitNumber) {
		this.commitNumber = commitNumber;
	}

	public Commit() {

	}

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public Date getCommitedDate() {
		return commitedDate;
	}

	public void setCommitedDate(Date commitedDate) {
		this.commitedDate = commitedDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getRepo() {
		return repo;
	}

	public void setRepo(String repo) {
		this.repo = repo;
	}

	public String getUserStoryId() {
		return userStoryId;
	}

	public void setUserStoryId(String userStoryId) {
		this.userStoryId = userStoryId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Map<String, String>> getModifiedFilesList() {
		return modifiedFilesList;
	}

	public void setModifiedFilesList(List<Map<String, String>> modifiedFilesList) {
		this.modifiedFilesList = modifiedFilesList;
	}

	@Override
	public String toString() {
		return "Commit [commitId=" + commitId + ", commitedDate=" + commitedDate + ", message=" + message + ", author="
				+ author + ", repo=" + repo + ", userStoryId=" + userStoryId + ", url=" + url + ", modifiedFilesList="
				+ modifiedFilesList + "]";
	}

}
