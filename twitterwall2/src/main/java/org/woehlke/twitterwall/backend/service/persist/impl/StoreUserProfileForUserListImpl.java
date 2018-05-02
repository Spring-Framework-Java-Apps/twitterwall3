package org.woehlke.twitterwall.backend.service.persist.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Component;
import org.woehlke.twitterwall.oodm.model.User;
import org.woehlke.twitterwall.oodm.model.Task;
import org.woehlke.twitterwall.oodm.model.Mention;
import org.woehlke.twitterwall.backend.service.persist.StoreUserProcess;
import org.woehlke.twitterwall.backend.service.persist.StoreUserProfileForScreenName;
import org.woehlke.twitterwall.backend.service.persist.StoreUserProfileForUserList;
import org.woehlke.twitterwall.backend.service.transform.UserTransformService;

/**
 * Created by tw on 09.07.17.
 */

@Component
//@Service
//@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
public class StoreUserProfileForUserListImpl implements StoreUserProfileForUserList {

    @Override
    public User storeUserProfileForUserList(TwitterProfile twitterProfile, Task task) {
        String msg = "storeUserProfileForUserList: idTwitter="+twitterProfile.getId()+" : "+task.getUniqueId()+" : ";
        try {
            User user = userTransformService.transform(twitterProfile, task);
            user = storeUserProcess.storeUserProcess(user, task);
            for (Mention mention : user.getEntities().getMentions()) {
                String screenName = mention.getScreenName();
                if (screenName != null) {
                    User userFromMention = storeUserProfileForScreenName.storeUserProfileForScreenName(screenName, task);
                    log.debug(msg + "storeUserProfile.storeUserProfileForScreenName(" + screenName + ") = " + userFromMention.getUniqueId()+" : "+task.getUniqueId());
                }
            }
            return user;
        } catch (Exception e){
            log.error(msg+e.getMessage());
            return null;
        }
    }

    private static final Logger log = LoggerFactory.getLogger(StoreUserProfileForUserListImpl.class);

    private final UserTransformService userTransformService;

    private final StoreUserProcess storeUserProcess;

    private final StoreUserProfileForScreenName storeUserProfileForScreenName;

    @Autowired
    public StoreUserProfileForUserListImpl(UserTransformService userTransformService, StoreUserProcess storeUserProcess, StoreUserProfileForScreenName storeUserProfileForScreenName) {
        this.userTransformService = userTransformService;
        this.storeUserProcess = storeUserProcess;
        this.storeUserProfileForScreenName = storeUserProfileForScreenName;
    }

}
