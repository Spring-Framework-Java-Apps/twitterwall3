package org.woehlke.twitterwall.frontend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.woehlke.twitterwall.configuration.properties.FrontendProperties;
import org.woehlke.twitterwall.configuration.properties.TwitterProperties;
import org.woehlke.twitterwall.frontend.content.Symbols;
import org.woehlke.twitterwall.frontend.content.ContentFactory;
import org.woehlke.twitterwall.oodm.model.Task;
import org.woehlke.twitterwall.oodm.model.TaskHistory;
import org.woehlke.twitterwall.oodm.model.User;
import org.woehlke.twitterwall.oodm.service.TaskHistoryService;
import org.woehlke.twitterwall.oodm.service.TaskService;
import org.woehlke.twitterwall.oodm.service.UserService;
import org.woehlke.twitterwall.backend.mq.tasks.TaskStartFireAndForget;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by tw on 11.07.17.
 */
@Controller
@RequestMapping(path="/task")
public class TaskController {

    @RequestMapping(path="/all")
    public String getAll(
            @RequestParam(name= "page",defaultValue=""+ ContentFactory.FIRST_PAGE_NUMBER) int page,
            Model model
    ) {
        String msg = "/task/all: ";
        String title = "Tasks";
        String subtitle = "List aller Tasks";
        String symbol = Symbols.TASKS_ALL.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        Pageable pageRequest = new PageRequest(
                page, frontendProperties.getPageSize(),
                Sort.Direction.DESC,
                "timeStarted"
        );
        Page<Task> allTasks = taskService.getAll(pageRequest);
        model.addAttribute("tasks",allTasks);
        return PATH+"/all";
    }

    @RequestMapping(path="/{id}")
    public String getTaskById(
        @RequestParam(name= "page" ,defaultValue=""+ ContentFactory.FIRST_PAGE_NUMBER) int page,
        @PathVariable("id") Task task, Model model) {
        if(task == null){
            throw new EntityNotFoundException();
        } else {
            String msg = "/task/ "+task.getId();
            String title = "Task "+task.getUniqueId();
            String subtitle = "List of TasksHistory for Task";
            String symbol = Symbols.TASK.toString();
            model = contentFactory.setupPage(model,title,subtitle,symbol);
            Pageable pageRequest = new PageRequest(page, frontendProperties.getPageSize());
            Page<TaskHistory> taskHistoryList = taskHistoryService.findByTask(task,pageRequest);
            model.addAttribute("task",task);
            model.addAttribute("taskHistoryList",taskHistoryList);
            return PATH+"/id";
        }
    }

    @RequestMapping("/start/createTestData")
    public String createTTestData(Model model) {
        model = contentFactory.setupPage(
                model,"Test Data Tweets",
                twitterProperties.getSearchQuery(),
                Symbols.GET_TEST_DATA.toString()
        );
        if(frontendProperties.getContextTest()){
            Task taskTweets = mqTaskStartFireAndForget.createTestDataForTweets();
            Task taskUsers =  mqTaskStartFireAndForget.createTestDataForUser();
            model.addAttribute("taskTweets", taskTweets);
            model.addAttribute("taskUsers",taskUsers);
        } else {
            model.addAttribute("taskTweets",null);
            model.addAttribute("taskUsers",null);
        }
        return PATH+"/start/createTestData";
    }

    @RequestMapping("/start/user/onlist/renew")
    public String getOnListRenew(
            @RequestParam(name= "page" ,defaultValue=""+ ContentFactory.FIRST_PAGE_NUMBER) int page,
            Model model
    ) {
        Pageable pageRequest = new PageRequest(page, frontendProperties.getPageSize());
        String msg = "getOnListRenew: ";
        log.debug(msg+"START startTask.fetchUsersFromList");
        Task task = mqTaskStartFireAndForget.fetchUsersFromList();
        model.addAttribute("task",task);
        log.debug(msg+"DONE startTask.fetchUsersFromList: ");
        log.debug(msg+"START userService.findOnList(): ");
        Page<User> usersOnList = userService.getOnList(pageRequest);
        log.debug(msg+"DONE userService.findOnList(): ");
        model.addAttribute("users", usersOnList);
        String symbol = Symbols.LEAF.toString();
        String title = "Renew List of Users On List";
        model = contentFactory.setupPage(model, title, "Users", symbol);
        return PATH+"/start/renew";
    }

