package com.truve.platform.musical.review.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.truve.platform.musical.review.domain.constant.ReviewPointName;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ReviewResponse {

	@Getter
	@AllArgsConstructor
	public static class ReviewItem {
		private Long reviewId;
		private UUID userId;
		private String userNickname;
		private String title;
		private String content;
		private boolean isPositive;
		private LocalDateTime createdAt;
	}

	@Getter
	@AllArgsConstructor
	public static class PointScore {
		private ReviewPointName name;
		private String label;
		private Long score;
	}

	@Getter
	@AllArgsConstructor
	public static class Search {
		private Long weeklyRanking;
		private Long truveScore;
		private Long showId;
		private List<PointScore> charmPointScores;
		private List<PointScore> emotionPointScores;
	}


}
