package com.noosphere.mental_central.service;

import com.noosphere.mental_central.domain.Visit;
import com.noosphere.mental_central.repository.VisitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Visit}.
 */
@Service
@Transactional
public class VisitService {

    private final Logger log = LoggerFactory.getLogger(VisitService.class);

    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    /**
     * Save a visit.
     *
     * @param visit the entity to save.
     * @return the persisted entity.
     */
    public Visit save(Visit visit) {
        log.debug("Request to save Visit : {}", visit);
        return visitRepository.save(visit);
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
    }
}
