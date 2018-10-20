package com.crossover.techtrial.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.crossover.techtrial.dto.TopDriverDTO;
import com.crossover.techtrial.exceptions.RideException;
import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.model.request.RideModel;
import com.crossover.techtrial.repositories.RideRepository;
import com.crossover.techtrial.security.JwtPersonPrincipal;
import com.crossover.techtrial.service.RideService;

@Service
public class RideServiceImpl implements RideService {

	private static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
	private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

	@Autowired
	private RideRepository rideRepository;

	@Autowired
	EntityManager entityManager;

	private static ModelMapper modelMapper = new ModelMapper();

	@Override
	@Transactional
	public Ride save(RideModel model) {
		Ride ride = rideRepository.save(getRideFromRideModel(model));
		entityManager.refresh(ride);
		return ride;
	}

	@Override
	public Ride findById(Long rideId) {
		Optional<Ride> optionalRide = rideRepository.findById(rideId);
		return optionalRide.orElse(null);
	}

	@Override
	public List<TopDriverDTO> getTopDrivers(LocalDateTime startTime, LocalDateTime endTime, int count) {
		return rideRepository.findTopDrivers(startTime, endTime, PageRequest.of(0, count));
	}

	private Ride getRideFromRideModel(RideModel model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new RideException("This operation is available only for authenticated users");
		}
		Ride ride = new Ride();
		ride.setDistance(model.getDistance());
		ride.setStartTime(model.getStartTime());
		ride.setEndTime(model.getEndTime());
		ride.setDriver(new Person());
		ride.setRider(new Person());
		Long driverId = ((JwtPersonPrincipal) authentication.getPrincipal()).getId();
		ride.getDriver().setId(driverId);
		Long riderId = model.getRiderId();
		if (riderId.equals(driverId)) {
			throw new RideException("Rider cannot be Driver");
		}
		ride.getRider().setId(riderId);
		return ride;
	}

	public boolean validateDate(RideModel model) {
		LocalDateTime startDate = getDate(model.getStartTime());
		LocalDateTime endDate = getDate(model.getEndTime());

		if (endDate.isBefore(startDate)) {
			return false;
		}

		return true;
	}

	/**
	 * Gets the date. if is bad formated or empty will throw DateTimeParseException
	 *
	 * @param stringDate the string date
	 * @return the date
	 */
	private LocalDateTime getDate(String stringDate) {
		return LocalDateTime.parse(stringDate, DATETIME_FORMAT);
	}

}
