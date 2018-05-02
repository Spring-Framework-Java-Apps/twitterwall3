package org.woehlke.twitterwall.backend.service.transform;

import org.woehlke.twitterwall.backend.service.transform.common.TransformService;

/**
 * Created by tw on 28.06.17.
 */
public interface TweetTransformService extends TransformService<org.woehlke.twitterwall.oodm.model.Tweet, org.springframework.social.twitter.api.Tweet> {
}
