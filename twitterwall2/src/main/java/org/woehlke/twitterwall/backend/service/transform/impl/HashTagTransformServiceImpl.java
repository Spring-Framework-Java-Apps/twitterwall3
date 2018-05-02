package org.woehlke.twitterwall.backend.service.transform.impl;

import org.springframework.social.twitter.api.HashTagEntity;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Component;
import org.woehlke.twitterwall.oodm.model.Task;
import org.woehlke.twitterwall.oodm.model.entities.EntitiesFilter;
import org.woehlke.twitterwall.oodm.model.HashTag;
import org.woehlke.twitterwall.backend.service.transform.HashTagTransformService;

import java.util.Set;


/**
 * Created by tw on 28.06.17.
 */
@Component
public class HashTagTransformServiceImpl extends EntitiesFilter implements HashTagTransformService {

    @Override
    public HashTag transform(HashTagEntity hashTag,Task task) {
        String text = hashTag.getText();
        HashTag myHashTagEntity = new HashTag(task, null, text);
        return myHashTagEntity;
    }

    @Override
    public Set<HashTag> getHashTagsFor(TwitterProfile userSource,Task tasks) {
        String description = userSource.getDescription();
        Set<HashTag> hashTagsTarget = getHashTagsForDescription(description,tasks);
        return hashTagsTarget;
    }
}