    @RequestMapping(path="/start/tweets/search")
    public String  fetchTweetsFromTwitterSearchStartTask(Model model) {
        String msg = "/start/tweets/search";
        String title = "Cronjob Task started: fetch Tweets from Search";
        String subtitle = "/start/tweets/search";
        String symbol = Symbols.TASK.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        Task task = mqTaskStartFireAndForget.fetchTweetsFromSearch();
        model.addAttribute("task",task);
        return PATH+"/start/taskStarted";
    }

    @RequestMapping(path="/start/tweets/update")
    public String updateTweetsStartTask(Model model) {
        String msg = "/start/tweets/update";
        String title = "Cronjob Task started: update Tweets";
        String subtitle = "/start/tweets/update";
        String symbol = Symbols.TASK.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        Task task = mqTaskStartFireAndForget.updateTweets();
        model.addAttribute("task",task);
        return PATH+"/start/taskStarted";
    }

    @RequestMapping(path="/start/users/update")
    public String updateUsersStartTask(Model model) {
        String msg = "/start/users/update";
        String title = "Cronjob Task started: update Users";
        String subtitle = "/start/users/update";
        String symbol = Symbols.TASK.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        Task task = mqTaskStartFireAndForget.updateUsers();
        model.addAttribute("task",task);
        return PATH+"/start/taskStarted";
    }

    @RequestMapping(path="/start/users/list/fetch")
    public String fetchUsersFromDefinedUserListStartTask(Model model){
        String msg = "/start/users/list/fetch";
        String title = "Cronjob Task started: fetch Users from List";
        String subtitle = "/start/users/list/fetch";
        String symbol = Symbols.TASK.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        Task task = mqTaskStartFireAndForget.fetchUsersFromList();
        model.addAttribute("task",task);
        return PATH+"/start/taskStarted";
    }

    @RequestMapping(path="/start/users/follower/fetch")
    public String fetchFollowerStartTask(Model model){
        String msg = "/start/users/follower/fetch";
        String title = "Cronjob Task started: fetch Follower";
        String subtitle = "/start/users/follower/fetch";
        String symbol = Symbols.TASK.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        Task task = mqTaskStartFireAndForget.fetchFollower();
        model.addAttribute("task",task);
        return PATH+"/start/taskStarted";
    }

    @RequestMapping(path="/start/users/friends/fetch")
    public String fetchFriendsStartTask(Model model){
        String msg = "/start/users/friends/fetch";
        String title = "Cronjob Task started: fetch Friends";
        String subtitle = "/start/users/friends/fetch";
        String symbol = Symbols.TASK.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        Task task = mqTaskStartFireAndForget.fetchFriends();
        model.addAttribute("task",task);
        return PATH+"/start/taskStarted";
    }

    @RequestMapping(path="/start/users/mentions/update")
    public String updateUserProfilesFromMentionsStartTask(Model model){
        String msg = "/start/users/mentions/update";
        String title = "Cronjob Task started: update Users from Mentions";
        String subtitle = "/start/users/mentions/update";
        String symbol = Symbols.TASK.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        Task task = mqTaskStartFireAndForget.updateUsersFromMentions();
        model.addAttribute("task",task);
        return PATH+"/start/taskStarted";
    }

    @RequestMapping(path="/start/tweets/timeline/home")
    public String getHomeTimeline(Model model) {
        String msg = "/start/tweets/timeline/home";
        String title = "Cronjob Task started: getHomeTimeline";
        String subtitle = "/start/tweets/timeline/home";
        String symbol = Symbols.TASK.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        Task task = mqTaskStartFireAndForget.getHomeTimeline();
        model.addAttribute("task",task);
        return PATH+"/start/taskStarted";
    }

