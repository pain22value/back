package com.truve.platform.musical.board.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truve.platform.common.response.ApiResult;
import com.truve.platform.musical.board.dto.BoardResponse;
import com.truve.platform.musical.board.service.ArtistBoardService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/musical/artists/{artistId}/board/posts")
public class ArtistBoardController {

	private final ArtistBoardService artistBoardService;

	@Operation(summary = "아티스트 게시판 게시글 조회", description = "멤버십 가입 사용자가 아티스트 게시판 게시글 목록을 최신순으로 조회합니다.")
	@GetMapping
	public ApiResult<BoardResponse.PostFeed> getPosts(
		@PathVariable Long artistId,
		@RequestHeader(name = "X-User-Id", required = false) UUID userId
	) {
		return ApiResult.ok(artistBoardService.getPosts(artistId, userId));
	}
}
