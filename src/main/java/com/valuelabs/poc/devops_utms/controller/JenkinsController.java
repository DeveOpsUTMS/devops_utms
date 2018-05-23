package com.valuelabs.poc.devops_utms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.valuelabs.poc.devops_utms.service.JenkinService;

@RestController
public class JenkinsController {
	@Autowired
	private JenkinService jenkinService;

	@RequestMapping(value = "/getJenkinsBuildInfo", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getJenkinsBuildInfo() {
		try {
			return jenkinService.getJenkinsBuildInfo();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getBuildDetails", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getBuildDetails(){
	try {
		return jenkinService.getBuildDetails();
	} catch (Exception e) {
		e.printStackTrace();
		return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
	@RequestMapping(value="/testResults",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getTestResults(){
		
		try {
			return new ResponseEntity<>(jenkinService.getTestResults(null), HttpStatus.ACCEPTED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
