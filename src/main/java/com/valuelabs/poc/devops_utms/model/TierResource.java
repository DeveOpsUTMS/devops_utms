package com.valuelabs.poc.devops_utms.model;

public class TierResource {
	
	
	private int tierId;
	private String name;
	private String type;
	private String agentType;
	private int numberOfNodes;
	private NodesResource nodesResource;
	
	public NodesResource getNodesResource() {
		return nodesResource;
	}
	public void setNodesResource(NodesResource nodesResource) {
		this.nodesResource = nodesResource;
	}
	public int getTierId() {
		return tierId;
	}
	public void setTierId(int tierId) {
		this.tierId = tierId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	public int getNumberOfNodes() {
		return numberOfNodes;
	}
	public void setNumberOfNodes(int numberOfNodes) {
		this.numberOfNodes = numberOfNodes;
	}
	

}
