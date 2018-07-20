package com.valuelabs.poc.devops_utms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.valuelabs.poc.devops_utms.client.JiraClient;
import com.valuelabs.poc.devops_utms.repository.mongo.JiraPropertiesRepository;
import com.valuelabs.poc.devops_utms.resource.BoardResource;
import com.valuelabs.poc.devops_utms.resource.BugResource;
import com.valuelabs.poc.devops_utms.resource.JiraBoard;
import com.valuelabs.poc.devops_utms.resource.JiraBoardResource;
import com.valuelabs.poc.devops_utms.resource.JiraBoards;
import com.valuelabs.poc.devops_utms.resource.JiraIssueType;
import com.valuelabs.poc.devops_utms.resource.JiraSprintDetailsResource;
import com.valuelabs.poc.devops_utms.resource.JiraSprintResource;
import com.valuelabs.poc.devops_utms.resource.JiraSprints;
import com.valuelabs.poc.devops_utms.resource.JiraUserStories;
import com.valuelabs.poc.devops_utms.resource.Sprint;
import com.valuelabs.poc.devops_utms.resource.SprintDetails;
import com.valuelabs.poc.devops_utms.resource.SubTaskResource;
import com.valuelabs.poc.devops_utms.resource.Subtasks;
import com.valuelabs.poc.devops_utms.resource.UserStorieResource;
import com.valuelabs.poc.devops_utms.resource.UserStory;
import com.valuelabs.poc.devops_utms.resource.UserStoryFields;
import com.valuelabs.poc.devops_utms.resource.UserStoryResource;
import com.valuelabs.poc.devops_utms.resource.Worklog;
import com.valuelabs.poc.devops_utms.resource.Worklogs;
import com.valuelabs.poc.devops_utms.util.AppUtil;
import com.valuelabs.poc.devops_utms.util.DateTimeUtil;

@EnableScheduling
public class JiraServiceImpl implements JiraService {

	private static final Logger logger = LoggerFactory.getLogger(JiraServiceImpl.class);

	@Autowired
	private JiraPropertiesRepository jiraPropertiesRepository;

	// @Autowired
	// private JiraSprintsRepository jiraSprintsRepository;

	// @Autowired
	// private JiraUserStoriesRepository jiraUserStoriesRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	JiraClient jiraClient;

