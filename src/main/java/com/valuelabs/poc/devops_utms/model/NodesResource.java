package com.valuelabs.poc.devops_utms.model;

import java.util.ArrayList;
import java.util.List;

public class NodesResource {

	private List<NodeResource> nodeList = new ArrayList<>();

	public List<NodeResource> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<NodeResource> nodeList) {
		this.nodeList = nodeList;
	}
	
}
