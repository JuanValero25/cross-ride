package com.crossover.techtrial.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.crossover.techtrial.model.Role;
import com.crossover.techtrial.model.RoleName;

/**
 * The Interface RoleRepository.
 */
@RestResource(exported = false)
public interface RoleRepository extends CrudRepository<Role, Long> {

	/**
	 * Find by role name.
	 *
	 * @param roleName the role name
	 * @return the optional
	 */
	Optional<Role> findByRoleName(RoleName roleName);
}
