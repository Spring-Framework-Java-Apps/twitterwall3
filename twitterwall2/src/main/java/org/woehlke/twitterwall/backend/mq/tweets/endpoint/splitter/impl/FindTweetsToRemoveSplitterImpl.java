package org.woehlke.twitterwall.backend.mq.tweets.endpoint.splitter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.woehlke.twitterwall.oodm.model.Task;
import org.woehlke.twitterwall.oodm.model.Tweet;
import org.woehlke.twitterwall.oodm.model.parts.CountedEntities;
import org.woehlke.twitterwall.oodm.service.CountedEntitiesService;
import org.woehlke.twitterwall.oodm.service.TaskService;
import org.woehlke.twitterwall.oodm.service.TweetService;
import org.woehlke.twitterwall.backend.mq.tweets.endpoint.splitter.FindTweetsToRemoveSplitter;
import org.woehlke.twitterwall.backend.mq.tasks.TaskMessage;
import org.woehlke.twitterwall.backend.mq.tweets.msg.TweetMessage;
import org.woehlke.twitterwall.backend.mq.tweets.msg.TweetMessageBuilder;

import java.util.ArrayList;
import java.util.List;

@Component("mqFindTweetsToRemoveSplitter")
public class FindTweetsToRemoveSplitterImpl implements FindTweetsToRemoveSplitter {

    private final TweetService tweetService;

    private final TaskService taskService;

    private final CountedEntitiesService countedEntitiesService;

    private final TweetMessageBuilder tweetMessageBuilder;

    @Autowired
    public FindTweetsToRemoveSplitterImpl(TweetService tweetService, TaskService taskService, CountedEntitiesService countedEntitiesService, TweetMessageBuilder tweetMessageBuilder) {
        this.tweetService = tweetService;
        this.taskService = taskService;
        this.countedEntitiesService = countedEntitiesService;
        this.tweetMessageBuilder = tweetMessageBuilder;
    }

    @Override
    public List<Message<TweetMessage>> splitTweetMessage(Message<TaskMessage> message) {
        CountedEntities countedEntities = countedEntitiesService.countAll();
        List<Message<TweetMessage>> tweets = new ArrayList<>();
        TaskMessage msgIn = message.getPayload();
        long id = msgIn.getTaskId();
        Task task = taskService.findById(id);
        task =  taskService.start(task,countedEntities);
        int pageTweet = 1;
        int pageSize = 20;
        Pageable pageRequestTweet = new PageRequest(pageTweet, pageSize);
        //TODO: #229 https://github.com/phasenraum2010/twitterwall2/issues/229
        Page<Tweet> tweetList = tweetService.getAll(pageRequestTweet);
        int loopId = 0;
        int loopAll = tweetList.getContent().size();
        for (Tweet tweet: tweetList) {
            loopId++;
            Message<TweetMessage> mqMessageOut = tweetMessageBuilder.buildTweetMessage(message,tweet,loopId,loopAll);
            tweets.add(mqMessageOut);
        }
        return tweets;
    }
}
