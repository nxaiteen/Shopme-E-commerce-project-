package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
	@Test
	public void testEncodePassword() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "12345";
		String encodedPasswordString = passwordEncoder.encode(rawPassword);
		
		System.out.println(encodedPasswordString);
		boolean matches = passwordEncoder.matches(rawPassword, encodedPasswordString);
		
		assertThat(matches).isTrue();
	}
}
