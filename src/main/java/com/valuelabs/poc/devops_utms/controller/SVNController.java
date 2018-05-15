package com.valuelabs.poc.devops_utms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.valuelabs.poc.devops_utms.service.SVNService;


@RestController
@RequestMapping("/api")
public class SVNController {

	@Autowired
	private SVNService svnService;
	
	 private static final Logger logger = LoggerFactory.getLogger(SVNController.class);
	 @RequestMapping(value="/getAllSVNCommits", method = RequestMethod.GET)
	 public ResponseEntity<Object> getSvnCommits() throws Exception {
		 logger.info(" Calling the getAllSVNCommits Webservice");
		 try {
			   return new ResponseEntity<>(svnService.getCommitsByDateWise(),HttpStatus.OK);
	    	   
	       	} catch (Exception e) {
	       		logger.error("getSvnCommits()", e);
	       		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	       	}
	 }
}
	

