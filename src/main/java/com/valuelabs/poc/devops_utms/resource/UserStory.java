package com.valuelabs.poc.devops_utms.resource;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties (ignoreUnknown = true)
public class UserStory implements Serializable {
	private static final long serialVersionUID = 1L;

	@SerializedName("id")
	@Id
	private String userStoryId;

	private String key;

	@SerializedName("fields")
	private UserStoryFields fields;
	
	public UserStoryFields getFields() {
		return fields;
	}

	public void setFields(UserStoryFields fields) {
		this.fields = fields;
	}

	public UserStory() {
	}
	

	public String getUserStoryId() {
		return userStoryId;
	}

	public void setUserStoryId(String userStoryId) {
		this.userStoryId = userStoryId;
	}
	
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	/*public UserStoryFields getFields() {
		return fields;
	}

	public void setFields(UserStoryFields fields) {
		this.fields = fields;
	}*/

	@Override
	public String toString() {
		return "UserStory [userStoryId=" + userStoryId + ", key=" + key +  "]";
	}

}
