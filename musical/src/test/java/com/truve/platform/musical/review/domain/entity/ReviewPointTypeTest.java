package com.truve.platform.musical.review.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.truve.platform.musical.review.domain.constant.ReviewPointCategory;
import com.truve.platform.musical.review.domain.constant.ReviewPointName;

class ReviewPointTypeTest {

	@Test
	@DisplayName("ReviewPointType을 생성한다.")
	void 리뷰포인트타입_생성_성공() {
		ReviewPointType reviewPointType = ReviewPointType.builder()
			.point(ReviewPointName.TOUCHING)
			.build();

		assertAll(
			() -> assertThat(reviewPointType.getCategory()).isEqualTo(ReviewPointCategory.CHARM),
			() -> assertThat(reviewPointType.getPoint()).isEqualTo("감동"),
			() -> assertThat(reviewPointType.getCode()).isEqualTo("E05"),
			() -> assertThat(reviewPointType.getOrder()).isEqualTo(5L)
		);
	}
}
