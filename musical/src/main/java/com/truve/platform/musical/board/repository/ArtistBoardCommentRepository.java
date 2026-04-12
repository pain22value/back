package com.truve.platform.musical.board.repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.truve.platform.musical.board.domain.constant.ArtistBoardCommentAuthorType;
import com.truve.platform.musical.board.domain.entity.ArtistBoardComment;

public interface ArtistBoardCommentRepository extends JpaRepository<ArtistBoardComment, Long> {

	interface PostCommentCountProjection {
		Long getPostId();

		long getCommentCount();
	}

	@Query("""
		select
			c.post.id as postId,
			count(c.id) as commentCount
		from ArtistBoardComment c
		where c.post.id in :postIds
		group by c.post.id
		""")
	List<PostCommentCountProjection> countCommentsByPostIds(@Param("postIds") Collection<Long> postIds);

	@EntityGraph(attributePaths = {"post", "post.artist"})
	List<ArtistBoardComment> findByPostIdOrderByCreatedAtDescIdDesc(Long postId);

	@EntityGraph(attributePaths = {"post", "post.artist"})
	List<ArtistBoardComment> findByPostIdAndUserIdOrderByCreatedAtDescIdDesc(Long postId, UUID userId);

	@EntityGraph(attributePaths = {"post", "post.artist"})
	List<ArtistBoardComment> findByPostIdAndAuthorTypeOrderByCreatedAtDescIdDesc(
		Long postId,
		ArtistBoardCommentAuthorType authorType
	);

	long countByPostId(Long postId);

	long countByPostIdAndUserId(Long postId, UUID userId);

	long countByPostIdAndAuthorType(Long postId, ArtistBoardCommentAuthorType authorType);
}
