package com.valuelabs.poc.devops_utms.repository.mongo;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.valuelabs.poc.devops_utms.resource.Commit;


public interface GitRepository extends PagingAndSortingRepository<Commit,String>{
	
	public List<String>  findAllUserStoryIdDistinctBy();

}
