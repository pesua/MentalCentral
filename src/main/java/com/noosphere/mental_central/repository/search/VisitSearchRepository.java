package com.noosphere.mental_central.repository.search;

import com.noosphere.mental_central.domain.Visit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Visit} entity.
 */
public interface VisitSearchRepository extends ElasticsearchRepository<Visit, Long> {
}
