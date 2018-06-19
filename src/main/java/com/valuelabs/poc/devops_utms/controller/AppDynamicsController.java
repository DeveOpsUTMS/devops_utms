package com.valuelabs.poc.devops_utms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.valuelabs.poc.devops_utms.model.MetricData;
import com.valuelabs.poc.devops_utms.service.AppDynamicService;

@RestController
@RequestMapping("/appDynamics")
public class AppDynamicsController {

	@Autowired
	private AppDynamicService appDynamicService;

	@RequestMapping(value = "/appDynamicsDataList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMonitoringData() {
		try {
			List<MetricData> metricList = appDynamicService.retriveMetricsData();
			return new ResponseEntity<>(metricList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