    @RequestMapping(path="/start/tweets/timeline/user")
    public String getUserTimeline(Model model) {
        String msg = "/start/tweets/timeline/user";
        String title = "Cronjob Task started: getUserTimeline";
        String subtitle = "/start/tweets/timeline/user";
        String symbol = Symbols.TASK.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        Task task = mqTaskStartFireAndForget.getUserTimeline();
        model.addAttribute("task",task);
        return PATH+"/start/taskStarted";
    }

    @RequestMapping(path="/start/tweets/mentions")
    public String getMentions(Model model) {
        String msg = "/start/tweets/mentions";
        String title = "Cronjob Task started: getMentions";
        String subtitle = "/start/tweets/mentions";
        String symbol = Symbols.TASK.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        Task task = mqTaskStartFireAndForget.getMentions();
        model.addAttribute("task",task);
        return PATH+"/start/taskStarted";
    }

    @RequestMapping(path="/start/tweets/favorites")
    public String getFavorites(Model model) {
        String msg = "/start/tweets/favorites";
        String title = "Cronjob Task started: getFavorites";
        String subtitle = "/start/tweets/favorites";
        String symbol = Symbols.TASK.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        Task task = mqTaskStartFireAndForget.getFavorites();
        model.addAttribute("task",task);
        return PATH+"/start/taskStarted";
    }

    @RequestMapping(path="/start/tweets/myretweets")
    public String getRetweetsOfMe(Model model) {
        String msg = "/start/tweets/myretweets";
        String title = "Cronjob Task started: getRetweetsOfMe";
        String subtitle = "/start/tweets/myretweets";
        String symbol = Symbols.TASK.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        Task task = mqTaskStartFireAndForget.getRetweetsOfMe();
        model.addAttribute("task",task);
        return PATH+"/start/taskStarted";
    }

    @RequestMapping(path="/start/userlists")
    public String getLists(Model model) {
        String msg = "/start/userlists";
        String title = "Cronjob Task started: getLists";
        String subtitle = "/start/userlists";
        String symbol = Symbols.TASK.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        List<Task> listOfTasks = new ArrayList<>();
        //Task task1 = mqTaskStartFireAndForget.getLists();
        //listOfTasks.add(task1);
        Task task1 = mqTaskStartFireAndForget.startFetchListsForUsers();
        listOfTasks.add(task1);
        Task task2 = mqTaskStartFireAndForget.fetchUserlistOwners();
        listOfTasks.add(task2);
        model.addAttribute("listOfTasks",listOfTasks);
        return PATH+"/start/tasksStarted";
    }

    @RequestMapping(path="/start/url/update")
    public String startUpdateUrls(Model model) {
        String msg = "/start/url/update";
        String title = "Cronjob Task started: UpdateUrls";
        String subtitle = "/start/url/update";
        String symbol = Symbols.TASK.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        Task task = mqTaskStartFireAndForget.startUpdateUrls();
        model.addAttribute("task",task);
        return PATH+"/start/taskStarted";
    }


    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    private final String PATH = "task";

    private final UserService userService;

    private final TaskService taskService;

    private final TaskHistoryService taskHistoryService;

    private final FrontendProperties frontendProperties;

    private final ContentFactory contentFactory;

    private final TaskStartFireAndForget mqTaskStartFireAndForget;

    private final TwitterProperties twitterProperties;

    @Autowired
    public TaskController(
            UserService userService, TaskService taskService,
            TaskHistoryService taskHistoryService,
            FrontendProperties frontendProperties,
            ContentFactory contentFactory,
            TaskStartFireAndForget mqTaskStartFireAndForget,
            TwitterProperties twitterProperties) {
        this.userService = userService;
        this.taskService = taskService;
        this.taskHistoryService = taskHistoryService;
        this.frontendProperties = frontendProperties;
        this.contentFactory = contentFactory;
        this.mqTaskStartFireAndForget = mqTaskStartFireAndForget;
        this.twitterProperties = twitterProperties;
    }

}
