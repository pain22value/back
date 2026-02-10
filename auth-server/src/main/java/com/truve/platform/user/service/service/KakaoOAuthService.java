package com.truve.platform.user.service.service;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.truve.platform.user.service.domain.dto.response.OAuthDTO;
import com.truve.platform.user.service.security.properties.KakaoOAuthProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {
	private final KakaoOAuthProperties kakaoOAuthProperties;
	private final RestClient kakaoOauthRestClient;
	private final RestClient kakaoApiRestClient;

	public void login(String code, String error, String errorDescription, String state) {

		OAuthDTO.KakaoLoginResponse kakaoDTO = requestToken(code);

		String kakaoAccessToken = kakaoDTO.getAccessToken();
		OAuthDTO.KakaoUserInfo req = requestUserInfo(kakaoAccessToken);

	}


	private OAuthDTO.KakaoLoginResponse requestToken(String code) {
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("grant_type", "authorization_code");
		form.add("client_id", kakaoOAuthProperties.getClientId());
		form.add("redirect_uri", kakaoOAuthProperties.getRedirectUri());
		form.add("code", code);
		form.add("client_secret", kakaoOAuthProperties.getClientSecret());

		return kakaoOauthRestClient.post()
			.uri("/token")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.body(form)
			.retrieve()
			.body(OAuthDTO.KakaoLoginResponse.class);
	}

	private OAuthDTO.KakaoUserInfo requestUserInfo(String accessToken) {
		System.out.println("Sadf");
		return kakaoApiRestClient.post()
			.uri("/user/me")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.header("Authorization", "Bearer " + accessToken)
			.body("property_keys=[\"kakao_account.email\"]")
			.retrieve()
			.body(OAuthDTO.KakaoUserInfo.class);
	}

}
