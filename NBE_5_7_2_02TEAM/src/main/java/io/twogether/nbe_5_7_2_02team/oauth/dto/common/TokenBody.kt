package io.twogether.nbe_5_7_2_02team.oauth.dto.common

import io.swagger.v3.oas.annotations.media.Schema
import io.twogether.nbe_5_7_2_02team.member.domain.Role

@Schema(description = "토큰에 포함된 사용자 정보")
data class TokenBody(
    @Schema(description = "사용자 ID", example = "12345")
    val memberId: Long,

    @Schema(description = "사용자 권한", example = "MEMBER")
    val role: Role,
)
