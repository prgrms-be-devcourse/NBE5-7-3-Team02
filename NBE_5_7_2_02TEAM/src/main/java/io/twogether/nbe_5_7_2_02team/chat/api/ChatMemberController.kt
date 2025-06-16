package io.twogether.nbe_5_7_2_02team.chat.api

import io.twogether.nbe_5_7_2_02team.chat.dto.request.ChatMemberUpdateRequest
import io.twogether.nbe_5_7_2_02team.chat.dto.response.ChatMemberGetResponse
import io.twogether.nbe_5_7_2_02team.chat.dto.response.ChatRoomGetResponse
import io.twogether.nbe_5_7_2_02team.chat.service.ChatMemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chatroom")
class ChatMemberController (
    private val chatMemberService: ChatMemberService
) {

    @GetMapping("/entered")
    fun getChatRoomListByUser(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<List<ChatRoomGetResponse>?> {
        val chatRoomGetResponse = chatMemberService.getChatRoomListByUser(userDetails)

        return ResponseEntity.ok<List<ChatRoomGetResponse>?>(chatRoomGetResponse)
    }

    @GetMapping("/{chatroomId}/member")
    fun getChatMemberList(
        @PathVariable("chatroomId") chatroomId: Long
    ): ResponseEntity<List<ChatMemberGetResponse>?> {
        val chatMemberGetResponse = chatMemberService.getChatMember(chatroomId)

        return ResponseEntity.ok<List<ChatMemberGetResponse>?>(chatMemberGetResponse)
    }

    @PostMapping("/{chatroomId}/member")
    fun createChatMember(
        @PathVariable("chatroomId") chatroomId: Long,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Long> {
        val chatMember = chatMemberService.createChatMember(chatroomId, userDetails)

        return ResponseEntity.status(HttpStatus.CREATED).body(chatMember)
    }

    @PutMapping("/{chatroomId}/member")
    fun updateChatMember(
        @PathVariable("chatroomId") chatroomId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody chatMemberUpdateRequest: ChatMemberUpdateRequest
    ): ResponseEntity<Long> {
        val chatMember = chatMemberService.updateChatMember(chatroomId, userDetails, chatMemberUpdateRequest.chatMemberStatus)

        return ResponseEntity.ok(chatMember)
    }
}
