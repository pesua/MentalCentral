package com.noosphere.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.noosphere.domain.Visit;
import com.noosphere.domain.*; // for static metamodels
import com.noosphere.repository.VisitRepository;
import com.noosphere.service.dto.VisitCriteria;

/**
 * Service for executing complex queries for {@link Visit} entities in the database.
 * The main input is a {@link VisitCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Visit} or a {@link Page} of {@link Visit} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VisitQueryService extends QueryService<Visit> {

    private final Logger log = LoggerFactory.getLogger(VisitQueryService.class);

    private final VisitRepository visitRepository;

    public VisitQueryService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    /**
     * Return a {@link List} of {@link Visit} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Visit> findByCriteria(VisitCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Visit> specification = createSpecification(criteria);
        return visitRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Visit} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Visit> findByCriteria(VisitCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Visit> specification = createSpecification(criteria);
        return visitRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VisitCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Visit> specification = createSpecification(criteria);
        return visitRepository.count(specification);
    }

    /**
     * Function to convert {@link VisitCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Visit> createSpecification(VisitCriteria criteria) {
        Specification<Visit> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Visit_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Visit_.type));
            }
            if (criteria.getTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTime(), Visit_.time));
            }
            if (criteria.getTherapy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTherapy(), Visit_.therapy));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Visit_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getPatientId() != null) {
                specification = specification.and(buildSpecification(criteria.getPatientId(),
                    root -> root.join(Visit_.patient, JoinType.LEFT).get(Patient_.id)));
            }
        }
        return specification;
    }
}
