package com.valuelabs.poc.devops_utms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.valuelabs.poc.devops_utms.client.RunDeckClient;
import com.valuelabs.poc.devops_utms.repository.mongo.RundeckJobRepository;
import com.valuelabs.poc.devops_utms.resource.Execution;
import com.valuelabs.poc.devops_utms.resource.ExecutionEnd;
import com.valuelabs.poc.devops_utms.resource.ExecutionStart;
import com.valuelabs.poc.devops_utms.resource.Executions;
import com.valuelabs.poc.devops_utms.resource.Jobs;
import com.valuelabs.poc.devops_utms.resource.Nodes;
import com.valuelabs.poc.devops_utms.resource.RundeckJob;
import com.valuelabs.poc.devops_utms.resource.RundeckJobName;
import com.valuelabs.poc.devops_utms.resource.RundeckResource;
import com.valuelabs.poc.devops_utms.util.DateTimeUtil;


@Service
public class RundeckService {

	@Autowired
	private RunDeckClient rundeckClient;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private RundeckJobRepository rundeckJobRepository;

	private static final Logger logger = LoggerFactory.getLogger(RundeckService.class);

	@Scheduled(fixedRate = 30000)
	public void retriveExecutions() throws Exception {

		List<RundeckJob> projectResource = new ArrayList<>();
		List<RundeckJob> projectList = rundeckClient.getProjects();

		logger.info("<<<---- Pulling the Rundeck Data --->>>");
		for (RundeckJob projects : projectList) {
			RundeckJob project = new RundeckJob();
			project.setName(projects.getName());
			project.setUrl(projects.getUrl());
			project.setDescription(projects.getDescription());

			List<Jobs> jobsList = new ArrayList<>();
			jobsList.addAll(rundeckClient.getJobs(project.getName()));

			List<Jobs> jobsResource = new ArrayList<>();
			for (int i = 0; i < jobsList.size(); i++) {

				Jobs job = jobsList.get(i);

				Execution execution = new Execution();
				List<Executions> executionList = new ArrayList<>();
				execution = rundeckClient.getExecutions(job.getId());

				for (Executions executions : execution.getExecutions()) {
					Executions executionsResource = new Executions();
					ExecutionEnd executionEnd = executions.getDateEended();

					ExecutionStart executionStart = executions.getDateStarted();
					executionsResource.setDateEended(executionEnd);
					executionsResource.setDateStarted(executionStart);
					executionsResource.setHref(executions.getHref());
					executionsResource.setId(executions.getId());
					executionsResource.setProject(executions.getProject());
					executionsResource.setUser(executions.getUser());
					executionsResource.setStatus(executions.getStatus());

					Nodes nodes = rundeckClient.getNodes(executions.getId());
					executionsResource.setNodes(nodes);
					executionList.add(executionsResource);
				}

				if (!executionList.isEmpty() && executionList != null) {
					String[] env = job.getName().split("_");
					job.setEnv(env[0]);
					job.setDeployedBy(executionList.get(0).getUser());
					job.setRevisionID(job.getId() + "-" + executionList.get(0).getId());
				}
				job.setExecutionsList(executionList);
				jobsResource.add(job);

			}

			project.setJobsList(jobsResource);
			projectResource.add(project);

		}
		rundeckJobRepository.save(projectResource);
	}

	// This for retriving all Rundeck data
	public List<RundeckJob> getRundeckData() {
		List<RundeckJob> rundeckList = new ArrayList<>();

		rundeckList.addAll((List<RundeckJob>) rundeckJobRepository.findAll());

		return rundeckList;
	}

	/*@SuppressWarnings("unchecked")
	public List<RundeckJob> getJobs() {
		List<RundeckJob> rundeckList = new ArrayList<>();

		List<String> projectNames = mongoTemplate.getCollection("rundeckJob").distinct("name");
		for (String name : projectNames) {
			rundeckList.add(rundeckJobRepository.findByName(name));
		}

		return rundeckList;
	}*/
	
	//This is for based on project name retrive rundeck data
	public List<RundeckJob> getRundeckByName(String projectName){
		List<RundeckJob> rundeckList = new ArrayList<>();
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.project("_class", "url", "description", "jobsList"),
				Aggregation.match(Criteria.where("_id").is(projectName)));
		
		AggregationResults<RundeckJob> aggregationResults = mongoTemplate.aggregate(aggregation, "rundeckJob",
				RundeckJob.class);
		rundeckList.addAll(aggregationResults.getMappedResults());
		
		return rundeckList;
	}

	//This is for based on job name retrive rundeck data
	public List<RundeckJobName> getJobByName(String jobName) {
		List<RundeckJobName> rundeckList = new ArrayList<>();
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.project("_class", "url", "description", "jobsList"), Aggregation.unwind("jobsList"),
				Aggregation.match(Criteria.where("jobsList.name").is(jobName)));

		AggregationResults<RundeckJobName> aggregationResults = mongoTemplate.aggregate(aggregation, "rundeckJob",
				RundeckJobName.class);
		rundeckList.addAll(aggregationResults.getMappedResults());

		return rundeckList;
	}

	//This is for based on execution date retrive rundeck data
	public List<RundeckResource> getExecutionByDate(String stringDate) {

		List<RundeckResource> rundeckList = new ArrayList<>();
		Date date = DateTimeUtil.stringToDate(stringDate);
		try {
			Aggregation aggregation = Aggregation.newAggregation(
					Aggregation.project("_class", "url", "description", "jobsList"), Aggregation.unwind("jobsList"),
					Aggregation.unwind("jobsList.executionsList"),
					Aggregation.match(Criteria.where("jobsList.executionsList.dateStarted.date").is(date)));

			AggregationResults<RundeckResource> aggregationResults = mongoTemplate.aggregate(aggregation, "rundeckJob",
					RundeckResource.class);
			rundeckList.addAll(aggregationResults.getMappedResults());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rundeckList;
	}
}
