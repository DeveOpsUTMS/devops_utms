package com.valuelabs.poc.devops_utms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.valuelabs.poc.devops_utms.service.GitHubService;


@RestController
public class GitController {
	
	@Autowired
	private GitHubService gitHubServiceImpl;
	
	 private static final Logger logger = LoggerFactory.getLogger(GitController.class);
	
	 @RequestMapping(value="/getDayWiseUserStoriesDummyData", method = RequestMethod.GET, produces = "application/json")
	 public ResponseEntity<Object> getDayWiseUserStories() throws Exception {
		 logger.info(" Calling the getDayWiseUserStoriesDummyData Webservice");
		 try {
			   return new ResponseEntity<>(gitHubServiceImpl.getDayWiseUserStoriesDummyData(),HttpStatus.OK);
	    	   
		 } catch (Exception e) {
	       		logger.error("getDayWiseUserStoriesDummyData()", e);
	       		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	       	}
	 
	 }
	
	
}
