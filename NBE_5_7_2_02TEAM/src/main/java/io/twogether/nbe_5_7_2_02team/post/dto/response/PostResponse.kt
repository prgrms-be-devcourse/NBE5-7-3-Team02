package io.twogether.nbe_5_7_2_02team.post.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "게시글 기본 응답")
data class PostResponse(
    @Schema(description = "게시글 ID", example = "123")
    val id: Long,
)
