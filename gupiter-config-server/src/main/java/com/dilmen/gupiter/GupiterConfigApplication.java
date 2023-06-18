package com.dilmen.gupiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;


@SpringBootApplication
@EnableConfigServer
public class GupiterConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(GupiterConfigApplication.class, args);
	}

}

