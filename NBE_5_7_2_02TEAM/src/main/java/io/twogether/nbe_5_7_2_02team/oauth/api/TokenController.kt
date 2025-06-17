package io.twogether.nbe_5_7_2_02team.oauth.api

import io.twogether.nbe_5_7_2_02team.member.dto.request.SignUpRequest
import io.twogether.nbe_5_7_2_02team.member.dto.response.SignUpResponse
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.MemberDetails
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.TokenPair
import io.twogether.nbe_5_7_2_02team.oauth.dto.request.LogoutRequest
import io.twogether.nbe_5_7_2_02team.oauth.dto.request.RefreshRequest
import io.twogether.nbe_5_7_2_02team.oauth.service.OAuthService
import io.twogether.nbe_5_7_2_02team.oauth.service.TokenService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class TokenController(
    private val tokenService: TokenService,
    private val oAuthService: OAuthService,
) {
    @PostMapping("/token/refresh")
    fun refresh(
        @RequestBody request: RefreshRequest,
    ): ResponseEntity<TokenPair> {
        val newToken = tokenService.refreshToken(request.refreshToken)
        return ResponseEntity.ok(newToken)
    }

    @PostMapping("/logout")
    fun logout(
        @RequestBody request: LogoutRequest,
    ): ResponseEntity<Void> {
        tokenService.invalidateRefreshToken(request.refreshToken)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/signup")
    fun signUp(
        @RequestBody request: SignUpRequest,
        @AuthenticationPrincipal memberDetails: MemberDetails,
    ): ResponseEntity<SignUpResponse> {
        val response = memberDetails.id?.let { oAuthService.signup(request, it) }
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}
