package com.truve.platform.user.service.domain.dto.response;

import org.springframework.data.util.Pair;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthResponse {

	@Getter
	@AllArgsConstructor
	public static class Login {
		public String accessToken;
		public String refreshToken;
	}
}
