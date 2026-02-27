package com.truve.platform.performance.service.controller;

import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.truve.platform.common.exception.CustomException;
import com.truve.platform.common.exception.ApiAdvice;
import com.truve.platform.common.exception.ErrorCode;
import com.truve.platform.performance.service.domain.constant.PerformanceScheduleStatus;
import com.truve.platform.performance.service.dto.PerformanceResponse;
import com.truve.platform.performance.service.service.PerformanceService;

@WebMvcTest(controllers = PerformanceController.class)
@org.springframework.context.annotation.Import(ApiAdvice.class)
class PerformanceControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private PerformanceService performanceService;
	@MockitoBean
	private JpaMetamodelMappingContext jpaMetamodelMappingContext;

	@Test
	@DisplayName("공연 상세 조회에 성공하면 200 OK와 상세 정보를 응답한다.")
	void 뮤지컬_상세_조회_성공() throws Exception {
		// given
		PerformanceResponse.Detail response = PerformanceResponse.Detail.builder()
			.performanceId(1L)
			.title("뮤지컬A")
			.description("설명")
			.runtimeMin(120)
			.ageLimit(8)
			.posterUrl("https://img/1.jpg")
			.noticeUrl("https://img/notice.jpg")
			.startTime(LocalDateTime.of(2026, 3, 1, 0, 0))
			.endTime(LocalDateTime.of(2026, 4, 1, 0, 0))
			.venue(
				PerformanceResponse.Venue.builder()
					.venueId(10L)
					.name("예술의전당")
					.address("서울")
					.build()
			)
			.schedules(List.of(
				PerformanceResponse.Schedule.builder()
					.scheduleId(1L)
					.performanceTime(LocalDateTime.of(2026, 3, 2, 19, 30))
					.status(PerformanceScheduleStatus.OPEN.name())
					.castings(List.of(
						PerformanceResponse.Casting.builder()
							.performanceCastId(101L)
							.artistId(501L)
							.artistName("배우A")
							.roleName("찰리")
							.order(1)
							.isLiked(false)
							.build()
					))
					.build()
			))
			.seatGrades(List.of(
				PerformanceResponse.SeatGrade.builder()
					.performanceSeatGradeId(1001L)
					.gradeName("VIP")
					.basePrice(150000)
					.colorCode("#FFD700")
					.build()
			))
			.build();

		given(performanceService.getDetail(anyLong())).willReturn(response);

		// when & then
		mockMvc.perform(get("/api/performances/{performanceId}", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("ok"))
			.andExpect(jsonPath("$.data.performanceId").value(1))
			.andExpect(jsonPath("$.data.noticeUrl").value("https://img/notice.jpg"))
			.andExpect(jsonPath("$.data.schedules[0].castings[0].artistName").value("배우A"))
			.andExpect(jsonPath("$.data.schedules[0].castings[0].isLiked").value(false));
	}

	@Test
	@DisplayName("존재하지 않는 공연을 조회하면 404를 응답한다.")
	void 뮤지컬_상세_조회_실패() throws Exception {
		// given
		willThrow(new CustomException(ErrorCode.NOT_FOUND_PERFORMANCE))
			.given(performanceService).getDetail(999L);

		// when & then
		mockMvc.perform(get("/api/performances/{performanceId}", 999L))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value("CLIENT_ERROR"))
			.andExpect(jsonPath("$.message").exists());
	}
}
