package com.noosphere.mental_central.repository.search;

import com.noosphere.mental_central.domain.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Patient} entity.
 */
public interface PatientSearchRepository extends ElasticsearchRepository<Patient, Long> {
}
