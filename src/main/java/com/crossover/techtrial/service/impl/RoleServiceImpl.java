package com.crossover.techtrial.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crossover.techtrial.model.Role;
import com.crossover.techtrial.model.RoleName;
import com.crossover.techtrial.repositories.RoleRepository;
import com.crossover.techtrial.service.RoleService;

/**
 * role service
 */
@Service
public class RoleServiceImpl implements RoleService {

	/** The role repository. */
	@Autowired
	private RoleRepository roleRepository;

	/*
	 * find role by RoleName
	 */
	@Override
	public Role findRoleByName(RoleName roleName) {
		return roleRepository.findByRoleName(roleName).orElse(null);
	}
}
