package com.valuelabs.poc.devops_utms.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.valuelabs.poc.devops_utms.resource.appdynamics.Applications;
import com.valuelabs.poc.devops_utms.resource.appdynamics.HealthRuleViolations;
import com.valuelabs.poc.devops_utms.resource.appdynamics.Nodes;
import com.valuelabs.poc.devops_utms.util.AppDynamicsUtil;

@Component
public class AppDynamicsClient {

	private static RestTemplate restTemplate = new RestTemplate();

	@Autowired
	AppDynamicsUtil appDynamicsUtil;

	@Value("${appdynamics.base.url}")
	private String baseurl;

	@Value("${appdynamics.username}")
	private String username;

	@Value("${appdynamics.password}")
	private String password;

	@Value("${appdynamics.list.applications}")
	private String applicationByList;
	@Value("${appdynamics.nodes.by.application}")
	private String nodesByApplication;
	@Value("${appdynamics.health.rule.violations}")
	private String healthRuleViolations;

	static {
		List<HttpMessageConverter<?>> messageConverterList = restTemplate.getMessageConverters();

		MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
		messageConverterList.add(jsonMessageConverter);
		restTemplate.setMessageConverters(messageConverterList);

	}

	public Applications getApplications() {
		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(baseurl).path(applicationByList);
		HttpHeaders headers = new HttpHeaders();
		// headers.set("", "");
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		Map<String, String> map = new HashMap<>();
		map.put("output", "JSON");

		String jsonString = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, entity, String.class, map)
				.getBody();

		return appDynamicsUtil.convertToApplicationsJson(jsonString);
	}

	public Nodes getNodes(int applicationid) {

		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(baseurl).path(applicationByList)
				.path(nodesByApplication.replace("{applicationId}", applicationid + ""));
		HttpHeaders headers = new HttpHeaders();
		// headers.set("", "");
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		Map<String, String> map = new HashMap<>();
		map.put("output", "JSON");

		String jsonString = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, entity, String.class, map)
				.getBody();

		return appDynamicsUtil.convertToNodesJson(jsonString);
	}

	public HealthRuleViolations getHealthRuleViolations(int applicationid) {

		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(baseurl).path(applicationByList)
				.path(healthRuleViolations.replace("{applicationId}", applicationid + ""));
		HttpHeaders headers = new HttpHeaders();
		// headers.set("", "");
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		Map<String, String> map = new HashMap<>();
		map.put("output", "JSON");

		String jsonString = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, entity, String.class, map)
				.getBody();

		return appDynamicsUtil.convertToHealthRuleViolationsJson(jsonString);
	}

}
