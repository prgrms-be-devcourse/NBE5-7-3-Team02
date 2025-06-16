package io.twogether.nbe_5_7_2_02team.chat.api

import io.twogether.nbe_5_7_2_02team.chat.dto.response.ChatRoomGetResponse
import io.twogether.nbe_5_7_2_02team.chat.service.ChatRoomService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chatroom")
class ChatRoomController (
    private val chatRoomService: ChatRoomService
) {

    @GetMapping
    fun chatRoomList(): ResponseEntity<List<ChatRoomGetResponse>?> {
            val chatRoomGetResponse = chatRoomService.getChatRoomList()

            return ResponseEntity.ok<List<ChatRoomGetResponse>?>(chatRoomGetResponse)
    }

    @GetMapping("/{postId}")
    fun getChatRoomByPost(
        @PathVariable("postId") postId: Long
    ): ResponseEntity<ChatRoomGetResponse> {
        val chatRoomGetResponse = chatRoomService.getChatRoomByPost(postId)

        return ResponseEntity.ok<ChatRoomGetResponse>(chatRoomGetResponse)
    }

    @PostMapping("/{postId}")
    fun createChatRoom(
        @PathVariable("postId") postId: Long
    ): ResponseEntity<Long> {
        val chatRoomId = chatRoomService.createChatroom(postId)

        return ResponseEntity.status(HttpStatus.CREATED).body<Long>(chatRoomId)
    }
}
