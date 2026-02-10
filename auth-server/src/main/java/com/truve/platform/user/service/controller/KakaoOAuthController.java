package com.truve.platform.user.service.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truve.platform.user.service.security.properties.KakaoOAuthProperties;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth/kakao")
@RequiredArgsConstructor
public class KakaoOAuthController {
	private final KakaoOAuthProperties kakaoOAuthProperties;

	@GetMapping("/login")
	public ResponseEntity<Void> login() {
		String redirectUri =
			kakaoOAuthProperties.getAuthorizationUrl()
				+ "?response_type=code&client_id=" + kakaoOAuthProperties.getClientId()
				+ "&redirect_uri=" + kakaoOAuthProperties.getRedirectUri();

		return ResponseEntity
			.status(HttpStatus.FOUND)
			.location(URI.create(redirectUri))
			.build();
	}


}
