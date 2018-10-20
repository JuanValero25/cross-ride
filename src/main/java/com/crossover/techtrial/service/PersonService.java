package com.crossover.techtrial.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.model.request.SignUpRequest;

/**
 * The Interface PersonService.
 *
 * @author cossover
 * 
 *         Service interface for objects {@link Person}
 */
public interface PersonService {

	/**
	 * Gets the all.
	 *
	 * @return the all
	 */
	List<Person> getAll();

	/**
	 * Save.
	 *
	 * @param model the model
	 * @return the person
	 */
	Person save(SignUpRequest model);

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the person
	 */
	Person findById(Long id);

	/**
	 * Find by email.
	 *
	 * @param email the email
	 * @return the person
	 */
	Person findByEmail(String email);

	/**
	 * Load user by email.
	 *
	 * @param email the email
	 * @return the user details
	 */
	UserDetails loadUserByEmail(String email);

	/**
	 * Email save.
	 *
	 * @param email the email
	 * @return the boolean
	 */
	Boolean emailSave(String email);
}
