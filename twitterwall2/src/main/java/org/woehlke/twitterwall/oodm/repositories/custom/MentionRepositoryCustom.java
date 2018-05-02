package org.woehlke.twitterwall.oodm.repositories.custom;

import org.woehlke.twitterwall.oodm.model.Mention;
import org.woehlke.twitterwall.oodm.repositories.common.DomainObjectEntityRepository;

public interface MentionRepositoryCustom extends DomainObjectEntityRepository<Mention>  {

    Mention findByUniqueId(Mention domainObject);
}
