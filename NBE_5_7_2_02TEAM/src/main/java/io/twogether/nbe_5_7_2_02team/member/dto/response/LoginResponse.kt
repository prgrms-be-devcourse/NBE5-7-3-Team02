package io.twogether.nbe_5_7_2_02team.member.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import io.twogether.nbe_5_7_2_02team.member.domain.Role
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.TokenPair

@Schema(description = "로그인 응답 DTO")
data class LoginResponse(
    @field:Schema(description = "액세스 및 리프레시 토큰 쌍")
    val tokenPair: TokenPair,

    @field:Schema(description = "회원 권한", example = "USER")
    val role: Role,

    @field:Schema(description = "회원 ID", example = "1")
    val memberId: Long,
)
