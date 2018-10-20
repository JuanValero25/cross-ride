package com.crossover.techtrial.controller;

import static org.junit.Assert.assertEquals;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.crossover.techtrial.dto.TopDriverDTO;
import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.repositories.RideRepository;
import com.crossover.techtrial.security.JwtAuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RideControllerTest {

	private static final String START_TIME = "2018-09-05T08:00:00";
	private static final String END_TIME = "2018-09-05T10:40:00";
	private static final String EMAIL = "bond@mail.com";
	private static final String REGISTRATION_NUMBER = "007";
	private static final String ADMIN_EMAIL = "juanvalero252@gmail.com";
	private static final String ADMIN_REGISTRATION_NUMBER = "001";

	private MockMvc mockMvc;
	private ObjectMapper mapper;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private RideRepository rideRepository;

	@Before
	public void init() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		this.mapper = new ObjectMapper();
	}

	@Test
	public void testCreateNewRideSuccess() throws Exception {
		Ride ride = addRide(START_TIME, END_TIME, 10L, 1L, EMAIL, REGISTRATION_NUMBER);
		assertEquals(EMAIL, ride.getDriver().getEmail());
		assertEquals(START_TIME, ride.getStartTime());
		rideRepository.delete(ride);
	}

	@Test
	public void testGetRideByIdSuccess() throws Exception {
		Ride newRide = addRide(START_TIME, END_TIME, 15L, 2L, ADMIN_EMAIL, ADMIN_REGISTRATION_NUMBER);
		MvcResult result = mockMvc
				.perform(get("/api/ride/" + newRide.getId()).contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, getToken(ADMIN_EMAIL, ADMIN_REGISTRATION_NUMBER)))
				.andExpect(status().isOk()).andReturn();
		Ride foundRide = mapper.readValue(result.getResponse().getContentAsByteArray(), Ride.class);
		assertEquals(newRide, foundRide);
		rideRepository.delete(newRide);
	}

	@Test
	public void testGetRideByIdFail() throws Exception {
		mockMvc.perform(get("/api/ride/8888").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION,
				getToken(ADMIN_EMAIL, ADMIN_REGISTRATION_NUMBER))).andExpect(status().isNotFound());
	}

	@Test
	public void testGetTopDriver() throws Exception {

		MvcResult result = mockMvc
				.perform(get("/api/top-rides").param("max", "10").param("startTime", "2018-09-01T08:00:00")
						.param("endTime", "2018-09-06T15:00:00")
						.header(HttpHeaders.AUTHORIZATION, getToken(EMAIL, REGISTRATION_NUMBER)))
				.andExpect(status().isOk()).andReturn();
		List<TopDriverDTO> topDrivers = mapper.readValue(result.getResponse().getContentAsByteArray(),
				mapper.getTypeFactory().constructCollectionType(List.class, TopDriverDTO.class));
		assertEquals(2, topDrivers.size());
		TopDriverDTO first = topDrivers.get(0);
		TopDriverDTO second = topDrivers.get(1);
		assertEquals(EMAIL, first.getEmail());
		assertEquals(ADMIN_EMAIL, second.getEmail());
		assertEquals(first.getTotalRideDurationInSeconds(), Long.valueOf(21600));
		assertEquals(first.getMaxRideDurationInSecods(), Long.valueOf(21600));
		assertEquals(50.0, first.getAverageDistance(), 0.001);
		assertEquals(second.getTotalRideDurationInSeconds(), Long.valueOf(9000));
		assertEquals(second.getMaxRideDurationInSecods(), Long.valueOf(5400));
		assertEquals(15.0, second.getAverageDistance(), 0.001);
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

	private Ride addRide(String startTime, String endTime, Long distance, Long riderId, String login, String password)
			throws Exception {
		MvcResult result = mockMvc
				.perform(post("/api/ride").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.createObjectNode().put("startTime", startTime)
				.put("endTime", endTime)
				.put("distance", distance).put("riderId", riderId).toString())
				.header(HttpHeaders.AUTHORIZATION, getToken(login, password)))
				.andExpect(status().isCreated()).andReturn();
		return mapper.readValue(result.getResponse().getContentAsByteArray(), Ride.class);
	}
}
