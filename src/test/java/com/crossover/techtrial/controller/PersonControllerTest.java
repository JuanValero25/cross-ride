package com.crossover.techtrial.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.crossover.techtrial.dto.ExceptionDTO;
import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.security.JwtAuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author kshah
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PersonControllerTest {

	private static final String ADMIN_EMAIL = "juanvalero252@gmail.com";
	private static final String ADMIN_REGISTRATION_NUMBER = "001";
	private static final String EMAIL = "bond@mail.com";
	private static final String REGISTRATION_NUMBER = "007";

	private MockMvc mockMvc;
	private ObjectMapper mapper;

	@Autowired
	private WebApplicationContext context;

	@Before
	public void init() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		this.mapper = new ObjectMapper();
	}

	@Test
	public void testPersonRegisterSuccess() throws Exception {
		String content = mapper.createObjectNode().put("name", "jose valero").put("email", "jose.valero@mail.com")
				.put("registrationNumber", "492727").toString();
		MvcResult result = mockMvc.perform(post("/api/person").contentType(MediaType.APPLICATION_JSON).content(content))
				.andExpect(status().isCreated()).andReturn();
		Person person = mapper.readValue(result.getResponse().getContentAsByteArray(), Person.class);
		assertEquals("jose valero", person.getName());
	}

	@Test
	public void testGetAllPersonsSuccess() throws Exception {
		MvcResult result = mockMvc.perform(
				get("/api/person").header(HttpHeaders.AUTHORIZATION, getToken(ADMIN_EMAIL, ADMIN_REGISTRATION_NUMBER)))
				.andExpect(status().isOk()).andReturn();
		List<Person> persons = mapper.readValue(result.getResponse().getContentAsString(),
				mapper.getTypeFactory().constructCollectionType(List.class, Person.class));
		assertTrue(persons.size() >= 2);
		Person admin = persons.stream().filter(person -> person.getEmail().equals(ADMIN_EMAIL)).findFirst()
				.orElse(null);
		assertNotNull(admin);
	}

	@Test
	public void testGetPersonByIdSuccess() throws Exception {
		MvcResult result = mockMvc
				.perform(get("/api/person/2").header(HttpHeaders.AUTHORIZATION, getToken(EMAIL, REGISTRATION_NUMBER)))
				.andExpect(status().isOk()).andReturn();
		Person person = mapper.readValue(result.getResponse().getContentAsString(), Person.class);
		assertEquals(EMAIL, person.getEmail());
	}

	@Test
	public void testGetPersonByIdFail() throws Exception {
		MvcResult result = mockMvc
				.perform(get("/api/person/999").header(HttpHeaders.AUTHORIZATION, getToken(EMAIL, REGISTRATION_NUMBER))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();
		ExceptionDTO errorDetails = mapper.readValue(result.getResponse().getContentAsString(), ExceptionDTO.class);
		assertEquals("Person not found", errorDetails.getMessage());
	}

	private String getToken(String login, String password) throws Exception {
		String bodyResponseAuthentication = mockMvc
				.perform(post("/api/auth/signin").contentType(MediaType.APPLICATION_JSON).content(
						mapper.createObjectNode().put("email", login).put("registrationNumber", password).toString()))
				.andReturn().getResponse().getContentAsString();
		JwtAuthenticationResponse response = mapper.readValue(bodyResponseAuthentication,
				JwtAuthenticationResponse.class);

		return "Bearer " + response.getAccessToken();
	}
}
