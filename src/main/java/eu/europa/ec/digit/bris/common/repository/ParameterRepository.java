package eu.europa.ec.digit.bris.common.repository;

import eu.europa.ec.digit.bris.common.domain.Parameter;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Parameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParameterRepository extends JpaRepository<Parameter,Long> {
    
}
