package io.twogether.nbe_5_7_2_02team.member.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.twogether.nbe_5_7_2_02team.member.dto.request.UpdateProfileRequest
import io.twogether.nbe_5_7_2_02team.member.dto.response.MemberUpdateResponse
import io.twogether.nbe_5_7_2_02team.member.dto.response.MyPageResponse
import io.twogether.nbe_5_7_2_02team.member.service.MemberService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/member")
@Tag(name = "Member", description = "회원 관련 API")
class MemberController(
    private val memberService: MemberService,
) {
    // 내 프로필 조회
    @GetMapping(value = ["/me"])
    @Operation(
        summary = "내 프로필 조회",
        description = "현재 로그인한 사용자의 마이페이지(프로필) 정보를 조회합니다."
    )
    fun getMyProfile(
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<MyPageResponse> {
        val response =
            memberService.getMemberPage(
                userDetails.username.toLong(),
                userDetails.username.toLong(),
            )
        return ResponseEntity.ok(response)
    }

    // 내 프로필 수정
    @PatchMapping(value = ["/me"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "내 프로필 수정",
        description = "현재 로그인한 사용자의 닉네임 또는 프로필 이미지를 수정합니다."
    )
    fun updateMyProfile(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestPart("nickname") nickname: String,
        @RequestPart(value = "image", required = false) image: MultipartFile,
    ): ResponseEntity<MemberUpdateResponse> {
        val request = UpdateProfileRequest(image, nickname)
        val memberId = userDetails.username.toLong()

        val response = memberService.updateProfile(memberId, request)
        return ResponseEntity.ok(response)
    }

    // 상대방 프로필 조회
    @GetMapping(value = ["/{memberId}"])
    @Operation(
        summary = "상대방 프로필 조회",
        description = "지정한 회원의 마이페이지(프로필) 정보를 조회합니다."
    )
    fun getOtherProfile(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable memberId: Long,
    ): ResponseEntity<MyPageResponse> {
        val response =
            memberService.getMemberPage(memberId, userDetails.username.toLong())

        return ResponseEntity.ok(response)
    }
}
