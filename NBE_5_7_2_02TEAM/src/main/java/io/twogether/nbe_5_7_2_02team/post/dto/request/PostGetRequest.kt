package io.twogether.nbe_5_7_2_02team.post.dto.request

data class PostGetRequest (
    val lastPostId: Long?,
    val limit: Int,
    val isRecruit: Boolean?,
    val isFollowing: Boolean?,
    val tags: List<String> = listOf()
)
