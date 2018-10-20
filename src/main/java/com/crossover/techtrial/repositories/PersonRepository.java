
package com.crossover.techtrial.repositories;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.crossover.techtrial.model.Person;

/**
 * Person repository for basic operations on Person entity.
 * 
 * @author crossover
 */
@RestResource(exported = false)
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.repository.CrudRepository#findById(java.lang.Object)
	 */
	Optional<Person> findById(Long id);

	/**
	 * Find person by email.
	 *
	 * @param email the email
	 * @return the optional
	 */
	Optional<Person> findPersonByEmail(String email);
}
