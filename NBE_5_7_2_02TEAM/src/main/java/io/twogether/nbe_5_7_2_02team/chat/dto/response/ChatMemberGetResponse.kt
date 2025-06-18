package io.twogether.nbe_5_7_2_02team.chat.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatMemberStatus

@Schema(description = "채팅 멤버 조회 응답 DTO")
data class ChatMemberGetResponse(
    @field:Schema(description = "회원 ID", example = "1")
    val memberId: Long,

    @field:Schema(description = "회원 이름", example = "홍길동")
    val memberName: String,

    @field:Schema(description = "회원 프로필 이미지 URL", example = "https://image.url/profile.png", nullable = true)
    val memberImage: String?,

    @field:Schema(description = "채팅 멤버 상태", example = "ACTIVE")
    val chatMemberStatus: ChatMemberStatus,
)
