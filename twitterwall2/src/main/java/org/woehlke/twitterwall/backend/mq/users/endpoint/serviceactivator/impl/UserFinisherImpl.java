package org.woehlke.twitterwall.backend.mq.users.endpoint.serviceactivator.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.woehlke.twitterwall.oodm.model.Task;
import org.woehlke.twitterwall.oodm.model.User;
import org.woehlke.twitterwall.oodm.model.parts.CountedEntities;
import org.woehlke.twitterwall.oodm.service.TaskService;
import org.woehlke.twitterwall.backend.mq.users.endpoint.serviceactivator.UserFinisher;
import org.woehlke.twitterwall.backend.mq.users.msg.UserMessage;
import org.woehlke.twitterwall.backend.mq.users.msg.UserMessageBuilder;
import org.woehlke.twitterwall.backend.mq.users.msg.UserResultList;
import org.woehlke.twitterwall.oodm.service.CountedEntitiesService;

import java.util.ArrayList;
import java.util.List;

@Component("mqUserFinisher")
public class UserFinisherImpl implements UserFinisher {

    @Override
    public Message<UserResultList> finish(Message<List<UserMessage>> incomingMessageList) {
        long taskId = 0L;
        List<User> users = new ArrayList<>();
        List<UserMessage> userMessageList = incomingMessageList.getPayload();
        for(UserMessage msg :userMessageList){
            taskId = msg.getTaskMessage().getTaskId();
            users.add(msg.getUser());
        }
        UserResultList userResultList = new UserResultList(taskId,users);
        Message<UserResultList> mqMessageOut = MessageBuilder.withPayload(userResultList)
                .copyHeaders(incomingMessageList.getHeaders())
                .setHeader("persisted",Boolean.TRUE)
                .build();
        return mqMessageOut;
    }

    @Override
    public void finishAsnyc(Message<List<UserMessage>> incomingMessageList) {
        List<UserMessage> userMessageList = incomingMessageList.getPayload();
        CountedEntities countedEntities = countedEntitiesService.countAll();
        long taskId=0L;
        for(UserMessage msg :userMessageList){
            taskId = msg.getTaskMessage().getTaskId();
            break;
        }
        Task task = taskService.findById(taskId);
        String msgDone = "Sucessfully finished task "+task.getTaskType()+" via MQ by FIRE_AND_FORGET_SENDER";
        taskService.done(msgDone,task,countedEntities);
        log.debug(msgDone);
    }

    @Override
    public void finishOneUserAsnyc(Message<UserMessage> incomingMessage) {
        CountedEntities countedEntities = countedEntitiesService.countAll();
        long taskId = incomingMessage.getPayload().getTaskMessage().getTaskId();
        Task task = taskService.findById(taskId);
        String msgDone = "Sucessfully finished task "+task.getTaskType()+" via MQ by FIRE_AND_FORGET_SENDER";
        taskService.done(msgDone,task,countedEntities);
        log.debug(msgDone);
    }

    @Autowired
    public UserFinisherImpl(TaskService taskService, CountedEntitiesService countedEntitiesService, UserMessageBuilder userMessageBuilder) {
        this.taskService = taskService;
        this.countedEntitiesService = countedEntitiesService;
        this.userMessageBuilder = userMessageBuilder;
    }

    private final TaskService taskService;

    private final CountedEntitiesService countedEntitiesService;

    private final UserMessageBuilder userMessageBuilder;

    private static final Logger log = LoggerFactory.getLogger(UserFinisherImpl.class);
}
