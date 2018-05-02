package org.woehlke.twitterwall.oodm.model;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.woehlke.twitterwall.oodm.model.tasks.TaskSendType;
import org.woehlke.twitterwall.oodm.model.tasks.TaskStatus;
import org.woehlke.twitterwall.oodm.model.tasks.TaskType;

import java.util.Date;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TweetTest implements DomainObjectMinimalTest  {

    private static final Logger log = LoggerFactory.getLogger(TweetTest.class);

    @Test
    @Override
    public void test001getUniqueIdTest() throws Exception {
        String descriptionTask = "start: ";
        TaskType type = TaskType.FETCH_TWEETS_FROM_SEARCH;
        TaskSendType taskSendType = TaskSendType.NO_MQ;
        TaskStatus taskStatus = TaskStatus.READY;
        Date timeStarted = new Date();
        Date timeLastUpdate = timeStarted;
        Date timeFinished = null;
        Task task = new Task(descriptionTask,type,taskStatus, taskSendType,timeStarted,timeLastUpdate,timeFinished);

        String msg = "getUniqueIdTest: ";
        Task createdBy = task;
        Task updatedBy = null;
        Long idTwitter = 57646546476L;
        String idStr = idTwitter.toString();
        String text  = "";
        Date createdAt = new Date();
        Tweet tweet = new Tweet(createdBy,updatedBy,idTwitter,idStr,text,createdAt);
        Assert.assertEquals(msg,idTwitter.toString(),tweet.getUniqueId());
    }

    @Test
    @Override
    public void test002isValidTest() throws Exception {
        String msg = "isValidTest: ";

        String descriptionTask = "start: ";
        TaskType type = TaskType.FETCH_TWEETS_FROM_SEARCH;
        TaskSendType taskSendType = TaskSendType.NO_MQ;
        TaskStatus taskStatus = TaskStatus.READY;
        Date timeStarted = new Date();
        Date timeLastUpdate = timeStarted;
        Date timeFinished = null;
        Task task = new Task(descriptionTask,type,taskStatus, taskSendType,timeStarted,timeLastUpdate,timeFinished);

        Task createdBy = task;
        Task updatedBy = null;
        Long idTwitter = 57646546476L;
        String idStr = idTwitter.toString();
        String text  = "";
        Date createdAt = new Date();
        Tweet tweet = new Tweet(createdBy,updatedBy,idTwitter,idStr,text,createdAt);
        Assert.assertEquals(msg,idTwitter.toString(),tweet.getUniqueId());
        tweet.setIdTwitter(null);
        Assert.assertFalse(msg,tweet.isValid());
    }
}
