package io.twogether.nbe_5_7_2_02team.member.dto.response

data class MyPageResponse(
    val id: Long,
    val email: String,
    val name: String,
    val job: String,
    val course: String,
    val profileImage: String?,
    val posts: List<PostSummary>,
    val followerCount: Long,
    val followingCount: Long,
    val following: Boolean,
    val owner: Boolean,
) {
    data class PostSummary(
        val postId: Long,
        val title: String,
    )
}