package org.woehlke.twitterwall.oodm.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.twitterwall.oodm.model.Task;
import org.woehlke.twitterwall.oodm.repositories.common.DomainObjectMinimalRepository;
import org.woehlke.twitterwall.oodm.repositories.custom.TaskRepositoryCustom;

/**
 * Created by tw on 15.07.17.
 */
@Repository
public interface TaskRepository extends
    DomainObjectMinimalRepository<Task>,
    TaskRepositoryCustom,
    PagingAndSortingRepository<Task,Long> {

}
