package com.truve.platform.musical.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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

	@Getter
	@AllArgsConstructor
	@Builder
	public static class CommentList {
		private CommentSummary summary;
		private List<CommentItem> comments;
	}

	@Getter
	@AllArgsConstructor
	@Builder
	public static class CommentSummary {
		private long totalCount;
		private long myCount;
		private long artistCount;
	}

	@Getter
	@AllArgsConstructor
	@Builder
	public static class CommentItem {
		private Long commentId;
		private LocalDateTime createdAt;
		private String authorName;
		private String authorThumbnailUrl;
		private String content;
		@JsonProperty("isMine")
		private boolean isMine;
		@JsonProperty("isArtist")
		private boolean isArtist;
	}
}
