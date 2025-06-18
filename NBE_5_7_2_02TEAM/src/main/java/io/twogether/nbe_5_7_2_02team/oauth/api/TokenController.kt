package io.twogether.nbe_5_7_2_02team.oauth.api

import io.swagger.v3.oas.annotations.Operation
import io.twogether.nbe_5_7_2_02team.member.dto.request.SignUpRequest
import io.twogether.nbe_5_7_2_02team.member.dto.response.SignUpResponse
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.MemberDetails
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.TokenPair
import io.twogether.nbe_5_7_2_02team.oauth.dto.request.LogoutRequest
import io.twogether.nbe_5_7_2_02team.oauth.dto.request.RefreshRequest
import io.twogether.nbe_5_7_2_02team.oauth.service.OAuthService
import io.twogether.nbe_5_7_2_02team.oauth.service.TokenService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.tags.Tag


@RestController
@RequestMapping("/api")
@Tag(name = "Token", description = "토큰 관리 API")
class TokenController(
    private val tokenService: TokenService,
    private val oAuthService: OAuthService,
) {
    @PostMapping("/token/refresh")
    @Operation(summary = "액세스토큰 재발급", description = "만료된 액세스 토큰을 갱신하기 위해 리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급합니다.")
    fun refresh(
        @Valid @RequestBody request: RefreshRequest,
    ): ResponseEntity<TokenPair> {
        val newToken = tokenService.refreshToken(request.refreshToken)
        return ResponseEntity.ok(newToken)
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "전달된 리프레시 토큰을 블랙리스트에 등록하여 로그아웃 처리합니다. 이후 해당 토큰은 사용할 수 없습니다.")
    fun logout(
        @Valid @RequestBody request: LogoutRequest,
    ): ResponseEntity<Void> {
        tokenService.invalidateRefreshToken(request.refreshToken)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입 (추가 정보 입력)", description = "소셜 로그인 후 최초 로그인 시 추가 정보를 입력받아 회원가입을 완료합니다.이미 회원가입된 경우 호출하지 않습니다.")
    fun signUp(
        @Valid @RequestBody request: SignUpRequest,
        @AuthenticationPrincipal memberDetails: MemberDetails,
    ): ResponseEntity<SignUpResponse> {
        val response = memberDetails.id?.let { oAuthService.signup(request, it) }
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}
