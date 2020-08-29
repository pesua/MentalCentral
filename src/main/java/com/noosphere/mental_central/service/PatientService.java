package com.noosphere.mental_central.service;

import com.noosphere.mental_central.domain.Patient;
import com.noosphere.mental_central.repository.PatientRepository;
import com.noosphere.mental_central.repository.search.PatientSearchRepository;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

/**
 * Service Implementation for managing {@link Patient}.
 */
@Service
@Transactional
public class PatientService {

    private final Logger log = LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;

    private final PatientSearchRepository patientSearchRepository;

    public PatientService(PatientRepository patientRepository, PatientSearchRepository patientSearchRepository) {
        this.patientRepository = patientRepository;
        this.patientSearchRepository = patientSearchRepository;
    }

    /**
     * Save a patient.
     *
     * @param patient the entity to save.
     * @return the persisted entity.
     */
    public Patient save(Patient patient) {
        log.debug("Request to save Patient : {}", patient);
        Patient result = patientRepository.save(patient);
        patientSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the patients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Patient> findAll(Pageable pageable) {
        log.debug("Request to get all Patients");
        return patientRepository.findAll(pageable);
    }


    /**
     * Get one patient by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Patient> findOne(Long id) {
        log.debug("Request to get Patient : {}", id);
        return patientRepository.findById(id);
    }

    /**
     * Delete the patient by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Patient : {}", id);
        patientRepository.deleteById(id);
        patientSearchRepository.deleteById(id);
    }

    /**
     * Search for the patient corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Patient> search(String query, Pageable pageable) {
        MultiMatchQueryBuilder multiMatchQueryBuilder = multiMatchQuery(
            query, Patient.getSearchFieldNames()
        ).fuzziness(Fuzziness.AUTO);

        log.debug("Request to search for a page of Patients for query {}", query);
        return patientSearchRepository.search(multiMatchQueryBuilder, pageable);
    }
}
