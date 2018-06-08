package com.valuelabs.poc.devops_utms.util;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.valuelabs.poc.devops_utms.resource.Tiers;
import com.valuelabs.poc.devops_utms.resource.appdynamics.Applications;
import com.valuelabs.poc.devops_utms.resource.appdynamics.HealthRuleViolations;
import com.valuelabs.poc.devops_utms.resource.appdynamics.Nodes;

public class AppDynamicsUtil {
	
	Gson gson  = new Gson();
	
	public Applications convertToApplicationsJson(String jsonString) {

		Type collectionType = new TypeToken<Applications>() {
		}.getType();
		Applications applications = gson.fromJson(jsonString, collectionType);

		return applications;
	}
	
	public Nodes convertToNodesJson(String jsonString) {

		Type collectionType = new TypeToken<Nodes>() {
		}.getType();
		Nodes nodes = gson.fromJson(jsonString, collectionType);

		return nodes;
	}
	
	public Tiers convertToTiersJson(String jsonString) {

		Type collectionType = new TypeToken<Tiers>() {
		}.getType();
		Tiers tiers = gson.fromJson(jsonString, collectionType);

		return tiers;
	}
	
	public HealthRuleViolations convertToHealthRuleViolationsJson(String jsonString) {

		Gson json = new GsonBuilder().create();
		Type collectionType = new TypeToken<HealthRuleViolations>() {
		}.getType();
		HealthRuleViolations nodes = json.fromJson(jsonString, collectionType);

		return nodes;
	}

}
