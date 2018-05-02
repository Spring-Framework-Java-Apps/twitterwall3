package org.woehlke.twitterwall.oodm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.twitterwall.oodm.model.Task;
import org.woehlke.twitterwall.oodm.model.Mention;
import org.woehlke.twitterwall.oodm.repositories.MentionRepository;
import org.woehlke.twitterwall.oodm.repositories.TaskRepository;
import org.woehlke.twitterwall.oodm.service.MentionService;

import java.util.List;


/**
 * Created by tw on 12.06.17.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class MentionServiceImpl extends DomainServiceWithTaskImpl<Mention> implements MentionService {

    private static final Logger log = LoggerFactory.getLogger(MentionServiceImpl.class);

    private final MentionRepository mentionRepository;

    @Autowired
    public MentionServiceImpl(MentionRepository mentionRepository, TaskRepository taskRepository) {
        super(mentionRepository, taskRepository);
        this.mentionRepository = mentionRepository;
    }

    @Override
    public Mention findByIdTwitter(long idTwitter) {
        if (idTwitter < 0L) {
            return null;
        }
        List<Mention> resultList = mentionRepository.findByIdTwitter(idTwitter);
        for (Mention result : resultList) {
            if (result.getIdTwitter() > 0L) {
                return result;
            }
        }
        return null;
    }

    @Override
    public Mention findByScreenName(String screenName) {
        String screenNameUnique = screenName.toLowerCase();
        return mentionRepository.findByScreenNameUnique(screenNameUnique);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Mention createProxyMention(Mention mention, Task task) {
        Mention foundPers = mentionRepository.findByScreenNameUnique(mention.getScreenNameUnique());
        if (foundPers != null) {
            return foundPers;
        } else {
            mention.setIdTwitter(Mention.ID_TWITTER_UNDEFINED);
            mention.setCreatedBy(task);
            mention = mentionRepository.save(mention);
            return mention;
        }
    }

    @Override
    public Page<Mention> getAllWithoutUser(Pageable pageRequest) {
        return mentionRepository.findAllWithoutUser(pageRequest);
    }

    @Override
    public Mention findByScreenNameAndIdTwitter(String screenName, Long idTwitter) {
        String screenNameUnique = screenName.toLowerCase();
        return mentionRepository.findByScreenNameUniqueAndIdTwitter(screenNameUnique, idTwitter);
    }

    @Override
    public Page<Mention> findByUserId(long idOfUser, Pageable pageRequest) {
        return mentionRepository.findByUserId(idOfUser,pageRequest);
    }

    @Override
    public Page<Mention> findAllByScreenName(String screenName, Pageable pageRequest) {
        String screenNameUnique = screenName.toLowerCase();
        return mentionRepository.findAllByScreenNameUnique(screenNameUnique,pageRequest);
    }

    @Override
    public Page<Mention> findByIdTwitterOfUser(long idOfUser, Pageable pageRequest) {
        return mentionRepository.findByIdTwitterOfUser(idOfUser,pageRequest);
    }

    @Override
    public Mention findByUniqueId(Mention domainExampleObject) {
        return mentionRepository.findByUniqueId(domainExampleObject);
    }
}
