package com.truve.platform.auth.service.service.social;

import com.truve.platform.common.constants.AuthProvider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialRegistrationInfo {
	private AuthProvider provider;
	private String providerUserId;
	private String email;
	private String oAuthAccessToken;
	private String oAuthRefreshToken;
}
