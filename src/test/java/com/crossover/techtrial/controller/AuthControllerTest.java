package com.crossover.techtrial.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.repositories.PersonRepository;
import com.crossover.techtrial.security.JwtAuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

	private static final String URL = "/api/auth/signin";
	private static final String EMAIL = "bond@mail.com";
	private static final String REGISTRATION_NUMBER = "007";

	private MockMvc mockMvc;
	private ObjectMapper mapper;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private Environment environment;

	@Autowired
	private PersonRepository personRepository;

	@Before
	public void init() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		this.mapper = new ObjectMapper();
	}

	@Test
	public void testAuthPersonSuccess() throws Exception {
		MvcResult result = mockMvc.perform(
				post(URL).contentType(MediaType.APPLICATION_JSON).content(getContent(EMAIL, REGISTRATION_NUMBER)))
				.andExpect(status().isOk()).andReturn();
		JwtAuthenticationResponse mockedResponse = mapper.readValue(result.getResponse().getContentAsString(),
				JwtAuthenticationResponse.class);
		String personEmail = Jwts.parser().setSigningKey(environment.getProperty("app.jwtSecret"))
				.parseClaimsJws(mockedResponse.getAccessToken()).getBody().getSubject();
		Person person = personRepository.findPersonByEmail(EMAIL).orElseThrow(Exception::new);
		Assert.assertEquals(person.getEmail(), personEmail);
	}

	@Test
	public void testAuthPersonBadCredentials() throws Exception {
		mockMvc.perform(
				post(URL).contentType(MediaType.APPLICATION_JSON).content(getContent("juan@gmail.com", "randomPassword")))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testAuthPersonInvalidCredentials() throws Exception {
		mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(getContent("noemail", "otherRandomPassword")))
				.andExpect(status().isBadRequest());
	}

	private String getContent(String login, String password) {
		return mapper.createObjectNode().put("email", login).put("registrationNumber", password).toString();
	}
}
