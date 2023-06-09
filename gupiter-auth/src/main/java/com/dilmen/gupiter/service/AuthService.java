package com.dilmen.gupiter.service;

import com.dilmen.gupiter.configuration.rabbitmq.model.CreateUser;
import com.dilmen.gupiter.configuration.rabbitmq.producer.CreateUserProducer;
import com.dilmen.gupiter.dto.request.LoginRequestDto;
import com.dilmen.gupiter.dto.request.RegisterRequestDto;
import com.dilmen.gupiter.dto.response.LoginResponseDto;
import com.dilmen.gupiter.dto.response.RegisterResponseDto;
import com.dilmen.gupiter.entity.Auth;
import com.dilmen.gupiter.exception.AuthException;
import com.dilmen.gupiter.exception.EErrorType;
import com.dilmen.gupiter.repository.IAuthRepository;
import com.dilmen.gupiter.utility.JwtTokenManager;
import com.dilmen.gupiter.utility.ServiceManager;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends ServiceManager<Auth, Long> {
	private final IAuthRepository iAuthRepository;
	private final JwtTokenManager jwtTokenManager;
	private final PasswordEncoder passwordEncoder;
	private final CreateUserProducer createUserProducer;
	public AuthService(IAuthRepository iAuthRepository, JwtTokenManager jwtTokenManager,PasswordEncoder passwordEncoder, CreateUserProducer createUser) {
		super(iAuthRepository);
		this.iAuthRepository = iAuthRepository;
		this.jwtTokenManager = jwtTokenManager;
		this.passwordEncoder = passwordEncoder;
		this.createUserProducer = createUser;
	}
	@Transactional
	public RegisterResponseDto register(RegisterRequestDto registerRequestDto) {
		if (!registerRequestDto.getPassword().equals(registerRequestDto.getRePassword()))
			throw new AuthException(EErrorType.AUTH_REPASSWORD_ERROR);
		if (iAuthRepository.findOptionalByEmail(registerRequestDto.getEmail()).isPresent())
			throw new AuthException(EErrorType.AUTH_REGISTRATION_EMAIL_ERROR);

		// add async logic to create user at user service and get user id to set user id field
		createUserProducer.createSendMessage(CreateUser.builder().email(registerRequestDto.getEmail()).userId(1L).build());
		Auth auth = save(Auth
				.builder()
				.email(registerRequestDto.getEmail())
				.password(passwordEncoder.encode(registerRequestDto.getPassword()))
				.userId(0L)
				.build());
		return RegisterResponseDto.builder().email(auth.getEmail()).build();
	}
	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		Auth auth = iAuthRepository
				.findOptionalByEmail(loginRequestDto.getEmail())
				.orElseThrow(() -> { throw new AuthException(EErrorType.AUTH_LOGIN_ERROR);});
		if (!passwordEncoder.matches(loginRequestDto.getPassword(), auth.getPassword()))
			throw new AuthException(EErrorType.AUTH_LOGIN_ERROR);
		Long userId;
		try {
			userId = auth.getUserId();
		} catch (Exception e) {
			throw new AuthException(EErrorType.USER_NOT_FOUND);
		}
		String token = jwtTokenManager.createToken(userId).orElseThrow(() -> {throw new AuthException(EErrorType.TOKEN_ERROR);});
		return LoginResponseDto.builder().token(token).build();
	}
}


