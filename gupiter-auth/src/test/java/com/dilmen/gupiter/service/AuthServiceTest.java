package com.dilmen.gupiter.service;

import com.dilmen.gupiter.dto.request.LoginRequestDto;
import com.dilmen.gupiter.dto.request.RegisterRequestDto;
import com.dilmen.gupiter.dto.response.LoginResponseDto;
import com.dilmen.gupiter.dto.response.RegisterResponseDto;
import com.dilmen.gupiter.entity.Auth;
import com.dilmen.gupiter.exception.AuthException;
import com.dilmen.gupiter.exception.EErrorType;
import com.dilmen.gupiter.repository.IAuthRepository;
import com.dilmen.gupiter.utility.JwtTokenManager;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@SpringBootTest
public class AuthServiceTest {
	@Mock
	private IAuthRepository authRepository;
	@Mock
	private JwtTokenManager jwtTokenManager;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private Auth auth;
	@InjectMocks
	private AuthService authService;
	@Test
	public void register_PasswordMismatch_ThrowsAuthException() {
		//prepare
		RegisterRequestDto requestDto = new RegisterRequestDto();
		requestDto.setPassword("password");
		requestDto.setRePassword("differentPassword");
		//assert
		assertThrows(AuthException.class, () -> authService.register(requestDto));
		verify(authRepository, never()).findOptionalByEmail(anyString());
		verify(authRepository, never()).save(any(Auth.class));
	}
	@Test
	void register_DuplicateEmail_ThrowsAuthException() {
		//prepare
		RegisterRequestDto requestDto = new RegisterRequestDto();
		requestDto.setEmail("test@example.com");
		requestDto.setPassword("123");
		requestDto.setRePassword("123");
		when(authRepository.findOptionalByEmail(requestDto.getEmail())).thenReturn(Optional.of(new Auth()));
		//assert
		AuthException exception = assertThrows(AuthException.class, () -> authService.register(requestDto));
		assertEquals(EErrorType.AUTH_REGISTRATION_EMAIL_ERROR, exception.getEErrorType());
		verify(authRepository, never()).save(any(Auth.class));
	}
	@Test
	void register_ValidRequest_ReturnsRegisterResponseDto2() {
		//prepare
		RegisterRequestDto requestDto = new RegisterRequestDto();
		requestDto.setEmail("test@example.com");
		requestDto.setPassword("password");
		requestDto.setRePassword("password");
		when(authRepository.save(any(Auth.class)))
				.thenAnswer(invocation -> {
					Auth savedAuth = invocation.getArgument(0);
					savedAuth.setId(1L);
					return savedAuth;
				});
		RegisterResponseDto responseDto = authService.register(requestDto);
		//assert
		verify(authRepository).save(any(Auth.class));
		assertEquals("test@example.com", responseDto.getEmail());
	}
	@Test
	void register_ValidRequest_ReturnsRegisterResponseDto() {
		RegisterRequestDto requestDto = new RegisterRequestDto();
		requestDto.setEmail("test@example.com");
		requestDto.setPassword("password");
		requestDto.setRePassword("password");
		// instead of serviceManager method overriding for testing purposes authRepository is used
		when(authRepository.save(any(Auth.class))).thenAnswer(invocation -> {
			Auth savedAuth = invocation.getArgument(0);
			savedAuth.setId(1L);
			return savedAuth;
		});
		RegisterResponseDto responseDto = authService.register(requestDto);
		//assert
		assertEquals("test@example.com", responseDto.getEmail());
	}
	@Test
	void register_encodePassword() {
		String password = "password";
		String encodedPassword = passwordEncoder.encode(password);
		assertNotEquals(password, encodedPassword);
	}
	@Test
	void login_emailIncorrect_throwsAthException(){
		LoginRequestDto dto = new LoginRequestDto();
		dto.setEmail("test@example.com");
		when(authRepository.findOptionalByEmail(dto.getEmail())).thenThrow(new AuthException(EErrorType.AUTH_LOGIN_ERROR));
		assertThrows(AuthException.class, () -> authService.login(dto));
		verify(jwtTokenManager,never()).createToken(any(Long.class));
	}
	@Test
	void login_passwordDoesNotMatch_throwsAuthException(){
		LoginRequestDto dto = new LoginRequestDto();
		dto.setPassword("123");
		when(authRepository.findOptionalByEmail(dto.getEmail()))
				.thenReturn(Optional.of(auth));
		doThrow(new AuthException(EErrorType.AUTH_LOGIN_ERROR)).when(passwordEncoder).matches(dto.getPassword(), "encryptedPassword");
		AuthException authException = assertThrows(AuthException.class, () -> authService.login(dto));
		assertEquals(EErrorType.AUTH_LOGIN_ERROR, authException.getEErrorType());
	}
	@Test
	void login_checkUserExistence_throwsAuthException() {
		LoginRequestDto dto = new LoginRequestDto();
		dto.setPassword("123");
		dto.setEmail("a@b.c");
		when(authRepository.findOptionalByEmail(dto.getEmail()))
				.thenReturn(Optional.of(auth));
		when(passwordEncoder.matches(dto.getPassword(), auth.getPassword()))
				.thenReturn(true);
		doThrow(new AuthException(EErrorType.USER_NOT_FOUND))
				.when(auth)
				.getUserId();
		AuthException authException = assertThrows(AuthException.class, () -> authService.login(dto));
		assertEquals(EErrorType.USER_NOT_FOUND, authException.getEErrorType());
	}
	@Test
	void login_generateToken_ReturnsLoginResponseDto() {
		LoginRequestDto dto = new LoginRequestDto();
		dto.setPassword("123");
		dto.setEmail("a@b.c");
		Auth auth = new Auth();
		auth.setUserId(1L);
		when(authRepository.findOptionalByEmail(dto.getEmail()))
				.thenReturn(Optional.of(auth));
		when(passwordEncoder.matches(dto.getPassword(), auth.getPassword()))
				.thenReturn(true);
		when(jwtTokenManager.createToken(auth.getUserId()))
				.thenReturn(Optional.of("tokenValue"));
		LoginResponseDto responseDto = authService.login(dto);
		assertNotNull(responseDto.getToken());
		assertEquals("tokenValue", responseDto.getToken());
	}

}