	/*
	 * private String username = "Anantha";// ckatta private String password =
	 * "Secure*12";// c!1209A023
	 * 
	 * // @Scheduled(fixedRate = 30000) public void jiraJob() throws Exception {
	 * JiraServiceImpl job = new JiraServiceImpl();
	 * job.getStorysFromJira(jiraPropertiesRepository); }
	 * 
	 * public void getStorysFromJira(JiraPropertiesRepository
	 * jiraPropertiesRepository) throws Exception {
	 * 
	 * logger.info("<<<<<<---- Pulling the Jira Data  --->>>>>>"); byte[]
	 * message = (username + ":" + password).getBytes(Constants.utf); String
	 * auth = DatatypeConverter.printBase64Binary(message); Client client =
	 * Client.create(); WebResource webResource =
	 * client.resource(Constants.GET_STORIES_WITH_PAGINATION); ClientResponse
	 * response = webResource.header(Constants.Content_Type,
	 * Constants.application_json) .header(Constants.Authorization,
	 * Constants.Basic + " " + auth).type(Constants.application_json)
	 * .accept(Constants.application_json).get(ClientResponse.class); String
	 * httpsResp = response.getEntity(String.class);
	 * 
	 * if (httpsResp != null) { JSONObject jObject = new JSONObject(httpsResp);
	 * JSONArray c = jObject.getJSONArray("issues");
	 * 
	 * for (int i = 0; i < c.length(); i++) { Date createdate = null; Date
	 * lastVieweddate = null; Date duedate = null; String assigneFirstname =
	 * null; String assigneLastName = null; String assigneEmailAddress = null;
	 * JiraPropertys jiradbobject = new JiraPropertys();
	 * 
	 * JSONObject jObject1 = c.getJSONObject(i);
	 * 
	 * JSONObject jObject2 = (JSONObject) jObject1.get("fields"); JSONObject
	 * jObject3 = (JSONObject) jObject2.get("issuetype"); JSONObject jpriority =
	 * (JSONObject) jObject2.get("priority"); JSONObject jproject = (JSONObject)
	 * jObject2.get("project"); JSONObject jOstatus = (JSONObject)
	 * jObject2.get("status");
	 * 
	 * JSONObject jOreporter = (JSONObject) jObject2.get("reporter");
	 * SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); if
	 * (!jObject2.get("created").equals(null)) { createdate =
	 * formatter.parse((String) jObject2.get("created")); }
	 * 
	 * if (!jObject2.get("lastViewed").equals(null)) { lastVieweddate =
	 * formatter.parse((String) jObject2.get("lastViewed")); } if
	 * (!jObject2.get("duedate").equals(null)) { duedate =
	 * formatter.parse((String) jObject2.get("duedate")); } if
	 * (!jObject2.get("assignee").equals(null)) { JSONObject jOassignee =
	 * (JSONObject) jObject2.get("assignee"); assigneFirstname = ((String)
	 * jOassignee.get("name")); // assigneLastName = ((String)
	 * jOassignee.get("key")); assigneEmailAddress = ((String)
	 * jOassignee.get("emailAddress")); }
	 * 
	 * jiradbobject.setId((String) jObject1.get("id"));
	 * jiradbobject.setSummery((String) jObject2.get("summary"));
	 * jiradbobject.setName((String) jObject3.get("name"));
	 * jiradbobject.setPriority((String) jpriority.get("name"));
	 * jiradbobject.setProjectName((String) jproject.get("name"));
	 * jiradbobject.setProjectKey((String) jproject.get("key"));
	 * jiradbobject.setCreatedDate(createdate);
	 * jiradbobject.setLastviewedDate(lastVieweddate);
	 * jiradbobject.setDuedateDate(duedate);
	 * jiradbobject.setStatusDescription((String) jOstatus.get("description"));
	 * jiradbobject.setReporterFirstName((String) jOreporter.get("name")); //
	 * jiradbobject.setReporterLastName((String) // jOreporter.get("key") );
	 * jiradbobject.setReporterEmailID((String) jOreporter.get("emailAddress"));
	 * jiradbobject.setAssigneeFirstname(assigneFirstname);
	 * jiradbobject.setAssigneeLastName(assigneLastName);
	 * jiradbobject.setAssigneeemailAddress(assigneEmailAddress);
	 * 
	 * jiraPropertiesRepository.save(jiradbobject);
	 * 
	 * }
	 * 
	 * } }
	 */

	/*
	 * @Override public JiraPropertys getJiraById(String id){ if(id!=null){
	 * JiraPropertys dbresults = jiraPropertiesRepository.findOne(id);
	 * //System.out.println("results data = " + results.getId()); return
	 * dbresults; } JiraPropertys dbresults; return dbresults; }
	 */

	/*
	 * @Override public Iterable<JiraPropertys> getAllJiraDetails() {
	 * logger.info(" Pulling all JiraDetails "); Iterable<JiraPropertys> dblist
	 * = jiraPropertiesRepository.findAll(); return dblist; }
	 * 
	 * @Override public ArrayList getAllJiraIds() {
	 * logger.info(" Pulling all JiraDetails by Jira ID's "); String id = null;
	 * ArrayList list = new ArrayList(); Iterable<JiraPropertys> dblist =
	 * jiraPropertiesRepository.findAll(); Iterator<JiraPropertys> iter =
	 * dblist.iterator(); while (iter.hasNext()) {
	 * list.add(iter.next().getId()); Collections.sort(list); } return list; }
	 */

	public List<JiraBoard> retriveJiraBoard() {
		List<JiraBoard> jobBoardList = new ArrayList<>();
		JiraBoards jiraBoards = jiraClient.getJiraBoards();

		for (JiraBoard jiraBoard : jiraBoards.getJiraBoard()) {
			jobBoardList.add(jiraBoard);
		}
		return jobBoardList;
	}

