package io.twogether.nbe_5_7_2_02team.member.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "마이페이지 응답 DTO")
data class MyPageResponse(
    @field:Schema(description = "회원 ID", example = "1")
    val id: Long,

    @field:Schema(description = "이메일", example = "user@example.com")
    val email: String,

    @field:Schema(description = "이름", example = "홍길동")
    val name: String,

    @field:Schema(description = "직업", example = "Backend Developer")
    val job: String,

    @field:Schema(description = "수강 과정", example = "SOPT Server")
    val course: String,

    @field:Schema(description = "프로필 이미지 URL", example = "https://image.url/profile.png", nullable = true)
    val profileImage: String?,

    @field:Schema(description = "작성한 게시글 목록")
    val posts: List<PostSummary>,

    @field:Schema(description = "팔로워 수", example = "100")
    val followerCount: Long,

    @field:Schema(description = "팔로잉 수", example = "50")
    val followingCount: Long,

    @field:Schema(description = "팔로잉 여부", example = "true")
    val following: Boolean,

    @field:Schema(description = "본인 여부", example = "true")
    val owner: Boolean,
) {
    @Schema(description = "게시글 요약 정보")
    data class PostSummary(
        @field:Schema(description = "게시글 ID", example = "10")
        val postId: Long,

        @field:Schema(description = "게시글 제목", example = "첫 게시글입니다.")
        val title: String,
    )
}
