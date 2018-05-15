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

import com.valuelabs.poc.devops_utms.resource.Execution;
import com.valuelabs.poc.devops_utms.resource.Jobs;
import com.valuelabs.poc.devops_utms.resource.Nodes;
import com.valuelabs.poc.devops_utms.resource.RundeckJob;
import com.valuelabs.poc.devops_utms.util.RunDeckUtil;

@Component
public class RunDeckClient implements CommandLineRunner {

	private static RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private RunDeckUtil runDeckUtil;

	@Value("${rundeck.base.url}")
	private String baseUrl;
	
	@Value("${rundeck.token}")
	private String rundeckToken;

	static {
		List<HttpMessageConverter<?>> messageConverterList = restTemplate.getMessageConverters();

		MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
		messageConverterList.add(jsonMessageConverter);
		restTemplate.setMessageConverters(messageConverterList);

	}

	public List<RundeckJob> getProjects() {

		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path("api/19/projects");
				

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		headers.set("X-Rundeck-Auth-Token",rundeckToken);
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);//

		String jsonString = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, entity, String.class).getBody();
		
		
		return runDeckUtil.convertProjectJsonToList(jsonString);
		
	}

	public List<Jobs> getJobs(String projectName) {

		String path = "/api/19/project/" + projectName + "/jobs";

		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path(path);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		headers.set("X-Rundeck-Auth-Token",rundeckToken);

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		String jsonString = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, entity, String.class).getBody();


		return runDeckUtil.convertJobsJsonToList(jsonString);
	}

	public Execution getExecutions(String jobId) {

		String path = "/api/19/job/"+jobId+"/executions";

		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path(path);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		headers.set("X-Rundeck-Auth-Token",rundeckToken);

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		String jsonString = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, entity, String.class).getBody();
		
		return runDeckUtil.convertExecutionsJsonToList(jsonString);
	}
	
	public Nodes getNodes(int executionId){
		
		String path = "/api/19/execution/"+executionId;

		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path(path);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		headers.set("X-Rundeck-Auth-Token",rundeckToken);

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		String jsonString = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, entity, String.class).getBody();
		
		return runDeckUtil.convertNodesJsonToObject(jsonString);
	}

	@Override
	public void run(String... arg0) throws Exception {

		//getJobs("devops_utms");
		//getProjects();
		
	}

}
