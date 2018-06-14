package com.valuelabs.poc.devops_utms.repository.mongo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.valuelabs.poc.devops_utms.resource.RundeckJob;

public interface RundeckJobRepository extends PagingAndSortingRepository<RundeckJob,String> {
	RundeckJob findAllByName(String name);
	
	//@Query("SELECT rJob " + "FROM RundeckJob rJob LEFT JOIN Jobs jobs LEFT JOIN Executions executions " + "WHERE jobs.project = executions.project AND rJob.project = jobs.project AND executions.dateStarted.date = :date ")
	//RundeckJob findAllByDate(@Param("date") Date date);
	
	//@Query("SELECT rJob " + "FROM RundeckJob rJob LEFT JOIN Jobs jobs " + "WHERE rJob.project = jobs.project")
	RundeckJob findByName(String name);
	
	
}
