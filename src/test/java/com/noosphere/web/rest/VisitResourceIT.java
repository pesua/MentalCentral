package com.noosphere.web.rest;

import com.noosphere.MentalCentralApp;
import com.noosphere.domain.Visit;
import com.noosphere.domain.User;
import com.noosphere.domain.Patient;
import com.noosphere.repository.VisitRepository;
import com.noosphere.service.VisitService;
import com.noosphere.service.dto.VisitCriteria;
import com.noosphere.service.VisitQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.noosphere.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link VisitResource} REST controller.
 */
@SpringBootTest(classes = MentalCentralApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class VisitResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_THERAPY = "AAAAAAAAAA";
    private static final String UPDATED_THERAPY = "BBBBBBBBBB";

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private VisitService visitService;

    @Autowired
    private VisitQueryService visitQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVisitMockMvc;

    private Visit visit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visit createEntity(EntityManager em) {
        Visit visit = new Visit()
            .type(DEFAULT_TYPE)
            .time(DEFAULT_TIME)
            .therapy(DEFAULT_THERAPY);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        visit.setUser(user);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        visit.setPatient(patient);
        return visit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visit createUpdatedEntity(EntityManager em) {
        Visit visit = new Visit()
            .type(UPDATED_TYPE)
            .time(UPDATED_TIME)
            .therapy(UPDATED_THERAPY);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        visit.setUser(user);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        visit.setPatient(patient);
        return visit;
    }

    @BeforeEach
    public void initTest() {
        visit = createEntity(em);
    }

    @Test
    @Transactional
    public void createVisit() throws Exception {
        int databaseSizeBeforeCreate = visitRepository.findAll().size();
        // Create the Visit
        restVisitMockMvc.perform(post("/api/visits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visit)))
            .andExpect(status().isCreated());

        // Validate the Visit in the database
        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeCreate + 1);
        Visit testVisit = visitList.get(visitList.size() - 1);
        assertThat(testVisit.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testVisit.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testVisit.getTherapy()).isEqualTo(DEFAULT_THERAPY);
    }

    @Test
    @Transactional
    public void createVisitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = visitRepository.findAll().size();

        // Create the Visit with an existing ID
        visit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisitMockMvc.perform(post("/api/visits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visit)))
            .andExpect(status().isBadRequest());

        // Validate the Visit in the database
        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitRepository.findAll().size();
        // set the field null
        visit.setType(null);

        // Create the Visit, which fails.


        restVisitMockMvc.perform(post("/api/visits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visit)))
            .andExpect(status().isBadRequest());

        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitRepository.findAll().size();
        // set the field null
        visit.setTime(null);

        // Create the Visit, which fails.


        restVisitMockMvc.perform(post("/api/visits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visit)))
            .andExpect(status().isBadRequest());

        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTherapyIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitRepository.findAll().size();
        // set the field null
        visit.setTherapy(null);

        // Create the Visit, which fails.


        restVisitMockMvc.perform(post("/api/visits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visit)))
            .andExpect(status().isBadRequest());

        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVisits() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList
        restVisitMockMvc.perform(get("/api/visits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visit.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))))
            .andExpect(jsonPath("$.[*].therapy").value(hasItem(DEFAULT_THERAPY)));
    }
    
    @Test
    @Transactional
    public void getVisit() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get the visit
        restVisitMockMvc.perform(get("/api/visits/{id}", visit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(visit.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.time").value(sameInstant(DEFAULT_TIME)))
            .andExpect(jsonPath("$.therapy").value(DEFAULT_THERAPY));
    }


    @Test
    @Transactional
    public void getVisitsByIdFiltering() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        Long id = visit.getId();

        defaultVisitShouldBeFound("id.equals=" + id);
        defaultVisitShouldNotBeFound("id.notEquals=" + id);

        defaultVisitShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVisitShouldNotBeFound("id.greaterThan=" + id);

        defaultVisitShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVisitShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllVisitsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where type equals to DEFAULT_TYPE
        defaultVisitShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the visitList where type equals to UPDATED_TYPE
        defaultVisitShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllVisitsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where type not equals to DEFAULT_TYPE
        defaultVisitShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the visitList where type not equals to UPDATED_TYPE
        defaultVisitShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllVisitsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultVisitShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the visitList where type equals to UPDATED_TYPE
        defaultVisitShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllVisitsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where type is not null
        defaultVisitShouldBeFound("type.specified=true");

        // Get all the visitList where type is null
        defaultVisitShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllVisitsByTypeContainsSomething() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where type contains DEFAULT_TYPE
        defaultVisitShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the visitList where type contains UPDATED_TYPE
        defaultVisitShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllVisitsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where type does not contain DEFAULT_TYPE
        defaultVisitShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the visitList where type does not contain UPDATED_TYPE
        defaultVisitShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllVisitsByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where time equals to DEFAULT_TIME
        defaultVisitShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the visitList where time equals to UPDATED_TIME
        defaultVisitShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllVisitsByTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where time not equals to DEFAULT_TIME
        defaultVisitShouldNotBeFound("time.notEquals=" + DEFAULT_TIME);

        // Get all the visitList where time not equals to UPDATED_TIME
        defaultVisitShouldBeFound("time.notEquals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllVisitsByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where time in DEFAULT_TIME or UPDATED_TIME
        defaultVisitShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the visitList where time equals to UPDATED_TIME
        defaultVisitShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllVisitsByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where time is not null
        defaultVisitShouldBeFound("time.specified=true");

        // Get all the visitList where time is null
        defaultVisitShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    public void getAllVisitsByTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where time is greater than or equal to DEFAULT_TIME
        defaultVisitShouldBeFound("time.greaterThanOrEqual=" + DEFAULT_TIME);

        // Get all the visitList where time is greater than or equal to UPDATED_TIME
        defaultVisitShouldNotBeFound("time.greaterThanOrEqual=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllVisitsByTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where time is less than or equal to DEFAULT_TIME
        defaultVisitShouldBeFound("time.lessThanOrEqual=" + DEFAULT_TIME);

        // Get all the visitList where time is less than or equal to SMALLER_TIME
        defaultVisitShouldNotBeFound("time.lessThanOrEqual=" + SMALLER_TIME);
    }

    @Test
    @Transactional
    public void getAllVisitsByTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where time is less than DEFAULT_TIME
        defaultVisitShouldNotBeFound("time.lessThan=" + DEFAULT_TIME);

        // Get all the visitList where time is less than UPDATED_TIME
        defaultVisitShouldBeFound("time.lessThan=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllVisitsByTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where time is greater than DEFAULT_TIME
        defaultVisitShouldNotBeFound("time.greaterThan=" + DEFAULT_TIME);

        // Get all the visitList where time is greater than SMALLER_TIME
        defaultVisitShouldBeFound("time.greaterThan=" + SMALLER_TIME);
    }


    @Test
    @Transactional
    public void getAllVisitsByTherapyIsEqualToSomething() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where therapy equals to DEFAULT_THERAPY
        defaultVisitShouldBeFound("therapy.equals=" + DEFAULT_THERAPY);

        // Get all the visitList where therapy equals to UPDATED_THERAPY
        defaultVisitShouldNotBeFound("therapy.equals=" + UPDATED_THERAPY);
    }

    @Test
    @Transactional
    public void getAllVisitsByTherapyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where therapy not equals to DEFAULT_THERAPY
        defaultVisitShouldNotBeFound("therapy.notEquals=" + DEFAULT_THERAPY);

        // Get all the visitList where therapy not equals to UPDATED_THERAPY
        defaultVisitShouldBeFound("therapy.notEquals=" + UPDATED_THERAPY);
    }

    @Test
    @Transactional
    public void getAllVisitsByTherapyIsInShouldWork() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where therapy in DEFAULT_THERAPY or UPDATED_THERAPY
        defaultVisitShouldBeFound("therapy.in=" + DEFAULT_THERAPY + "," + UPDATED_THERAPY);

        // Get all the visitList where therapy equals to UPDATED_THERAPY
        defaultVisitShouldNotBeFound("therapy.in=" + UPDATED_THERAPY);
    }

    @Test
    @Transactional
    public void getAllVisitsByTherapyIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where therapy is not null
        defaultVisitShouldBeFound("therapy.specified=true");

        // Get all the visitList where therapy is null
        defaultVisitShouldNotBeFound("therapy.specified=false");
    }
                @Test
    @Transactional
    public void getAllVisitsByTherapyContainsSomething() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where therapy contains DEFAULT_THERAPY
        defaultVisitShouldBeFound("therapy.contains=" + DEFAULT_THERAPY);

        // Get all the visitList where therapy contains UPDATED_THERAPY
        defaultVisitShouldNotBeFound("therapy.contains=" + UPDATED_THERAPY);
    }

    @Test
    @Transactional
    public void getAllVisitsByTherapyNotContainsSomething() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

        // Get all the visitList where therapy does not contain DEFAULT_THERAPY
        defaultVisitShouldNotBeFound("therapy.doesNotContain=" + DEFAULT_THERAPY);

        // Get all the visitList where therapy does not contain UPDATED_THERAPY
        defaultVisitShouldBeFound("therapy.doesNotContain=" + UPDATED_THERAPY);
    }


    @Test
    @Transactional
    public void getAllVisitsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = visit.getUser();
        visitRepository.saveAndFlush(visit);
        Long userId = user.getId();

        // Get all the visitList where user equals to userId
        defaultVisitShouldBeFound("userId.equals=" + userId);

        // Get all the visitList where user equals to userId + 1
        defaultVisitShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllVisitsByPatientIsEqualToSomething() throws Exception {
        // Get already existing entity
        Patient patient = visit.getPatient();
        visitRepository.saveAndFlush(visit);
        Long patientId = patient.getId();

        // Get all the visitList where patient equals to patientId
        defaultVisitShouldBeFound("patientId.equals=" + patientId);

        // Get all the visitList where patient equals to patientId + 1
        defaultVisitShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVisitShouldBeFound(String filter) throws Exception {
        restVisitMockMvc.perform(get("/api/visits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visit.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))))
            .andExpect(jsonPath("$.[*].therapy").value(hasItem(DEFAULT_THERAPY)));

        // Check, that the count call also returns 1
        restVisitMockMvc.perform(get("/api/visits/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVisitShouldNotBeFound(String filter) throws Exception {
        restVisitMockMvc.perform(get("/api/visits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVisitMockMvc.perform(get("/api/visits/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingVisit() throws Exception {
        // Get the visit
        restVisitMockMvc.perform(get("/api/visits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVisit() throws Exception {
        // Initialize the database
        visitService.save(visit);

        int databaseSizeBeforeUpdate = visitRepository.findAll().size();

        // Update the visit
        Visit updatedVisit = visitRepository.findById(visit.getId()).get();
        // Disconnect from session so that the updates on updatedVisit are not directly saved in db
        em.detach(updatedVisit);
        updatedVisit
            .type(UPDATED_TYPE)
            .time(UPDATED_TIME)
            .therapy(UPDATED_THERAPY);

        restVisitMockMvc.perform(put("/api/visits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVisit)))
            .andExpect(status().isOk());

        // Validate the Visit in the database
        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeUpdate);
        Visit testVisit = visitList.get(visitList.size() - 1);
        assertThat(testVisit.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVisit.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testVisit.getTherapy()).isEqualTo(UPDATED_THERAPY);
    }

    @Test
    @Transactional
    public void updateNonExistingVisit() throws Exception {
        int databaseSizeBeforeUpdate = visitRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisitMockMvc.perform(put("/api/visits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visit)))
            .andExpect(status().isBadRequest());

        // Validate the Visit in the database
        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVisit() throws Exception {
        // Initialize the database
        visitService.save(visit);

        int databaseSizeBeforeDelete = visitRepository.findAll().size();

        // Delete the visit
        restVisitMockMvc.perform(delete("/api/visits/{id}", visit.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
