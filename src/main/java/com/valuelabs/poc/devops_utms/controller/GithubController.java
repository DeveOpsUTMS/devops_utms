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
@RequestMapping("/gitapi")
public class GithubController {

	@Autowired
	private GitHubService gitHubServiceImpl;

	private static final Logger logger = LoggerFactory.getLogger(GithubController.class);

	@RequestMapping(value = "/getAllGitCommits", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllGitCommits() throws Exception {
		logger.info(" Calling the getAllSVNCommits Webservice");
		try {
			return new ResponseEntity<>(gitHubServiceImpl.getCommitsByDateWise(), HttpStatus.OK);

		} catch (Exception e) {
			logger.error("getSvnCommits()", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/getAnalysisDetails", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> getAnalysisDetails() throws Exception {
		logger.info(" Calling the getAnalysisDetails Webservice");
		try {
			return new ResponseEntity<>(gitHubServiceImpl.getAnalysisReport(), HttpStatus.OK);

		} catch (Exception e) {
			logger.error("getAnalysisDetails()", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/getDayWiseUserStoriesWithBugsData", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> getDayWiseUserStoriesWithWise() throws Exception {
		logger.info(" Calling the getDayWiseUserStoriesWithBugsData Webservice");
		try {
			return new ResponseEntity<>(gitHubServiceImpl.getDayWiseUserStoriesWithBugsData(), HttpStatus.OK);

		} catch (Exception e) {
			logger.error("getDayWiseUserStoriesWithBugsData()", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
}
	
	@RequestMapping(value = "/getDayWiseUserStories", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> getDayWiseUserStories() throws Exception {
		logger.info(" Calling the getDayWiseUserStories Webservice");
		try {
			return new ResponseEntity<>(gitHubServiceImpl.getDayWiseUserStories(), HttpStatus.OK);

		} catch (Exception e) {
			logger.error("getDayWiseUserStories()", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
}

	@RequestMapping(value = "/getWeekWiseData", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> getWeekWiseData() throws Exception {
		logger.info(" Calling the getWeekWiseData Webservice");
		try {
			return new ResponseEntity<>(gitHubServiceImpl.getWeekWiseData(), HttpStatus.OK);

		} catch (Exception e) {
			logger.error("getWeekWiseData()", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@RequestMapping(value = "/gitUserStoryInfo", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> getUserStoryeData() throws Exception {
		logger.info(" Calling the getWeekWiseData Webservice");
		try {
			return new ResponseEntity<>(gitHubServiceImpl.getUserStorieInfo(), HttpStatus.OK);

		} catch (Exception e) {
			logger.error("getWeekWiseData()", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/*@RequestMapping(value = "/getJiraFullDetails", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> getJiraFullDetails() throws Exception {
		logger.info(" Calling the getJiraFullDetails Webservice");
		try {
			return new ResponseEntity<>(gitHubServiceImpl.getJiraDetailsFull(), HttpStatus.OK);

		} catch (Exception e) {
			logger.error("getJiraFullDetails()", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}*/

}
