package com.valuelabs.poc.devops_utms.service;

import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;

import com.valuelabs.poc.devops_utms.resource.AnalysisReport;
import com.valuelabs.poc.devops_utms.resource.GitUserStorieResource;
import com.valuelabs.poc.devops_utms.resource.MonthWiseResponse;

public interface GitHubService {

	GHRepository getUserRepository(String user, String repository);

	List<GHCommit> getUserCommits(String user, String repository);

	public void timerGit();

	// public List<Commit> getAllGitCommits();

	public GHRepository getNewRepoCommitsDateWise(String user, String repository);

	List<Map<String, Object>> getCommitsByDateWise();

	public List<String> getAllUserStoryIds();

	public AnalysisReport getAnalysisReport();

	public MonthWiseResponse getDayWiseUserStories();

	public MonthWiseResponse getDayWiseUserStoriesWithBugsData();

	public List<Map<String, Object>> getWeekWiseData();

	public MonthWiseResponse getDayWiseUserStoriesDummyData();

	// for getting bug data
	public Map<String, Object> getBugData();

	public String getJiraDetailsFull();

	public List<GitUserStorieResource> getUserStorieInfo();

}
