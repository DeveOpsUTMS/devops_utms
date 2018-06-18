package com.valuelabs.poc.devops_utms.resource.appdynamics;

import com.google.gson.annotations.SerializedName;

public class Policies {
	
	@SerializedName("policy-violations")
	private HealthRuleViolations policyViolations;

	public HealthRuleViolations getPolicyViolations() {
		return policyViolations;
	}

	public void setPolicyViolations(HealthRuleViolations policyViolations) {
		this.policyViolations = policyViolations;
	}

}
