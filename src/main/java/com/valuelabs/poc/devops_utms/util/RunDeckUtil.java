package com.valuelabs.poc.devops_utms.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.valuelabs.poc.devops_utms.resource.Execution;
import com.valuelabs.poc.devops_utms.resource.Jobs;
import com.valuelabs.poc.devops_utms.resource.Nodes;
import com.valuelabs.poc.devops_utms.resource.RundeckJob;

@Component
public class RunDeckUtil {

	public List<RundeckJob> convertProjectJsonToList(String jsonString) {
		Gson gson = new Gson();

		Type collectionType = new TypeToken<ArrayList<RundeckJob>>() {
		}.getType();
		List<RundeckJob> projectsList = gson.fromJson(jsonString, collectionType);

		return projectsList;

	}

	public List<Jobs> convertJobsJsonToList(String jsonString) {
		Gson gson = new Gson();

		Type collectionType = new TypeToken<ArrayList<Jobs>>() {
		}.getType();
		List<Jobs> jobsList = gson.fromJson(jsonString, collectionType);

		return jobsList;

	}

	public Execution convertExecutionsJsonToList(String jsonString) {
		Gson gson = new Gson();

		Type collectionType = new TypeToken<Execution>() {
		}.getType();
		Execution execution = gson.fromJson(jsonString, collectionType);

		return execution;

	}

	public Nodes convertNodesJsonToObject(String jsonString) {

		Gson gson = new Gson();

		Type collectionType = new TypeToken<Nodes>() {}.getType();
		Nodes nodes = gson.fromJson(jsonString, collectionType);

		return nodes;
	}

}
