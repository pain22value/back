package com.truve.platform.musical.board.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.truve.platform.common.exception.ApiAdvice;
import com.truve.platform.common.exception.CustomException;
import com.truve.platform.common.exception.ErrorCode;
import com.truve.platform.musical.MusicalApplication;
import com.truve.platform.musical.board.dto.BoardResponse;
import com.truve.platform.musical.board.service.ArtistBoardService;

@WebMvcTest(controllers = ArtistBoardController.class)
@org.springframework.context.annotation.Import(ApiAdvice.class)
@ContextConfiguration(classes = MusicalApplication.class)
class ArtistBoardControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ArtistBoardService artistBoardService;

	@MockitoBean
	private JpaMetamodelMappingContext jpaMetamodelMappingContext;

	@Test
	@DisplayName("게시판 게시글 조회에 성공하면 200 OK와 게시글 목록을 응답한다.")
	void 게시판_게시글_조회_성공() throws Exception {
		UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
		BoardResponse.PostFeed response = BoardResponse.PostFeed.builder()
			.posts(List.of(
				BoardResponse.PostItem.builder()
					.postId(10L)
					.createdAt(LocalDateTime.of(2026, 4, 12, 12, 0))
					.artistName("이재환")
					.artistThumbnailUrl("https://img.example/artist.png")
					.content("게시글 내용")
					.imageUrls(List.of("https://img.example/post-1.png", "https://img.example/post-2.png"))
					.likeCount(7L)
					.commentCount(3L)
					.likedByMe(true)
					.build()
			))
			.build();

		given(artistBoardService.getPosts(1L, userId)).willReturn(response);

		mockMvc.perform(get("/api/musical/artists/{artistId}/board/posts", 1L)
				.header("X-User-Id", userId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("ok"))
			.andExpect(jsonPath("$.data.posts[0].postId").value(10))
			.andExpect(jsonPath("$.data.posts[0].artistName").value("이재환"))
			.andExpect(jsonPath("$.data.posts[0].imageUrls[0]").value("https://img.example/post-1.png"))
			.andExpect(jsonPath("$.data.posts[0].likeCount").value(7))
			.andExpect(jsonPath("$.data.posts[0].commentCount").value(3))
			.andExpect(jsonPath("$.data.posts[0].likedByMe").value(true));
	}

	@Test
	@DisplayName("게시판 접근 권한이 없으면 403을 응답한다.")
	void 게시판_게시글_조회_권한없음_실패() throws Exception {
		willThrow(new CustomException(ErrorCode.FORBIDDEN_ARTIST_BOARD_ACCESS))
			.given(artistBoardService).getPosts(anyLong(), nullable(UUID.class));

		mockMvc.perform(get("/api/musical/artists/{artistId}/board/posts", 1L))
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$.errorType").value("CLIENT_ERROR"))
			.andExpect(jsonPath("$.code").value("M05"));
	}

	@Test
	@DisplayName("존재하지 않는 아티스트 게시판 게시글 조회는 404를 응답한다.")
	void 게시판_게시글_조회_아티스트없음_실패() throws Exception {
		willThrow(new CustomException(ErrorCode.NOT_FOUND_ARTIST))
			.given(artistBoardService).getPosts(anyLong(), nullable(UUID.class));

		mockMvc.perform(get("/api/musical/artists/{artistId}/board/posts", 999L))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.errorType").value("CLIENT_ERROR"))
			.andExpect(jsonPath("$.code").value("M02"));
	}
}
