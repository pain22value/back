package com.truve.platform.performance.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truve.platform.common.response.ApiResult;
import com.truve.platform.performance.service.dto.PerformanceResponse;
import com.truve.platform.performance.service.service.PerformanceService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/performances")
public class PerformanceController {

	private final PerformanceService performanceService;

	@Operation(summary = "공연 상세 조회", description = "공연 상세 정보를 조회합니다.")
	@GetMapping("/{performanceId}")
	public ApiResult<PerformanceResponse.Detail> getDetail(@PathVariable Long performanceId) {
		return ApiResult.ok(performanceService.getDetail(performanceId));
	}
}
