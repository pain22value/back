package com.truve.platform.musical.show.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truve.platform.common.response.ApiResult;
import com.truve.platform.musical.show.dto.HomeResponse;
import com.truve.platform.musical.show.service.HomeService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/musical/home")
public class HomeController {

	private final HomeService homeService;

	@Operation(summary = "홈 배너 조회", description = "홈 화면 상단 배너 목록을 조회합니다.")
	@GetMapping("/banners")
	public ApiResult<HomeResponse.BannerList> getHomeBanners() {
		return ApiResult.ok(homeService.getHomeBanners());
	}
}
