package io.twogether.nbe_5_7_2_02team.chat.dto.response

import io.twogether.nbe_5_7_2_02team.chat.domain.ChatMember
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatMemberStatus

data class ChatMemberGetResponse(
    val memberId: Long?,
    val memberName: String,
    val memberImage: String,
    val chatMemberStatus: ChatMemberStatus,
)

fun ChatMember.toGetResponse(): ChatMemberGetResponse =
    ChatMemberGetResponse(
        memberId = this.member.id,
        memberName = this.member.name,
        memberImage = this.member.profileImage,
        chatMemberStatus = this.chatMemberStatus,
    )
