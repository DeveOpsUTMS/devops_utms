package com.valuelabs.poc.devops_utms.repository.mongo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.valuelabs.poc.devops_utms.resource.GitProperties;

public interface GitPropertiesRepository extends PagingAndSortingRepository<GitProperties,String>{

}
