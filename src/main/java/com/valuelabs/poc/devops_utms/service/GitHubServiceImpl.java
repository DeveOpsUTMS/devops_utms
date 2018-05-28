package com.valuelabs.poc.devops_utms.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Fields.fields;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommit.File;
import org.kohsuke.github.GHCommitQueryBuilder;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.valuelabs.poc.devops_utms.repository.mongo.GitRepository;
import com.valuelabs.poc.devops_utms.repository.mongo.JiraPropertiesRepository;
import com.valuelabs.poc.devops_utms.resource.AnalysisReport;
import com.valuelabs.poc.devops_utms.resource.Commit;
import com.valuelabs.poc.devops_utms.resource.DayWiseList;
import com.valuelabs.poc.devops_utms.resource.GitHubInfo;
import com.valuelabs.poc.devops_utms.resource.GitUserStorieResource;
import com.valuelabs.poc.devops_utms.resource.JiraBoardResource;
import com.valuelabs.poc.devops_utms.resource.Month;
import com.valuelabs.poc.devops_utms.resource.MonthWiseResponse;
import com.valuelabs.poc.devops_utms.resource.WeekData;
import com.valuelabs.poc.devops_utms.util.Constants;
import com.valuelabs.poc.devops_utms.util.DateTimeUtil;

public class GitHubServiceImpl implements GitHubService {

	private static final Logger logger = LoggerFactory.getLogger(GitHubServiceImpl.class);

	@Value("${repository.name}")
	private String repositoryName;

	@Autowired
	private GitRepository gitRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	JiraPropertiesRepository jiraPropertiesRepository;

	@Autowired
	GitHub gitHub;
	
	@Autowired
	JiraService jiraService;

