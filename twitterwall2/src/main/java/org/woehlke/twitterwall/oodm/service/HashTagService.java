package org.woehlke.twitterwall.oodm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.twitterwall.oodm.model.HashTag;
import org.woehlke.twitterwall.oodm.model.transients.HashTagCounted;
import org.woehlke.twitterwall.oodm.service.common.DomainObjectEntityService;
import org.woehlke.twitterwall.oodm.service.common.DomainService;


/**
 * Created by tw on 12.06.17.
 */
public interface HashTagService extends DomainService<HashTag>,DomainObjectEntityService<HashTag> {

    HashTag findByText(String text);

    Page<HashTagCounted> getHashTagsTweets(Pageable pageRequestTweets);

    Page<HashTagCounted> getHashTagsUsers(Pageable pageRequestUsers);
}
