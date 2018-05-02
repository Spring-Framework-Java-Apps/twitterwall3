package org.woehlke.twitterwall.backend.mq.tweets.endpoint.serviceactivator.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.woehlke.twitterwall.oodm.model.Task;
import org.woehlke.twitterwall.oodm.model.Tweet;
import org.woehlke.twitterwall.oodm.service.TaskService;
import org.woehlke.twitterwall.backend.mq.tweets.endpoint.serviceactivator.TweetTransformator;
import org.woehlke.twitterwall.backend.mq.tweets.msg.TweetMessage;
import org.woehlke.twitterwall.backend.mq.tweets.msg.TweetMessageBuilder;
import org.woehlke.twitterwall.backend.service.transform.TweetTransformService;

@Component("mqTweetTransformator")
public class TweetTransformatorImpl implements TweetTransformator {

    private final TweetTransformService tweetTransformService;

    private final TaskService taskService;

    private final TweetMessageBuilder tweetMessageBuilder;

    @Autowired
    public TweetTransformatorImpl(TweetTransformService tweetTransformService, TaskService taskService, TweetMessageBuilder tweetMessageBuilder) {
        this.tweetTransformService = tweetTransformService;
        this.taskService = taskService;
        this.tweetMessageBuilder = tweetMessageBuilder;
    }

    @Override
    public Message<TweetMessage> transformTweet(Message<TweetMessage> mqMessageIn) {
        TweetMessage inComingTweetMessage = mqMessageIn.getPayload();
        TweetMessage retVal = null;
        if(inComingTweetMessage.isIgnoreTransformation()){
            Message<TweetMessage> mqMessageOut = MessageBuilder.withPayload(inComingTweetMessage)
                    .copyHeaders(mqMessageIn.getHeaders())
                    .setHeader("transformed",Boolean.TRUE)
                    .build();
            return mqMessageOut;
        } else {
            long taskId = inComingTweetMessage.getTaskMessage().getTaskId();
            Task task = taskService.findById(taskId);
            Tweet myTweet = tweetTransformService.transform(inComingTweetMessage.getTweetFromTwitter(),task);
            boolean tansformed = true;
            retVal = new TweetMessage(inComingTweetMessage.getTaskMessage(),myTweet,inComingTweetMessage.getTweetFromTwitter());
            Message<TweetMessage> mqMessageOut = MessageBuilder.withPayload(retVal)
                    .copyHeaders(mqMessageIn.getHeaders())
                    .setHeader("transformed",Boolean.TRUE)
                    .build();
            return mqMessageOut;
        }
    }
}
