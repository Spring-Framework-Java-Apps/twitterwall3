package org.woehlke.twitterwall.backend.service.persist.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.woehlke.twitterwall.oodm.model.entities.Entities;
import org.woehlke.twitterwall.oodm.model.User;
import org.woehlke.twitterwall.oodm.model.Task;
import org.woehlke.twitterwall.oodm.service.UserService;
import org.woehlke.twitterwall.backend.service.persist.StoreEntitiesProcess;
import org.woehlke.twitterwall.backend.service.persist.StoreUserProcess;


/**
 * Created by tw on 09.07.17.
 */
@Component
public class StoreUserProcessImpl implements StoreUserProcess {

    @Override
    public User storeUserProcess(User user, Task task){
        String msg = "User.storeUserProcess "+user.getUniqueId()+" : "+task.getUniqueId()+" : ";
        try {
            Entities entities = user.getEntities();
            entities = storeEntitiesProcess.storeEntitiesProcess(entities, task);
            user.setEntities(entities);
            user = userService.store(user, task);
            entities = storeEntitiesProcess.updateEntitiesForUserProcess(user, task);
            user.setEntities(entities);
        } catch (Exception e){
            log.error(msg+e.getMessage());
        }
        return user;
    }

    private static final Logger log = LoggerFactory.getLogger(StoreUserProcessImpl.class);

    private final UserService userService;

    private final StoreEntitiesProcess storeEntitiesProcess;

    @Autowired
    public StoreUserProcessImpl(UserService userService, StoreEntitiesProcess storeEntitiesProcess) {
        this.userService = userService;
        this.storeEntitiesProcess = storeEntitiesProcess;
    }

}
