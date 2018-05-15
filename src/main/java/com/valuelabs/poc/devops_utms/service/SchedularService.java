package com.valuelabs.poc.devops_utms.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedularService {

	@Autowired
	JiraServiceImpl jiraServiceImpl;

	@Autowired
	GitHubServiceImpl gitHubServiceImpl;

	@Autowired
	RundeckService rundeckService;

	@Autowired
	JenkinService jenkinService;

	private static final Logger logger = LoggerFactory.getLogger(SchedularService.class);
	
	public String runServices() throws Exception {

		String msg = "Batch process started";
		logger.info(msg);
		try {
			FileInputStream fstream = new FileInputStream("src/main/java/com/valuelabs/poc/devops_utms/files/schedular_properties.csv");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				String[] services = strLine.split(",");
				System.out.println(" running service is " + services[0]);
				

				if (services[0].equalsIgnoreCase("JiraService")) {
					try {
						jiraServiceImpl.syncJiraInfo();
					} catch (Exception e) {
						msg = "Batch process not completed error occured in Jira Service";
						logger.error("Batch process not completed error occured in Jira Service");
						e.printStackTrace();
						//return msg;
					}
				} else if (services[0].equalsIgnoreCase("GitService")) {
					try{
					gitHubServiceImpl.readGitHubData();
					}catch(Exception e){
						msg = "Batch process not completed error occured in Git Service";
						logger.error("Batch process not completed error occured in Git Service");
						e.printStackTrace();
						//return msg; 
					}
				} else if (services[0].equalsIgnoreCase("JenkinService")) {
					try {
						jenkinService.retrieveJenkinJobs();
					} catch (Exception e) {
						msg = "Batch process not completed error occured in Jenkins Service";
						logger.error("Batch process not completed error occured in Jenkins Service");
						e.printStackTrace();
						//return msg; 
					}
				} else if (services[0].equalsIgnoreCase("RundeckService")) {
					try {
						rundeckService.retriveExecutions();
					} catch (Exception e) {
						msg = "Batch process not completed error occured in Rundeck Service";
						logger.error("Batch process not completed error occured in Rundeck Service");
						e.printStackTrace();
						//return msg; 
					}
				} else {
					msg = "Service or method name not correct";
					logger.error("Service or method name not correct");
					//return msg; 
				}
			}
		logger.info("Batch process end");
		} catch (IOException e) {
			logger.error("Batch process not completed");
			e.printStackTrace();
		}
		return msg;
	}

}
