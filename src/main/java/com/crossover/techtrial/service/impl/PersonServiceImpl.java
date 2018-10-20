package com.crossover.techtrial.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crossover.techtrial.exceptions.PersonException;
import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.model.RoleName;
import com.crossover.techtrial.model.request.SignUpRequest;
import com.crossover.techtrial.repositories.PersonRepository;
import com.crossover.techtrial.security.JwtPersonPrincipal;
import com.crossover.techtrial.service.PersonService;
import com.crossover.techtrial.service.RoleService;;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleService roleService;

	private static ModelMapper modelMapper = new ModelMapper();

	@Override
	public List<Person> getAll() {
		List<Person> personList = new ArrayList<>();
		personRepository.findAll().forEach(personList::add);
		return personList;
	}

	@Override
	public Person save(SignUpRequest model) {
		Person personToSingUp = modelMapper.map(model, Person.class);
		if (emailSave(personToSingUp.getEmail())) {
			throw new PersonException("Email is in used please use other email", "creating new User", "email in use");
		}
		personToSingUp.setRegistrationNumber(passwordEncoder.encode(personToSingUp.getRegistrationNumber()));
		personToSingUp.getRoles().add(roleService.findRoleByName(RoleName.ROLE_PERSON));
		return personRepository.save(personToSingUp);
	}

	@Override
	public Boolean emailSave(String email) {
		return personRepository.findPersonByEmail(email).isPresent();
	}

	@Override
	public Person findByEmail(String email) {
		return personRepository.findPersonByEmail(email)
				.orElseThrow(() -> new PersonException("person by Email", email, "Person not found"));
	}

	@Transactional
	public UserDetails loadUserByEmail(String email) {
		Person user = personRepository.findPersonByEmail(email)
				.orElseThrow(() -> new PersonException("User Email", email, "User not found"));
		return JwtPersonPrincipal.create(user);
	}

	@Override
	public Person findById(Long id) {
		return personRepository.findById(id)
				.orElseThrow(() -> new PersonException("person by id", id.toString(), "Person not found"));
	}

}
