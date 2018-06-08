package com.valuelabs.poc.devops_utms.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Tiers implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SerializedName("tier")
	private List<Tier> tierList = new ArrayList<>();

	public List<Tier> getTierList() {
		return tierList;
	}

	public void setTierList(List<Tier> tierList) {
		this.tierList = tierList;
	}

}
