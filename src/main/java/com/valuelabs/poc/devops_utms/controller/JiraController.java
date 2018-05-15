package com.valuelabs.poc.devops_utms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.valuelabs.poc.devops_utms.service.JiraService;

@RestController
@RequestMapping("/jiraapi")
public class JiraController {

	private static final Logger logger = LoggerFactory.getLogger(JiraController.class);

	@Autowired
	private JiraService JiraServiceImpl;

	/*
	 * @RequestMapping(value="/getJiradetails",method = RequestMethod.GET)
	 * public ResponseEntity<Object> getJiraStories(){
	 * logger.info(" getting all JiraDetails Webservice"); try{ return new
	 * ResponseEntity<>(JiraServiceImpl.getAllJiraDetails(),HttpStatus.OK); }
	 * catch (Exception e ){ logger.error("getJiradetails()", e); return new
	 * ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); }
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/getJiraid",method = RequestMethod.GET) public
	 * ResponseEntity<Object> getJiraIds(){
	 * logger.info(" getting all JiraID's Webservice"); try{ return new
	 * ResponseEntity<>(JiraServiceImpl.getAllJiraIds(),HttpStatus.OK); } catch
	 * (Exception e ){ logger.error("getJiraIds()", e); return new
	 * ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); }
	 * 
	 * }
	 */

	@RequestMapping(value = "/jira", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> syncJiraInfo() {
		logger.info(" getting jira Webservice");

		try {
			return new ResponseEntity<>(JiraServiceImpl.retriveJiraData(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("syncJiraInfo()", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	

	@RequestMapping(value = "/jiraUserStories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> retriveUserStories() {

		logger.info(" getting jira User Stories");

		try {
			return new ResponseEntity<>(JiraServiceImpl.getuserStory(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("jiraUserStories()", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/jiraBugs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> retriveBugs() {

		logger.info(" getting jira Bugs");

		try {
			return new ResponseEntity<>(JiraServiceImpl.getBug(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("retriveBugs()", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/jiraSubTasks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> retriveSubTasks() {

		logger.info(" getting jira SubTasks");

		try {
			return new ResponseEntity<>(JiraServiceImpl.getSubTask(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("retriveSubTasks()", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/jiraByProjectName", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> retriveJiraByProjectName(@RequestParam("name") String projectName) {

		logger.info(" getting jira By Project Name");

		try {
			return new ResponseEntity<>(JiraServiceImpl.getJiraByProjectName(projectName), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("jiraByProjectName()", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/jiraBySprintStartDate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> retriveJiraBySprintStartDate(@RequestParam(value = "startDate") String startDate,@RequestParam(value = "endDate",defaultValue="") String endDate) {

		logger.info(" getting jira By Sprint Start Date");

		try {
			return new ResponseEntity<>(JiraServiceImpl.retirveJiraSprintsByStartDate(startDate,endDate), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("jiraBySprintStartDate()", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/jiraSprintData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> retriveJiraSprintData() {

		logger.info(" getting jira By Sprint Data");

		try {
			return new ResponseEntity<>(JiraServiceImpl.retirveJiraSprints(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("retriveJiraSprintData()", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}
