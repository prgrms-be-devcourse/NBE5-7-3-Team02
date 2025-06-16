package io.twogether.nbe_5_7_2_02team.chat.service

import io.twogether.nbe_5_7_2_02team.chat.dao.ChatMemberRepository
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatMember
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatMemberStatus
import io.twogether.nbe_5_7_2_02team.chat.dto.response.ChatMemberGetResponse
import io.twogether.nbe_5_7_2_02team.chat.dto.response.ChatRoomGetResponse
import io.twogether.nbe_5_7_2_02team.chat.dto.response.toGetResponse
import io.twogether.nbe_5_7_2_02team.chat.util.CheckUserLogin
import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import lombok.RequiredArgsConstructor
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class ChatMemberService (
    private val chatRoomService: ChatRoomService,
    private val chatMemberRepository: ChatMemberRepository,
    private val checkUserLogin: CheckUserLogin,
) {

    @Transactional(readOnly = true)
    fun getChatRoomListByUser(
        @AuthenticationPrincipal userDetails: UserDetails?
    ): List<ChatRoomGetResponse>? {
        val member = checkUserLogin.checkUserLogin(userDetails)

        val joinedChatRoomList: List<ChatMember?> =
            chatMemberRepository.findByMemberAndChatMemberStatusIn(
                member,
                listOf(ChatMemberStatus.ONLINE, ChatMemberStatus.OFFLINE)
            )

        return joinedChatRoomList.map { chatMember -> chatMember?.chatRoom!!.toGetResponse() }
    }

    @Transactional(readOnly = true)
    fun getChatMember(chatroomId: Long): List<ChatMemberGetResponse?> {
        val chatRoom = chatRoomService.checkChatRoomExists(chatroomId)

        val chatMemberList: List<ChatMember?> =
            chatMemberRepository.findByChatRoom(chatRoom)

        if (chatMemberList.isEmpty()) {
            throw ErrorException(ErrorCode.CHAT_ROOM_EMPTY)
        }

        return chatMemberList.map { chatMember -> chatMember?.toGetResponse() }
    }

    @Transactional
    fun createChatMember(chatroomId: Long, userDetails: UserDetails?): Long? {
        val member = checkUserLogin.checkUserLogin(userDetails)

        val chatRoom = chatRoomService.checkChatRoomExists(chatroomId)

        val chatMember = chatMemberRepository.findByChatRoomAndMember(chatRoom, member)

        if (chatMember != null) {
            if (chatMember.chatMemberStatus == ChatMemberStatus.LEFT) {
                chatMember.chatMemberStatus = ChatMemberStatus.ONLINE
            }

            return chatMember.id
        }

        val id =
            chatMemberRepository
                .save(
                    ChatMember.builder()
                        .chatRoom(chatRoom)
                        .member(member)
                        .chatMemberStatus(ChatMemberStatus.ONLINE)
                        .build()
                )
                .id

        val size = chatMemberRepository.countByChatRoom(chatRoom)

        chatRoom.memberCount = size

        return id
    }

    @Transactional
    fun updateChatMember(
        chatroomId: Long,
        userDetails: UserDetails?,
        chatMemberStatus: ChatMemberStatus
    ): Long? {
        val member = checkUserLogin.checkUserLogin(userDetails)

        val chatRoom = chatRoomService.checkChatRoomExists(chatroomId)

        val chatMember = chatMemberRepository.findByChatRoomAndMember(chatRoom, member)

        if (chatMember == null) {
            throw ErrorException(ErrorCode.CHAT_MEMBER_NOT_ENTER)
        }

        chatMember.chatMemberStatus = chatMemberStatus

        return chatMemberRepository.save(chatMember).id
    }
}
