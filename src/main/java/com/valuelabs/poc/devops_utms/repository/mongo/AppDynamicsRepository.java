package com.valuelabs.poc.devops_utms.repository.mongo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.valuelabs.poc.devops_utms.model.MetricData;

public interface AppDynamicsRepository extends PagingAndSortingRepository<MetricData, Integer>{

}
