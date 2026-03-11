package com.truve.platform.musical.show.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class HomeResponse {

	@Getter
	@AllArgsConstructor
	@Builder
	public static class BannerList {
		private List<Banner> banners;
	}

	@Getter
	@AllArgsConstructor
	@Builder
	public static class Banner {
		private Long bannerId;
		private Long showId;
		private String showTitle;
		private String venueName;
		private String date;
		private String posterUrl;
		private Integer displayOrder;
	}
}
