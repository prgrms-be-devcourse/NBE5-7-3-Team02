package io.twogether.nbe_5_7_2_02team.member.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "회원 수정 응답 DTO")
data class MemberUpdateResponse(
    @field:Schema(description = "회원 ID", example = "1")
    val id: Long,
    @field:Schema(description = "이름", example = "홍길동")
    val name: String,
    @field:Schema(description = "프로필 이미지 URL", example = "https://image.url/profile.png", nullable = true)
    val profileImage: String?,
    @field:Schema(description = "팔로워 수", example = "100")
    val followerCount: Long,
    @field:Schema(description = "팔로잉 수", example = "50")
    val followingCount: Long,
    @field:Schema(description = "팔로잉 여부", example = "true")
    val following: Boolean,
    @field:Schema(description = "본인 여부", example = "true")
    val owner: Boolean,
)
