package io.twogether.nbe_5_7_2_02team.chat.service

import io.twogether.nbe_5_7_2_02team.chat.dao.ChatRoomRepository
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatRoom
import io.twogether.nbe_5_7_2_02team.chat.dto.response.ChatRoomGetResponse
import io.twogether.nbe_5_7_2_02team.chat.dto.response.toGetResponse
import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import io.twogether.nbe_5_7_2_02team.post.dao.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
    private val postRepository: PostRepository,
) {
    @Transactional(readOnly = true)
    fun getChatRoomList(): List<ChatRoomGetResponse> {
        val chatRoomList = chatRoomRepository.findAll()

        return chatRoomList.map { chatRoom -> chatRoom.toGetResponse() }
    }

    @Transactional(readOnly = true)
    fun getChatRoomByPost(postId: Long): ChatRoomGetResponse {
        val post =
            postRepository
                .findById(postId)
                .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_POST) })

        val chatRoom =
            chatRoomRepository
                .findByPost(post) ?: throw ErrorException(ErrorCode.CHAT_ROOM_NOT_FOUND)

        return chatRoom.toGetResponse()
    }

    @Transactional
    fun createChatroom(postId: Long): Long {
        val post =
            postRepository
                .findById(postId)
                .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_POST) })

        chatRoomRepository
            .findByPost(post)
            ?.let { throw ErrorException(ErrorCode.CHAT_ROOM_ALREADY_EXISTS) }

        return chatRoomRepository.save(ChatRoom(post = post)).id!!
    }

    @Transactional
    fun deleteChatroom(id: Long) {
        val chatRoom = checkChatRoomExists(id)

        chatRoomRepository.delete(chatRoom)
    }

    fun checkChatRoomExists(id: Long): ChatRoom =
        chatRoomRepository.findChatRoomById(id) ?: throw ErrorException(ErrorCode.CHAT_ROOM_NOT_FOUND)
}
