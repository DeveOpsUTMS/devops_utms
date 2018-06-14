package com.valuelabs.poc.devops_utms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
	
	public void getMetricData(){
		
		MetricData mdata = new MetricData();
		
		List<Application> appList = appDynamicsClient.getApplications().getApplication();
		
		for(Application application:appList){
			
			//Retriving application info
			int applicationId = application.getId();
			mdata.setApplicationId(applicationId);
			mdata.setDescription(application.getDescription());
			mdata.setName(application.getName());
			Tiers tiers = appDynamicsClient.getTiers(applicationId);
			
			TiersResource tiersResource = new TiersResource();
			//tiersResource.setTierList(tiers.getTierList());
			
			List<TierResource> tierList = new ArrayList<>();
			
			for(Tier tier:tiers.getTierList()){
				TierResource tierResource = new TierResource();
				tierResource.setAgentType(tier.getAgentType());
				tierResource.setName(tier.getName());
				tierResource.setNumberOfNodes(tier.getNumberOfNodes());
				tierResource.setTierId(tier.getId());
				tierResource.setType(tier.getType());
				
				Nodes nodes = appDynamicsClient.getNodes(applicationId);
				NodesResource nodesResource = new NodesResource();
				List<NodeResource> nodeList = new ArrayList<>();
				for(Node node : nodes.getNodes()){
					if(node.getTierId() == tierResource.getTierId()){
						NodeResource nodeResource = new NodeResource();
						nodeResource.setMachineOSType(node.getMachineOSType());
						nodeResource.setName(node.getName());
						nodeResource.setNodeId(node.getId());
						nodeResource.setTierId(node.getTierId());
						nodeResource.setTierName(node.getTierName());
						nodeResource.setType(node.getType());
						
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
	

	
}
