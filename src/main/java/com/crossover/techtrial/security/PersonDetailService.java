package com.crossover.techtrial.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crossover.techtrial.service.PersonService;

@Service
public class PersonDetailService implements UserDetailsService {

	@Autowired
	PersonService personService;

	@Transactional
	public UserDetails loadUserByEmail(String email) {
		return personService.loadUserByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return personService.loadUserByEmail(username);
	}
}