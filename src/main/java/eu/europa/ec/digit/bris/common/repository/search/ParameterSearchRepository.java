package eu.europa.ec.digit.bris.common.repository.search;

import eu.europa.ec.digit.bris.common.domain.Parameter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Parameter entity.
 */
public interface ParameterSearchRepository extends ElasticsearchRepository<Parameter, Long> {
}
