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
import com.truve.platform.musical.board.domain.constant.ArtistBoardCommentAuthorType;
import com.truve.platform.musical.board.domain.constant.ArtistBoardCommentFilter;
import com.truve.platform.musical.board.domain.entity.ArtistBoardComment;
import com.truve.platform.musical.board.domain.entity.ArtistBoardPost;
import com.truve.platform.musical.board.dto.BoardRequest;
import com.truve.platform.musical.board.dto.BoardResponse;
import com.truve.platform.musical.board.repository.ArtistBoardCommentRepository;
import com.truve.platform.musical.board.repository.ArtistBoardPostLikeRepository;
import com.truve.platform.musical.board.repository.ArtistBoardPostRepository;
import com.truve.platform.musical.s3.S3Service;
import com.truve.platform.musical.show.domain.entity.Artist;
import com.truve.platform.musical.show.repository.ArtistRepository;
import com.truve.platform.musical.show.dto.ArtistResponse;
import com.truve.platform.musical.show.service.ArtistService;
import com.truve.platform.musical.user.domain.entity.User;
import com.truve.platform.musical.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtistBoardService {

	private static final int MAX_IMAGE_COUNT = 4;
	private static final String UNKNOWN_MEMBER_NAME = "알 수 없는 사용자";

	private final ArtistBoardPostRepository artistBoardPostRepository;
	private final ArtistBoardPostLikeRepository artistBoardPostLikeRepository;
	private final ArtistBoardCommentRepository artistBoardCommentRepository;
	private final ArtistService artistService;
	private final S3Service s3Service;
	private final UserRepository userRepository;
	private final ArtistRepository artistRepository;

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

	@Transactional(readOnly = true)
	public BoardResponse.CommentList getComments(Long artistId, Long postId, UUID userId, ArtistBoardCommentFilter filter) {
		validateBoardAccessible(artistId, userId);
		ArtistBoardPost post = getPost(artistId, postId);

		List<ArtistBoardComment> comments = getCommentsByFilter(postId, userId, filter);
		Map<UUID, User> usersByUserId = findUsersByUserId(comments);
		Map<Long, Artist> artistsByArtistId = findArtistsByArtistId(comments);

		List<BoardResponse.CommentItem> items = comments.stream()
			.map(comment -> toCommentItem(comment, userId, usersByUserId, artistsByArtistId))
			.toList();

		return BoardResponse.CommentList.builder()
			.summary(BoardResponse.CommentSummary.builder()
				.totalCount(artistBoardCommentRepository.countByPostId(post.getId()))
				.myCount(artistBoardCommentRepository.countByPostIdAndUserId(post.getId(), userId))
				.artistCount(artistBoardCommentRepository.countByPostIdAndAuthorType(post.getId(), ArtistBoardCommentAuthorType.ARTIST))
				.build())
			.comments(items)
			.build();
	}

	@Transactional
	public void createComment(Long artistId, Long postId, UUID userId, BoardRequest.CreateComment request) {
		validateBoardAccessible(artistId, userId);
		ArtistBoardPost post = getPost(artistId, postId);

		ArtistBoardComment comment = ArtistBoardComment.builder()
			.post(post)
			.authorType(ArtistBoardCommentAuthorType.MEMBER)
			.userId(userId)
			.artistId(null)
			.content(request.getContent().trim())
			.build();

		artistBoardCommentRepository.save(comment);
	}

	private void validateBoardAccessible(Long artistId, UUID userId) {
		ArtistResponse.BoardAccess boardAccess = artistService.getBoardAccess(artistId, userId);
		Preconditions.validate(Boolean.TRUE.equals(boardAccess.getAccessible()), ErrorCode.FORBIDDEN_ARTIST_BOARD_ACCESS);
	}

	private ArtistBoardPost getPost(Long artistId, Long postId) {
		return artistBoardPostRepository.findByIdAndArtistId(postId, artistId)
			.orElseThrow(() -> new com.truve.platform.common.exception.CustomException(ErrorCode.NOT_FOUND_ARTIST_BOARD_POST));
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

	private List<ArtistBoardComment> getCommentsByFilter(Long postId, UUID userId, ArtistBoardCommentFilter filter) {
		return switch (filter) {
			case ALL -> artistBoardCommentRepository.findByPostIdOrderByCreatedAtDescIdDesc(postId);
			case MINE -> artistBoardCommentRepository.findByPostIdAndUserIdOrderByCreatedAtDescIdDesc(postId, userId);
			case ARTIST -> artistBoardCommentRepository.findByPostIdAndAuthorTypeOrderByCreatedAtDescIdDesc(
				postId,
				ArtistBoardCommentAuthorType.ARTIST
			);
		};
	}

	private Map<UUID, User> findUsersByUserId(List<ArtistBoardComment> comments) {
		List<UUID> userIds = comments.stream()
			.filter(comment -> comment.getAuthorType() == ArtistBoardCommentAuthorType.MEMBER)
			.map(ArtistBoardComment::getUserId)
			.filter(Objects::nonNull)
			.distinct()
			.toList();

		if (userIds.isEmpty()) {
			return Map.of();
		}

		return userRepository.findByUserIdIn(userIds).stream()
			.collect(Collectors.toMap(User::getUserId, user -> user));
	}

	private Map<Long, Artist> findArtistsByArtistId(List<ArtistBoardComment> comments) {
		List<Long> artistIds = comments.stream()
			.filter(comment -> comment.getAuthorType() == ArtistBoardCommentAuthorType.ARTIST)
			.map(ArtistBoardComment::getArtistId)
			.filter(Objects::nonNull)
			.distinct()
			.toList();

		if (artistIds.isEmpty()) {
			return Map.of();
		}

		return artistRepository.findAllById(artistIds).stream()
			.collect(Collectors.toMap(Artist::getId, artist -> artist));
	}

	private BoardResponse.CommentItem toCommentItem(
		ArtistBoardComment comment,
		UUID userId,
		Map<UUID, User> usersByUserId,
		Map<Long, Artist> artistsByArtistId
	) {
		boolean isArtist = comment.getAuthorType() == ArtistBoardCommentAuthorType.ARTIST;
		boolean isMine = userId != null && userId.equals(comment.getUserId());

		return BoardResponse.CommentItem.builder()
			.commentId(comment.getId())
			.createdAt(comment.getCreatedAt())
			.authorName(resolveAuthorName(comment, usersByUserId, artistsByArtistId))
			.authorThumbnailUrl(resolveAuthorThumbnailUrl(comment, artistsByArtistId))
			.content(comment.getContent())
			.isMine(isMine)
			.isArtist(isArtist)
			.build();
	}

	private String resolveAuthorName(
		ArtistBoardComment comment,
		Map<UUID, User> usersByUserId,
		Map<Long, Artist> artistsByArtistId
	) {
		if (comment.getAuthorType() == ArtistBoardCommentAuthorType.ARTIST) {
			Artist artist = artistsByArtistId.get(comment.getArtistId());
			if (artist != null) {
				return artist.getName();
			}
			return comment.getPost().getArtist().getName();
		}

		User user = usersByUserId.get(comment.getUserId());
		return user != null ? user.getNickname() : UNKNOWN_MEMBER_NAME;
	}

	private String resolveAuthorThumbnailUrl(ArtistBoardComment comment, Map<Long, Artist> artistsByArtistId) {
		if (comment.getAuthorType() != ArtistBoardCommentAuthorType.ARTIST) {
			return null;
		}

		Artist artist = artistsByArtistId.get(comment.getArtistId());
		if (artist != null) {
			return toImageUrl(artist.getProfileImg());
		}

		return toImageUrl(comment.getPost().getArtist().getProfileImg());
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
