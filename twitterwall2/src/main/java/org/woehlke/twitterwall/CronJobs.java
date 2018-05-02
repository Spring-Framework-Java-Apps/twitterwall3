package org.woehlke.twitterwall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.woehlke.twitterwall.configuration.properties.SchedulerProperties;
import org.woehlke.twitterwall.oodm.model.Task;
import org.woehlke.twitterwall.backend.mq.tasks.TaskStartFireAndForget;

/**
 * Created by tw on 10.06.17.
 */
@Component
public class CronJobs {

    @Scheduled(initialDelay= TEN_SECONDS, fixedRate = ONE_DAY)
    public void createImprintUserAsync(){
        String msg = "create Imprint User (Async) ";
        if(!schedulerProperties.getSkipFortesting()) {
            Task task = mqTaskStartFireAndForget.createImprintUserAsync();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 2, fixedRate = FIVE_MINUTES)
    public void fetchTweetsFromTwitterSearch() {
        String msg = "fetch Tweets From TwitterSearch ";
        if((schedulerProperties.getAllowFetchTweetsFromTwitterSearch())  && (!schedulerProperties.getSkipFortesting())) {
            Task task = mqTaskStartFireAndForget.fetchTweetsFromSearch();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 3, fixedRate = TWELVE_HOURS)
    public void fetchUsersFromDefinedUserList(){
        String msg = "fetch Users from Defined User List ";
        if((schedulerProperties.getFetchUsersFromDefinedUserListAllow()) && (!schedulerProperties.getSkipFortesting())) {
            Task task = mqTaskStartFireAndForget.fetchUsersFromList();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 4, fixedRate = TWELVE_HOURS)
    public void getHomeTimeline() {
        String msg = "get Home Timeline Tweets ";
        if((schedulerProperties.getAllowGetHomeTimeline())  && (!schedulerProperties.getSkipFortesting())) {
            Task task = mqTaskStartFireAndForget.getHomeTimeline();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 5, fixedRate = TWELVE_HOURS)
    public void getUserTimeline() {
        String msg = " get User Timeline Tweets ";
        if((schedulerProperties.getAllowGetUserTimeline())  && (!schedulerProperties.getSkipFortesting())) {
            Task task = mqTaskStartFireAndForget.getUserTimeline();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 6, fixedRate = TWELVE_HOURS)
    public void getMentions() {
        String msg = " get Mentions ";
        if((schedulerProperties.getAllowGetMentions())  && (!schedulerProperties.getSkipFortesting())) {
            Task task = mqTaskStartFireAndForget.getMentions();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 7, fixedRate = TWELVE_HOURS)
    public void getFavorites() {
        String msg = " get Favorites ";
        if((schedulerProperties.getAllowGetFavorites())  && (!schedulerProperties.getSkipFortesting())) {
            Task task = mqTaskStartFireAndForget.getFavorites();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 8, fixedRate = TWELVE_HOURS)
    public void getRetweetsOfMe() {
        String msg = " get Retweets Of Me ";
        if((schedulerProperties.getAllowGetRetweetsOfMe())  && (!schedulerProperties.getSkipFortesting())) {
            Task task = mqTaskStartFireAndForget.getRetweetsOfMe();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 9, fixedRate = TWELVE_HOURS)
    public void getLists() {
        String msg = " get Lists ";
        if((schedulerProperties.getAllowGetLists())  && (!schedulerProperties.getSkipFortesting())) {
            Task task = mqTaskStartFireAndForget.getLists();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
            task = mqTaskStartFireAndForget.fetchUserlistOwners();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 10, fixedRate = ONE_DAY)
    public void fetchFollower(){
        String msg = "fetch Follower ";
        if((schedulerProperties.getFetchFollowerAllow())  && (!schedulerProperties.getSkipFortesting())) {
            Task task = mqTaskStartFireAndForget.fetchFollower();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 11, fixedRate = ONE_DAY)
    public void fetchFriends(){
        String msg = "fetch Friends ";
        if((schedulerProperties.getFetchFriendsAllow()) && (!schedulerProperties.getSkipFortesting())) {
            Task task = mqTaskStartFireAndForget.fetchFriends();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 12, fixedRate = ONE_HOUR)
    public void removeOldDataFromStorage(){
        String msg = "remove Old Data From Storage: ";
        if((schedulerProperties.getRemoveOldDataFromStorageAllow())  && (!schedulerProperties.getSkipFortesting())) {
            Task task = mqTaskStartFireAndForget.removeOldDataFromStorage();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 13, fixedRate = ONE_HOUR)
    public void updateUserProfilesFromMentions(){
        String msg = "update User Profiles From Mentions";
        if((schedulerProperties.getAllowUpdateUserProfilesFromMention()) && (!schedulerProperties.getSkipFortesting())) {
            Task task = mqTaskStartFireAndForget.updateUsersFromMentions();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 14, fixedRate = ONE_DAY)
    public void updateTweets() {
        String msg = "update Tweets ";
        if((schedulerProperties.getAllowUpdateTweets()) && (!schedulerProperties.getSkipFortesting())){
            Task task = mqTaskStartFireAndForget.updateTweets();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 15, fixedRate = ONE_DAY)
    public void updateUserProfiles() {
        String msg = "update User Profiles ";
        if((schedulerProperties.getAllowUpdateUserProfiles())  && (!schedulerProperties.getSkipFortesting())) {
            Task task = mqTaskStartFireAndForget.updateUsers();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 16, fixedRate = ONE_DAY)
    public void startUpdateUrls(){
        String msg = "start UpdateUrls ";
        if(!schedulerProperties.getSkipFortesting()) {
            Task task = mqTaskStartFireAndForget.startUpdateUrls();
            log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Scheduled(initialDelay= TEN_SECONDS * 16, fixedRate = ONE_DAY)
    public void startGarbageCollection(){
        String msg = "start UpdateUrls ";
        if(!schedulerProperties.getSkipFortesting()) {
            //TODO:
            //Task task = mqTaskStartFireAndForget.startGarbageCollection();
            //log.debug(msg+ "SCHEDULED: task "+task.getUniqueId());
        }
    }

    @Autowired
    public CronJobs(
        SchedulerProperties schedulerProperties,
        @Qualifier("mqTaskStartFireAndForget") TaskStartFireAndForget mqTaskStartFireAndForget
    ) {
        this.schedulerProperties = schedulerProperties;
        this.mqTaskStartFireAndForget = mqTaskStartFireAndForget;
    }

    public final static long TEN_SECONDS = 10 * 1000;

    public final static long ONE_MINUTE = 60 * 1000;

    public final static long FIVE_MINUTES = 5 * ONE_MINUTE;

    public final static long ONE_HOUR = 60 * ONE_MINUTE;

    public final static long TWELVE_HOURS = 12 * ONE_HOUR;

    public final static long ONE_DAY = 24 * ONE_HOUR;

    private static final Logger log = LoggerFactory.getLogger(CronJobs.class);

    private final SchedulerProperties schedulerProperties;

    private final TaskStartFireAndForget mqTaskStartFireAndForget;
}
