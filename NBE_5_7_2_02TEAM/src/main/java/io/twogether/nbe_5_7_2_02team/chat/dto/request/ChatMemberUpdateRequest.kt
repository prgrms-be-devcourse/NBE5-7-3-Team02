package io.twogether.nbe_5_7_2_02team.chat.dto.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatMemberStatus

data class ChatMemberUpdateRequest
    @JsonCreator constructor(
    @JsonProperty(
        "chatMemberStatus"
    ) val chatMemberStatus: ChatMemberStatus
)
