package com.valuelabs.poc.devops_utms.resource;

public class UserStorieDetailsResource {
	
	private String userStoryId;

	private String key;

	private UserStoryFields fields;

	public String getUserStoryId() {
		return userStoryId;
	}

	public void setUserStoryId(String userStoryId) {
		this.userStoryId = userStoryId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public UserStoryFields getFields() {
		return fields;
	}

	public void setFields(UserStoryFields fields) {
		this.fields = fields;
	}

}
