package com.valuelabs.poc.devops_utms.resource;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class JiraBoardResource {

	private Integer boardId;
	
	private String boardName;
	
	private String type;

	//JiraSprints jiraSprints;
	
	@Id
	private String boardUniqueId;
	
	public String getBoardUniqueId() {
		return boardUniqueId;
	}

	public void setBoardUniqueId(String boardUniqueId) {
		this.boardUniqueId = boardUniqueId;
	}

	private JiraSprintResource jiraSprints;

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

	public JiraSprintResource getJiraSprints() {
		return jiraSprints;
	}

	public void setJiraSprints(JiraSprintResource jiraSprints) {
		this.jiraSprints = jiraSprints;
	}
	
}
