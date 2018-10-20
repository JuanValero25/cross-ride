package com.crossover.techtrial.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.crossover.techtrial.dto.TopDriverDTO;
import com.crossover.techtrial.model.Ride;

/**
 * The Interface RideRepository.
 *
 * @author crossover
 */
@RestResource(exported = false)
public interface RideRepository extends CrudRepository<Ride, Long> {

	/**
	 * Find top drivers.
	 *
	 * @param startTime the start time
	 * @param endTime   the end time
	 * @param pageable  the pageable
	 * @return the list
	 */
	@Query(nativeQuery = true)
	List<TopDriverDTO> findTopDrivers(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

}
