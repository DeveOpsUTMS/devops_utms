package com.valuelabs.poc.devops_utms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.valuelabs.poc.devops_utms.service.SchedularService;

@RestController
@RequestMapping("/schedular")
public class SchedularController {
	
	@Autowired
	SchedularService schedularService;
	
	private static final Logger logger = LoggerFactory.getLogger(SchedularController.class);
	
	
	@RequestMapping(value = "/tasks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> retriveUserStories() {

		logger.info(" Starting Schedular");

		try {
			String result = schedularService.runServices();
			return new ResponseEntity<>(result,HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error Occured in Schedular Service", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

}
