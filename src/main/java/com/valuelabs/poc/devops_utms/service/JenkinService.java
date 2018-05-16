package com.valuelabs.poc.devops_utms.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.io.StringReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.valuelabs.poc.devops_utms.repository.mongo.JenkinBuildJobRepository;
import com.valuelabs.poc.devops_utms.repository.mongo.JenkinJobRepository;
import com.valuelabs.poc.devops_utms.resource.BuildDetails;
import com.valuelabs.poc.devops_utms.resource.JenkinBuild;
import com.valuelabs.poc.devops_utms.resource.JenkinBuildC;
import com.valuelabs.poc.devops_utms.resource.JenkinJob;
import com.valuelabs.poc.devops_utms.resource.JenkinsJoin;
import com.valuelabs.poc.devops_utms.util.Constants;

@Service
public class JenkinService  {
	@Value("${jenkins.url}")
	private String jenkinURL;

	@Value("${jenkins.username}")
	private String jenkinUser;

	@Value("${jenkins.password}")
	private String jenkinPwd;
	
	@Autowired
	private JenkinJobRepository jenkinJobRepository;
	@Autowired
	private JenkinBuildJobRepository jenkinBuildJobRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	private static final Logger logger = LoggerFactory.getLogger(JenkinService.class);

	@Scheduled(fixedRate=55000)
	public void retrieveJenkinJobs() throws Exception {
		//System.out.println("jenkinURL " +jenkinURL);
		//URI uri=new URI(jenkinURL);
		JenkinsServer jenkins = new JenkinsServer(new URI(jenkinURL), jenkinUser, jenkinPwd);
		Map<String, Job> jobs = jenkins.getJobs();//getting Jenkins jobs
		Iterator<String> jobsIterator = jobs.keySet().iterator();
		logger.info("<<<---- Pulling the Jenkins Data --->>>");
		while(jobsIterator.hasNext()){
			String key = jobsIterator.next();
			Job job = (Job)jobs.get(key);
			JenkinJob jenkinJob = new JenkinJob();
			String jobConfig = jenkins.getJobXml(job.getName());
			jenkinJob.setJobName(job.getName());
			//jenkinJob.setJobName("newJob");
			jenkinJob.setJobUrl(job.getUrl());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(jobConfig)));
			jenkinJob.setSourceCodeRepo(getValueForElement(document, "remote"));
			JobWithDetails JobWithDetails = job.details();//getting all details
			List<Build> builds = JobWithDetails.getAllBuilds();
			List<JenkinBuild> JenkinBuildList = new ArrayList<JenkinBuild>();
			for(Build build : builds){
				//JenkinBuildList.add(getJenkinBuild(build));
				JenkinBuildList.add(getJenkinBuild(build,key));
			}
			jenkinJob.setAllBuilds(JenkinBuildList);
			jenkinJob.setLastBuild(getJenkinBuild(JobWithDetails.getLastBuild(),key));
			jenkinJob.setLastFailedBuild(getJenkinBuild(JobWithDetails.getLastFailedBuild(),key));
			jenkinJob.setLastSuccessfulBuild(getJenkinBuild(JobWithDetails.getLastSuccessfulBuild(),key));
			//jenkinJob.setLastBuild(getJenkinBuild(JobWithDetails.getLastBuild()));
			//jenkinJob.setLastFailedBuild(getJenkinBuild(JobWithDetails.getLastFailedBuild()));
			//jenkinJob.setLastSuccessfulBuild(getJenkinBuild(JobWithDetails.getLastSuccessfulBuild()));
			jenkinJobRepository.save(jenkinJob);
		}
	}
	
		private JenkinBuild getJenkinBuild(Build build, String key) throws Exception{
			JenkinBuild jenkinBuild = new JenkinBuild();
			List<String> commitLt = new ArrayList<>();
			if(build!=null && build.details() != null){
				//jenkinBuild.setBuildId(build.getNumber());
				//jenkinBuild.setBuildResult(build.details().getResult().toString());
				//jenkinBuild.setTimeStamp(build.details().getTimestamp());

				if(key.equals("devops_utms")) {
					String str=build.details().getConsoleOutputText().toString();
					System.out.println("2----"+str);
					String revisionFind = "Revision";
					Pattern CommitId = Pattern.compile(revisionFind);
					Matcher matchCommit = CommitId.matcher(str);

					while (matchCommit.find()) {
						commitLt=Arrays.asList((str.substring(matchCommit.start(), matchCommit.end()+43)).split(" "));
					}
					if(!commitLt.isEmpty()){
						jenkinBuild.setCommitNumber(commitLt.get(1));
					}
				}else{
					jenkinBuild.setCommitNumber("1197eb67e5efae0a78c1775dd7a20a369851edda");
				}
				//jenkinBuild.setCommitNumber(commitLt.get(0));
				jenkinBuild.setBuildNumber(build.getNumber());
				jenkinBuild.setStatus(build.details().getResult().toString());
				//jenkinBuild.setDate(build.details().getTimestamp());
				jenkinBuild.setDate(new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(new Date(build.details().getTimestamp())));
				jenkinBuild.setBuildDuration(build.details().getDuration());
			}
			return jenkinBuild;
		}

	//@Scheduled(fixedRate=550000)
	public void retrieveJenkinJobsBuildCommit() throws Exception {
		JenkinsServer jenkins = new JenkinsServer(new URI(jenkinURL), jenkinUser, jenkinPwd);
		Map<String, Job> jobs = jenkins.getJobs();
		Iterator<String> jobsIterator = jobs.keySet().iterator();
		logger.info("<<<---- Pulling the Jenkins Data with only build and commit --->>>");
		while(jobsIterator.hasNext()){
			String key = jobsIterator.next();
			Job job = (Job)jobs.get(key);
			JenkinsJoin jenkinJoin = new JenkinsJoin();
			String jobConfig = jenkins.getJobXml(job.getName());
			jenkinJoin.setJobName(job.getName());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(jobConfig)));
			JobWithDetails JobWithDetails = job.details();
			List<Build> builds = JobWithDetails.getAllBuilds();
			List<JenkinBuildC> JenkinBuildCList = new ArrayList<JenkinBuildC>();
			for(Build build : builds){
				JenkinBuildCList.add(getJenkinBuildCommit(build,key));
			}
			jenkinJoin.setAllBuilds(JenkinBuildCList);
			jenkinBuildJobRepository.save(jenkinJoin);
		}
	}

	private JenkinBuildC getJenkinBuildCommit(Build build, String key) throws Exception{
		JenkinBuildC jenkinBuildc = new JenkinBuildC();
		List<String> commitLt = new ArrayList<>();
		if(build!=null){
			if(key.equals("devops_utms")) {
				String str=build.details().getConsoleOutputText().toString();
				String revisionFind = "Revision";
				Pattern CommitId = Pattern.compile(revisionFind);
				Matcher matchCommit = CommitId.matcher(str);
				
				System.out.println("1----"+str);

				while (matchCommit.find()) {
					commitLt=Arrays.asList((str.substring(matchCommit.start(), matchCommit.end()+43)).split(" "));
				}
				jenkinBuildc.setCommitNumber(commitLt.get(1));
			}
			else{
				jenkinBuildc.setCommitNumber("1197eb67e5efae0a78c1775dd7a20a369851edda");
			}
			jenkinBuildc.setBuildNumber(build.getNumber());
		}
		return jenkinBuildc;
	}

	protected String getValueForElement(Document document, String element) {
		String value = "";
		if(document.getElementsByTagName("remote")!=null && document.getElementsByTagName("remote").getLength()>0){
			value = document.getElementsByTagName("remote").item(0).getTextContent();
		}
		return value;
	}

	public ResponseEntity<Object> getJenkinsBuildInfo()throws Exception {
		 Map<String, List<JenkinBuild>> buildMap = null;

		 Aggregation aggregations = newAggregation(
				 match(Criteria.where(Constants._ID).is(Constants.JOB_NAME)),
				 unwind(Constants.ALL_BUILDS),
				 sort(Direction.ASC, Constants.ALL_BUILDS+Constants.DOT+Constants.DATE),
				 //sort(Direction.ASC, Constants.ALL_BUILDS+Constants.DOT+Constants.TIME_STAMP),
				 project(
						 Fields.from(
								//Fields.field(Constants.ALL_BUILDS+Constants.DOT+Constants.TIME_STAMP),
								//Fields.field(Constants.ALL_BUILDS+Constants.DOT+"buildId"),
								//Fields.field(Constants.ALL_BUILDS+Constants.DOT+"buildResult"),
								Fields.field(Constants.ALL_BUILDS+Constants.DOT+Constants.DATE),
								Fields.field(Constants.ALL_BUILDS+Constants.DOT+"buildNumber"),
								Fields.field(Constants.ALL_BUILDS+Constants.DOT+"status"),
								Fields.field(Constants.ALL_BUILDS+Constants.DOT+"commitNumber"),
								Fields.field(Constants.ALL_BUILDS+Constants.DOT+"buildDuration")
								))
				);
		AggregationResults<JenkinBuild> groupResults = mongoTemplate.aggregate(aggregations, JenkinJob.class, JenkinBuild.class);

		List<JenkinBuild> builds = groupResults.getMappedResults();
		List<JenkinBuild> buildList = new ArrayList<JenkinBuild>();
		if(builds!= null){
			buildMap = new LinkedHashMap<String, List<JenkinBuild>>();
			for(JenkinBuild build : builds){
				//String strDate = dateFormat.format(new Date(build.getDate()));
					buildList.add(build);
			}
		}

		return new ResponseEntity<Object>(buildMap != null ? buildList : "", HttpStatus.OK);
	}
	
	/*public ResponseEntity<Object> getJenkinsBuildInfo()throws Exception {
		 Map<String, List<JenkinBuild>> buildMap = null;

		 Aggregation aggregations = newAggregation(
				 match(Criteria.where(Constants._ID).is(Constants.JOB_NAME)),
				 unwind(Constants.ALL_BUILDS),
				 sort(Direction.ASC, Constants.ALL_BUILDS+Constants.DOT+Constants.DATE),
				 //sort(Direction.ASC, Constants.ALL_BUILDS+Constants.DOT+Constants.TIME_STAMP),
				 project(
						 Fields.from(
								//Fields.field(Constants.ALL_BUILDS+Constants.DOT+Constants.TIME_STAMP),
								//Fields.field(Constants.ALL_BUILDS+Constants.DOT+"buildId"),
								//Fields.field(Constants.ALL_BUILDS+Constants.DOT+"buildResult"),
								Fields.field(Constants.ALL_BUILDS+Constants.DOT+Constants.DATE),
								Fields.field(Constants.ALL_BUILDS+Constants.DOT+"buildNumber"),
								Fields.field(Constants.ALL_BUILDS+Constants.DOT+"status"),
								Fields.field(Constants.ALL_BUILDS+Constants.DOT+"commitNumber"),
								Fields.field(Constants.ALL_BUILDS+Constants.DOT+"buildDuration")
								))
				);
		AggregationResults<JenkinBuild> groupResults = mongoTemplate.aggregate(aggregations, JenkinJob.class, JenkinBuild.class);

		List<JenkinBuild> builds = groupResults.getMappedResults();
		if(builds!= null){
			buildMap = new LinkedHashMap<String, List<JenkinBuild>>();
			for(JenkinBuild build : builds){
				//String strDate = dateFormat.format(new Date(build.getTimeStamp()));
				String strDate = dateFormat.format(new Date(build.getDate()));
				if(buildMap.containsKey(strDate)){
					List<JenkinBuild> buildList = buildMap.get(strDate);
					buildList.add(build);
					buildMap.put(strDate, buildList);
				}else{
					List<JenkinBuild> buildList = new ArrayList<JenkinBuild>();
					buildList.add(build);
					buildMap.put(strDate, buildList);
				}
			}
		}

		return new ResponseEntity<Object>(buildMap != null ? buildMap : "", HttpStatus.OK);
	}*/
	
	
	
	public ResponseEntity<Object> getBuildDetails()throws Exception {
		 Map<String, List<BuildDetails>> buildMap = null;

		 Aggregation aggregations = newAggregation(
				 match(Criteria.where(Constants._ID).is(Constants.JOB_NAME)),
				 unwind(Constants.ALL_BUILDS),
				 sort(Direction.ASC, Constants.ALL_BUILDS+Constants.DOT+Constants.DATE),
				 //sort(Direction.ASC, Constants.ALL_BUILDS+Constants.DOT+Constants.TIME_STAMP),
				 project(
						 Fields.from(
								Fields.field(Constants.ALL_BUILDS+Constants.DOT+"buildNumber"),
								Fields.field(Constants.ALL_BUILDS+Constants.DOT+"commitNumber")
								))
				);
		AggregationResults<BuildDetails> groupResults = mongoTemplate.aggregate(aggregations, JenkinJob.class, BuildDetails.class);
		List<BuildDetails> builds = groupResults.getMappedResults();
		List<BuildDetails> buildList = new ArrayList<BuildDetails>();
		if(builds!= null){
			
			buildMap = new LinkedHashMap<String, List<BuildDetails>>();
			for(BuildDetails build : builds){
					
					buildList.add(build);
				}
			//}
		}

		return new ResponseEntity<Object>(buildMap != null ? buildList : "", HttpStatus.OK);
	}
	
	
}