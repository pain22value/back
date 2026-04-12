package com.truve.platform.musical.board.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.truve.platform.common.exception.ErrorCode;
import com.truve.platform.common.support.Preconditions;
import com.truve.platform.musical.board.domain.entity.ArtistBoardPost;
import com.truve.platform.musical.board.dto.BoardResponse;
import com.truve.platform.musical.board.repository.ArtistBoardCommentRepository;
import com.truve.platform.musical.board.repository.ArtistBoardPostLikeRepository;
import com.truve.platform.musical.board.repository.ArtistBoardPostRepository;
import com.truve.platform.musical.s3.S3Service;
import com.truve.platform.musical.show.dto.ArtistResponse;
import com.truve.platform.musical.show.service.ArtistService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtistBoardService {

	private static final int MAX_IMAGE_COUNT = 4;

	private final ArtistBoardPostRepository artistBoardPostRepository;
	private final ArtistBoardPostLikeRepository artistBoardPostLikeRepository;
	private final ArtistBoardCommentRepository artistBoardCommentRepository;
	private final ArtistService artistService;
	private final S3Service s3Service;

	@Transactional(readOnly = true)
	public BoardResponse.PostFeed getPosts(Long artistId, UUID userId) {
		validateBoardAccessible(artistId, userId);

		List<ArtistBoardPost> posts = artistBoardPostRepository.findByArtistIdOrderByCreatedAtDescIdDesc(artistId);
		if (posts.isEmpty()) {
			return BoardResponse.PostFeed.builder()
				.posts(List.of())
				.build();
		}

		List<Long> postIds = posts.stream()
			.map(ArtistBoardPost::getId)
			.toList();

		Map<Long, Long> likeCounts = artistBoardPostLikeRepository.countLikesByPostIds(postIds).stream()
			.collect(Collectors.toMap(
				ArtistBoardPostLikeRepository.PostLikeCountProjection::getPostId,
				ArtistBoardPostLikeRepository.PostLikeCountProjection::getLikeCount
			));
		Map<Long, Long> commentCounts = artistBoardCommentRepository.countCommentsByPostIds(postIds).stream()
			.collect(Collectors.toMap(
				ArtistBoardCommentRepository.PostCommentCountProjection::getPostId,
				ArtistBoardCommentRepository.PostCommentCountProjection::getCommentCount
			));
		Set<Long> likedPostIds = artistBoardPostLikeRepository.findLikedPostIds(userId, postIds).stream()
			.collect(Collectors.toSet());

		List<BoardResponse.PostItem> items = posts.stream()
			.map(post -> toPostItem(post, likeCounts, commentCounts, likedPostIds))
			.toList();

		return BoardResponse.PostFeed.builder()
			.posts(items)
			.build();
	}

	private void validateBoardAccessible(Long artistId, UUID userId) {
		ArtistResponse.BoardAccess boardAccess = artistService.getBoardAccess(artistId, userId);
		Preconditions.validate(Boolean.TRUE.equals(boardAccess.getAccessible()), ErrorCode.FORBIDDEN_ARTIST_BOARD_ACCESS);
	}

	private BoardResponse.PostItem toPostItem(
		ArtistBoardPost post,
		Map<Long, Long> likeCounts,
		Map<Long, Long> commentCounts,
		Set<Long> likedPostIds
	) {
		Long postId = post.getId();

		return BoardResponse.PostItem.builder()
			.postId(postId)
			.createdAt(post.getCreatedAt())
			.artistName(post.getArtist().getName())
			.artistThumbnailUrl(toImageUrl(post.getArtist().getProfileImg()))
			.content(post.getContent())
			.imageUrls(toImageUrls(post.getImageKeys()))
			.likeCount(likeCounts.getOrDefault(postId, 0L))
			.commentCount(commentCounts.getOrDefault(postId, 0L))
			.likedByMe(likedPostIds.contains(postId))
			.build();
	}

	private String toImageUrl(String imageKey) {
		if (!StringUtils.hasText(imageKey)) {
			return null;
		}
		return s3Service.getImageUrl(imageKey);
	}

	private List<String> toImageUrls(List<String> imageKeys) {
		if (imageKeys == null || imageKeys.isEmpty()) {
			return Collections.emptyList();
		}

		return imageKeys.stream()
			.filter(StringUtils::hasText)
			.limit(MAX_IMAGE_COUNT)
			.map(this::toImageUrl)
			.filter(Objects::nonNull)
			.toList();
	}
}
