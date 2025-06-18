package io.twogether.nbe_5_7_2_02team.chat.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "채팅 메시지 등록 요청 DTO")
data class ChatMessagePostRequest(
    @field:Schema(description = "회원 ID", example = "1")
    val memberId: Long = 0L,
    @field:Schema(description = "메시지 내용", example = "안녕하세요!")
    val content: String = "",
)
