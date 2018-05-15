package com.valuelabs.poc.devops_utms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.valuelabs.poc.devops_utms.resource.RundeckJob;
import com.valuelabs.poc.devops_utms.resource.RundeckJobName;
import com.valuelabs.poc.devops_utms.resource.RundeckResource;
import com.valuelabs.poc.devops_utms.service.RundeckService;

@RestController
@RequestMapping("/rundeck")
public class RunDeckController {

	@Autowired
	private RundeckService rundeckService;

	@RequestMapping(value = "/executionsByDate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getExecutionsByDate(@RequestParam("date") String stringDate) {
		List<RundeckResource> projectsList = null;
		try {
			projectsList = rundeckService.getExecutionByDate(stringDate);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("something went wrong", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(projectsList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/executionsByJobName", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getExecutionsByJobName(@RequestParam("name") String jobName) {
		List<RundeckJobName> projectsList = null;
		try {
			projectsList = rundeckService.getJobByName(jobName);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("something went wrong", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(projectsList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/executionsByProjectName", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getExecutionsByProjectName(@RequestParam("name") String projectName) {
		List<RundeckJob> projectsList = null;
		try {
			projectsList = rundeckService.getRundeckByName(projectName);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("something went wrong", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(projectsList, HttpStatus.OK);
	}
	@RequestMapping(value = "/executions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getExecutions() {
		List<RundeckJob> projectsList = null;
		try {
			projectsList = rundeckService.getRundeckData();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("something went wrong", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(projectsList, HttpStatus.OK);
	}
	
}
