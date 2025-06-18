package io.twogether.nbe_5_7_2_02team.chat.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Chat", description = "채팅 멤버 API")
class ChatMemberController(
    private val chatMemberService: ChatMemberService,
) {
    @GetMapping("/entered")
    @Operation(
        summary = "내가 참여한 채팅방 목록 조회",
        description = "현재 로그인된 사용자가 참여 중인 모든 채팅방 목록을 반환합니다.",
    )
    fun getChatRoomListByUser(
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<List<ChatRoomGetResponse>> {
        val chatRoomGetResponse = chatMemberService.getChatRoomListByUser(userDetails)

        return ResponseEntity.ok(chatRoomGetResponse)
    }

    @GetMapping("/{chatroomId}/member")
    @Operation(
        summary = "채팅방 멤버 목록 조회",
        description = "지정한 채팅방에 참여하고 있는 모든 멤버의 정보를 조회합니다.",
    )
    fun getChatMemberList(
        @PathVariable("chatroomId") chatroomId: Long,
    ): ResponseEntity<List<ChatMemberGetResponse>> {
        val chatMemberGetResponse = chatMemberService.getChatMember(chatroomId)

        return ResponseEntity.ok(chatMemberGetResponse)
    }

    @PostMapping("/{chatroomId}/member")
    @Operation(
        summary = "채팅방 참여",
        description = "로그인한 사용자가 지정한 채팅방에 참여합니다.",
    )
    fun createChatMember(
        @PathVariable("chatroomId") chatroomId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<Long> {
        val chatMember = chatMemberService.createChatMember(chatroomId, userDetails)

        return ResponseEntity.status(HttpStatus.CREATED).body(chatMember)
    }

    @PutMapping("/{chatroomId}/member")
    @Operation(
        summary = "채팅 멤버 상태 수정",
        description = "지정한 채팅방에서 현재 사용자의 채팅 멤버 상태(예: OFFLINE/ONLINE 등)를 변경합니다.",
    )
    fun updateChatMember(
        @PathVariable("chatroomId") chatroomId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody chatMemberUpdateRequest: ChatMemberUpdateRequest,
    ): ResponseEntity<Long> {
        val chatMember = chatMemberService.updateChatMember(chatroomId, userDetails, chatMemberUpdateRequest.chatMemberStatus)

        return ResponseEntity.ok(chatMember)
    }
}
