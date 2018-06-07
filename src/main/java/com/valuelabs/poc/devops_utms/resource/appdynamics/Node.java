package com.valuelabs.poc.devops_utms.resource.appdynamics;

import java.io.Serializable;

public class Node implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getTierId() {
		return tierId;
	}
	public void setTierId(int tierId) {
		this.tierId = tierId;
	}
	public String getTierName() {
		return tierName;
	}
	public void setTierName(String tierName) {
		this.tierName = tierName;
	}
	public String getMachineOSType() {
		return machineOSType;
	}
	public void setMachineOSType(String machineOSType) {
		this.machineOSType = machineOSType;
	}
	private int id;
	private String name;
	private String type;
	private int tierId;
	private String tierName;
	private String machineOSType;

}