	@Override
	@Scheduled(fixedRate = 6000)
	public List<JiraBoard> syncJiraInfo() throws Exception {

		List<JiraBoard> jobBoardsList = new ArrayList<>();

		List<JiraBoard> jiraBoardList = retriveJiraBoard();
		JiraBoard jobBoardsResource = new JiraBoard();
		// if (!AppUtil.isBlank(jiraBoards)) {

		// jobBoardsResource.setMaxResults(jiraBoards.getMaxResults());
		// jobBoardsResource.setStartAt(jiraBoards.getStartAt());
		logger.info("<-------  pulling Jira Data     -------------->");
		for (JiraBoard jiraBoard : jiraBoardList) {
			if (!AppUtil.isBlank(jiraBoard)) {

				String boardId = jiraBoard.getBoardId().toString();
				jiraBoard.setBoardUniqueId(boardId);
				JiraSprints jiraSprints = jiraClient.getJiraSprints(boardId);
				List<Sprint> sprintList = new ArrayList<>();

				if (!AppUtil.isBlank(jiraSprints)) {
					for (Sprint sprint : jiraSprints.getSprintDetails()) {
						List<String> storyId = new ArrayList<>();
						String sprintId = String.valueOf(sprint.getSprintId());
						JiraUserStories jiraUserStories = jiraClient.getJiraUserStories(boardId, sprintId);
						if (!AppUtil.isBlank(jiraUserStories)) {
							// List<Subtasks> taskIds = new ArrayList<>();
							for (UserStory userStory : jiraUserStories.getIssues()) {
								// storyId.add(userStory.getKey());
								if (!AppUtil.isBlank(userStory)) {
									UserStoryFields storyFeilds = userStory.getFields();
									JiraIssueType issueType = storyFeilds.getIssueType();
									if (!AppUtil.isBlank(issueType)) {
										if (issueType.getName().equals("Story")) {
											storyFeilds.setUserStoryType("Enhancement");

											storyId.add(userStory.getKey());
											sprint.setUserStoryList(storyId);

										} else if (issueType.equals("Bug")) {
											storyFeilds.setUserStoryType("Bug");
										} else if (issueType.equals("Sub-task")) {
											storyFeilds.setUserStoryType("Sub-task");
										}
									}

								}
							}
						}
						sprint.setUserStories(jiraUserStories);

						sprintList.add(sprint);

					}
					jiraSprints.setSprintDetails(sprintList);
					jiraBoard.setJiraSprints(jiraSprints);
				}

			}
			jobBoardsList.add(jiraBoard);
			// jobBoardsResource.setJiraBoard(jobBoardsList);

			// }
			mongoTemplate.save(jiraBoard);
		}
		return jobBoardsList;

	}

	@Override
	public List<JiraBoard> getJiraData() {

		List<JiraBoard> jiraBoardList = new ArrayList<>();
		Aggregation aggregation = Aggregation.newAggregation(Aggregation.project("boardId", "boardUniqueId", "boardName", "type", "jiraSprints"));

		AggregationResults<JiraBoard> aggregationResults = mongoTemplate.aggregate(aggregation, "jiraBoard",
				JiraBoard.class);
		jiraBoardList.addAll(aggregationResults.getMappedResults());

		return jiraBoardList;
	}

	// This API for getting UserStories
	@Override
	public List<JiraBoardResource> getUserStories() {

		List<JiraBoardResource> jiraBoardsList = new ArrayList<>();

		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.project("boardId", "boardUniqueId", "boardName", "type", "jiraSprints"),
				Aggregation.unwind("jiraSprints.sprintDetails"),
				Aggregation.unwind("jiraSprints.sprintDetails.userStories.issues"), Aggregation.match(Criteria
						.where("jiraSprints.sprintDetails.userStories.issues.fields.issueType.name").is("Story")));

		AggregationResults<JiraBoardResource> aggregationResults = mongoTemplate.aggregate(aggregation, "jiraBoard",
				JiraBoardResource.class);
		jiraBoardsList.addAll(aggregationResults.getMappedResults());

