package io.twogether.nbe_5_7_2_02team.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "회원 가입 요청 DTO")
data class SignUpRequest(
    @field:Schema(description = "회원 이름", example = "홍길동")
    val name: String,
    @field:Schema(description = "직업", example = "Backend Developer")
    val job: String,
    @field:Schema(description = "수강 과정", example = "SOPT Server")
    val course: String,
)
