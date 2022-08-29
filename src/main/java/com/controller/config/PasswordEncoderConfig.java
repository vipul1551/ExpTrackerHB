package com.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration  //It is same as component and repository but for configuration we use @Configuration
public class PasswordEncoderConfig {

	@Bean  //Method through Singleton Object created by @Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		System.out.println("Encoder is ready...");
		return new BCryptPasswordEncoder();
	}
}
