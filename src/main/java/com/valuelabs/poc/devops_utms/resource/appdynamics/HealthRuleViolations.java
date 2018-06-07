package com.valuelabs.poc.devops_utms.resource.appdynamics;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;


public class HealthRuleViolations {
	
	@SerializedName("policy-violation")
	private List<HealthRuleViolation> healthRuleViolation  = new ArrayList<>();

}
