package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Role;
import com.crossover.techtrial.model.RoleName;

/**
 * The Interface RoleService.
 */
public interface RoleService {

	/**
	 * Find role by name.
	 *
	 * @param authority the authority
	 * @return the role
	 */
	Role findRoleByName(RoleName authority);
}
