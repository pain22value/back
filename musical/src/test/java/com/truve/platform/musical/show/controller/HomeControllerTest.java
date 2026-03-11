package com.truve.platform.musical.show.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.truve.platform.common.exception.ApiAdvice;
import com.truve.platform.musical.MusicalApplication;
import com.truve.platform.musical.show.dto.HomeResponse;
import com.truve.platform.musical.show.service.HomeService;

@WebMvcTest(controllers = HomeController.class)
@org.springframework.context.annotation.Import(ApiAdvice.class)
@ContextConfiguration(classes = MusicalApplication.class)
class HomeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private HomeService homeService;
	@MockitoBean
	private JpaMetamodelMappingContext jpaMetamodelMappingContext;

	@Test
	@DisplayName("홈 배너 조회에 성공하면 200과 배너 목록을 응답한다.")
	void 홈_배너_조회_성공() throws Exception {
		HomeResponse.BannerList response = HomeResponse.BannerList.builder()
			.banners(List.of(
				HomeResponse.Banner.builder()
					.bannerId(11L)
					.showId(1L)
					.showTitle("Wicked")
					.venueName("샤롯데씨어터")
					.date("2026.03.01 - 2026.05.31")
					.posterUrl("https://img.example/home/banner1.jpg")
					.displayOrder(1)
					.build()
			))
			.build();

		given(homeService.getHomeBanners()).willReturn(response);

		mockMvc.perform(get("/api/musical/home/banners"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("ok"))
			.andExpect(jsonPath("$.data.banners[0].bannerId").value(11))
			.andExpect(jsonPath("$.data.banners[0].showId").value(1))
			.andExpect(jsonPath("$.data.banners[0].showTitle").value("Wicked"))
			.andExpect(jsonPath("$.data.banners[0].venueName").value("샤롯데씨어터"))
			.andExpect(jsonPath("$.data.banners[0].date").value("2026.03.01 - 2026.05.31"))
			.andExpect(jsonPath("$.data.banners[0].posterUrl").value("https://img.example/home/banner1.jpg"));
	}
}
