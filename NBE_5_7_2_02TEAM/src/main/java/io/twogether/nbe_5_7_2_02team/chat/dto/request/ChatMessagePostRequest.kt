package io.twogether.nbe_5_7_2_02team.chat.dto.request

data class ChatMessagePostRequest (
    val memberId: Long = 0L,
    val content: String = ""
)
