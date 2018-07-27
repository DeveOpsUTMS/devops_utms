package com.valuelabs.poc.devops_utms.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.valuelabs.poc.devops_utms.resource.JiraBoards;
import com.valuelabs.poc.devops_utms.resource.JiraSprints;
import com.valuelabs.poc.devops_utms.resource.JiraUserStories;
import com.valuelabs.poc.devops_utms.resource.Session;
import com.valuelabs.poc.devops_utms.resource.UserStory;
import com.valuelabs.poc.devops_utms.util.JiraUtil;

@Component
public class JiraClient implements CommandLineRunner{
	
	private static RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private JiraUtil jiraUtil;
	
	@Value("${jira.base.url}")
	private String BASE_URL;
	//private static final String FIELDS_INFO = "id,name,project,summary,description,priority";
	//private static final String GET_ALL_PROJECTS = "api/2/project";
	//private static final String GET_STORIES_WITH_PAGINATION = "api/2/search";
	@Value("${jira.get_issue.by.issue.id}")
	private String GET_ISSUE_BY_ISSUE_ID;
	@Value("${jira.establish.session}")
	private String ESTABLISH_SESSION;
	@Value("${jira.boards.info}")
	private String GET_BOARDS_INFO;
	@Value("${jira.get.sprints.by.board}")
	private String GET_SPRINTS_BY_BOARD;
	@Value("${jira.get.sprint.based.issues}")
	private String GET_SPRINT_BASED_ISSUES;
	//private static final String GET_BOARD_BASED_BACKLOG_ITEMS = "agile/1.0/board/{BoardId}/backlog";
	

	static {
		List<HttpMessageConverter<?>> messageConverterList = restTemplate.getMessageConverters();

		MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
		messageConverterList.add(jsonMessageConverter);
		restTemplate.setMessageConverters(messageConverterList);

	}
	
	public JiraBoards getJiraBoards(){
		
		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(BASE_URL).path(GET_BOARDS_INFO);
		Session session = getAuthentication();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type","application/json;charset=UTF-8");
		headers.set("Cookie",session.getName()+"="+session.getValue());
		headers.set("X-AUSERNAME", "admin");
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		String jsonString = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, entity, String.class).getBody();
		
		return jiraUtil.convertToJiraBoardsJson(jsonString);
	}
	
	public JiraSprints getJiraSprints(String boardId){
		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(BASE_URL).path(GET_SPRINTS_BY_BOARD.replace("{BoardId}", boardId+""));
		Session session = getAuthentication();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type","application/json;charset=UTF-8");
		headers.set("Cookie",session.getName()+"="+session.getValue());
		headers.set("X-AUSERNAME", "admin");
		
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		String jsonString = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, entity, String.class).getBody();
		
		return jiraUtil.convertToJiraSprintsJson(jsonString);
	}
	
	public JiraUserStories getJiraUserStories(String boardId,String sprintId){

		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(BASE_URL).path(GET_SPRINT_BASED_ISSUES.replace("{BoardId}", boardId+"").replace("{SprintId}", sprintId+""));
		Session session = getAuthentication();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type","application/json;charset=UTF-8");
		headers.set("Cookie",session.getName()+"="+session.getValue());
		headers.set("X-AUSERNAME", "admin");
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		String jsonString = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, entity, String.class).getBody();
		//System.out.println("jsonString "+jsonString);
		
		return jiraUtil.convertToJiraUserStoriesJson(jsonString);
	}
	
	public UserStory getJiraIssue(String issueId){
		
		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(BASE_URL).path(GET_ISSUE_BY_ISSUE_ID.replace("{IssueId}", issueId+""));
		Session session = getAuthentication();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type","application/json;charset=UTF-8");
		headers.set("Cookie",session.getName()+"="+session.getValue());
		headers.set("X-AUSERNAME", "admin");
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		String jsonString = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, entity, String.class).getBody();
		
		return jiraUtil.convertToJiraUserStoryJson(jsonString);
	}
	
	public Session getAuthentication(){
		

		String uri = BASE_URL.concat(ESTABLISH_SESSION);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type","application/json;charset=UTF-8");
		
		String requestBody = "{" + "\"username\":\"admin\"" + ","  + "\"password\":\"NewPass@123\"" + "}";
		

		HttpEntity<?> entity = new HttpEntity<>(requestBody,headers);
		
		String jsonString = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class).getBody();
		
		//ResponseEntity<String> response = restTemplate.postForEntity("https://tafdevops.atlassian.net/rest/auth/1/session", entity, String.class);
		
		
		return jiraUtil.convertToSessionJson(jsonString);
	}
	
	//
	
	@Override
	public void run(String... args) throws Exception {
		
		//getJiraBoards();
	}

}
