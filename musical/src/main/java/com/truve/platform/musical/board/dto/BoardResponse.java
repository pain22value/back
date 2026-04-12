package com.truve.platform.musical.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class BoardResponse {

	@Getter
	@AllArgsConstructor
	@Builder
	public static class PostFeed {
		private List<PostItem> posts;
	}

	@Getter
	@AllArgsConstructor
	@Builder
	public static class PostItem {
		private Long postId;
		private LocalDateTime createdAt;
		private String artistName;
		private String artistThumbnailUrl;
		private String content;
		private List<String> imageUrls;
		private long likeCount;
		private long commentCount;
		private boolean likedByMe;
	}
}
