package io.twogether.nbe_5_7_2_02team.post.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "게시글 목록 응답")
data class PostGetResponse(
    @field:Schema(description = "게시글 리스트")
    val posts: List<PostGetResult>,
)
