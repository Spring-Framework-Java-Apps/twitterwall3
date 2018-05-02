package org.woehlke.twitterwall.backend.mq.users.endpoint.serviceactivator.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.woehlke.twitterwall.oodm.model.Task;
import org.woehlke.twitterwall.oodm.model.User;
import org.woehlke.twitterwall.oodm.service.TaskService;
import org.woehlke.twitterwall.backend.mq.users.msg.UserMessageBuilder;
import org.woehlke.twitterwall.backend.service.transform.UserTransformService;
import org.woehlke.twitterwall.backend.mq.users.endpoint.serviceactivator.UserTransformator;
import org.woehlke.twitterwall.backend.mq.users.msg.UserMessage;

@Component("mqUserTransformator")
public class UserTransformatorImpl implements UserTransformator {

    private final UserTransformService userTransformService;

    private final TaskService taskService;

    private final UserMessageBuilder userMessageBuilder;

    @Autowired
    public UserTransformatorImpl(UserTransformService userTransformService, TaskService taskService, UserMessageBuilder userMessageBuilder) {
        this.userTransformService = userTransformService;
        this.taskService = taskService;
        this.userMessageBuilder = userMessageBuilder;
    }

    @Override
    public Message<UserMessage> transformUser(Message<UserMessage> mqMessageIn) {
        UserMessage receivedMessage = mqMessageIn.getPayload();
        if(receivedMessage.isIgnoreTransformation()){
            Message<UserMessage> mqMessageOut = MessageBuilder.withPayload(receivedMessage)
                    .copyHeaders(mqMessageIn.getHeaders())
                    .setHeader("transformed",Boolean.TRUE)
                    .build();
            return mqMessageOut;
        } else {
            long id = receivedMessage.getTaskMessage().getTaskId();
            Task task = taskService.findById(id);
            User user = userTransformService.transform(receivedMessage.getTwitterProfile(),task);
            UserMessage outMsg = new UserMessage(
                    receivedMessage.getTaskMessage(),
                    receivedMessage.getTwitterProfile(),
                    user
            );
            Message<UserMessage> mqMessageOut =
                    MessageBuilder.withPayload(outMsg)
                    .copyHeaders(mqMessageIn.getHeaders())
                    .setHeader("transformed",Boolean.TRUE)
                    .build();
            return mqMessageOut;
        }
    }
}
