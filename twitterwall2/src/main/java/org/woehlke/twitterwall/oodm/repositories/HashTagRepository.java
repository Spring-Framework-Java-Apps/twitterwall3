package org.woehlke.twitterwall.oodm.repositories;


import org.springframework.stereotype.Repository;
import org.woehlke.twitterwall.oodm.model.HashTag;
import org.woehlke.twitterwall.oodm.repositories.common.DomainRepository;
import org.woehlke.twitterwall.oodm.repositories.custom.HashTagRepositoryCustom;

/**
 * Created by tw on 15.07.17.
 */
@Repository
public interface HashTagRepository extends DomainRepository<HashTag>,HashTagRepositoryCustom {

    HashTag findByText(String text);
}
