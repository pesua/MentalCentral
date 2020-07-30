package com.noosphere.mental_central.web.rest;

import com.noosphere.mental_central.domain.Visit;
import com.noosphere.mental_central.security.AuthoritiesConstants;
import com.noosphere.mental_central.security.SecurityUtils;
import com.noosphere.mental_central.service.VisitService;
import com.noosphere.mental_central.web.rest.errors.BadRequestAlertException;
import com.noosphere.mental_central.service.dto.VisitCriteria;
import com.noosphere.mental_central.service.VisitQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.noosphere.mental_central.domain.Visit}.
 */
@RestController
@RequestMapping("/api")
public class VisitResource {

    private final Logger log = LoggerFactory.getLogger(VisitResource.class);

    private static final String ENTITY_NAME = "visit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VisitService visitService;

    private final VisitQueryService visitQueryService;

    public VisitResource(VisitService visitService, VisitQueryService visitQueryService) {
        this.visitService = visitService;
        this.visitQueryService = visitQueryService;
    }

    /**
     * {@code POST  /visits} : Create a new visit.
     *
     * @param visit the visit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new visit, or with status {@code 400 (Bad Request)} if the visit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/visits")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTION', 'ROLE_DOCTOR')")
    public ResponseEntity<Visit> createVisit(@Valid @RequestBody Visit visit) throws URISyntaxException {
        log.debug("REST request to save Visit : {}", visit);
        if (visit.getId() != null) {
            throw new BadRequestAlertException("A new visit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Visit result = visitService.save(visit);
        return ResponseEntity.created(new URI("/api/visits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /visits} : Updates an existing visit.
     *
     * @param visit the visit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visit,
     * or with status {@code 400 (Bad Request)} if the visit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the visit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/visits")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTION', 'ROLE_DOCTOR')")
    public ResponseEntity<Visit> updateVisit(@Valid @RequestBody Visit visit) throws URISyntaxException {
        log.debug("REST request to update Visit : {}", visit);
        if (visit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Visit result = visitService.save(visit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visit.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /visits} : get all the visits.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of visits in body.
     */
    @GetMapping("/visits")
    public ResponseEntity<List<Visit>> getAllVisits(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Visits");
        Page<Visit> page;
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.DOCTOR)) {
            page = visitService.findAllDocVisits(pageable);
        }
        else if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.RECEPTION)){
            page = visitService.findAllOrderedByTime(pageable);
        }
        else{
            page = null;
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromUriString("/api/visit"), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET  /visits/count} : count all the visits.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/visits/count")
    public ResponseEntity<Long> countVisits(VisitCriteria criteria) {
        log.debug("REST request to count Visits by criteria: {}", criteria);
        return ResponseEntity.ok().body(visitQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /visits/:id} : get the "id" visit.
     *
     * @param id the id of the visit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the visit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/visits/{id}")
    public ResponseEntity<Visit> getVisit(@PathVariable Long id) {
        log.debug("REST request to get Visit : {}", id);
        Optional<Visit> visit = visitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(visit);
    }

    /**
     * {@code DELETE  /visits/:id} : delete the "id" visit.
     *
     * @param id the id of the visit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/visits/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTION')")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long id) {
        log.debug("REST request to delete Visit : {}", id);
        visitService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
