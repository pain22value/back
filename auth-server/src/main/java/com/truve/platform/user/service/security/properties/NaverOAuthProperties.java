package com.truve.platform.user.service.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.naver")
public class NaverOAuthProperties {
	private String clientId;
	private String clientSecret;
}
