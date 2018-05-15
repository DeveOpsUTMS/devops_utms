package com.valuelabs.poc.devops_utms.resource;

import com.google.gson.annotations.SerializedName;

public class Subtasks {
	
	@SerializedName("key")
	private String taskId;
	
	private UserStory subUserStory;
	
	

	public UserStory getSubUserStory() {
		return subUserStory;
	}

	public void setSubUserStory(UserStory subUserStory) {
		this.subUserStory = subUserStory;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
