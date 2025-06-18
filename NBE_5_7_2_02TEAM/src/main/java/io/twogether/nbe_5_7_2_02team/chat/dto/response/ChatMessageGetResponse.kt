package io.twogether.nbe_5_7_2_02team.chat.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatMessage
import java.time.LocalDateTime

@Schema(description = "채팅 메시지 조회 응답 DTO")
data class ChatMessageGetResponse(
    @field:Schema(description = "메시지 ID", example = "1")
    val id: Long,
    @field:Schema(description = "회원 ID", example = "1")
    val memberId: Long,
    @field:Schema(description = "회원 이름", example = "홍길동")
    val memberName: String,
    @field:Schema(description = "메시지 내용", example = "안녕하세요!")
    val content: String,
    @field:Schema(description = "메시지 생성 시간", example = "2025-06-18T15:30:00")
    val createdAt: LocalDateTime,
)

fun ChatMessage.toGetResponse(): ChatMessageGetResponse =
    ChatMessageGetResponse(
        id = this.id,
        memberId = this.chatMember.member.id!!,
        memberName = this.chatMember.member.name,
        content = this.content,
        createdAt = this.createdAt,
    )
