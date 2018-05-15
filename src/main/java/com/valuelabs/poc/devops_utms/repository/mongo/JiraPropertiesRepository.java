package com.valuelabs.poc.devops_utms.repository.mongo;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.valuelabs.poc.devops_utms.resource.JiraBoard;
import com.valuelabs.poc.devops_utms.resource.JiraBoardResource;

public interface JiraPropertiesRepository extends PagingAndSortingRepository<JiraBoard,String>{
	

}
