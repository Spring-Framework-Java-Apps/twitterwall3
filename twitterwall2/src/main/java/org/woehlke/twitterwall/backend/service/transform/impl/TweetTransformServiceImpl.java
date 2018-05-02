package org.woehlke.twitterwall.backend.service.transform.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.*;
import org.springframework.stereotype.Component;
import org.woehlke.twitterwall.oodm.model.*;
import org.woehlke.twitterwall.oodm.model.entities.Entities;
import org.woehlke.twitterwall.backend.service.transform.EntitiesTransformService;
import org.woehlke.twitterwall.backend.service.transform.TweetTransformService;
import org.woehlke.twitterwall.backend.service.transform.UserTransformService;

import java.util.Date;

/**
 * Created by tw on 28.06.17.
 */
@Component
public class TweetTransformServiceImpl implements TweetTransformService {

    private final UserTransformService userTransformService;

    private final EntitiesTransformService entitiesTransformService;

    @Autowired
    public TweetTransformServiceImpl(UserTransformService userTransformService, EntitiesTransformService entitiesTransformService) {
        this.userTransformService = userTransformService;
        this.entitiesTransformService = entitiesTransformService;
    }

    @Override
    public org.woehlke.twitterwall.oodm.model.Tweet transform(org.springframework.social.twitter.api.Tweet tweetSource, Task task) {
        if (tweetSource == null) { return null; } else {
            long idTwitter = tweetSource.getId();
            String idStr = tweetSource.getIdStr();
            String text = tweetSource.getText();
            Date createdAt = tweetSource.getCreatedAt();
            String fromUser = tweetSource.getFromUser();
            String profileImageUrl = tweetSource.getProfileImageUrl();
            Long toUserId = tweetSource.getToUserId();
            long fromUserId = tweetSource.getFromUserId();
            String languageCode = tweetSource.getLanguageCode();
            String source = tweetSource.getSource();
            org.woehlke.twitterwall.oodm.model.Tweet tweetTarget = new org.woehlke.twitterwall.oodm.model.Tweet(task,null,idTwitter, idStr, text, createdAt, fromUser, profileImageUrl, toUserId, fromUserId, languageCode, source);
            tweetTarget.setFavoriteCount(tweetSource.getFavoriteCount());
            tweetTarget.setFavorited(tweetSource.isFavorited());
            tweetTarget.setInReplyToScreenName(tweetSource.getInReplyToScreenName());
            tweetTarget.setInReplyToUserId(tweetSource.getInReplyToUserId());
            tweetTarget.setLanguageCode(tweetSource.getLanguageCode());
            tweetTarget.setRetweetCount(tweetSource.getRetweetCount());
            tweetTarget.setRetweeted(tweetSource.isRetweeted());
            tweetTarget.setSource(tweetSource.getSource());
            tweetTarget.setFromUser(tweetSource.getFromUser());
            tweetTarget.setFavorited(tweetSource.isFavorited());
            tweetTarget.setInReplyToStatusId(tweetSource.getInReplyToStatusId());
            /* retweetedStatus */
            org.woehlke.twitterwall.oodm.model.Tweet retweetedStatus = transform(tweetSource.getRetweetedStatus(),task);
            tweetTarget.setRetweetedStatus(retweetedStatus);
            /* Entities */
            Entities entitiesTarget = entitiesTransformService.transformEntitiesForTweet(tweetSource,task);
            tweetTarget.setEntities(entitiesTarget);
            /* transformEntitiesForTweet userTarget */
            TwitterProfile userSource = tweetSource.getUser();
            User userTarget = userTransformService.transform(userSource,task);
            tweetTarget.setUser(userTarget);
            /* ExtraData */
            tweetTarget.setExtraData(tweetSource.getExtraData());
            return tweetTarget;
        }
    }
}
