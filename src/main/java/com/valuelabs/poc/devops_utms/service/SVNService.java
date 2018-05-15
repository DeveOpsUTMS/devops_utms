package com.valuelabs.poc.devops_utms.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Fields.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.valuelabs.poc.devops_utms.repository.mongo.SVNPropertiesRepository;
import com.valuelabs.poc.devops_utms.resource.SVNProperties;


@Service
public class SVNService  {
	
	private final String repoName = "DevOpsPOC";
	String url = "https://hydtlp-wvpsvn.tlp.ads.valuelabs.net/svn/ATandT/"+repoName;
	String name = "kperikipati";
	String password = "Nov@2017";
	
	private static final Logger logger = LoggerFactory.getLogger(SVNService.class);
	
    public static final String DB_FORMAT_DATETIME = "yyyy-M-d HH:mm:ss";        

	
	@Autowired
	private SVNPropertiesRepository svnRepository;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public SVNProperties create(SVNProperties svnProperties) {
		return svnRepository.save(svnProperties);
	}
	
	public List<Map<String, Object>> getCommitsByDateWise() {
		Aggregation agg = Aggregation.newAggregation(
				project().andExpression("dayOfMonth(commitedDate)").as("day").andExpression("month(commitedDate)")
						.as("month").andExpression("year(commitedDate)").as("year"),
				group(fields().and("day").and("month").and("year")).count().as("count")
		);
		AggregationResults<Map> groupResults = mongoTemplate.aggregate(agg, SVNProperties.class, Map.class);
		List<Map<String, Object>> finalResults = new ArrayList<Map<String, Object>>();
		Map<String, Object> eachDayMap = null;
		String dateString = null;
		for (Map map : groupResults) {
			eachDayMap = new HashMap<>();
			dateString = map.get("day") + "/" + map.get("month") + "/" + map.get("year");

			eachDayMap.put("jsonDate",  map.get("year") + "/"+ map.get("month") +"/"+ map.get("day") );
			eachDayMap.put("jsonHitCount", map.get("count").toString());
			eachDayMap.put("jsonFileInfo", findMatchedSVNPropertiesByDate(dateString));
			finalResults.add(eachDayMap);
			
		}
		return finalResults;
	}
	
	public  List<SVNProperties> findMatchedSVNPropertiesByDate(String dateString){
	    Query query = new Query();
	    query.addCriteria(Criteria.where("commitedDate").ne(null).andOperator(
                Criteria.where("commitedDate").gte(getDate(dateString+" 00:00")),
                Criteria.where("commitedDate").lte(getDate(dateString+" 23:59"))
            ));
	    List<SVNProperties> listOfObjects = mongoTemplate.find(query, SVNProperties.class);
	    return listOfObjects;
	}
	
	public long getLastRevisionId(){
		Query query = new Query();
		query.with(new Sort(Sort.Direction.DESC, "commitedDate"));
		SVNProperties SVNPropertiesObj = mongoTemplate.findOne(query, SVNProperties.class);
		return  SVNPropertiesObj != null ? SVNPropertiesObj.getRevisionId() : 0;
	}
		
	public ResponseEntity<Object> getAll(){
		return new ResponseEntity<Object>(svnRepository.findAll(), HttpStatus.OK);		
	}
	
	//@Scheduled(fixedRate=30000) //every 30 seconds
	public void readSVNData(){
		DAVRepositoryFactory.setup();
		SVNRepository repository = null;
		logger.info("<<<---- Pulling the SVN Data --->>>");
		try {
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
			@SuppressWarnings("deprecation")
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
			repository.setAuthenticationManager(authManager);
			
			// Saving the data to SVNProperties.
			saveToSVNProperties(repository);
			
		} catch (SVNException e) {
			e.printStackTrace();
		}
	}
	
	public void saveToSVNProperties(SVNRepository repository) {

		try {
			long startRevision = getLastRevisionId();
			long endRevision = -1;
			logger.info("startRevision :::: "+startRevision);
			logger.info("endRevision :::: "+endRevision);
			Collection<?> logEntries = null;
			logEntries = repository.log(new String[] { "" }, null, startRevision, endRevision, true, true);
			logger.info("logEntries :::: "+logEntries.size());
			for (Iterator<?> entries = logEntries.iterator(); entries.hasNext();) {
				SVNLogEntry logEntry = (SVNLogEntry) entries.next();
				if (logEntry.getChangedPaths().size() > 0) {
					SVNProperties svnObject = new SVNProperties();
					Map<String, String> filesMap = null;
					List<Map<String,String>> modifiedFileslist = new ArrayList<>();
					Set<?> changedPathsSet = logEntry.getChangedPaths().keySet();
					for (Iterator<?> changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
						filesMap = new HashMap<String, String>();
						SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
						String filePath = entryPath.getPath()+ ((entryPath.getCopyPath() != null) ? " (from "+ entryPath.getCopyPath()+ " revision "+ entryPath.getCopyRevision()+ ")": "");

						filesMap.put(getStatus(entryPath.getType()), filePath);
						modifiedFileslist.add(filesMap);
					}
					svnObject.setRevisionId(logEntry.getRevision());
					svnObject.setAuthor(logEntry.getAuthor());
					svnObject.setMessage(logEntry.getMessage());
					svnObject.setCommitedDate(logEntry.getDate());
					svnObject.setModifiedFilesList(modifiedFileslist);
					
					create(svnObject);
				}
			}
		} catch (Exception ex) {

		}
	}
	
	public String getStatus(char val){
		String value = null;
		switch (val) {
			case 'A' :
				value = "Added";
				break;
			case 'D' :
				value =  "Deleted";
				break;
			case 'M':
				value =  "Modified";
				break;
			case 'R' :
				value = "Replaced";
				break;
		}
		return value;
	}
	
	public Date getDate(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {                
            return null;
        }
    }
}
