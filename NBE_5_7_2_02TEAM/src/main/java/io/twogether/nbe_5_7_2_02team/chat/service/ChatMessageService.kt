package io.twogether.nbe_5_7_2_02team.chat.service

import io.twogether.nbe_5_7_2_02team.chat.dao.ChatMemberRepository
import io.twogether.nbe_5_7_2_02team.chat.dao.ChatMessageRepository
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatMessage
import io.twogether.nbe_5_7_2_02team.chat.dto.request.ChatMessagePostRequest
import io.twogether.nbe_5_7_2_02team.chat.dto.response.ChatMessageGetResponse
import io.twogether.nbe_5_7_2_02team.chat.dto.response.toGetResponse
import io.twogether.nbe_5_7_2_02team.chat.util.CheckUserLogin
import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import io.twogether.nbe_5_7_2_02team.member.dao.MemberRepository
import lombok.RequiredArgsConstructor
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

@Service
@RequiredArgsConstructor
class ChatMessageService(
    private val chatRoomService: ChatRoomService,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatMemberRepository: ChatMemberRepository,
    private val memberRepository: MemberRepository,
    private val checkUserLogin: CheckUserLogin,
) {
    @Transactional(readOnly = true)
    fun getChatMessage(chatRoomId: Long): List<ChatMessageGetResponse>? {
        val chatRoom = chatRoomService.checkChatRoomExists(chatRoomId)

        val chatMessageList: List<ChatMessage?> =
            chatMessageRepository.findByChatRoomOrderByCreatedAtAsc(chatRoom)

        return chatMessageList.map { chatMessage -> chatMessage!!.toGetResponse() }
    }

    @Transactional
    fun createChatMessage(
        chatRoomId: Long,
        chatMessagePostRequest: ChatMessagePostRequest,
        memberId: Long?,
    ): ChatMessageGetResponse {
        val member =
            memberRepository
                .findById(memberId)
                .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_MEMBER) })
//                TODO: MemberRepository.java가 마이그레이션 된 후 적용 예정
//                ?: ErrorException(ErrorCode.NOT_FOUND_MEMBER)

        val chatRoom = chatRoomService.checkChatRoomExists(chatRoomId)

        val chatMember = chatMemberRepository.findByChatRoomAndMember(chatRoom, member) ?: throw ErrorException(ErrorCode.CHAT_MEMBER_NOT_ENTER)

        val content = chatMessagePostRequest.content

        if (content.isBlank()) {
            throw ErrorException(ErrorCode.CHAT_MESSAGE_CONTENT_BLANK)
        }

        val chatMessageId =
            chatMessageRepository
                .save(
                    ChatMessage(chatRoom, chatMember, content)
                ).id

        val chatMessage = chatMessageRepository.findById(chatMessageId).orElseThrow()

        chatRoom.lastChatId = chatMessageId

        return chatMessage.toGetResponse()
    }

    @Transactional
    fun deleteChatMessage(
        chatMessageId: Long,
        chatRoomId: Long,
        userDetails: UserDetails?,
    ) {
        val member = checkUserLogin.checkUserLogin(userDetails)

        val chatRoom = chatRoomService.checkChatRoomExists(chatRoomId)

        val chatMember = chatMemberRepository.findByChatRoomAndMember(chatRoom, member) ?: throw ErrorException(ErrorCode.CHAT_MEMBER_NOT_ENTER)

        val chatMessage =
            chatMessageRepository.findByIdAndChatRoomAndChatMember(
                chatMessageId,
                chatRoom,
                chatMember,
            ) ?: throw ErrorException(ErrorCode.CHAT_MESSAGE_NOT_FOUND)

        chatMessageRepository.deleteById(chatMessage.id)
    }
}
