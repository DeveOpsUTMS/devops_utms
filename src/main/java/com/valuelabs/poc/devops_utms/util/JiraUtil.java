package com.valuelabs.poc.devops_utms.util;

import java.lang.reflect.Type;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.valuelabs.poc.devops_utms.resource.JiraBoards;
import com.valuelabs.poc.devops_utms.resource.JiraSprints;
import com.valuelabs.poc.devops_utms.resource.JiraUserStories;
import com.valuelabs.poc.devops_utms.resource.Session;
import com.valuelabs.poc.devops_utms.resource.SessionManagement;
import com.valuelabs.poc.devops_utms.resource.UserStory;

@Component
public class JiraUtil {

	Gson gson = new Gson();

	public Session convertToSessionJson(String jsonString) {

		Type collectionType = new TypeToken<SessionManagement>() {
		}.getType();
		SessionManagement session = gson.fromJson(jsonString, collectionType);

		return session.getSession();
	}

	public JiraBoards convertToJiraBoardsJson(String jsonString) {

		Type collectionType = new TypeToken<JiraBoards>() {
		}.getType();
		JiraBoards jiraBoards = gson.fromJson(jsonString, collectionType);

		return jiraBoards;
	}
	
	public JiraSprints convertToJiraSprintsJson(String jsonString) {
		
		Type collectionType = new TypeToken<JiraSprints>() {
		}.getType();
		JiraSprints jiraSprints = gson.fromJson(jsonString, collectionType);
		
		return jiraSprints;
	}
	
	public JiraUserStories convertToJiraUserStoriesJson(String jsonString){
		
		Type collectionType = new TypeToken<JiraUserStories>() {
		}.getType();
		JiraUserStories jiraUserStories = gson.fromJson(jsonString, collectionType);
		
		return jiraUserStories;
	}
	
	public UserStory convertToJiraUserStoryJson(String jsonString){
		
		Type collectionType = new TypeToken<UserStory>() {
		}.getType();
		UserStory jiraUserStory = gson.fromJson(jsonString, collectionType);
		
		return jiraUserStory;
	}
}
