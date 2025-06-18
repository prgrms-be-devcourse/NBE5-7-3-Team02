package io.twogether.nbe_5_7_2_02team.post.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "게시글 조회 요청 필터")
data class PostGetRequest(
    @Schema(description = "마지막 조회 게시글 ID (페이징용)", example = "100")
    val lastPostId: Long?,

    @Schema(description = "조회할 게시글 수", example = "10", required = true)
    val limit: Int,

    @Schema(description = "모집 중 여부 필터", example = "true")
    val isRecruit: Boolean?,

    @Schema(description = "팔로잉 여부 필터", example = "false")
    val isFollowing: Boolean?,

    @Schema(description = "태그 필터 리스트", example = "[\"Kotlin\", \"Backend\"]")
    val tags: List<String> = listOf(),
)
