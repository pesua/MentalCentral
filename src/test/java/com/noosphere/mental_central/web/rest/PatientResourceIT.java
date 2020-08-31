package com.noosphere.mental_central.web.rest;

import com.noosphere.mental_central.MentalCentralApp;
import com.noosphere.mental_central.domain.Patient;
import com.noosphere.mental_central.domain.Visit;
import com.noosphere.mental_central.repository.PatientRepository;
import com.noosphere.mental_central.repository.search.PatientSearchRepository;
import com.noosphere.mental_central.service.PatientService;
import com.noosphere.mental_central.service.dto.PatientCriteria;
import com.noosphere.mental_central.service.PatientQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PatientResource} REST controller.
 */
@SpringBootTest(classes = MentalCentralApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PatientResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTH_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "+380493900703";
    private static final String UPDATED_PHONE_NUMBER = "+380416227041";

    private static final String DEFAULT_DIAGNOSIS = "a";
    private static final String UPDATED_DIAGNOSIS = "b";
    private static final String SMALLER_DIAGNOSIS = "a";

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    /**
     * This repository is mocked in the com.noosphere.mental_central.repository.search test package.
     *
     * @see com.noosphere.mental_central.repository.search.PatientSearchRepositoryMockConfiguration
     */
    @Autowired
    private PatientSearchRepository mockPatientSearchRepository;

    @Autowired
    private PatientQueryService patientQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientMockMvc;

    private Patient patient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .fullName(DEFAULT_FULL_NAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .address(DEFAULT_ADDRESS)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .diagnosis(DEFAULT_DIAGNOSIS);
        return patient;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createUpdatedEntity(EntityManager em) {
        Patient patient = new Patient()
            .fullName(UPDATED_FULL_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .diagnosis(UPDATED_DIAGNOSIS);
        return patient;
    }

    @BeforeEach
    public void initTest() {
        patient = createEntity(em);
    }

    @Test
    @Transactional
    public void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();
        // Create the Patient
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testPatient.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testPatient.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPatient.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPatient.getDiagnosis()).isEqualTo(DEFAULT_DIAGNOSIS);

        // Validate the Patient in Elasticsearch
        verify(mockPatientSearchRepository, times(1)).save(testPatient);
    }

    @Test
    @Transactional
    public void createPatientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient with an existing ID
        patient.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate);

        // Validate the Patient in Elasticsearch
        verify(mockPatientSearchRepository, times(0)).save(patient);
    }


    @Test
    @Transactional
    public void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setFullName(null);

        // Create the Patient, which fails.


        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setBirthDate(null);

        // Create the Patient, which fails.


        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].diagnosis").value(hasItem(DEFAULT_DIAGNOSIS)));
    }

    @Test
    @Transactional
    public void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.diagnosis").value(DEFAULT_DIAGNOSIS));
    }


    @Test
    @Transactional
    public void getPatientsByIdFiltering() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        Long id = patient.getId();

        defaultPatientShouldBeFound("id.equals=" + id);
        defaultPatientShouldNotBeFound("id.notEquals=" + id);

        defaultPatientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.greaterThan=" + id);

        defaultPatientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPatientsByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName equals to DEFAULT_FULL_NAME
        defaultPatientShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the patientList where fullName equals to UPDATED_FULL_NAME
        defaultPatientShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName not equals to DEFAULT_FULL_NAME
        defaultPatientShouldNotBeFound("fullName.notEquals=" + DEFAULT_FULL_NAME);

        // Get all the patientList where fullName not equals to UPDATED_FULL_NAME
        defaultPatientShouldBeFound("fullName.notEquals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultPatientShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the patientList where fullName equals to UPDATED_FULL_NAME
        defaultPatientShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName is not null
        defaultPatientShouldBeFound("fullName.specified=true");

        // Get all the patientList where fullName is null
        defaultPatientShouldNotBeFound("fullName.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByFullNameContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName contains DEFAULT_FULL_NAME
        defaultPatientShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the patientList where fullName contains UPDATED_FULL_NAME
        defaultPatientShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName does not contain DEFAULT_FULL_NAME
        defaultPatientShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the patientList where fullName does not contain UPDATED_FULL_NAME
        defaultPatientShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }


    @Test
    @Transactional
    public void getAllPatientsByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the patientList where birthDate equals to UPDATED_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate not equals to DEFAULT_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.notEquals=" + DEFAULT_BIRTH_DATE);

        // Get all the patientList where birthDate not equals to UPDATED_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.notEquals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the patientList where birthDate equals to UPDATED_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate is not null
        defaultPatientShouldBeFound("birthDate.specified=true");

        // Get all the patientList where birthDate is null
        defaultPatientShouldNotBeFound("birthDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate is greater than or equal to DEFAULT_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the patientList where birthDate is greater than or equal to UPDATED_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate is less than or equal to DEFAULT_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the patientList where birthDate is less than or equal to SMALLER_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate is less than DEFAULT_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.lessThan=" + DEFAULT_BIRTH_DATE);

        // Get all the patientList where birthDate is less than UPDATED_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.lessThan=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate is greater than DEFAULT_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);

        // Get all the patientList where birthDate is greater than SMALLER_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.greaterThan=" + SMALLER_BIRTH_DATE);
    }


    @Test
    @Transactional
    public void getAllPatientsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address equals to DEFAULT_ADDRESS
        defaultPatientShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the patientList where address equals to UPDATED_ADDRESS
        defaultPatientShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address not equals to DEFAULT_ADDRESS
        defaultPatientShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the patientList where address not equals to UPDATED_ADDRESS
        defaultPatientShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultPatientShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the patientList where address equals to UPDATED_ADDRESS
        defaultPatientShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address is not null
        defaultPatientShouldBeFound("address.specified=true");

        // Get all the patientList where address is null
        defaultPatientShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByAddressContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address contains DEFAULT_ADDRESS
        defaultPatientShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the patientList where address contains UPDATED_ADDRESS
        defaultPatientShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where address does not contain DEFAULT_ADDRESS
        defaultPatientShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the patientList where address does not contain UPDATED_ADDRESS
        defaultPatientShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllPatientsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultPatientShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the patientList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultPatientShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultPatientShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the patientList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultPatientShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultPatientShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the patientList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultPatientShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phoneNumber is not null
        defaultPatientShouldBeFound("phoneNumber.specified=true");

        // Get all the patientList where phoneNumber is null
        defaultPatientShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultPatientShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the patientList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultPatientShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultPatientShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the patientList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultPatientShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllPatientsByDiagnosisIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where diagnosis equals to DEFAULT_DIAGNOSIS
        defaultPatientShouldBeFound("diagnosis.equals=" + DEFAULT_DIAGNOSIS);

        // Get all the patientList where diagnosis equals to UPDATED_DIAGNOSIS
        defaultPatientShouldNotBeFound("diagnosis.equals=" + UPDATED_DIAGNOSIS);
    }

    @Test
    @Transactional
    public void getAllPatientsByDiagnosisIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where diagnosis not equals to DEFAULT_DIAGNOSIS
        defaultPatientShouldNotBeFound("diagnosis.notEquals=" + DEFAULT_DIAGNOSIS);

        // Get all the patientList where diagnosis not equals to UPDATED_DIAGNOSIS
        defaultPatientShouldBeFound("diagnosis.notEquals=" + UPDATED_DIAGNOSIS);
    }

    @Test
    @Transactional
    public void getAllPatientsByDiagnosisIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where diagnosis in DEFAULT_DIAGNOSIS or UPDATED_DIAGNOSIS
        defaultPatientShouldBeFound("diagnosis.in=" + DEFAULT_DIAGNOSIS + "," + UPDATED_DIAGNOSIS);

        // Get all the patientList where diagnosis equals to UPDATED_DIAGNOSIS
        defaultPatientShouldNotBeFound("diagnosis.in=" + UPDATED_DIAGNOSIS);
    }

    @Test
    @Transactional
    public void getAllPatientsByDiagnosisIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where diagnosis is not null
        defaultPatientShouldBeFound("diagnosis.specified=true");

        // Get all the patientList where diagnosis is null
        defaultPatientShouldNotBeFound("diagnosis.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByDiagnosisIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where diagnosis is greater than or equal to DEFAULT_DIAGNOSIS
        defaultPatientShouldBeFound("diagnosis.greaterThanOrEqual=" + DEFAULT_DIAGNOSIS);

        // Get all the patientList where diagnosis is greater than or equal to UPDATED_DIAGNOSIS
        defaultPatientShouldNotBeFound("diagnosis.greaterThanOrEqual=" + UPDATED_DIAGNOSIS);
    }

    @Test
    @Transactional
    public void getAllPatientsByDiagnosisIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where diagnosis is less than or equal to DEFAULT_DIAGNOSIS
        defaultPatientShouldBeFound("diagnosis.lessThanOrEqual=" + DEFAULT_DIAGNOSIS);

        // Get all the patientList where diagnosis is less than or equal to SMALLER_DIAGNOSIS
        defaultPatientShouldNotBeFound("diagnosis.lessThanOrEqual=" + SMALLER_DIAGNOSIS);
    }

    @Test
    @Transactional
    public void getAllPatientsByDiagnosisIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where diagnosis is less than DEFAULT_DIAGNOSIS
        defaultPatientShouldNotBeFound("diagnosis.lessThan=" + DEFAULT_DIAGNOSIS);

        // Get all the patientList where diagnosis is less than UPDATED_DIAGNOSIS
        defaultPatientShouldBeFound("diagnosis.lessThan=" + UPDATED_DIAGNOSIS);
    }

    @Test
    @Transactional
    public void getAllPatientsByDiagnosisIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where diagnosis is greater than DEFAULT_DIAGNOSIS
        defaultPatientShouldNotBeFound("diagnosis.greaterThan=" + DEFAULT_DIAGNOSIS);

        // Get all the patientList where diagnosis is greater than SMALLER_DIAGNOSIS
        defaultPatientShouldBeFound("diagnosis.greaterThan=" + SMALLER_DIAGNOSIS);
    }


    @Test
    @Transactional
    public void getAllPatientsByVisitIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        Visit visit = VisitResourceIT.createEntity(em);
        em.persist(visit);
        em.flush();
        patient.addVisit(visit);
        patientRepository.saveAndFlush(patient);
        Long visitId = visit.getId();

        // Get all the patientList where visit equals to visitId
        defaultPatientShouldBeFound("visitId.equals=" + visitId);

        // Get all the patientList where visit equals to visitId + 1
        defaultPatientShouldNotBeFound("visitId.equals=" + (visitId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatientShouldBeFound(String filter) throws Exception {
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].diagnosis").value(hasItem(DEFAULT_DIAGNOSIS)));

        // Check, that the count call also returns 1
        restPatientMockMvc.perform(get("/api/patients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPatientShouldNotBeFound(String filter) throws Exception {
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPatientMockMvc.perform(get("/api/patients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatient() throws Exception {
        // Initialize the database
        patientService.save(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).get();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .fullName(UPDATED_FULL_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .diagnosis(UPDATED_DIAGNOSIS);

        restPatientMockMvc.perform(put("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPatient)))
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testPatient.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testPatient.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPatient.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPatient.getDiagnosis()).isEqualTo(UPDATED_DIAGNOSIS);

        // Validate the Patient in Elasticsearch
        verify(mockPatientSearchRepository, times(2)).save(testPatient);
    }

    @Test
    @Transactional
    public void updateNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc.perform(put("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Patient in Elasticsearch
        verify(mockPatientSearchRepository, times(0)).save(patient);
    }

    @Test
    @Transactional
    public void deletePatient() throws Exception {
        // Initialize the database
        patientService.save(patient);

        int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Delete the patient
        restPatientMockMvc.perform(delete("/api/patients/{id}", patient.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Patient in Elasticsearch
        verify(mockPatientSearchRepository, times(1)).deleteById(patient.getId());
    }

    @Test
    @Transactional
    public void searchPatient() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        patientService.save(patient);
        when(mockPatientSearchRepository.search(queryStringQuery("id:" + patient.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(patient), PageRequest.of(0, 1), 1));

        // Search the patient
        restPatientMockMvc.perform(get("/api/_search/patients?query=id:" + patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].diagnosis").value(hasItem(DEFAULT_DIAGNOSIS)));
    }
}
