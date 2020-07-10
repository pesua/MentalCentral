package com.noosphere.web.rest;

import com.noosphere.MentalCentralApp;
import com.noosphere.domain.Patient;
import com.noosphere.repository.PatientRepository;
import com.noosphere.service.PatientService;
import com.noosphere.service.dto.PatientCriteria;
import com.noosphere.service.PatientQueryService;

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
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PatientResource} REST controller.
 */
@SpringBootTest(classes = MentalCentralApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PatientResourceIT {

    private static final String DEFAULT_FULLNAME = "AAAAAAAAAA";
    private static final String UPDATED_FULLNAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_BIRTHDAY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_BIRTHDAY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_DIAGNOSIS = 1;
    private static final Integer UPDATED_DIAGNOSIS = 2;
    private static final Integer SMALLER_DIAGNOSIS = 1 - 1;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

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
            .fullname(DEFAULT_FULLNAME)
            .dateBirthday(DEFAULT_DATE_BIRTHDAY)
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
            .fullname(UPDATED_FULLNAME)
            .dateBirthday(UPDATED_DATE_BIRTHDAY)
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
        assertThat(testPatient.getFullname()).isEqualTo(DEFAULT_FULLNAME);
        assertThat(testPatient.getDateBirthday()).isEqualTo(DEFAULT_DATE_BIRTHDAY);
        assertThat(testPatient.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPatient.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPatient.getDiagnosis()).isEqualTo(DEFAULT_DIAGNOSIS);
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
    }


    @Test
    @Transactional
    public void checkFullnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setFullname(null);

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
    public void checkDateBirthdayIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setDateBirthday(null);

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
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setAddress(null);

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
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setPhoneNumber(null);

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
    public void checkDiagnosisIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setDiagnosis(null);

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
            .andExpect(jsonPath("$.[*].fullname").value(hasItem(DEFAULT_FULLNAME)))
            .andExpect(jsonPath("$.[*].dateBirthday").value(hasItem(DEFAULT_DATE_BIRTHDAY.toString())))
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
            .andExpect(jsonPath("$.fullname").value(DEFAULT_FULLNAME))
            .andExpect(jsonPath("$.dateBirthday").value(DEFAULT_DATE_BIRTHDAY.toString()))
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
    public void getAllPatientsByFullnameIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullname equals to DEFAULT_FULLNAME
        defaultPatientShouldBeFound("fullname.equals=" + DEFAULT_FULLNAME);

        // Get all the patientList where fullname equals to UPDATED_FULLNAME
        defaultPatientShouldNotBeFound("fullname.equals=" + UPDATED_FULLNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFullnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullname not equals to DEFAULT_FULLNAME
        defaultPatientShouldNotBeFound("fullname.notEquals=" + DEFAULT_FULLNAME);

        // Get all the patientList where fullname not equals to UPDATED_FULLNAME
        defaultPatientShouldBeFound("fullname.notEquals=" + UPDATED_FULLNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFullnameIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullname in DEFAULT_FULLNAME or UPDATED_FULLNAME
        defaultPatientShouldBeFound("fullname.in=" + DEFAULT_FULLNAME + "," + UPDATED_FULLNAME);

        // Get all the patientList where fullname equals to UPDATED_FULLNAME
        defaultPatientShouldNotBeFound("fullname.in=" + UPDATED_FULLNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFullnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullname is not null
        defaultPatientShouldBeFound("fullname.specified=true");

        // Get all the patientList where fullname is null
        defaultPatientShouldNotBeFound("fullname.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByFullnameContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullname contains DEFAULT_FULLNAME
        defaultPatientShouldBeFound("fullname.contains=" + DEFAULT_FULLNAME);

        // Get all the patientList where fullname contains UPDATED_FULLNAME
        defaultPatientShouldNotBeFound("fullname.contains=" + UPDATED_FULLNAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFullnameNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullname does not contain DEFAULT_FULLNAME
        defaultPatientShouldNotBeFound("fullname.doesNotContain=" + DEFAULT_FULLNAME);

        // Get all the patientList where fullname does not contain UPDATED_FULLNAME
        defaultPatientShouldBeFound("fullname.doesNotContain=" + UPDATED_FULLNAME);
    }


    @Test
    @Transactional
    public void getAllPatientsByDateBirthdayIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where dateBirthday equals to DEFAULT_DATE_BIRTHDAY
        defaultPatientShouldBeFound("dateBirthday.equals=" + DEFAULT_DATE_BIRTHDAY);

        // Get all the patientList where dateBirthday equals to UPDATED_DATE_BIRTHDAY
        defaultPatientShouldNotBeFound("dateBirthday.equals=" + UPDATED_DATE_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllPatientsByDateBirthdayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where dateBirthday not equals to DEFAULT_DATE_BIRTHDAY
        defaultPatientShouldNotBeFound("dateBirthday.notEquals=" + DEFAULT_DATE_BIRTHDAY);

        // Get all the patientList where dateBirthday not equals to UPDATED_DATE_BIRTHDAY
        defaultPatientShouldBeFound("dateBirthday.notEquals=" + UPDATED_DATE_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllPatientsByDateBirthdayIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where dateBirthday in DEFAULT_DATE_BIRTHDAY or UPDATED_DATE_BIRTHDAY
        defaultPatientShouldBeFound("dateBirthday.in=" + DEFAULT_DATE_BIRTHDAY + "," + UPDATED_DATE_BIRTHDAY);

        // Get all the patientList where dateBirthday equals to UPDATED_DATE_BIRTHDAY
        defaultPatientShouldNotBeFound("dateBirthday.in=" + UPDATED_DATE_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllPatientsByDateBirthdayIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where dateBirthday is not null
        defaultPatientShouldBeFound("dateBirthday.specified=true");

        // Get all the patientList where dateBirthday is null
        defaultPatientShouldNotBeFound("dateBirthday.specified=false");
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

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatientShouldBeFound(String filter) throws Exception {
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullname").value(hasItem(DEFAULT_FULLNAME)))
            .andExpect(jsonPath("$.[*].dateBirthday").value(hasItem(DEFAULT_DATE_BIRTHDAY.toString())))
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
            .fullname(UPDATED_FULLNAME)
            .dateBirthday(UPDATED_DATE_BIRTHDAY)
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
        assertThat(testPatient.getFullname()).isEqualTo(UPDATED_FULLNAME);
        assertThat(testPatient.getDateBirthday()).isEqualTo(UPDATED_DATE_BIRTHDAY);
        assertThat(testPatient.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPatient.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPatient.getDiagnosis()).isEqualTo(UPDATED_DIAGNOSIS);
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
    }
}
