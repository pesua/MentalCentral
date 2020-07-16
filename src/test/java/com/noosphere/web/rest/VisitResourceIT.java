package com.noosphere.web.rest;

import com.noosphere.MentalCentralApp;
import com.noosphere.domain.Visit;
import com.noosphere.domain.Doctor;
import com.noosphere.domain.Patient;
import com.noosphere.repository.VisitRepository;
import com.noosphere.service.VisitService;
import com.noosphere.service.dto.VisitDTO;
import com.noosphere.service.mapper.VisitMapper;

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

    private static final String DEFAULT_TERAPHY = "AAAAAAAAAA";
    private static final String UPDATED_TERAPHY = "BBBBBBBBBB";

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private VisitMapper visitMapper;

    @Autowired
    private VisitService visitService;

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
            .teraphy(DEFAULT_TERAPHY);
        // Add required entity
        Doctor doctor;
        if (TestUtil.findAll(em, Doctor.class).isEmpty()) {
            doctor = DoctorResourceIT.createEntity(em);
            em.persist(doctor);
            em.flush();
        } else {
            doctor = TestUtil.findAll(em, Doctor.class).get(0);
        }
        visit.setDoctor(doctor);
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
            .teraphy(UPDATED_TERAPHY);
        // Add required entity
        Doctor doctor;
        if (TestUtil.findAll(em, Doctor.class).isEmpty()) {
            doctor = DoctorResourceIT.createUpdatedEntity(em);
            em.persist(doctor);
            em.flush();
        } else {
            doctor = TestUtil.findAll(em, Doctor.class).get(0);
        }
        visit.setDoctor(doctor);
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
        VisitDTO visitDTO = visitMapper.toDto(visit);
        restVisitMockMvc.perform(post("/api/visits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visitDTO)))
            .andExpect(status().isCreated());

        // Validate the Visit in the database
        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeCreate + 1);
        Visit testVisit = visitList.get(visitList.size() - 1);
        assertThat(testVisit.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testVisit.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testVisit.getTeraphy()).isEqualTo(DEFAULT_TERAPHY);
    }

    @Test
    @Transactional
    public void createVisitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = visitRepository.findAll().size();

        // Create the Visit with an existing ID
        visit.setId(1L);
        VisitDTO visitDTO = visitMapper.toDto(visit);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisitMockMvc.perform(post("/api/visits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visitDTO)))
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
        VisitDTO visitDTO = visitMapper.toDto(visit);


        restVisitMockMvc.perform(post("/api/visits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visitDTO)))
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
        VisitDTO visitDTO = visitMapper.toDto(visit);


        restVisitMockMvc.perform(post("/api/visits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visitDTO)))
            .andExpect(status().isBadRequest());

        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTeraphyIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitRepository.findAll().size();
        // set the field null
        visit.setTeraphy(null);

        // Create the Visit, which fails.
        VisitDTO visitDTO = visitMapper.toDto(visit);


        restVisitMockMvc.perform(post("/api/visits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visitDTO)))
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
            .andExpect(jsonPath("$.[*].teraphy").value(hasItem(DEFAULT_TERAPHY)));
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
            .andExpect(jsonPath("$.teraphy").value(DEFAULT_TERAPHY));
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
        visitRepository.saveAndFlush(visit);

        int databaseSizeBeforeUpdate = visitRepository.findAll().size();

        // Update the visit
        Visit updatedVisit = visitRepository.findById(visit.getId()).get();
        // Disconnect from session so that the updates on updatedVisit are not directly saved in db
        em.detach(updatedVisit);
        updatedVisit
            .type(UPDATED_TYPE)
            .time(UPDATED_TIME)
            .teraphy(UPDATED_TERAPHY);
        VisitDTO visitDTO = visitMapper.toDto(updatedVisit);

        restVisitMockMvc.perform(put("/api/visits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visitDTO)))
            .andExpect(status().isOk());

        // Validate the Visit in the database
        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeUpdate);
        Visit testVisit = visitList.get(visitList.size() - 1);
        assertThat(testVisit.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVisit.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testVisit.getTeraphy()).isEqualTo(UPDATED_TERAPHY);
    }

    @Test
    @Transactional
    public void updateNonExistingVisit() throws Exception {
        int databaseSizeBeforeUpdate = visitRepository.findAll().size();

        // Create the Visit
        VisitDTO visitDTO = visitMapper.toDto(visit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisitMockMvc.perform(put("/api/visits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(visitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Visit in the database
        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVisit() throws Exception {
        // Initialize the database
        visitRepository.saveAndFlush(visit);

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
