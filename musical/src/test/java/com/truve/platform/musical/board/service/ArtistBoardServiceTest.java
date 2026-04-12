package com.truve.platform.musical.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.truve.platform.common.exception.CustomException;
import com.truve.platform.common.exception.ErrorCode;
import com.truve.platform.musical.board.domain.entity.ArtistBoardPost;
import com.truve.platform.musical.board.dto.BoardResponse;
import com.truve.platform.musical.board.repository.ArtistBoardCommentRepository;
import com.truve.platform.musical.board.repository.ArtistBoardPostLikeRepository;
import com.truve.platform.musical.board.repository.ArtistBoardPostRepository;
import com.truve.platform.musical.s3.S3Service;
import com.truve.platform.musical.show.domain.entity.Artist;
import com.truve.platform.musical.show.dto.ArtistResponse;
import com.truve.platform.musical.show.service.ArtistService;

@ExtendWith(MockitoExtension.class)
class ArtistBoardServiceTest {

	private static final UUID USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

	@Mock
	private ArtistBoardPostRepository artistBoardPostRepository;
	@Mock
	private ArtistBoardPostLikeRepository artistBoardPostLikeRepository;
	@Mock
	private ArtistBoardCommentRepository artistBoardCommentRepository;
	@Mock
	private ArtistService artistService;
	@Mock
	private S3Service s3Service;

	@InjectMocks
	private ArtistBoardService artistBoardService;

	@Test
	@DisplayName("게시판 게시글 조회는 최신순 게시글 목록과 좋아요/댓글 수를 함께 응답한다.")
	void 게시판_게시글_조회_성공() {
		ArtistBoardPost post = mock(ArtistBoardPost.class);
		Artist artist = mock(Artist.class);
		ArtistBoardPostLikeRepository.PostLikeCountProjection likeCount = mock(
			ArtistBoardPostLikeRepository.PostLikeCountProjection.class
		);
		ArtistBoardCommentRepository.PostCommentCountProjection commentCount = mock(
			ArtistBoardCommentRepository.PostCommentCountProjection.class
		);

		when(artistService.getBoardAccess(1L, USER_ID)).thenReturn(
			ArtistResponse.BoardAccess.builder()
				.joined(true)
				.accessible(true)
				.build()
		);
		when(artistBoardPostRepository.findByArtistIdOrderByCreatedAtDescIdDesc(1L)).thenReturn(List.of(post));
		when(post.getId()).thenReturn(10L);
		when(post.getCreatedAt()).thenReturn(LocalDateTime.of(2026, 4, 12, 12, 0));
		when(post.getArtist()).thenReturn(artist);
		when(post.getContent()).thenReturn("게시글 내용입니다.");
		when(post.getImageKeys()).thenReturn(List.of("1.png", "2.png", "3.png", "4.png", "5.png"));
		when(artist.getName()).thenReturn("이재환");
		when(artist.getProfileImg()).thenReturn("artists/lee.png");
		when(likeCount.getPostId()).thenReturn(10L);
		when(likeCount.getLikeCount()).thenReturn(7L);
		when(commentCount.getPostId()).thenReturn(10L);
		when(commentCount.getCommentCount()).thenReturn(3L);
		when(artistBoardPostLikeRepository.countLikesByPostIds(List.of(10L))).thenReturn(List.of(likeCount));
		when(artistBoardCommentRepository.countCommentsByPostIds(List.of(10L))).thenReturn(List.of(commentCount));
		when(artistBoardPostLikeRepository.findLikedPostIds(USER_ID, List.of(10L))).thenReturn(List.of(10L));
		when(s3Service.getImageUrl("artists/lee.png")).thenReturn("https://img.example/artists/lee.png");
		when(s3Service.getImageUrl("1.png")).thenReturn("https://img.example/posts/1.png");
		when(s3Service.getImageUrl("2.png")).thenReturn("https://img.example/posts/2.png");
		when(s3Service.getImageUrl("3.png")).thenReturn("https://img.example/posts/3.png");
		when(s3Service.getImageUrl("4.png")).thenReturn("https://img.example/posts/4.png");

		BoardResponse.PostFeed response = artistBoardService.getPosts(1L, USER_ID);

		assertThat(response.getPosts()).hasSize(1);
		assertThat(response.getPosts().get(0).getPostId()).isEqualTo(10L);
		assertThat(response.getPosts().get(0).getArtistName()).isEqualTo("이재환");
		assertThat(response.getPosts().get(0).getArtistThumbnailUrl()).isEqualTo("https://img.example/artists/lee.png");
		assertThat(response.getPosts().get(0).getContent()).isEqualTo("게시글 내용입니다.");
		assertThat(response.getPosts().get(0).getImageUrls()).containsExactly(
			"https://img.example/posts/1.png",
			"https://img.example/posts/2.png",
			"https://img.example/posts/3.png",
			"https://img.example/posts/4.png"
		);
		assertThat(response.getPosts().get(0).getLikeCount()).isEqualTo(7L);
		assertThat(response.getPosts().get(0).getCommentCount()).isEqualTo(3L);
		assertThat(response.getPosts().get(0).isLikedByMe()).isTrue();
		verify(s3Service, never()).getImageUrl("5.png");
	}

	@Test
	@DisplayName("게시판 접근 권한이 없으면 게시글을 조회할 수 없다.")
	void 게시판_게시글_조회_권한없음_실패() {
		when(artistService.getBoardAccess(1L, USER_ID)).thenReturn(
			ArtistResponse.BoardAccess.builder()
				.joined(false)
				.accessible(false)
				.build()
		);

		CustomException exception = assertThrows(
			CustomException.class,
			() -> artistBoardService.getPosts(1L, USER_ID)
		);

		assertEquals(ErrorCode.FORBIDDEN_ARTIST_BOARD_ACCESS, exception.getErrorCode());
		verifyNoInteractions(artistBoardPostRepository, artistBoardPostLikeRepository, artistBoardCommentRepository);
	}
}
