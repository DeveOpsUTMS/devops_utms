package com.valuelabs.poc.devops_utms.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.valuelabs.poc.devops_utms.DevopsUtmsApplicationTests;
import com.valuelabs.poc.devops_utms.controller.JiraController;
import com.valuelabs.poc.devops_utms.service.GitHubService;
import com.valuelabs.poc.devops_utms.service.JiraService;

import net.sf.json.test.JSONAssert;

@RunWith(SpringRunner.class)
@WebMvcTest(value=JiraController.class,secure=false)
@SpringBootTest
public class JiraTestController extends DevopsUtmsApplicationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private JiraService JiraServiceImpl;
	
	@MockBean
	private GitHubService gitHubServiceImpl;
	
	String exampleJson= "[{\"sprintId\":1,\"userStoryId\":\"PROJ-1\",\"sprintDescription\":\"P1 Sprint 1\",\"sprintStartDate\":\"26/03/2018 16:28:15\",\"sprintEndDate\":\"09/04/2018 16:28:00\",\"sprintStatus\":\"active\",\"lastUpdatedTimeStamp\":\"24/05/2018 16:27:50\",\"productName\":\"PROJECT 1\"}]";
	
	@Test
	public void getJiraData(){
		
		Mockito.when(JiraServiceImpl.retirveJiraSprints()).thenReturn(exampleJson);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/jiraapi/jiraSprintData").accept(MediaType.APPLICATION_JSON_VALUE);
		MvcResult result;
		try {
			result = mockMvc.perform(requestBuilder).andReturn();
			System.out.println("response "+result.getResponse().getContentType());
			JSONAssert.assertEquals(exampleJson, result.getResponse().getContentAsString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void getJiraException(){
		Mockito.when(JiraServiceImpl.retirveJiraSprints()).thenReturn(exampleJson);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/jiraapi/jiraSprintData").accept(MediaType.APPLICATION_JSON_VALUE);
		MvcResult result;
		try {
			JSONAssert.assertNotSame(MediaType.APPLICATION_JSON_VALUE, mockMvc.perform(requestBuilder).andReturn().getRequest().getContentType());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
