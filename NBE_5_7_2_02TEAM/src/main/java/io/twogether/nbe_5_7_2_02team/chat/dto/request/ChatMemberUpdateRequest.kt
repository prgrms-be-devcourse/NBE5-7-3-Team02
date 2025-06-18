package io.twogether.nbe_5_7_2_02team.chat.dto.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatMemberStatus

@Schema(description = "채팅 멤버 상태 수정 요청 DTO")
data class ChatMemberUpdateRequest
    @JsonCreator
    constructor(
        @field:Schema(description = "채팅 멤버 상태", example = "ACTIVE")
        @JsonProperty("chatMemberStatus")
        val chatMemberStatus: ChatMemberStatus,
    )
