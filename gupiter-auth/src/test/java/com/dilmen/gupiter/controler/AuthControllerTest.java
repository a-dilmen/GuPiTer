package com.dilmen.gupiter.controler;

import com.dilmen.gupiter.controller.AuthController;
import com.dilmen.gupiter.dto.request.LoginRequestDto;
import com.dilmen.gupiter.dto.request.RegisterRequestDto;
import com.dilmen.gupiter.dto.response.LoginResponseDto;
import com.dilmen.gupiter.dto.response.RegisterResponseDto;
import com.dilmen.gupiter.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AuthService authService;
	@Test
	void login_ValidRequest_ReturnsLoginResponseDto() throws Exception {
		LoginRequestDto requestDto = new LoginRequestDto();
		requestDto.setEmail("test@example.com");
		requestDto.setPassword("password");
		LoginResponseDto responseDto = LoginResponseDto.builder().token("tokenValue").build();
		when(authService.login(any(LoginRequestDto.class))).thenReturn(responseDto);
		mockMvc.perform(post("http://localhost/api/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(requestDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("tokenValue"));
		verify(authService).login(any(LoginRequestDto.class));
	}
	private String asJsonString(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}
}