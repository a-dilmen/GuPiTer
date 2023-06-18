package com.dilmen.gupiter.constants;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;

public class RestEndPoints {
	public static final String API = "/api";
	public static final String VERSION = "/v1";

	public static final String AUTH = API + VERSION + "/auth";

	public static final String REGISTER = "/register";
	public static final String LOGIN = "/login";
	@Value("${apigateway.url:*}")
	@Builder.Default
	public static final String CROSS_ORIGIN = "*";
}