	@Override
	public GHRepository getUserRepository(String user, String repositoryName) {

		GHRepository repository = null;
		try {

			repository = gitHub.getRepository(repositoryName);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return repository;
	}

	@Override
	public GHRepository getNewRepoCommitsDateWise(String user, String repositoryName) {

		GHRepository repository = null;
		try {
			// newReadSVNData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return repository;
	}

	// Its perfect and new
	//@Scheduled(fixedRate = 6000)
	public void readGitHubData() {
		GHRepository repository = null;
		try {
			logger.info("<<<<<<---- Pulling the GITHUB Data --->>>>>>");
			String userStoryId = "USS1234";
			Date date = getlastRDate("dateLast");
			if (date == null) {
				updateLastRunDate("dateLast", new Date());
			}
			repository = gitHub.getRepository(repositoryName);
			Calendar cal = Calendar.getInstance();
			/*
			 * cal.set(2016, 0, 4); Date since = cal.getTime();
			 */
			cal.set(2017, 11, 4);
			Date since = cal.getTime();

			if (date != null) {
				/*
				 * GHCommitQueryBuilder queryBuilder =
				 * repository.queryCommits().since(date).until(new Date());
				 */
				GHCommitQueryBuilder queryBuilder = repository.queryCommits().since(since).until(new Date());

				PagedIterable<GHCommit> commits = queryBuilder.list();
				String repo = repository.getFullName();
				String url = repository.getUrl().toString();
				updateLastRunDate("dateLast", new Date());
				for (Iterator<GHCommit> entries = commits.iterator(); entries.hasNext();) {

					GHCommit commitEntry = (GHCommit) entries.next();
					if (commitEntry.getFiles().size() > 0) {
						// mongoTemplate.save(getNewCommit(commitEntry, repo,
						// url, userStoryId));
						Commit commit = getNewCommit(commitEntry, repo, url, userStoryId,repository);
						gitCommitByUserStorie(commit);
						mongoTemplate.save(commit);
					}

				}
				// updateLastRunDate("dateLast", new Date());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void gitCommitByUserStorie(Commit commit)throws Exception{
		GitUserStorieResource commitByStorie = new GitUserStorieResource();
		
		if(commit != null){
			commitByStorie.setId(commit.getCommitId());
			commitByStorie.setCommitNumber(commit.getCommitId());
			commitByStorie.setProject(commit.getProject());
			commitByStorie.setUserStoryId(commit.getUserStoryId());
			
			mongoTemplate.save(commitByStorie);
		}
	}

	private Commit getNewCommit(GHCommit commit, String repo, String url, String userStoryId,GHRepository repository) throws Exception {
		Commit commitLo = new Commit();
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		if (commit != null && commit.getFiles().size() > 0) {

			listMap = getFileList(commit.getFiles());
			commitLo.setCommitId(commit.getSHA1());
			commitLo.setCommitedDate(commit.getCommitShortInfo().getCommitDate());
			commitLo.setAuthor(commit.getCommitShortInfo().getAuthor().getName());
			commitLo.setRepo(repo);
			commitLo.setUrl(url);
			commitLo.setUserStoryId(getUserStoryIdFromMsg(commit.getCommitShortInfo().getMessage()));
			//removed "*#" + userStoryId + "#* - " + 
			commitLo.setMessage(commit.getCommitShortInfo().getMessage());
			commitLo.setModifiedFilesList(listMap);
			commitLo.setCommitNumber(commit.getSHA1());
			commitLo.setCommittedBy(commit.getCommitShortInfo().getCommitter().getName());
			commitLo.setGitPath(repository.getGitTransportUrl());
			commitLo.setLinesAdded(commit.getLinesAdded());
			commitLo.setLinesRemoved(commit.getLinesDeleted());
			
			commitLo.setTotalLinesChanged(commit.getLinesChanged());

			if (repo != null && !repo.isEmpty()) {
				String[] project = repo.split("/");
				commitLo.setProject(project.length > 0 ? project[1] : "");
			}
			

		}
		return commitLo;
	}

	public Date getlastRDate(String key) {
		GitHubInfo userTest2 = null;
		Date date = null;
		Query query2 = new Query();
		query2.addCriteria(Criteria.where("_id").is(key));

		userTest2 = mongoTemplate.findOne(query2, GitHubInfo.class);
		if (userTest2 != null) {
			date = userTest2.getLastRunDateTime();
		}
		return date;
	}

	public void updateLastRunDate(String key, Date date) {

		Query query = new Query(Criteria.where("_id").is(key));

		Update update = new Update();
		update.set("lastRunDateTime", date);
		// return new increased id
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		GitHubInfo gitHubInfo = mongoTemplate.findAndModify(query, update, options, GitHubInfo.class);
		if (gitHubInfo == null) {
			// if this the first time, create the collection in DB
			mongoTemplate.save(getCounters(key, date));
			gitHubInfo = mongoTemplate.findAndModify(query, update, options, GitHubInfo.class);
		}
	}

	public String getUserStoryIdFromMsg(String msg) {
		String userStoryId = "";
		if (msg != "" && !msg.isEmpty()) {
			Pattern pattern = Pattern.compile("\\*#(.*?)#\\*");
			Matcher matcher = pattern.matcher(msg);
			if (matcher.find()) {
				userStoryId = matcher.group(1).trim();
			} else {
				String mm = "*#USS1234#* - Handle exclusion checks for audit (#1800)\\n\\n* Handle exclusion";
				String[] al = mm.split("-");
				userStoryId = al[0].replace("*#", "").replace("#*", "").trim();
			}
		}
		return userStoryId;
	}

	private List<Map<String, String>> getFileList(List<File> filesList) {

		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		Map<String, String> mapData = new HashMap<String, String>();
		try {
			if (!filesList.isEmpty() && filesList.size() > 0) {
				for (File f : filesList) {
					System.out.println("Stats : " + f.getStatus() + " File : " + f.getFileName());
					// mapData.put(getStatus(f.getStatus()), f.getFileName());
					mapData.put(f.getStatus(), f.getFileName());
				}
				listMap.add(mapData);
			}
		} catch (Exception e) {
			System.out.println(" Except : " + e.getMessage());
			e.printStackTrace();
		}
		return listMap;

	}

	public String getStatus(String val) {
		String value = null;
		switch (val) {
		case "added":
			value = "added";
			break;
		case "deleted":
			value = "deleted";
			break;
		case "modified":
			value = "modified";
			break;
		case "replaced":
			value = "replaced";
			break;
		}
		return value;
	}

	private GitHubInfo getCounters(String key, Date date) {
		return new GitHubInfo(key, date);
	}

	@Override
	public List<Map<String, Object>> getCommitsByDateWise() {

		Aggregation agg = Aggregation.newAggregation(sort(Sort.Direction.ASC, "commitedDate"),
				project().andExpression("dayOfMonth(commitedDate)").as("day").andExpression("month(commitedDate)")
						.as("month").andExpression("year(commitedDate)").as("year"),
				group(fields().and("day").and("month").and("year")).count().as("count")

		);
		
		AggregationResults<Map> groupResults = mongoTemplate.aggregate(agg, Commit.class, Map.class);
		List<Map<String, Object>> finalResults = new ArrayList<Map<String, Object>>();
		Map<String, Object> eachDayMap = null;
		String dateString = null;
		for (Map map : groupResults) {
			eachDayMap = new HashMap<>();
			dateString = map.get("day") + "/" + map.get("month") + "/" + map.get("year");

			eachDayMap.put("jsonDate", map.get("year") + "/" + map.get("month") + "/" + map.get("day"));
			eachDayMap.put("jsonHitCount", map.get("count").toString());
			eachDayMap.put("jsonFileInfo", findMatchedGitCommitsByDate(dateString));
			eachDayMap.put("lastUpdatedDate", DateTimeUtil.dateToString(new Date()));
			finalResults.add(eachDayMap);

		}

		return finalResults;
	}

	public List<Commit> findMatchedGitCommitsByDate(String dateString) {
		Query query = new Query();
		query.addCriteria(Criteria.where("commitedDate").ne(null).andOperator(
				Criteria.where("commitedDate").gte(getDate(dateString + " 00:00")),
				Criteria.where("commitedDate").lte(getDate(dateString + " 23:59"))));
		List<Commit> listOfObjects = mongoTemplate.find(query, Commit.class);
		return listOfObjects;
	}

	public Date getDate(String dateStr) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try {
			return simpleDateFormat.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	@Override
	public List<GHCommit> getUserCommits(String user, String repositoryName) {
		List<GHCommit> list = new ArrayList<GHCommit>();
		try {
			GHRepository repository = gitHub.getRepository(repositoryName);

			for (GHCommit c : repository.listCommits()) {
				// System.out.println(c.getSHA1());
				// System.out.println("Commit ShortInfo :
				// "+c.getCommitShortInfo().getMessage());
				list.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void timerGit() {
		// TODO Auto-generated method stub
		// readSVNData();
		System.out.println("completed");
	}

	private Commit getCommit(GHCommit commit, String userStoryId, String repo) throws Exception {
		Commit commitLo = new Commit();
		List<Map<String, String>> listMap = Collections.EMPTY_LIST;
		if (commit != null && commit.getFiles().size() > 0) {

			listMap = getFileList(commit.getFiles());
			commitLo.setCommitId(commit.getSHA1());
			commitLo.setCommitedDate(commit.getCommitDate());
			commitLo.setAuthor(commit.getCommitShortInfo().getAuthor().getName());
			commitLo.setRepo(repo);
			commitLo.setUserStoryId(getUserStoryIdFromMsg(commit.getCommitShortInfo().getMessage()));
			commitLo.setMessage("*#" + userStoryId + "#* - " + commit.getCommitShortInfo().getMessage());
			commitLo.setModifiedFilesList(listMap);
		}
		return commitLo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllUserStoryIds() {
		System.out.println(" getAllUserStoryIds : ");
		List<String> list = Collections.emptyList();
		list = mongoTemplate.getCollection("gitUserStorieResource").distinct("userStoryId");
		// list = gitRepository.findAllUserStoryIdDistinctBy();
		if (!list.isEmpty() && list.size() > 0) {
			return list;
		}
		return Collections.emptyList();
	}

	@Override
	public AnalysisReport getAnalysisReport() {
		AnalysisReport analysisReport = new AnalysisReport();
		List<String> allStoryIds = new ArrayList<>();
		allStoryIds.add("");

		List<String> storyIdsInCommit = getAllUserStoryIds();
		if (allStoryIds.retainAll(storyIdsInCommit)) {
			System.out.println("allStoryIds.removeAll(storyIdsInCommit) : " + allStoryIds.removeAll(storyIdsInCommit));
			System.out.println(" allStoryIds : " + allStoryIds.toString());
			System.out.println(" allStoryIds size : " + allStoryIds.size());
			System.out.println(" storyIdsInCommit  : " + storyIdsInCommit.toString());
			System.out.println(" storyIdsInCommit size : " + storyIdsInCommit.size());

			allStoryIds.addAll(storyIdsInCommit);
			analysisReport.setUserStoryIds(String.valueOf(allStoryIds.size()));
			analysisReport.setCommits(String.valueOf(storyIdsInCommit.size()));
			analysisReport.setBuild(String.valueOf(0));
			analysisReport.setDeployAndQA(String.valueOf(0));
		} else {
			analysisReport.setUserStoryIds(String.valueOf(0));
			analysisReport.setCommits(String.valueOf(0));
			analysisReport.setBuild(String.valueOf(0));
			analysisReport.setDeployAndQA(String.valueOf(0));
		}
		return analysisReport;
	}

	/*
	 * @Override public MonthWiseResponse getDayWiseUserStories(){
	 * List<ResponseList> list2 = new ArrayList<>(); List<DayWiseList> list =
	 * new ArrayList<>(); ResponseList responseList = null; MonthWiseResponse
	 * monthWiseResponse = new MonthWiseResponse(); List<Map<String, Object>>
	 * finalResults = new ArrayList<Map<String, Object>>();
	 * 
	 * Aggregation agg = Aggregation.newAggregation( sort(Sort.Direction.ASC,
	 * "commitedDate"),
	 * project().andExpression("week(commitedDate)").as("Label").andExpression(
	 * "dayOfMonth(commitedDate)").as("day").andExpression(
	 * "month(commitedDate)")
	 * .as("month").andExpression("year(commitedDate)").as("year"),
	 * group(fields().and("Label").and("day").and("month").and("year")).count().
	 * as("count") );
	 * 
	 * AggregationResults<Map> groupResults = mongoTemplate.aggregate(agg,
	 * Commit.class, Map.class); Map<String, String> eachDayMap = null; String
	 * dateString = null;
	 * 
	 * for (Map map : groupResults) { eachDayMap = new HashMap<>(); dateString =
	 * map.get("day") + "/" + map.get("month") + "/" + map.get("year"); //
	 * eachDayMap.put("commitDate", map.get("day") + "/" + map.get("month") +
	 * "/" + map.get("year")); // eachDayMap.put("commitsTotal",
	 * map.get("count").toString()); eachDayMap.put(map.get("day") + "/" +
	 * map.get("month") + "/" + map.get("year"), map.get("count").toString());
	 * // responseList = getDataFromDate(eachDayMap); list2.add(responseList);
	 * monthWiseResponse = getDatesCommitList(eachDayMap); //
	 * System.out.println("SS : " +ss.toString());
	 * 
	 * } return monthWiseResponse; }
	 */

	@Override
	public MonthWiseResponse getDayWiseUserStoriesWithBugsData() {
		MonthWiseResponse monthWiseResponse = null;
		Map<String, Object> keyMap = new HashMap<>();
		MonthWiseResponse responseList = new MonthWiseResponse();

		List<Map<String, String>> list2 = new ArrayList<>();
		List<DayWiseList> list = new ArrayList<>();

		List<Map<String, Object>> finalResults = new ArrayList<Map<String, Object>>();

		Aggregation agg = Aggregation.newAggregation(sort(Sort.Direction.ASC, "commitedDate"),
				project().andExpression("week(commitedDate)").as("Label").andExpression("dayOfMonth(commitedDate)")
						.as("day").andExpression("month(commitedDate)").as("month").andExpression("year(commitedDate)")
						.as("year"),
				group(fields().and("Label").and("day").and("month").and("year")).count().as("count"));

		AggregationResults<Map> groupResults = mongoTemplate.aggregate(agg, Commit.class, Map.class);
		Map<String, String> eachDayMap = new HashMap<>();
		String dateString = null;
		Map<String, Object> groupDayMap;
		for (Map map : groupResults) {
			// eachDayMap = new HashMap<>();
			dateString = map.get("day") + "/" + map.get("month") + "/" + map.get("year");
			eachDayMap.put(map.get("day") + "/" + map.get("month") + "/" + map.get("year"),
					map.get("count").toString());

		}
		groupDayMap = new HashMap();
		List<Map<String, Object>> listMonth = new ArrayList<>();
		List<Month> lll = new ArrayList<>();

		List<Month> lastMonth = new ArrayList<>();

		monthWiseResponse = dayWiseDataResponse(eachDayMap);
		if(monthWiseResponse != null && monthWiseResponse.getMonths() != null){
			monthWiseResponse = getMonthlyWiseData(monthWiseResponse);
		}

		return monthWiseResponse;
	}

	public MonthWiseResponse getMonthlyWiseData(MonthWiseResponse getMonthlyWiseData) {

		MonthWiseResponse monthWiseResponse = null;
		List<Month> monthList = new ArrayList<>();
		List<Month> monthsList = getMonthlyWiseData.getMonths();

		Map<String, Object> coMap;
		Map<String, Object> coMap2;
		Map<String, Object> coMap3;
		List<String> monthArray = Arrays.asList("January", "February", "March", "April", "May", "June", "July",
				"August", "September", "October", "November", "December");

		int count = 0;
		for (Month month : monthsList) {
			coMap3 = new ConcurrentHashMap<>();
			for (String oneMonth : monthArray) {

				Map<String, Object> monthMap = month.getDatesList();
				if (!month.getMonthName().isEmpty() && month.getMonthName().equals(oneMonth)) {
					coMap3 = new HashMap<String, Object>();
					Set<Map.Entry<String, Object>> entrySet = monthMap.entrySet();
					List<Entry<String, Object>> list = new ArrayList<Entry<String, Object>>(entrySet);
					count = list.size();
					coMap3.put("Sprint 1", list.subList(0, 15));
					coMap3.put("Sprint 2", list.subList(15, count));
					month.setDatesList(coMap3);
				}
			}
			monthList.add(month);
		}
		getMonthlyWiseData.setMonths(monthList);
		return getMonthlyWiseData;
	}

	//This API will retrive no.of user stories commited in sprint dates
	@Override
	public MonthWiseResponse getDayWiseUserStories() {
		MonthWiseResponse monthWiseResponse = null;
		Map<String, Object> keyMap = new HashMap<>();
		MonthWiseResponse responseList = new MonthWiseResponse();

		List<Map<String, String>> list2 = new ArrayList<>();
		List<DayWiseList> list = new ArrayList<>();

		List<Map<String, Object>> finalResults = new ArrayList<Map<String, Object>>();

		Aggregation agg = Aggregation.newAggregation(sort(Sort.Direction.ASC, "commitedDate"),
				project().andExpression("week(commitedDate)").as("Label").andExpression("dayOfMonth(commitedDate)")
						.as("day").andExpression("month(commitedDate)").as("month").andExpression("year(commitedDate)")
						.as("year"),
				group(fields().and("Label").and("day").and("month").and("year")).count().as("count"));

		AggregationResults<Map> groupResults = mongoTemplate.aggregate(agg, Commit.class, Map.class);
		Map<String, String> eachDayMap = new HashMap<>();
		String dateString = null;
		Map<String, Object> groupDayMap;
		for (Map map : groupResults) {
			// eachDayMap = new HashMap<>();
			dateString = map.get("day") + "/" + map.get("month") + "/" + map.get("year");
			eachDayMap.put(map.get("day") + "/" + map.get("month") + "/" + map.get("year"),
					map.get("count").toString());

		}
		groupDayMap = new HashMap();
		List<Map<String, Object>> listMonth = new ArrayList<>();
		List<Month> lll = new ArrayList<>();

		List<Month> lastMonth = new ArrayList<>();

		monthWiseResponse = dayWiseWithOutBugDataResponse(eachDayMap);
		monthWiseResponse = getMonthlyWiseData(monthWiseResponse);

		/*
		 * List<Month> monthsList = monthWiseResponse.getMonths();
		 * 
		 * Map<String, Object> coMap = new ConcurrentHashMap<>(); Map<String,
		 * Object> coMap2 = new ConcurrentHashMap<>(); Map<String, Object>
		 * coMap3 = new ConcurrentHashMap<>();
		 * 
		 * List<String> monthArray =
		 * Arrays.asList("January","February","March","April","May","June",
		 * "July","August","September","October","November","December");
		 * 
		 * List<Object> list1 = new ArrayList<>(); List<Object> list22 = new
		 * ArrayList<>(); int count =0; String x=""; for(Month month :
		 * monthsList){ if(!month.getMonthName().isEmpty() &&
		 * month.getMonthName().equals("September")){ Map<String, Object>
		 * monthMap = month.getDatesList(); for (Map.Entry<String, Object> entry
		 * : monthMap.entrySet()) { count ++; if(count<=15){
		 * coMap.put(entry.getKey(), entry.getValue()); }else{
		 * coMap2.put(entry.getKey(), entry.getValue()); } }
		 * coMap3.put("Sprint1", coMap); coMap3.put("Sprint2", coMap2);
		 * month.setDatesList(coMap3); } lll.add(month); lastMonth.add(month); }
		 */

		return monthWiseResponse;
	}

	public MonthWiseResponse dayWiseWithOutBugDataResponse(Map<String, String> eachDayMap) {

		MonthWiseResponse monthWiseResponse = new MonthWiseResponse();
		List<String> months = new ArrayList<>();
		List<String> dateList = Collections.EMPTY_LIST;
		List<Month> monhtsList = new ArrayList<>();
		Month month = null;
		Map<String, Object> internalMap;
		Set set2 = eachDayMap.entrySet();
		Iterator iterator2 = set2.iterator();
		while (iterator2.hasNext()) {
			Map.Entry mentry2 = (Map.Entry) iterator2.next();
			String oldDate = correctFormatDate(String.valueOf(mentry2.getKey()));
			Date date = getNewDate(oldDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int month1 = cal.get(Calendar.MONTH);
			String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
			if (monthWiseResponse.getMonths() == null || !months.contains(monthName)) {
				months.add(monthName);
				month = new Month();
				dateList = new ArrayList<>();
				internalMap = new TreeMap<>();
				cal.set(Calendar.MONTH, month1);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				month.setMonthName(monthName);
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				for (int i = 0; i < maxDay; i++) {
					cal.set(Calendar.DAY_OF_MONTH, i + 1);
					String currentDate = correctFormatDate(df.format(cal.getTime()));
					if (currentDate.equalsIgnoreCase(oldDate)) {
						internalMap.put(currentDate, String.valueOf(mentry2.getValue()));
					} else {

						internalMap.put(currentDate, 0);
					}
					month.setDatesList(internalMap);
				}
				monhtsList.add(month);
				monthWiseResponse.setMonths(monhtsList);

			} else if (months.contains(monthName)) {
				List<Month> ds = monthWiseResponse.getMonths();
				for (Month dsMonts : ds) {
					String monthN = dsMonts.getMonthName();
					if (monthN.equalsIgnoreCase(monthName)) {
						Map<String, Object> oldDates = dsMonts.getDatesList();
						for (Map.Entry<String, Object> entry : oldDates.entrySet()) {
							if (correctFormatDate(entry.getKey()).equalsIgnoreCase(oldDate)) {
								oldDates.put(oldDate, mentry2.getValue());
							}
						}
					}
				}
				monthWiseResponse.setMonths(ds);

				monthWiseResponse.setMonths(monhtsList);
			}
		}
		return monthWiseResponse;
	}

	/*
	 * @Override public MonthWiseResponse getDayWiseUserStories(){
	 * MonthWiseResponse monthWiseResponse = null; Map<String, Object> keyMap =
	 * new HashMap<>(); MonthWiseResponse responseList = new
	 * MonthWiseResponse();
	 * 
	 * List<Map<String, String>> list2 = new ArrayList<>(); List<DayWiseList>
	 * list = new ArrayList<>();
	 * 
	 * 
	 * List<Map<String, Object>> finalResults = new ArrayList<Map<String,
	 * Object>>();
	 * 
	 * Aggregation agg = Aggregation.newAggregation( sort(Sort.Direction.ASC,
	 * "commitedDate"),
	 * project().andExpression("week(commitedDate)").as("Label").andExpression(
	 * "dayOfMonth(commitedDate)").as("day").andExpression(
	 * "month(commitedDate)")
	 * .as("month").andExpression("year(commitedDate)").as("year"),
	 * group(fields().and("Label").and("day").and("month").and("year")).count().
	 * as("count") );
	 * 
	 * AggregationResults<Map> groupResults = mongoTemplate.aggregate(agg,
	 * Commit.class, Map.class); Map<String, String> eachDayMap = new
	 * HashMap<>(); String dateString = null; Map<String, Object> groupDayMap;
	 * for (Map map : groupResults) { //eachDayMap = new HashMap<>(); dateString
	 * = map.get("day") + "/" + map.get("month") + "/" + map.get("year");
	 * eachDayMap.put(map.get("day") + "/" + map.get("month") + "/" +
	 * map.get("year"), map.get("count").toString());
	 * 
	 * } groupDayMap = new HashMap(); List<Map<String, Object>> listMonth = new
	 * ArrayList<>(); List<Month> lll = new ArrayList<>();
	 * 
	 * monthWiseResponse = dayWiseDataResponse(eachDayMap);
	 * 
	 * List<Month> monthsList = monthWiseResponse.getMonths(); Map<String,
	 * Object> coMap = new ConcurrentHashMap<>(); Map<String, Object> coMap2 =
	 * new ConcurrentHashMap<>(); Map<String, Object> coMap3 = new
	 * ConcurrentHashMap<>(); List<String> monthArray =
	 * Arrays.asList("January","February","March","April","May","June","July",
	 * "August","September","October","November","December"); for(String
	 * s:monthArray){ System.out.println("s"+s); } List<Object> list1 = new
	 * ArrayList<>(); List<Object> list22 = new ArrayList<>();
	 * 
	 * for(Month month : monthsList){ String monthName = month.getMonthName();
	 * Map<String, Object> monthMap = month.getDatesList(); //
	 * System.out.println("monthMap : " + monthMap.toString());
	 * 
	 * int count =0;
	 * 
	 * //if(month.getMonthName().equals("December")){
	 * if(month.getMonthName().isEmpty()){ for (Map.Entry<String, Object> entry
	 * : monthMap.entrySet()) { count ++; if(count<=15){ //
	 * System.out.println("1111111111111111111111111"); //
	 * System.out.println("entry.getKey()  " + entry.getKey() + "Values  : " +
	 * entry.getValue()) ; //coMap.put("Sprint1", ""); coMap.put(entry.getKey(),
	 * entry.getValue());
	 * 
	 * //month.setDatesList(coMap); }else{
	 * //System.out.println("2222222222222222222");
	 * //System.out.println("entry.getKey()  " + entry.getKey() + "Values  : " +
	 * entry.getValue()) ; // coMap.put("Sprint2", "");
	 * coMap2.put(entry.getKey(), entry.getValue());
	 * //month.setDatesList(coMap);
	 * 
	 * } } coMap3.put("Sprint1", coMap); coMap3.put("Sprint2", coMap2);
	 * 
	 * month.setDatesList(coMap3); } lll.add(month); //
	 * System.out.println("lll " +lll.toString());
	 * monthWiseResponse.setMonths(lll); }
	 * 
	 * 
	 * 
	 * return monthWiseResponse; }
	 */
	// for getting bug data
	@Override
	public Map<String, Object> getBugData() {

		Map<String, Object> mapData = new ConcurrentHashMap<>();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		List<JiraBoardResource> jiraList = jiraService.getBugs();//(List) jiraPropertiesRepository.findAll();
		
		for(JiraBoardResource resource : jiraList){
			if(resource.getJiraSprints() != null && resource.getJiraSprints().getSprintDetails() != null 
					&& resource.getJiraSprints().getSprintDetails().getUserStories() != null && resource.getJiraSprints().getSprintDetails().getUserStories().getIssues() != null){
				mapData.put(resource.getJiraSprints().getSprintDetails().getUserStories().getIssues().getKey(), resource.getJiraSprints().getSprintDetails().getUserStories().getIssues().getFields().getCreatedDate());
			}
			resource.getJiraSprints().getSprintDetails().getUserStories().getIssues().getFields().getCreatedDate();
		}
		
		
		return mapData;
	}

	public MonthWiseResponse dayWiseDataResponse(Map<String, String> eachDayMap) {

		MonthWiseResponse monthWiseResponse = new MonthWiseResponse();
		List<String> months = new ArrayList<>();
		List<String> dateList = Collections.EMPTY_LIST;
		List<Month> monhtsList = new ArrayList<>();

		Map<String, Object> bugData = getBugData();

		for (Map.Entry<String, Object> bugs : bugData.entrySet()) {

			Month month = null;
			Map<String, Object> internalMap;
			Map<String, Object> dayDataMap;
			Set set2 = eachDayMap.entrySet();
			Iterator iterator2 = set2.iterator();
			while (iterator2.hasNext()) {
				Map.Entry mentry2 = (Map.Entry) iterator2.next();
				String oldDate = correctFormatDate(String.valueOf(mentry2.getKey()));
				Date date = getNewDate(oldDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				int month1 = cal.get(Calendar.MONTH);
				String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
				if (monthWiseResponse.getMonths() == null || !months.contains(monthName)) {
					months.add(monthName);
					month = new Month();
					dayDataMap = new HashMap<>();
					dateList = new ArrayList<>();
					internalMap = new TreeMap<>();
					cal.set(Calendar.MONTH, month1);
					cal.set(Calendar.DAY_OF_MONTH, 1);
					int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
					month.setMonthName(monthName);
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					for (int i = 0; i < maxDay; i++) {
						cal.set(Calendar.DAY_OF_MONTH, i + 1);
						String currentDate = correctFormatDate(df.format(cal.getTime()));
						if (currentDate.equalsIgnoreCase(oldDate)) {
							Date datess = (Date) bugs.getValue();
							Date time = (Date) cal.getTime();

							dayDataMap.put("userStories", String.valueOf(mentry2.getValue()));
							if (datess != null && datess.compareTo(time) == 0) {
								// if(datess!= null && datess == time) {
								dayDataMap.put("Bug", bugs.getKey());
								System.out.println("bugs.getKey() 1 " + bugs.getKey());
							} else {
								dayDataMap.put("Bug", 0);
							}
							internalMap.put(currentDate, dayDataMap);
						} else {
							dayDataMap.put("userStories", 0);
							dayDataMap.put("Bug", 0);
							internalMap.put(currentDate, dayDataMap);
						}
						month.setDatesList(internalMap);
					}
					monhtsList.add(month);
					monthWiseResponse.setMonths(monhtsList);

				} else if (months.contains(monthName)) {
					List<Month> ds = monthWiseResponse.getMonths();
					for (Month dsMonts : ds) {
						String monthN = dsMonts.getMonthName();
						if (monthN.equalsIgnoreCase(monthName)) {
							Map<String, Object> oldDates = dsMonts.getDatesList();
							for (Map.Entry<String, Object> entry : oldDates.entrySet()) {
								if (correctFormatDate(entry.getKey()).equalsIgnoreCase(oldDate)) {
									dayDataMap = new HashMap<>();

									Date datess = (Date) bugs.getValue();
									// if(date == datess){
									// if(datess != null && datess == date){
									dayDataMap.put("userStories", mentry2.getValue());
									if (datess != null && datess.compareTo(date) == 0) {

										// if(datess != null && datess == date){
										dayDataMap.put("Bug", bugs.getKey());
										System.out.println("bugs.getKey() 2 " + bugs.getKey());
									} else {
										dayDataMap.put("Bug", 0);
									}
									oldDates.put(oldDate, dayDataMap);
								}
							}
						}
					}
					monthWiseResponse.setMonths(ds);
					monthWiseResponse.setMonths(monhtsList);
				}
			}
		}
		return monthWiseResponse;
	}

	/*
	 * public MonthWiseResponse dayWiseDataResponse(Map<String, String>
	 * eachDayMap){
	 * 
	 * MonthWiseResponse monthWiseResponse = new MonthWiseResponse();
	 * List<String> months = new ArrayList<>(); List<String> dateList
	 * =Collections.EMPTY_LIST; List<Month> monhtsList = new ArrayList<>();
	 * 
	 * Map<String, Object> bugData = getBugData();
	 * 
	 * for(Map.Entry<String, Object> bugs : bugData.entrySet()) {
	 * 
	 * 
	 * Month month = null; Map<String, Object> internalMap; Map<String, Object>
	 * dayDataMap; Set set2 = eachDayMap.entrySet(); Iterator iterator2 =
	 * set2.iterator(); while(iterator2.hasNext()) { Map.Entry mentry2 =
	 * (Map.Entry)iterator2.next(); String oldDate =
	 * correctFormatDate(String.valueOf(mentry2.getKey())); Date date =
	 * getNewDate(oldDate); Calendar cal = Calendar.getInstance();
	 * cal.setTime(date); int month1 = cal.get(Calendar.MONTH); String monthName
	 * = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
	 * if(monthWiseResponse.getMonths() == null || !months.contains(monthName))
	 * { months.add(monthName); month = new Month(); dayDataMap = new
	 * HashMap<>(); dateList = new ArrayList<>(); internalMap = new TreeMap<>();
	 * cal.set(Calendar.MONTH, month1); cal.set(Calendar.DAY_OF_MONTH, 1); int
	 * maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	 * month.setMonthName(monthName); SimpleDateFormat df = new
	 * SimpleDateFormat("dd/MM/yyyy"); for (int i = 0; i < maxDay; i++) {
	 * cal.set(Calendar.DAY_OF_MONTH, i + 1); String currentDate =
	 * correctFormatDate(df.format(cal.getTime()));
	 * if(currentDate.equalsIgnoreCase(oldDate)){ Date datess =
	 * (Date)bugs.getValue(); Date time = (Date) cal.getTime(); if(datess!= null
	 * && datess == time) { dayDataMap.put("userStories",
	 * String.valueOf(mentry2.getValue())); dayDataMap.put("Bug",
	 * bugs.getKey()); } internalMap.put(currentDate, dayDataMap); }else{
	 * dayDataMap.put("userStories", 0); dayDataMap.put("Bug", 0);
	 * internalMap.put(currentDate, dayDataMap); }
	 * month.setDatesList(internalMap); } monhtsList.add(month);
	 * monthWiseResponse.setMonths(monhtsList);
	 * 
	 * }else if(months.contains(monthName)){ List<Month> ds =
	 * monthWiseResponse.getMonths(); for(Month dsMonts : ds){ String monthN =
	 * dsMonts.getMonthName(); if(monthN.equalsIgnoreCase(monthName)){
	 * Map<String, Object> oldDates = dsMonts.getDatesList(); for
	 * (Map.Entry<String, Object> entry : oldDates.entrySet()) {
	 * if(correctFormatDate(entry.getKey()).equalsIgnoreCase(oldDate)){
	 * dayDataMap = new HashMap<>();
	 * 
	 * Date datess = (Date)bugs.getValue(); //if(date == datess){ if(datess !=
	 * null && datess == date){ dayDataMap.put("userStories",
	 * mentry2.getValue()); dayDataMap.put("Bug", bugs.getKey()); }
	 * oldDates.put(oldDate, dayDataMap); } } } }
	 * monthWiseResponse.setMonths(ds); monthWiseResponse.setMonths(monhtsList);
	 * } } } return monthWiseResponse; }
	 */

	/*
	 * public MonthWiseResponse dayWiseDataResponse(Map<String, String>
	 * eachDayMap){
	 * 
	 * MonthWiseResponse monthWiseResponse = new MonthWiseResponse();
	 * List<String> months = new ArrayList<>(); List<String> dateList
	 * =Collections.EMPTY_LIST; List<Month> monhtsList = new ArrayList<>();
	 * Month month = null; Map<String, Object> internalMap; Set set2 =
	 * eachDayMap.entrySet(); Iterator iterator2 = set2.iterator();
	 * while(iterator2.hasNext()) { Map.Entry mentry2 =
	 * (Map.Entry)iterator2.next(); String oldDate =
	 * correctFormatDate(String.valueOf(mentry2.getKey())); Date date =
	 * getNewDate(oldDate); Calendar cal = Calendar.getInstance();
	 * cal.setTime(date); int month1 = cal.get(Calendar.MONTH); String monthName
	 * = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
	 * if(monthWiseResponse.getMonths() == null || !months.contains(monthName))
	 * { months.add(monthName); month = new Month(); dateList = new
	 * ArrayList<>(); internalMap = new TreeMap<>(); cal.set(Calendar.MONTH,
	 * month1); cal.set(Calendar.DAY_OF_MONTH, 1); int maxDay =
	 * cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	 * month.setMonthName(monthName); SimpleDateFormat df = new
	 * SimpleDateFormat("dd/MM/yyyy"); for (int i = 0; i < maxDay; i++) {
	 * cal.set(Calendar.DAY_OF_MONTH, i + 1); String currentDate =
	 * correctFormatDate(df.format(cal.getTime()));
	 * if(currentDate.equalsIgnoreCase(oldDate)){ internalMap.put(currentDate,
	 * String.valueOf(mentry2.getValue())); }else{
	 * 
	 * internalMap.put(currentDate, 0); } month.setDatesList(internalMap); }
	 * monhtsList.add(month); monthWiseResponse.setMonths(monhtsList);
	 * 
	 * }else if(months.contains(monthName)){ List<Month> ds =
	 * monthWiseResponse.getMonths(); for(Month dsMonts : ds){ String monthN =
	 * dsMonts.getMonthName(); if(monthN.equalsIgnoreCase(monthName)){
	 * Map<String, Object> oldDates = dsMonts.getDatesList(); for
	 * (Map.Entry<String, Object> entry : oldDates.entrySet()) {
	 * if(correctFormatDate(entry.getKey()).equalsIgnoreCase(oldDate)){
	 * oldDates.put(oldDate, mentry2.getValue()); } } } }
	 * monthWiseResponse.setMonths(ds);
	 * 
	 * monthWiseResponse.setMonths(monhtsList); } } return monthWiseResponse; }
	 */

	/*
	 * public MonthWiseResponse dayWiseDataResponse(Map<String, String>
	 * eachDayMap){
	 * 
	 * MonthWiseResponse monthWiseResponse = new MonthWiseResponse();
	 * List<String> months = new ArrayList<>(); List<String> dateList
	 * =Collections.EMPTY_LIST; List<Month> monhtsList = new ArrayList<>();
	 * Month month = null; Map<String, Object> internalMap; Set set2 =
	 * eachDayMap.entrySet(); Iterator iterator2 = set2.iterator();
	 * while(iterator2.hasNext()) { Map.Entry mentry2 =
	 * (Map.Entry)iterator2.next(); String oldDate =
	 * correctFormatDate(String.valueOf(mentry2.getKey())); Date date =
	 * getNewDate(oldDate); Calendar cal = Calendar.getInstance();
	 * cal.setTime(date); int month1 = cal.get(Calendar.MONTH); String monthName
	 * = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
	 * if(monthWiseResponse.getMonths() == null || !months.contains(monthName))
	 * { months.add(monthName); month = new Month(); dateList = new
	 * ArrayList<>(); internalMap = new TreeMap<>(); cal.set(Calendar.MONTH,
	 * month1); cal.set(Calendar.DAY_OF_MONTH, 1); int maxDay =
	 * cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	 * month.setMonthName(monthName); SimpleDateFormat df = new
	 * SimpleDateFormat("dd/MM/yyyy"); for (int i = 0; i < maxDay; i++) {
	 * cal.set(Calendar.DAY_OF_MONTH, i + 1); String currentDate =
	 * correctFormatDate(df.format(cal.getTime()));
	 * if(currentDate.equalsIgnoreCase(oldDate)){ internalMap.put(currentDate,
	 * String.valueOf(mentry2.getValue())); }else{
	 * 
	 * internalMap.put(currentDate, 0); } month.setDatesList(internalMap); }
	 * monhtsList.add(month); monthWiseResponse.setMonths(monhtsList);
	 * 
	 * }else if(months.contains(monthName)){ List<Month> ds =
	 * monthWiseResponse.getMonths(); for(Month dsMonts : ds){ String monthN =
	 * dsMonts.getMonthName(); if(monthN.equalsIgnoreCase(monthName)){
	 * Map<String, Object> oldDates = dsMonts.getDatesList(); for
	 * (Map.Entry<String, Object> entry : oldDates.entrySet()) {
	 * if(correctFormatDate(entry.getKey()).equalsIgnoreCase(oldDate)){
	 * oldDates.put(oldDate, mentry2.getValue()); } } } }
	 * monthWiseResponse.setMonths(ds);
	 * 
	 * monthWiseResponse.setMonths(monhtsList); } } return monthWiseResponse; }
	 */

	public MonthWiseResponse testDatesWithNew(List<String> d1) {
		MonthWiseResponse monthWiseResponse = new MonthWiseResponse();
		List<String> months = new ArrayList<>();
		List<String> dateList = Collections.EMPTY_LIST;
		List<Month> monhtsList = new ArrayList<>();
		Month month = null;
		Map<String, Object> internalMap;

		for (String da : d1) {
			Date date = getNewDate(da);

			System.out.println("date : " + date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int month1 = cal.get(Calendar.MONTH);
			String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
			System.out.println("monthName : " + monthName);
			System.out.println("months " + months.toString());
			if (monthWiseResponse.getMonths() == null || !months.contains(monthName)) {
				months.add(monthName);
				month = new Month();
				dateList = new ArrayList<>();
				internalMap = new HashMap<>();
				System.out.println("new month creating ");
				cal.set(Calendar.MONTH, month1);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				month.setMonthName(monthName);
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				for (int i = 0; i < maxDay; i++) {
					cal.set(Calendar.DAY_OF_MONTH, i + 1);

					internalMap.put(df.format(cal.getTime()), 0);
				}
				month.setDatesList(internalMap);
				monhtsList.add(month);
				monthWiseResponse.setMonths(monhtsList);

			} else if (months.contains(monthName)) {
				List<Month> ds = monthWiseResponse.getMonths();
				monthWiseResponse.setMonths(ds);
				System.out.println("month already created ");
				monthWiseResponse.setMonths(monhtsList);
			}
		}
		System.out.println("responseList : " + monthWiseResponse.toString());
		return monthWiseResponse;
	}

	public MonthWiseResponse getDatesCommitList(Map<String, String> eachDayMap) {

		System.out.println(
				"commit date " + eachDayMap.get("commitDate") + "Commit value : " + eachDayMap.get("commitsTotal"));

		MonthWiseResponse monthWiseResponse = new MonthWiseResponse();
		List<String> months = new ArrayList<>();
		List<Month> monhtsList = new ArrayList<>();
		Month month = null;
		Map<String, Object> internalMap;

		List<String> d1 = new ArrayList<>();
		// need to remove
		Set<String> keys = eachDayMap.keySet();
		System.out.println("keys : " + keys.toString());

		for (String key : keys) {
			System.out.println(" key :" + key);
			Date date = getNewDate(key);

			System.out.println("date : " + date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int month1 = cal.get(Calendar.MONTH);
			String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
			System.out.println("monthName : " + monthName);
			System.out.println("List :" + months.toString());
			if (monthWiseResponse.getMonths() == null || !months.contains(monthName)) {
				months.add(monthName);
				System.out.println("In MOnths : " + months.toString());
				month = new Month();
				internalMap = new HashMap<>();
				System.out.println("new month creating ");
				cal.set(Calendar.MONTH, month1);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				month.setMonthName(monthName);
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				for (int i = 0; i < maxDay; i++) {
					cal.set(Calendar.DAY_OF_MONTH, i + 1);
					// df.format(cal.getTime())
					internalMap.put(df.format(cal.getTime()), 0);
				}
				month.setDatesList(internalMap);
				monhtsList.add(month);
				monthWiseResponse.setMonths(monhtsList);

			} else if (months.contains(monthName)) {
				List<Month> ds = monthWiseResponse.getMonths();
				monthWiseResponse.setMonths(ds);
				System.out.println("month already created ");
				monthWiseResponse.setMonths(monhtsList);
			}
		}
		System.out.println("responseList : " + monthWiseResponse.toString());
		return monthWiseResponse;

	}

	/*
	 * public ResponseList getDataFromDate(Map<String, Object> map){ Map<String,
	 * Object> keyMap = new HashMap<>(); List<ResponseList> list = new
	 * ArrayList<>(); List<String> datesList = new ArrayList<>(); ResponseList
	 * responseList = new ResponseList(); List<String> dateList = new
	 * ArrayList<>(); map.entrySet().forEach(entry -> { //
	 * System.out.println("Key : " + entry.getKey() + " Value : " +
	 * entry.getValue()); });
	 * 
	 * Date month = getNewDate(String.valueOf(map.get("commitDate"))); Calendar
	 * cal = Calendar.getInstance(); cal.setTime(month); int month1 =
	 * cal.get(Calendar.MONTH); String monthName =
	 * cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
	 * cal.set(Calendar.MONTH, month1); cal.set(Calendar.DAY_OF_MONTH, 1);
	 * 
	 * int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	 * SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy"); for (int i = 1;
	 * i < maxDay; i++) { cal.set(Calendar.DAY_OF_MONTH, i + 1);
	 * dateList.add(df.format(cal.getTime()));
	 * 
	 * } keyMap.put(monthName, dateList);
	 * 
	 * responseList.setKeyMap(keyMap); return responseList; }
	 */

	// public ResponseList getDataFromDate(Map<String, Object> map){

	/*
	 * // working
	 * 
	 * @Override public MonthWiseResponse getDayWiseUserStories(){ Map<String,
	 * Object> keyMap = new HashMap<>(); MonthWiseResponse responseList = new
	 * MonthWiseResponse();
	 * 
	 * List<String> dates = new ArrayList(); dates.add("11/12/2017");
	 * dates.add("8/12/2017"); dates.add("26/9/2017"); dates.add("28/10/2017");
	 * dates.add("18/10/2017");
	 * 
	 * 
	 * 
	 * 
	 * responseList= testDatesWith(dates); return responseList; }
	 */

	public MonthWiseResponse testDatesWith(List<String> d1) {
		MonthWiseResponse monthWiseResponse = new MonthWiseResponse();
		List<String> months = new ArrayList<>();
		List<String> dateList = Collections.EMPTY_LIST;
		List<Month> monhtsList = new ArrayList<>();
		Month month = null;
		Map<String, Object> internalMap;
		for (String da : d1) {
			Date date = getNewDate(da);

			System.out.println("date : " + date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int month1 = cal.get(Calendar.MONTH);
			String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
			System.out.println("monthName : " + monthName);
			System.out.println("months " + months.toString());
			if (monthWiseResponse.getMonths() == null || !months.contains(monthName)) {
				months.add(monthName);
				month = new Month();
				dateList = new ArrayList<>();
				internalMap = new HashMap<>();
				System.out.println("new month creating ");
				cal.set(Calendar.MONTH, month1);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				month.setMonthName(monthName);
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				for (int i = 0; i < maxDay; i++) {
					cal.set(Calendar.DAY_OF_MONTH, i + 1);

					internalMap.put(df.format(cal.getTime()), 0);
				}
				month.setDatesList(internalMap);
				monhtsList.add(month);
				monthWiseResponse.setMonths(monhtsList);

			} else if (months.contains(monthName)) {
				List<Month> ds = monthWiseResponse.getMonths();
				monthWiseResponse.setMonths(ds);
				System.out.println("month already created ");
				monthWiseResponse.setMonths(monhtsList);
			}
		}
		System.out.println("responseList : " + monthWiseResponse.toString());
		return monthWiseResponse;
	}

	/*
	 * Working .............. public MonthWiseResponse
	 * testDatesWith(List<String> d1) { MonthWiseResponse monthWiseResponse =
	 * new MonthWiseResponse(); List<String> months = new ArrayList<>();
	 * List<String> dateList =Collections.EMPTY_LIST; List<Month> monhtsList =
	 * new ArrayList<>(); Month month = null; for(String da : d1) { Date date =
	 * getNewDate(da);
	 * 
	 * System.out.println("date : " + date); Calendar cal =
	 * Calendar.getInstance(); cal.setTime(date); int month1 =
	 * cal.get(Calendar.MONTH); String monthName =
	 * cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
	 * System.out.println("monthName : " +monthName);
	 * 
	 * if(monthWiseResponse.getMonths() == null || !months.contains(monthName))
	 * { months.add(monthName); month = new Month(); dateList = new
	 * ArrayList<>(); System.out.println("new month creating ");
	 * cal.set(Calendar.MONTH, month1); cal.set(Calendar.DAY_OF_MONTH, 1); int
	 * maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	 * month.setMonthName(monthName); SimpleDateFormat df = new
	 * SimpleDateFormat("dd/MM/yyyy"); for (int i = 0; i < maxDay; i++) {
	 * cal.set(Calendar.DAY_OF_MONTH, i + 1);
	 * dateList.add(df.format(cal.getTime())); } month.setDates(dateList);
	 * monhtsList.add(month); monthWiseResponse.setMonths(monhtsList); }else
	 * if(months.contains(monthName)){ List<Month> ds =
	 * monthWiseResponse.getMonths(); monthWiseResponse.setMonths(ds);
	 * System.out.println("month already created ");
	 * monthWiseResponse.setMonths(monhtsList); } }
	 * System.out.println("responseList : " +monthWiseResponse.toString());
	 * return monthWiseResponse; }
	 */

	/*
	 * working perfectly public MonthWiseResponse testDatesWith(List<String> d1)
	 * { ResponseList responseList = new ResponseList(); MonthWiseResponse
	 * monthWiseResponse = new MonthWiseResponse(); List<String> months = new
	 * ArrayList<>(); List<String> dateList =Collections.EMPTY_LIST; List<Month>
	 * monhtsList = new ArrayList<>(); Month month = null; // Date date =
	 * getNewDate("11/12/2017"); List<String> d1 = new ArrayList<>();
	 * d1.add("11/12/2017"); d1.add("21/12/2017");
	 * 
	 * 
	 * int count =0; for(String da : d1) { Date date = getNewDate(da);
	 * 
	 * System.out.println("date : " + date); Calendar cal =
	 * Calendar.getInstance(); cal.setTime(date); int month1 =
	 * cal.get(Calendar.MONTH); String monthName =
	 * cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
	 * System.out.println("monthName : " +monthName);
	 * 
	 * 
	 * 
	 * if(monthWiseResponse.getMonths() == null || !months.contains(monthName))
	 * { months.add(monthName); month = new Month(); dateList = new
	 * ArrayList<>(); System.out.println("new month creating ");
	 * cal.set(Calendar.MONTH, month1); cal.set(Calendar.DAY_OF_MONTH, 1); int
	 * maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	 * month.setMonthName(monthName); //SimpleDateFormat df = new
	 * SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat df = new
	 * SimpleDateFormat("dd/MM/yyyy"); for (int i = 0; i < maxDay; i++) {
	 * cal.set(Calendar.DAY_OF_MONTH, i + 1); //
	 * System.out.println(df.format(cal.getTime()));
	 * dateList.add(df.format(cal.getTime())); } month.setDates(dateList);
	 * monhtsList.add(month); monthWiseResponse.setMonths(monhtsList); }else
	 * if(months.contains(monthName)){ // if(monthWiseResponse.getMonths() !=
	 * null && monthWiseResponse.getMonths().contains(monthName)) { //
	 * if(monthWiseResponse.getMonths() != null &&
	 * monthWiseResponse.getMonths().contains(monthName)) { //month = new
	 * Month(); //months.add(monthName); //responseList.setMonth(monthName); //
	 * responseList.setDates(responseList.getDates());
	 * //month.setMonthName(monthName); List<Month> ds =
	 * monthWiseResponse.getMonths(); monthWiseResponse.setMonths(ds);
	 * System.out.println("month already created ");
	 * 
	 * monthWiseResponse.setMonths(monhtsList); }
	 * 
	 * } System.out.println("responseList : " +monthWiseResponse.toString());
	 * return monthWiseResponse; }
	 */

	/*
	 * public MonthWiseResponse testDatesWith(List<String> d1) { ResponseList
	 * responseList = new ResponseList(); MonthWiseResponse monthWiseResponse =
	 * new MonthWiseResponse(); List<String> months = new ArrayList<>();
	 * List<String> dateList =Collections.EMPTY_LIST; List<Month> monhtsList =
	 * new ArrayList<>(); Month month = null; // Date date =
	 * getNewDate("11/12/2017"); List<String> d1 = new ArrayList<>();
	 * d1.add("11/12/2017"); d1.add("21/12/2017");
	 * 
	 * 
	 * int count =0; for(String da : d1) { Date date = getNewDate(da);
	 * 
	 * System.out.println("date : " + date); Calendar cal =
	 * Calendar.getInstance(); cal.setTime(date); int month1 =
	 * cal.get(Calendar.MONTH); String monthName =
	 * cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
	 * System.out.println("monthName : " +monthName);
	 * 
	 * // if(monthWiseResponse.getMonths() != null &&
	 * monthWiseResponse.getMonths().contains(monthName)) {
	 * if(monthWiseResponse.getMonths() != null &&
	 * monthWiseResponse.getMonths().contains(monthName)) { //month = new
	 * Month(); //months.add(monthName); //responseList.setMonth(monthName); //
	 * responseList.setDates(responseList.getDates());
	 * //month.setMonthName(monthName); List<Month> ds =
	 * monthWiseResponse.getMonths(); monthWiseResponse.setMonths(ds);
	 * System.out.println("month already created ");
	 * 
	 * } else if(monthWiseResponse.getMonths() == null ||
	 * !monthWiseResponse.getMonths().contains(monthName)) { // else
	 * if(monthWiseResponse.getMonths() == null ) { month = new Month();
	 * dateList = new ArrayList<>(); System.out.println("new month creating ");
	 * cal.set(Calendar.MONTH, month1); cal.set(Calendar.DAY_OF_MONTH, 1); int
	 * maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	 * month.setMonthName(monthName); //SimpleDateFormat df = new
	 * SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat df = new
	 * SimpleDateFormat("dd/MM/yyyy"); for (int i = 0; i < maxDay; i++) {
	 * cal.set(Calendar.DAY_OF_MONTH, i + 1); //
	 * System.out.println(df.format(cal.getTime()));
	 * dateList.add(df.format(cal.getTime())); } month.setDates(dateList);
	 * monhtsList.add(month);
	 * 
	 * } monthWiseResponse.setMonths(monhtsList);
	 * 
	 * } System.out.println("responseList : " +monthWiseResponse.toString());
	 * return monthWiseResponse; }
	 */

	/*
	 * public MonthWiseResponse testDatesWith(List<String> d1) { ResponseList
	 * responseList = new ResponseList(); MonthWiseResponse monthWiseResponse =
	 * new MonthWiseResponse(); List<String> months = new ArrayList<>();
	 * List<String> dateList =Collections.EMPTY_LIST; List<Month> monhtsList =
	 * new ArrayList<>(); Month month = null; // Date date =
	 * getNewDate("11/12/2017"); List<String> d1 = new ArrayList<>();
	 * d1.add("11/12/2017"); d1.add("21/12/2017"); int count =0; for(String da :
	 * d1) { Date date = getNewDate(da);
	 * 
	 * System.out.println("date : " + date); Calendar cal =
	 * Calendar.getInstance(); cal.setTime(date); int month1 =
	 * cal.get(Calendar.MONTH); String monthName =
	 * cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
	 * System.out.println("monthName : " +monthName);
	 * 
	 * if(monhtsList != null && monhtsList.size()>0){ for(Month m :monhtsList){
	 * System.out.println("mm : "+m); if(m.getMonthName().equals("December")){
	 * System.out.println("2222222222"); } } }
	 * 
	 * 
	 * // if(monthWiseResponse.getMonths() != null &&
	 * monthWiseResponse.getMonths().contains(monthName)) {
	 * if(monthWiseResponse.getMonths() != null &&
	 * monthWiseResponse.getMonths().contains(monthName)) { month = new Month();
	 * //months.add(monthName); //responseList.setMonth(monthName); //
	 * responseList.setDates(responseList.getDates());
	 * month.setMonthName(monthName); month.setDates(responseList.getDates());
	 * monhtsList.add(month); monthWiseResponse.setMonths(monhtsList);
	 * System.out.println("month already created ");
	 * 
	 * } // else if(monthWiseResponse.getMonths() == null ||
	 * !monthWiseResponse.getMonths().contains(monthName)) { else
	 * if(monthWiseResponse.getMonths() == null ) { month = new Month();
	 * dateList = new ArrayList<>(); System.out.println("new month creating ");
	 * cal.set(Calendar.MONTH, month1); cal.set(Calendar.DAY_OF_MONTH, 1); int
	 * maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	 * month.setMonthName(monthName); //SimpleDateFormat df = new
	 * SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat df = new
	 * SimpleDateFormat("dd/MM/yyyy"); for (int i = 0; i < maxDay; i++) {
	 * cal.set(Calendar.DAY_OF_MONTH, i + 1); //
	 * System.out.println(df.format(cal.getTime()));
	 * dateList.add(df.format(cal.getTime())); } month.setDates(dateList);
	 * monhtsList.add(month);
	 * 
	 * }
	 * 
	 * monthWiseResponse.setMonths(monhtsList); }
	 * System.out.println("responseList : " +monthWiseResponse.toString());
	 * return monthWiseResponse; }
	 */

	public Date getNewDate(String dateStr) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return simpleDateFormat.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	/*
	 * @Override public List aggregationByAll() { Aggregation aggregation =
	 * newAggregation(
	 * group("brand","salesyear").sum("numberOfCars").as("total"),
	 * sort(Sort.Direction.ASC, previousOperation(), "brand") );
	 * 
	 * AggregationResults groupResults = mongoTemplate.aggregate( aggregation,
	 * Car.class, SalesReport.class);
	 * 
	 * List salesReport = groupResults.getMappedResults();
	 * 
	 * return salesReport; }
	 */

	@Override
	public MonthWiseResponse getDayWiseUserStoriesDummyData() {

		MonthWiseResponse monthWiseResponse = null;
		Map<String, Object> keyMap = new HashMap<>();
		MonthWiseResponse responseList = new MonthWiseResponse();

		List<Map<String, String>> list2 = new ArrayList<>();
		List<DayWiseList> list = new ArrayList<>();

		List<Map<String, Object>> finalResults = new ArrayList<Map<String, Object>>();

		Aggregation agg = Aggregation.newAggregation(sort(Sort.Direction.ASC, "commitedDate"),
				project().andExpression("week(commitedDate)").as("Label").andExpression("dayOfMonth(commitedDate)")
						.as("day").andExpression("month(commitedDate)").as("month").andExpression("year(commitedDate)")
						.as("year"),
				group(fields().and("Label").and("day").and("month").and("year")).count().as("count"));

		AggregationResults<Map> groupResults = mongoTemplate.aggregate(agg, Commit.class, Map.class);
		Map<String, String> eachDayMap = new HashMap<>();
		String dateString = null;

		for (Map map : groupResults) {
			// eachDayMap = new HashMap<>();
			dateString = map.get("day") + "/" + map.get("month") + "/" + map.get("year");
			System.out.println("dateString "+dateString+" map.get "+map.get("count").toString());//dateString 21/3/2018 map.get 9
			eachDayMap.put(map.get("day") + "/" + map.get("month") + "/" + map.get("year"),
					map.get("count").toString());
		}
		//System.out.println("monthWiseResponse.getMonths() "+monthWiseResponse.getMonths() +);
		monthWiseResponse = updateResponse();
		return monthWiseResponse;

	}

	public MonthWiseResponse updateResponse() {

		Map<String, String> eachDayMap = new HashMap();

		eachDayMap.put("06/12/2017", "2");
		eachDayMap.put("03/12/2017", "3");
		eachDayMap.put("10/11/2017", "1");
		eachDayMap.put("28/9/2017", "3");
		eachDayMap.put("26/9/2017", "1");
		eachDayMap.put("26/10/2017", "3");

		MonthWiseResponse monthWiseResponse = new MonthWiseResponse();
		List<String> months = new ArrayList<>();
		List<String> dateList = Collections.EMPTY_LIST;
		List<Month> monhtsList = new ArrayList<>();
		Month month = null;
		Map<String, Object> internalMap;

		Set set2 = eachDayMap.entrySet();
		Iterator iterator2 = set2.iterator();

		while (iterator2.hasNext()) {
			Map.Entry mentry2 = (Map.Entry) iterator2.next();
			System.out.print("Key is: " + mentry2.getKey() + " & Value is: " + mentry2.getValue());
			String oldDate = correctFormatDate(String.valueOf(mentry2.getKey()));
			System.out.println("oldDate : " + oldDate);
			Date date = getNewDate(oldDate);
			// System.out.println("date : " + date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int month1 = cal.get(Calendar.MONTH);
			String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
			// System.out.println("monthName : " +monthName);
			// System.out.println("months "+months.toString());

			if (monthWiseResponse.getMonths() == null || !months.contains(monthName)) {
				System.out.println("new month creating ");
				months.add(monthName);
				month = new Month();
				dateList = new ArrayList<>();
				internalMap = new TreeMap<>();

				cal.set(Calendar.MONTH, month1);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				month.setMonthName(monthName);
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				for (int i = 0; i < maxDay; i++) {
					cal.set(Calendar.DAY_OF_MONTH, i + 1);
					String currentDate = correctFormatDate(df.format(cal.getTime()));//not correct
					System.out.println("currentDate : " + currentDate + " oldDate : " + oldDate);

					/*
					 * if(currentDate.equalsIgnoreCase(oldDate)){
					 * System.out.println("PPPPPPPPPPPPPPPPPPPPPP"); }
					 */

					if (currentDate.equalsIgnoreCase(oldDate)) {

						internalMap.put(currentDate, String.valueOf(mentry2.getValue()));
					} else {
						
						internalMap.put(currentDate, 0);
					}
					month.setDatesList(internalMap);

				}

				// month.setDatesList(internalMap);
				monhtsList.add(month);
				monthWiseResponse.setMonths(monhtsList);

			} else if (months.contains(monthName)) {
				System.out.println("month already created ");
				List<Month> ds = monthWiseResponse.getMonths();
				for (Month dsMonts : ds) {
					String monthN = dsMonts.getMonthName();
					if (monthN.equalsIgnoreCase(monthName)) {
						Map<String, Object> oldDates = dsMonts.getDatesList();
						System.out.println("oldDates : " + oldDates.toString());

						for (Map.Entry<String, Object> entry : oldDates.entrySet()) {
							// System.out.println(" entry.getKey() " +
							// entry.getKey() + "oldDate : " + oldDate);
							if (correctFormatDate(entry.getKey()).equalsIgnoreCase(oldDate)) {
								// System.out.println("oldDate : "+oldDate +
								// "entry.getKey() : "+entry.getKey() +"value :
								// "+mentry2.getValue());
								oldDates.put(oldDate, mentry2.getValue());
								// System.out.println(" From DB oldDate " +
								// oldDate + "entry.getKey() "
								// +entry.getValue());
							}
						}
					}
				}
				monthWiseResponse.setMonths(ds);

				monthWiseResponse.setMonths(monhtsList);
			}
		}
		System.out.println("responseList : " + monthWiseResponse.toString());
		return monthWiseResponse;
	}

	public String correctFormatDate(String dateToFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String returnDate = "";
		try {
			Date date = formatter.parse(dateToFormat);
			returnDate = formatter.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnDate;
	}

	/*
	 * Working public MonthWiseResponse updateResponse( Map<String, String>
	 * eachDayMap){
	 * 
	 * MonthWiseResponse monthWiseResponse = new MonthWiseResponse();
	 * List<String> months = new ArrayList<>(); List<String> dateList
	 * =Collections.EMPTY_LIST; List<Month> monhtsList = new ArrayList<>();
	 * Month month = null; Map<String, Object> internalMap;
	 * 
	 * Set set2 = eachDayMap.entrySet(); Iterator iterator2 = set2.iterator();
	 * 
	 * while(iterator2.hasNext()) { Map.Entry mentry2 =
	 * (Map.Entry)iterator2.next(); System.out.print("Key is: "+mentry2.getKey()
	 * + " & Value is: "); System.out.println(mentry2.getValue()); String
	 * oldDate = String.valueOf(mentry2.getKey()); Date date =
	 * getNewDate(oldDate); System.out.println("date : " + date); Calendar cal =
	 * Calendar.getInstance(); cal.setTime(date); int month1 =
	 * cal.get(Calendar.MONTH); String monthName =
	 * cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
	 * System.out.println("monthName : " +monthName);
	 * System.out.println("months "+months.toString());
	 * 
	 * if(monthWiseResponse.getMonths() == null || !months.contains(monthName))
	 * { months.add(monthName); month = new Month(); dateList = new
	 * ArrayList<>(); internalMap = new TreeMap<>();
	 * System.out.println("new month creating "); cal.set(Calendar.MONTH,
	 * month1); cal.set(Calendar.DAY_OF_MONTH, 1); int maxDay =
	 * cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	 * month.setMonthName(monthName); SimpleDateFormat df = new
	 * SimpleDateFormat("dd/MM/yyyy"); for (int i = 0; i < maxDay; i++) {
	 * cal.set(Calendar.DAY_OF_MONTH, i + 1); String currentDate =
	 * df.format(cal.getTime()); if(currentDate.equalsIgnoreCase(oldDate)){
	 * internalMap.put(df.format(cal.getTime()),
	 * String.valueOf(mentry2.getValue())); }else{
	 * internalMap.put(df.format(cal.getTime()), 0); } }
	 * 
	 * month.setDatesList(internalMap); monhtsList.add(month);
	 * monthWiseResponse.setMonths(monhtsList); }else
	 * if(months.contains(monthName)){ List<Month> ds =
	 * monthWiseResponse.getMonths(); for(Month dsMonts : ds){ String monthN =
	 * dsMonts.getMonthName(); if(monthN.equalsIgnoreCase(monthName)){
	 * Map<String, Object> oldDates = dsMonts.getDatesList();
	 * 
	 * for (Map.Entry<String, Object> entry : oldDates.entrySet()) {
	 * if(entry.getKey().equalsIgnoreCase(oldDate)){ System.out.println("Key : "
	 * + entry.getKey() + " Value : " + entry.getValue());
	 * oldDates.put(entry.getKey(), mentry2.getValue()); } } } }
	 * monthWiseResponse.setMonths(ds);
	 * System.out.println("month already created ");
	 * monthWiseResponse.setMonths(monhtsList); } }
	 * System.out.println("responseList : " +monthWiseResponse.toString());
	 * return monthWiseResponse; }
	 */

	@Override
	public List<Map<String, Object>> getWeekWiseData() {
		List<WeekData> list = Collections.EMPTY_LIST;
		MonthWiseResponse monthWiseResponse = new MonthWiseResponse();
		List<String> listMonth = new ArrayList<>();
		Aggregation agg = Aggregation.newAggregation(sort(Sort.Direction.ASC, "commitedDate"),
				project().andExpression("week(commitedDate)").as("Label").andExpression("dayOfMonth(commitedDate)")
						.as("day").andExpression("month(commitedDate)").as("month").andExpression("year(commitedDate)")
						.as("year"),
				group(fields().and("Label").and("day").and("month").and("year")).count().as("count"));

		AggregationResults<Map> groupResults = mongoTemplate.aggregate(agg, Commit.class, Map.class);
		List<Map<String, Object>> responseResults = new ArrayList<Map<String, Object>>();
		Map<String, Object> weekDatMap;
		List<String> monthList = new ArrayList<>();
		Map<String, Object> eachDayMap = null;
		List<WeekData> weeDataList = Collections.EMPTY_LIST;
		String dateString = null;

		for (Map map : groupResults) {
			eachDayMap = new HashMap<>();
			dateString = map.get("day") + "/" + map.get("month") + "/" + map.get("year");
			// eachDayMap.put(map.get("day") + "/" + map.get("month") + "/" +
			// map.get("year"), map.get("count").toString());
			eachDayMap.put("year", map.get("year"));
			// eachDayMap.put("monthName", DateTimeUtil.getMonthName((int)
			// map.get("month")));
			monthList.add(DateTimeUtil.getMonthName((int) map.get("month")));
			// eachDayMap.put("jsonFileInfo",
			// findMatchedGitCommitsByDate(dateString));
			// System.out.println("eachDayMap : "+eachDayMap.toString());
		}

		Set<String> ss = new TreeSet<>(monthList);
		for (String mo : ss) {
			weekDatMap = new HashMap();
			weeDataList = new ArrayList<>();
			list = getMatchResult(2018, mo);
			weekDatMap.put("MonthName", mo);
			weekDatMap.put("MonthWiseInfo", list);
			responseResults.add(weekDatMap);
		}
		return responseResults;
	}

	public List<WeekData> getMatchResult(int year, String month) {

		List<WeekData> listWeekData = new ArrayList<>();
		WeekData weekData;
		Map<String, String> dates = DateTimeUtil.getDateDatesOldBetween(year, month);
		int count = 1;

		for (Map.Entry m : dates.entrySet()) {
			int commitCount = 0;

			System.out.println(m.getKey() + " " + m.getValue());
			weekData = getWeekWiseDateForAll(String.valueOf(m.getKey()), String.valueOf(m.getValue()));
			weekData.setLabel("Week " + count++);
			listWeekData.add(weekData);
		}
		// System.out.println("listWeekData : "+listWeekData.toString());
		return listWeekData;
	}

	public WeekData getWeekWiseDateForAll(String startDate, String endDate) {
		Query query = new Query();

		List<String> commitList = new ArrayList();
		List<String> jiraList = new ArrayList();
		WeekData weekData;

		query.addCriteria(Criteria.where("commitedDate").ne(null).andOperator(
				Criteria.where("commitedDate").gte(getDate(startDate + " 00:00")),
				Criteria.where("commitedDate").lte(getDate(endDate + " 23:59"))));

		List<Commit> listOfObjects = mongoTemplate.find(query, Commit.class);

		for (Commit commit : listOfObjects) {
			commitList.add(commit.getUserStoryId());
		}

		/*Query query2 = new Query();
		query2.addCriteria(Criteria.where("createdDate").ne(null).andOperator(
				Criteria.where("createdDate").gte(getDate(startDate + " 00:00")),
				Criteria.where("createdDate").lte(getDate(endDate + " 23:59"))));

		List<JiraBoards> jiraListOfObjects = mongoTemplate.find(query2, JiraBoards.class);
		for (JiraBoards jiraPropertys : jiraListOfObjects) {
			jiraList.add(jiraPropertys.getId());
		}*/

		weekData = new WeekData();
		//weekData.setAnalysis((int) jiraList.stream().count());
		weekData.setCommits(((int) commitList.stream().count()));
		//getJiraDetailsFull(); not use
		System.out.println("Completed done ");
		return weekData;
	}
	
	@Override
	public List<GitUserStorieResource> getUserStorieInfo(){
		List<GitUserStorieResource> gitUserStoryList = new ArrayList<>();
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.project("_id","_class", "userStoryId", "commitNumber", "project"));
		
		AggregationResults<GitUserStorieResource> aggregationResults = mongoTemplate.aggregate(aggregation, "gitUserStorieResource",
				GitUserStorieResource.class);
		gitUserStoryList.addAll(aggregationResults.getMappedResults());
		
		
		return gitUserStoryList;
	}

	@Override
	public String getJiraDetailsFull() {
		ResponseEntity<Object> summaryDataResultResponseEntity = null;

		String username = "susmitha";
		String password = "Secure*12";
		String httpsResp = "";

		try {

			logger.info("<<<<<<---- Pulling the JiraFull Details Data  --->>>>>>");
			byte[] message = (username + ":" + password).getBytes(Constants.utf);
			String auth = DatatypeConverter.printBase64Binary(message);
			Client client = Client.create();

			WebResource webResource = client.resource(Constants.GET_STORIES_WITH_PAGINATION);
			ClientResponse response = webResource.header(Constants.Content_Type, Constants.application_json)
					.header(Constants.Authorization, Constants.Basic + " " + auth).type(Constants.application_json)
					.accept(Constants.application_json).get(ClientResponse.class);
			httpsResp = response.getEntity(String.class);

			System.out.println("httpsResp : " + httpsResp.toString());

			/*
			 * HttpHeaders postHeaders = createHeaders(Username, Password);
			 * postHeaders.setContentType(MediaType.APPLICATION_JSON);
			 * 
			 * RestTemplate restTemplate = new RestTemplate(); String
			 * fooResourceUrl = "http://10.10.53.171/secure/Dashboard.jspa"; //
			 * ResponseEntity<String> response =
			 * restTemplate.getForEntity(fooResourceUrl + "/1", String.class);
			 * Map<String, String> params = new HashMap<>(2);
			 * params.put("username", "susmitha"); params.put("password",
			 * "Secure*12"); // dataLakeServiceSixtyDaysSummary, HttpMethod.GET,
			 * new HttpEntity<String>(postHeaders), SummaryDataResult.class,
			 * params summaryDataResultResponseEntity =
			 * restTemplate.exchange(fooResourceUrl, HttpMethod.GET, new
			 * HttpEntity<String>(postHeaders), Object.class);
			 * System.out.println("summaryDataResultResponseEntity : " +
			 * summaryDataResultResponseEntity.toString());
			 */

			// assertThat(response.getStatusCode(),
			// equalTo(org.springframework.http.HttpStatus.OK));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpsResp;
	}

	private HttpHeaders createHeaders(final String username, final String password) {
		HttpHeaders headers = new HttpHeaders() {
			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);
				set("Authorization", authHeader);
			}
		};
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

		logger.info("headers inside Datalakeserviceclient" + headers);

		return headers;
	}

}
