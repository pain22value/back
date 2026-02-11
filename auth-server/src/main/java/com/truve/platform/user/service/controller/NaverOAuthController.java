package com.truve.platform.user.service.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truve.platform.user.service.security.properties.NaverOAuthProperties;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/naver")
public class NaverOAuthController {
	private final NaverOAuthProperties naverOAuthProperties;

	@GetMapping("/login")
	public ResponseEntity<Void> login() {
		String redirectUri =
			"https://nid.naver.com/oauth2.0/authorize"
				+ "?response_type=code&client_id=" + naverOAuthProperties.getClientId()
				// TODO: 유저 별 랜덤 문자열 레디스 저장 후 CSRF 방지
				+ "&state=" + UUID.randomUUID().toString()
				+ "&redirect_uri=" + "http://localhost:8081/test/callback";

		return ResponseEntity
			.status(HttpStatus.FOUND)
			.location(URI.create(redirectUri))
			.build();
	}

}
