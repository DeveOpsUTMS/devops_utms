package com.valuelabs.poc.devops_utms.model;

import java.util.ArrayList;
import java.util.List;

public class TiersResource {

	private List<TierResource> tierList = new ArrayList<>();

	public List<TierResource> getTierList() {
		return tierList;
	}

	public void setTierList(List<TierResource> tierList) {
		this.tierList = tierList;
	}
	
}
