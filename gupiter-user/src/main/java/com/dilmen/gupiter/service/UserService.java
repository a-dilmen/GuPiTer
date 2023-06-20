package com.dilmen.gupiter.service;

import com.dilmen.gupiter.entity.User;
import com.dilmen.gupiter.repository.IUserRepository;
import com.dilmen.gupiter.utility.JwtTokenManager;
import com.dilmen.gupiter.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends ServiceManager<User, Long> {
	private final IUserRepository iUserRepository;
	private final JwtTokenManager jwtTokenManager;
	public AuthService(IUserRepository iUserRepository, JwtTokenManager jwtTokenManager) {
		super(iUserRepository);
		this.iUserRepository = iUserRepository;
		this.jwtTokenManager = jwtTokenManager;
	}

}


