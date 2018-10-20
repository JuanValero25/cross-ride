package com.crossover.techtrial.service;

import java.time.LocalDateTime;
import java.util.List;

import com.crossover.techtrial.dto.TopDriverDTO;
import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.model.request.RideModel;

/**
 * The Interface RideService.
 *
 * @author crossover
 * 
 *         Service interface for objects {@link Ride}
 */
public interface RideService {

	/**
	 * Save.
	 *
	 * @param model the model
	 * @return the ride
	 */
	Ride save(RideModel model);

	/**
	 * Find by id.
	 *
	 * @param rideId the ride id
	 * @return the ride
	 */
	Ride findById(Long rideId);

	/**
	 * Gets the top drivers.
	 *
	 * @param startTime the start time
	 * @param endTime   the end time
	 * @param count     the count
	 * @return the top drivers
	 */
	List<TopDriverDTO> getTopDrivers(LocalDateTime startTime, LocalDateTime endTime, int count);
}
