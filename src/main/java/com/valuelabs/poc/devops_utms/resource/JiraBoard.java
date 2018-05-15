package com.valuelabs.poc.devops_utms.resource;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document
public class JiraBoard implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//@JsonProperty("id")
	@SerializedName("id")
	private Integer boardId;
	
	@Id
	private String boardUniqueId;
	
	
	public String getBoardUniqueId() {
		return boardUniqueId;
	}

	public void setBoardUniqueId(String boardUniqueId) {
		this.boardUniqueId = boardUniqueId;
	}

	//@JsonProperty("name")
	@SerializedName("name")
	private String boardName;
	
	private String type;

	JiraSprints jiraSprints;
	
	//JiraUserStories backlogItems;
	
	public Integer getBoardId() {
		return boardId;
	}

	public void setBoardId(Integer boardId) {
		this.boardId = boardId;
	}

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public JiraSprints getJiraSprints() {
		return jiraSprints;
	}

	public void setJiraSprints(JiraSprints jiraSprints) {
		this.jiraSprints = jiraSprints;
	}

	/*public JiraUserStories getBacklogItems() {
		return backlogItems;
	}

	public void setBacklogItems(JiraUserStories backlogItems) {
		this.backlogItems = backlogItems;
	}*/

	@Override
	public String toString() {
		return "JiraBoard [boardId=" + boardId + ", boardName=" + boardName + ", type=" + type + "]";
	}
	
}

