package io.twogether.nbe_5_7_2_02team.member.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "회원 생성 응답 DTO")
data class MemberCreateResponse(
    @field:Schema(description = "회원 ID", example = "1")
    val id: Long,

    @field:Schema(description = "이메일", example = "user@example.com")
    val email: String,

    @field:Schema(description = "이름", example = "홍길동")
    val name: String,

    @field:Schema(description = "프로필 이미지 URL", example = "https://image.url/profile.png", nullable = true)
    val profileImage: String?,

    @field:Schema(description = "직업", example = "Backend Developer")
    val job: String,

    @field:Schema(description = "수강 과정", example = "SOPT Server")
    val course: String,

    @field:Schema(description = "GitHub ID", example = "gildong123")
    val githubId: String,
)
