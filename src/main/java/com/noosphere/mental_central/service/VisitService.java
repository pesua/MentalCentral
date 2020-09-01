package com.noosphere.mental_central.service;

import com.noosphere.mental_central.domain.Patient;
import com.noosphere.mental_central.domain.Visit;
import com.noosphere.mental_central.repository.VisitRepository;
import com.noosphere.mental_central.repository.search.VisitSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Visit}.
 */
@Service
@Transactional
public class VisitService {

    private final Logger log = LoggerFactory.getLogger(VisitService.class);

    private final VisitRepository visitRepository;

    private final VisitSearchRepository visitSearchRepository;

    public VisitService(VisitRepository visitRepository, VisitSearchRepository visitSearchRepository) {
        this.visitRepository = visitRepository;
        this.visitSearchRepository = visitSearchRepository;
    }

    /**
     * Save a visit.
     *
     * @param visit the entity to save.
     * @return the persisted entity.
     */
    public Visit save(Visit visit) {
        log.debug("Request to save Visit : {}", visit);
        Visit result = visitRepository.save(visit);
        visitSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the visits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Visit> findAll(Pageable pageable) {
        log.debug("Request to get all Visits");
        return visitRepository.findAll(pageable);
    }
    @Transactional(readOnly = true)
    public Page<Visit> findAllDocVisits(Pageable pageable) {
        log.debug("Request to get all doctor’s Visits");
        return visitRepository.findByUserIsCurrentUser(pageable);
    }
    @Transactional(readOnly = true)
    public Page<Visit> findAllOrderedByTime(Pageable pageable) {
        log.debug("Request to get all Visits ordered by date");
        return visitRepository.findAllByOrderByTimeDesc(pageable);
    }

    /**
     * Get one visit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Visit> findOne(Long id) {
        log.debug("Request to get Visit : {}", id);
        return visitRepository.findById(id);
    }

    /**
     * Delete the visit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Visit : {}", id);
        visitRepository.deleteById(id);
        visitSearchRepository.deleteById(id);
    }


    /**
     * Search for the visit corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Visit> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Visits for query {}", query);
        return visitSearchRepository.search(queryStringQuery(query), pageable);    }
}
