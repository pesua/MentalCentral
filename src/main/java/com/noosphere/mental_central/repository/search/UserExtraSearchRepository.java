package com.noosphere.mental_central.repository.search;

import com.noosphere.mental_central.domain.UserExtra;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link UserExtra} entity.
 */
public interface UserExtraSearchRepository extends ElasticsearchRepository<UserExtra, Long> {
}
