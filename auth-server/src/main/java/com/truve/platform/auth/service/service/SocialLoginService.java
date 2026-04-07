package com.truve.platform.auth.service.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.truve.platform.auth.service.domain.entity.User;
import com.truve.platform.auth.service.repository.SocialRegistrationRepository;
import com.truve.platform.auth.service.repository.UserRepository;
import com.truve.platform.auth.service.security.JwtService;
import com.truve.platform.auth.service.security.TokenType;
import com.truve.platform.auth.service.service.social.OAuthProviderClient;
import com.truve.platform.auth.service.service.social.OAuthTokenInfo;
import com.truve.platform.auth.service.service.social.OAuthUserInfo;
import com.truve.platform.auth.service.service.social.SocialLoginResult;
import com.truve.platform.auth.service.service.social.SocialRegistrationInfo;
import com.truve.platform.common.constants.AuthProvider;
import com.truve.platform.common.exception.CustomException;
import com.truve.platform.common.exception.ErrorCode;
import com.truve.platform.common.support.Preconditions;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

	private final List<OAuthProviderClient> oauthProviderClients;
	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final RefreshTokenService refreshTokenService;
	private final SocialRegistrationRepository socialRegistrationRepository;
	private Map<AuthProvider, OAuthProviderClient> providerClients;

	@PostConstruct
	void initializeProviderClients() {
		this.providerClients = oauthProviderClients.stream()
			.collect(Collectors.toMap(OAuthProviderClient::supports, Function.identity()));
	}

	@Transactional
	public SocialLoginResult start(AuthProvider provider, String code, String state) {
		OAuthProviderClient providerClient = providerClients.get(provider);
		Preconditions.validate(providerClient != null, ErrorCode.INVALID_AUTH_PROVIDER);

		OAuthTokenInfo tokenInfo = providerClient.exchangeToken(code, state);
		Preconditions.validate(tokenInfo != null, ErrorCode.NOT_FOUND_EMAIL);

		OAuthUserInfo userInfo = providerClient.getUserInfo(tokenInfo.getAccessToken());
		Preconditions.validate(userInfo != null && userInfo.getEmail() != null, ErrorCode.NOT_FOUND_EMAIL);

		if (userRepository.existsByEmail(userInfo.getEmail())) {
			User user = userRepository.findByEmailOrThrow(userInfo.getEmail());
			validateNotWithdrawn(user);
			return issueLoginResult(user);
		}

		String registrationToken = UUID.randomUUID().toString();
		socialRegistrationRepository.save(
			registrationToken,
			new SocialRegistrationInfo(
				provider,
				userInfo.getProviderUserId(),
				userInfo.getEmail(),
				tokenInfo.getAccessToken(),
				tokenInfo.getRefreshToken()
			)
		);

		return SocialLoginResult.signUpRequired(registrationToken, userInfo.getEmail(), provider);
	}

	private SocialLoginResult issueLoginResult(User user) {
		Date accessExp = jwtService.getAccessExpiration();
		Date refreshExp = jwtService.getRefreshExpiration();

		String accessToken = jwtService.issue(
			user.getPublicId(),
			user.getId(),
			user.getEmail(),
			user.getRole(),
			accessExp,
			TokenType.ACCESS_TOKEN.getType()
		);

		String refreshToken = jwtService.issue(
			user.getPublicId(),
			user.getId(),
			user.getEmail(),
			user.getRole(),
			refreshExp,
			TokenType.REFRESH_TOKEN.getType()
		);

		long refreshTtlMs = refreshExp.getTime() - System.currentTimeMillis();
		refreshTokenService.save(user.getPublicId(), refreshToken, refreshTtlMs);

		return SocialLoginResult.loginSuccess(accessToken, refreshToken, user.getProvider());
	}

	private void validateNotWithdrawn(User user) {
		if (user.isWithdrawn()) {
			throw new CustomException(ErrorCode.ALREADY_WITHDRAWN_USER);
		}
	}
}