		return jiraBoardsList;
	}

	public List<UserStory> retriveUserStories() {

		List<JiraBoardResource> jira = getUserStories();
		List<UserStory> userStoryList = new ArrayList<>();

		for (JiraBoardResource jiraBoardResource : jira) {
			JiraSprintResource jirSprint = jiraBoardResource.getJiraSprints();
			JiraSprintDetailsResource jiraSprintDetails = jirSprint.getSprintDetails();
			UserStorieResource jiraUserStories = jiraSprintDetails.getUserStories();
			UserStory storyDetails = jiraUserStories.getIssues();
			userStoryList.add(storyDetails);

		}

		return userStoryList;
	}

	public List<UserStoryResource> getuserStory() {

		List<JiraBoardResource> jira = getUserStories();
		List<UserStoryResource> userStoryList = new ArrayList<>();

		for (JiraBoardResource jiraBoardResource : jira) {
			JiraSprintResource jirSprint = jiraBoardResource.getJiraSprints();
			JiraSprintDetailsResource jiraSprintDetails = jirSprint.getSprintDetails();
			UserStorieResource jiraUserStories = jiraSprintDetails.getUserStories();
			UserStory storyDetails = jiraUserStories.getIssues();

			UserStoryResource userStoryResource = new UserStoryResource();
			if (storyDetails != null) {
				userStoryResource.setUserStoryId(storyDetails.getKey());
				UserStoryFields storyFields = storyDetails.getFields();

				userStoryResource.setUserStoryType(storyFields.getUserStoryType());
				userStoryResource.setUserStoryTitle(storyFields.getUserStoryTitle());
				userStoryResource.setLastUpdatedTimeStamp(DateTimeUtil.dateToString(new Date()));
				userStoryResource.setUserStoryStatus(
						storyFields.getUserStoryStatus() != null ? storyFields.getUserStoryStatus().getName() : "");
				userStoryResource.setUserStoryDescrtption(storyFields.getDescription());
				userStoryResource.setUpdatedDate(DateTimeUtil.dateToString(storyFields.getUpdatedDate()));
				userStoryResource
						.setAssignedTo(storyFields.getAssignee() != null ? storyFields.getAssignee().getName() : "");
				userStoryResource
						.setCreatedBy(storyFields.getCreator() != null ? storyFields.getCreator().getName() : "");
				userStoryResource.setCreatedDate(DateTimeUtil.dateToString(storyFields.getCreatedDate()));
				userStoryResource.setProductName(
						storyFields.getProject() != null ? storyFields.getProject().getProjectName() : "");
				userStoryResource.setSprintId(jiraSprintDetails.getSprintName());
				Worklog worklog = storyFields.getWorklog();
				int effort = 0;
				for (Worklogs logs : worklog.getLogs()) {
					// todo
					// String newstr = "Word#$#$% Word
					// 1234".replaceAll("[^A-Za-z]+", "");
					String[] effortArray = logs.getTimeSpent().split(" ");
					int i=0,j = 0;
					for(String log : effortArray){
						if(log.indexOf('d')>=1){
							i = Integer.parseInt(log.replaceAll("[\\D]", ""));							
						}else if(log.indexOf('h')>=1){
							 j = 1;
						}
					}
					
					effort = effort + i+j;
				}
				userStoryResource.setEfforts(effort);
				userStoryResource.setStoryPoints(getStoryPoints(effort));
			}
			userStoryList.add(userStoryResource);

		}

		return userStoryList;
	}

	// This API for getting SubTasks
	@Override
	public List<JiraBoardResource> getSubTasks() {

		List<JiraBoardResource> jiraBoardsList = new ArrayList<>();
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.project("boardId", "boardUniqueId", "boardName", "type", "jiraSprints"),
				Aggregation.unwind("jiraSprints.sprintDetails"),
				Aggregation.unwind("jiraSprints.sprintDetails.userStories.issues"), Aggregation.match(Criteria
						.where("jiraSprints.sprintDetails.userStories.issues.fields.issueType.name").is("Sub-task")));

		AggregationResults<JiraBoardResource> aggregationResults = mongoTemplate.aggregate(aggregation, "jiraBoard",
				JiraBoardResource.class);
		jiraBoardsList.addAll(aggregationResults.getMappedResults());

		return jiraBoardsList;

	}

	public List<SubTaskResource> getSubTask() {

		List<JiraBoardResource> jira = getSubTasks();
		List<SubTaskResource> taskList = new ArrayList<>();

		for (JiraBoardResource jiraBoardResource : jira) {

			JiraSprintResource jirSprint = jiraBoardResource.getJiraSprints();
			JiraSprintDetailsResource jiraSprintDetails = jirSprint.getSprintDetails();
			UserStorieResource jiraUserStories = jiraSprintDetails.getUserStories();
			UserStory storyDetails = jiraUserStories.getIssues();

			SubTaskResource taskResource = new SubTaskResource();
			if (storyDetails != null) {

				taskResource.setTaskId(storyDetails.getKey());
				UserStoryFields storyFields = storyDetails.getFields();
				taskResource
						.setAssignedTo(storyFields.getAssignee() != null ? storyFields.getAssignee().getName() : "");
				taskResource.setCreatedBy(storyFields.getCreator() != null ? storyFields.getCreator().getName() : "");
				Worklog worklog = storyFields.getWorklog();
				int effort = 0;
				for (Worklogs logs : worklog.getLogs()) {
					// todo
					// String newstr = "Word#$#$% Word
					// 1234".replaceAll("[^A-Za-z]+", "");
					effort = effort + Integer.parseInt(logs.getTimeSpent().replaceAll("[\\D]", ""));
				}
				taskResource.setEffort(effort);
				taskResource.setLastUpdatedTimeStamp(DateTimeUtil.dateToString(new Date()));
				taskResource.setTaskCreatedDate(DateTimeUtil.dateToString(storyFields.getCreatedDate()));
				taskResource.setTaskStatus(storyFields.getUserStoryStatus().getName());
				taskResource.setUserStoryId(storyFields.getParent().getKey());

			}
			taskList.add(taskResource);

		}

		return taskList;
	}

	public List<UserStory> getSubUserStory() {

		List<JiraBoardResource> jira = getSubTasks();
		List<UserStory> taskList = new ArrayList<>();

		for (JiraBoardResource jiraBoardResource : jira) {

			JiraSprintResource jirSprint = jiraBoardResource.getJiraSprints();
			JiraSprintDetailsResource jiraSprintDetails = jirSprint.getSprintDetails();
			UserStorieResource jiraUserStories = jiraSprintDetails.getUserStories();
			UserStory storyDetails = jiraUserStories.getIssues();

			if (storyDetails != null) {
				taskList.add(storyDetails);
			}

		}

		return taskList;
	}

	public List<UserStory> getUserStoryTaskDetails() {

		List<UserStory> subTaskList = getSubUserStory();
		List<UserStory> userStoryList = retriveUserStories();
		List<UserStory> storyTaskList = new ArrayList<>();

		for (UserStory story : userStoryList) {

			List<Subtasks> taskList = new ArrayList<>();
			UserStoryFields storyFields = story.getFields();
			for (UserStory task : subTaskList) {
				UserStoryFields taskFields = task.getFields();
				Subtasks subTask = new Subtasks();
				if (taskFields != null && taskFields.getParent() != null
						&& taskFields.getParent().getKey().equals(story.getKey())) {
					subTask.setSubUserStory(task);
					taskList.add(subTask);
				}
				storyFields.setSubtasks(taskList);
				story.setFields(storyFields);

			}
			storyTaskList.add(story);
		}

		return storyTaskList;
	}

	// This API for getting Bugs
	@Override
	public List<JiraBoardResource> getBugs() {

		List<JiraBoardResource> jiraBoardsList = new ArrayList<>();
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.project("boardId", "boardUniqueId", "boardName", "type", "jiraSprints"),
				Aggregation.unwind("jiraSprints.sprintDetails"),
				Aggregation.unwind("jiraSprints.sprintDetails.userStories.issues"), Aggregation.match(Criteria
						.where("jiraSprints.sprintDetails.userStories.issues.fields.issueType.name").is("Bug")));

		AggregationResults<JiraBoardResource> aggregationResults = mongoTemplate.aggregate(aggregation, "jiraBoard",
				JiraBoardResource.class);
		jiraBoardsList.addAll(aggregationResults.getMappedResults());

		return jiraBoardsList;

	}

	public List<BugResource> getBug() {

		List<JiraBoardResource> jira = getBugs();
		List<BugResource> bugList = new ArrayList<>();

		for (JiraBoardResource jiraBoardResource : jira) {

			// JiraBoardResource jiraBoardResource =
			// jiraBoardsResource.getJiraBoard();
			JiraSprintResource jirSprint = jiraBoardResource.getJiraSprints();
			JiraSprintDetailsResource jiraSprintDetails = jirSprint.getSprintDetails();
			UserStorieResource jiraUserStories = jiraSprintDetails.getUserStories();
			UserStory storyDetails = jiraUserStories.getIssues();

			BugResource bugResource = new BugResource();

			if (storyDetails != null) {

				bugResource.setBugId(storyDetails.getKey());
				UserStoryFields storyFields = storyDetails.getFields();
				bugResource.setAssignedTo(storyFields.getAssignee() != null ? storyFields.getAssignee().getName() : "");
				bugResource.setCreatedby(storyFields.getCreator() != null ? storyFields.getCreator().getName() : "");
				bugResource.setCreatedDate(DateTimeUtil.dateToString(storyFields.getCreatedDate()));
				bugResource.setDescription(storyFields.getDescription());
				bugResource.setLastUpdateTimeStamp(DateTimeUtil.dateToString(new Date()));

			}

			bugList.add(bugResource);
		}

		return bugList;
	}

	// This API for getting Jira Data by project Name
	@Override
	public List<JiraBoardResource> getJiraByProjectName(String projectName) {

		List<JiraBoardResource> jiraBoardsList = new ArrayList<>();
		Aggregation aggregation = Aggregation
				.newAggregation(Aggregation.project("boardId", "boardUniqueId", "boardName", "type", "jiraSprints"),
						Aggregation.unwind("jiraSprints.sprintDetails"),
						Aggregation.unwind("jiraSprints.sprintDetails.userStories.issues"),
						Aggregation.match(Criteria
								.where("jiraSprints.sprintDetails.userStories.issues.fields.project.projectName")
								.is(projectName)));

		AggregationResults<JiraBoardResource> aggregationResults = mongoTemplate.aggregate(aggregation, "jiraBoard",
				JiraBoardResource.class);
		jiraBoardsList.addAll(aggregationResults.getMappedResults());

		return jiraBoardsList;

	}
	/*
	 * @Override public List<BoardsResource> retirveJiraSprintsDetails() {
	 * 
	 * logger.info("Retriving JiraDetails from DB ");
	 * 
	 * List<BoardsResource> jiraBoardsList = new ArrayList<>();
	 * 
	 * Aggregation aggregation =
	 * Aggregation.newAggregation(Aggregation.project("maxResults", "startAt",
	 * "jiraBoard"), Aggregation.unwind("jiraBoard"),
	 * Aggregation.unwind("jiraBoard.jiraSprints.sprintDetails"));
	 * 
	 * 
	 * 
	 * AggregationResults<BoardsResource> aggregationResults =
	 * mongoTemplate.aggregate(aggregation, "jiraBoards", BoardsResource.class);
	 * jiraBoardsList.addAll(aggregationResults.getMappedResults());
	 * 
	 * return jiraBoardsList; }
	 */

	public List<SprintDetails> retirveJiraSprints() {

		List<JiraBoardResource> jira = getUserStories();
		List<SprintDetails> sprintList = new ArrayList<>();

		for (JiraBoardResource jiraBoardResource : jira) {

			JiraSprintResource jirSprint = jiraBoardResource.getJiraSprints();
			JiraSprintDetailsResource jiraSprintDetails = jirSprint.getSprintDetails();

			SprintDetails sprint = new SprintDetails();
			if (jiraSprintDetails != null) {
				if (jiraSprintDetails.getUserStories() != null && jiraSprintDetails.getUserStories().getIssues() != null
						&& jiraSprintDetails.getUserStories().getIssues().getFields() != null
						&& jiraSprintDetails.getUserStories().getIssues().getFields().getIssueType() != null
						&& jiraSprintDetails.getUserStories().getIssues().getFields().getIssueType().getName()
								.equals("Story"))
					;
				String projectName = jiraSprintDetails.getUserStories().getIssues().getFields().getProject() != null ? jiraSprintDetails.getUserStories().getIssues().getFields().getProject().getProjectName() : "";
				sprint.setProductName(projectName);
				sprint.setLastUpdatedTimeStamp(DateTimeUtil.dateToString(new Date()));
				sprint.setSprintDescription(jiraSprintDetails.getSprintName());
				if (jiraSprintDetails.getEndDate() != null) {
					sprint.setSprintEndDate(DateTimeUtil.dateToString(jiraSprintDetails.getEndDate()));
				}
				if (jiraSprintDetails.getStartDate() != null) {
					sprint.setSprintStartDate(DateTimeUtil.dateToString(jiraSprintDetails.getStartDate()));
				}
				sprint.setSprintId(jiraSprintDetails.getSprintId());
				sprint.setSprintStatus(jiraSprintDetails.getState());
				sprint.setUserStoryId(jiraSprintDetails.getUserStories().getIssues().getKey());
			}
			sprintList.add(sprint);

		}

		return sprintList;
	}

	public int getStoryPoints(int effort) {
		int storyPoint = 0;

		if (effort < 1) {
			storyPoint = 1;
		} else if (effort >= 1 && effort < 3) {
			storyPoint = 3;
		} else if (effort >= 3 && effort < 5) {
			storyPoint = 5;
		} else if (effort >= 5 && effort < 8) {
			storyPoint = 8;
		} else if (effort >= 8 && effort < 13) {
			storyPoint = 13;
		} else if (effort >= 13 && effort < 20) {
			storyPoint = 20;
		} else if (effort >= 20 && effort < 40) {
			storyPoint = 40;
		}

		return storyPoint;
	}

	// This API for getting Jira data by Sprint Start date
	@Override
	public List<BoardResource> retirveJiraSprintsByStartDate(String startDate, String endDate) {

		logger.info("Retriving JiraDetails from DB ");

		List<BoardResource> jiraBoardsList = new ArrayList<>();
		Date startate = DateTimeUtil.stringToDate(startDate);
		Date endate = null;
		if (!endDate.isEmpty()) {
			endate = DateTimeUtil.stringToDate(endDate);
		}

		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.project("boardId", "boardUniqueId", "boardName", "type", "jiraSprints"),
				Aggregation.unwind("jiraSprints.sprintDetails"),
				Aggregation.unwind("jiraSprints.sprintDetails.userStories.issues"),
				endDate.isEmpty()
						? Aggregation
								.match(Criteria.where("jiraBoard.jiraSprints.sprintDetails.startDate").is(startate))
						: Aggregation.match(Criteria.where("jiraBoard.jiraSprints.sprintDetails.endDate").is(endate)),
				Aggregation.match(Criteria.where("jiraBoard.jiraSprints.sprintDetails.startDate").is(startate)));

		AggregationResults<BoardResource> aggregationResults = mongoTemplate.aggregate(aggregation, "jiraBoard",
				BoardResource.class);
		jiraBoardsList.addAll(aggregationResults.getMappedResults());

		return jiraBoardsList;
	}

	public List<JiraBoard> getSprintWiseUserStories() {

		return null;
	}

	@Override
	public List<JiraBoard> retriveJiraData() {

		logger.info("Retriving JiraDetails from DB ");

		List<JiraBoard> jiraUserStotyList = getJiraData();
		List<UserStory> storyTaskList = getUserStoryTaskDetails();
		List<JiraBoard> jiraBoardList = new ArrayList<>();
		Map<String, String> storySubTaskMap = new HashMap<>();
		
		

		for (JiraBoard jiraBoard : jiraUserStotyList) {
			JiraSprints sprintResource = jiraBoard.getJiraSprints();
				
			if (sprintResource != null) {
				List<Sprint> sprintList = new ArrayList<>();
				List<Sprint> sprintDetails = sprintResource.getSprintDetails();
				for (Sprint sprint : sprintDetails) {
					JiraUserStories jiraUserStories = sprint.getUserStories();
					List<UserStory> storyList = new ArrayList<>();
					for (UserStory jiraStory : jiraUserStories.getIssues()) {
						for (UserStory story : storyTaskList) {
							if (story.getKey().equals(jiraStory.getKey())
									&& !storySubTaskMap.containsKey(jiraStory.getKey())) {
								storySubTaskMap.put(story.getKey(), story.getKey());
								storyList.add(story);

							}
						}

					}
					jiraUserStories.setIssues(storyList);
					sprint.setUserStories(jiraUserStories);
					sprintList.add(sprint);
				}
				sprintResource.setSprintDetails(sprintList);
				
			}
			jiraBoard.setJiraSprints(sprintResource);
			jiraBoardList.add(jiraBoard);
		}

		return jiraBoardList;
	}

	@Override
	public Object getUserStory() {
		// TODO Auto-generated method stub
		return null;
	}
}
