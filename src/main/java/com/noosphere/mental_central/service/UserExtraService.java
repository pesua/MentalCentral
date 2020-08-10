package com.noosphere.mental_central.service;

import com.noosphere.mental_central.domain.UserExtra;
import com.noosphere.mental_central.repository.UserExtraRepository;
import com.noosphere.mental_central.repository.UserRepository;
import com.noosphere.mental_central.repository.search.UserExtraSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link UserExtra}.
 */
@Service
@Transactional
public class UserExtraService {

    private final Logger log = LoggerFactory.getLogger(UserExtraService.class);

    private final UserExtraRepository userExtraRepository;

    private final UserExtraSearchRepository userExtraSearchRepository;

    private final UserRepository userRepository;

    public UserExtraService(UserExtraRepository userExtraRepository, UserExtraSearchRepository userExtraSearchRepository, UserRepository userRepository) {
        this.userExtraRepository = userExtraRepository;
        this.userExtraSearchRepository = userExtraSearchRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a userExtra.
     *
     * @param userExtra the entity to save.
     * @return the persisted entity.
     */
    public UserExtra save(UserExtra userExtra) {
        log.debug("Request to save UserExtra : {}", userExtra);
        Long userId = userExtra.getUser().getId();
        userRepository.findById(userId).ifPresent(userExtra::user);
        UserExtra result = userExtraRepository.save(userExtra);
        userExtraSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the userExtras.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserExtra> findAll() {
        log.debug("Request to get all UserExtras");
        return userExtraRepository.findAll();
    }


    /**
     * Get one userExtra by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserExtra> findOne(Long id) {
        log.debug("Request to get UserExtra : {}", id);
        return userExtraRepository.findById(id);
    }

    /**
     * Delete the userExtra by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserExtra : {}", id);
        userExtraRepository.deleteById(id);
        userExtraSearchRepository.deleteById(id);
    }

    /**
     * Search for the userExtra corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserExtra> search(String query) {
        log.debug("Request to search UserExtras for query {}", query);
        return StreamSupport
            .stream(userExtraSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
