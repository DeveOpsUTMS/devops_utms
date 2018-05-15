package com.valuelabs.poc.devops_utms.service;

import java.util.List;
import java.util.Map;

import com.valuelabs.poc.devops_utms.resource.BoardResource;
import com.valuelabs.poc.devops_utms.resource.BoardsResource;
import com.valuelabs.poc.devops_utms.resource.BugResource;
import com.valuelabs.poc.devops_utms.resource.JiraBoard;
import com.valuelabs.poc.devops_utms.resource.JiraBoardResource;
import com.valuelabs.poc.devops_utms.resource.JiraBoards;
import com.valuelabs.poc.devops_utms.resource.JiraBoardsResource;
import com.valuelabs.poc.devops_utms.resource.SubTaskResource;
import com.valuelabs.poc.devops_utms.resource.UserStoryResource;


public interface JiraService {
	
	//public void getStorysFromJira(JiraPropertiesRepository jiraPropertiesRepository) throws Exception;
	
	//public Iterable<JiraPropertys> getAllJiraDetails();
	
	//public ArrayList getAllJiraIds();
	
	public List<JiraBoard> syncJiraInfo() throws Exception;

	//public Object retriveJiraBoards();
	
	public List<JiraBoardResource> getUserStories();
	
	public List<JiraBoardResource> getSubTasks();
	
	public List<JiraBoardResource> getBugs();
	
	public List<JiraBoardResource> getJiraByProjectName(String projectName);
	
	public List<BoardResource> retirveJiraSprintsByStartDate(String startDate ,String endDate);

	List<JiraBoard> getJiraData();

	List<JiraBoard> retriveJiraData();

	public List<UserStoryResource> getuserStory();
	
	public List<SubTaskResource> getSubTask();
	
	public List<BugResource> getBug();

	//List<BoardsResource> retirveJiraSprintsDetails();

	public Object retirveJiraSprints();

	public Object getUserStory();

	public Object getSubUserStory();

	public Object getUserStoryTaskDetails();

}
