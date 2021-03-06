package com.valuelabs.poc.devops_utms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.valuelabs.poc.devops_utms.client.AppDynamicsClient;
import com.valuelabs.poc.devops_utms.model.MetricData;
import com.valuelabs.poc.devops_utms.model.NodeResource;
import com.valuelabs.poc.devops_utms.model.NodesResource;
import com.valuelabs.poc.devops_utms.model.TierResource;
import com.valuelabs.poc.devops_utms.model.TiersResource;
import com.valuelabs.poc.devops_utms.repository.mongo.AppDynamicsRepository;
import com.valuelabs.poc.devops_utms.resource.Tier;
import com.valuelabs.poc.devops_utms.resource.Tiers;
import com.valuelabs.poc.devops_utms.resource.appdynamics.Application;
import com.valuelabs.poc.devops_utms.resource.appdynamics.MetricReport;
import com.valuelabs.poc.devops_utms.resource.appdynamics.MetricResource;
import com.valuelabs.poc.devops_utms.resource.appdynamics.Node;
import com.valuelabs.poc.devops_utms.resource.appdynamics.Nodes;

@Service
public class AppDynamicService {

	@Autowired
	private AppDynamicsClient appDynamicsClient;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private AppDynamicsRepository appDynamicsRepository;

	public void getMetricData() {

		MetricData mdata = new MetricData();

		List<Application> appList = appDynamicsClient.getApplications().getApplication();

		for (Application application : appList) {

			// Retriving application info
			int applicationId = application.getId();
			mdata.setApplicationId(applicationId);
			mdata.setDescription(application.getDescription());
			mdata.setName(application.getName());
			Tiers tiers = appDynamicsClient.getTiers(applicationId);

			TiersResource tiersResource = new TiersResource();

			List<TierResource> tierList = new ArrayList<>();

			for (Tier tier : tiers.getTierList()) {
				TierResource tierResource = new TierResource();
				tierResource.setAgentType(tier.getAgentType());
				tierResource.setName(tier.getName());
				tierResource.setNumberOfNodes(tier.getNumberOfNodes());
				tierResource.setTierId(tier.getId());
				tierResource.setType(tier.getType());

				Nodes nodes = appDynamicsClient.getNodes(applicationId);
				NodesResource nodesResource = new NodesResource();
				List<NodeResource> nodeList = new ArrayList<>();
				for (Node node : nodes.getNodes()) {
					if (node.getTierId() == tierResource.getTierId()) {
						String appName = application.getName();
						String tierName = tier.getName();
						String nodeName = node.getName();
						NodeResource nodeResource = new NodeResource();
						nodeResource.setMachineOSType(node.getMachineOSType());
						nodeResource.setName(nodeName);
						MetricReport metrics = appDynamicsClient.getMetrics(appName, tierName, nodeName);
						// metrics.setMetrics(metrics);

						nodeResource.setNodeId(node.getId());
						nodeResource.setTierId(node.getTierId());
						nodeResource.setTierName(tierName);
						nodeResource.setType(node.getType());
						nodeResource.setMetricReport(metrics);

						nodeList.add(nodeResource);
					}

				}
				nodesResource.setNodeList(nodeList);
				tierResource.setNodesResource(nodesResource);
				tierList.add(tierResource);
			}
			tiersResource.setTierList(tierList);

		}

		appDynamicsRepository.save(mdata);

	}

	// This will retrive all appDynamics metrics
	public List<MetricData> retriveMetricsData() {
		List<MetricData> metricList = new ArrayList<>();

		metricList.addAll((List<MetricData>) appDynamicsRepository.findAll());

		return metricList;
	}

	public List<MetricResource> getMetricsByTierId(String tierId) {
		List<MetricResource> metricList = new ArrayList<>();
		Aggregation aggregation = Aggregation.newAggregation(Aggregation.project("applicationId", "name", "tiers"),
				Aggregation.unwind("tiers.tierList"),
				Aggregation.match(Criteria.where("tiers.tierList.tierId").is(tierId)));
		
		AggregationResults<MetricResource> aggregationResults = mongoTemplate.aggregate(aggregation, "metricData",
				MetricResource.class);
		metricList.addAll(aggregationResults.getMappedResults());

		return metricList;
	}

	//by using job id
	public List<MetricData> getMetricsByNodeId() {

		return null;
	}

}
