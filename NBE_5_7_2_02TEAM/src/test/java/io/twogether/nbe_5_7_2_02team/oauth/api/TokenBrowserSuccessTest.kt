package io.twogether.nbe_5_7_2_02team.oauth.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.database.rider.core.api.dataset.DataSet
import io.twogether.nbe_5_7_2_02team.browser.template.BrowserTestTemplate
import io.twogether.nbe_5_7_2_02team.global.annotation.FlywayReset
import io.twogether.nbe_5_7_2_02team.member.dao.MemberRepository
import io.twogether.nbe_5_7_2_02team.member.dto.request.SignUpRequest
import io.twogether.nbe_5_7_2_02team.oauth.dao.RefreshTokenBlackListRepository
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.TokenPair
import io.twogether.nbe_5_7_2_02team.oauth.dto.request.LogoutRequest
import io.twogether.nbe_5_7_2_02team.oauth.dto.request.RefreshRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@FlywayReset
class TokenBrowserSuccessTest : BrowserTestTemplate() {
    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var refreshTokenBlackListRepository: RefreshTokenBlackListRepository

    @Test
    @DataSet(value = ["datasets/v2/member.yml"], cleanBefore = true, cleanAfter = true)
    @DisplayName("POST: /api/token/refresh 리프레시 토큰을 이용해 토큰 재발급")
    fun refreshToken() {
        // given
        val tokenPair = genTokenPair(1L)
        val request = RefreshRequest(tokenPair.refreshToken)

        // when & then
        mockMvc
            .post("/api/token/refresh") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isOk() }
                jsonPath("$.access_token") { value(tokenPair.accessToken) }
                jsonPath("$.refresh_token") { value(tokenPair.refreshToken) }
            }
    }

    @Test
    @DataSet(value = ["datasets/v2/member.yml"], cleanBefore = true, cleanAfter = true)
    @DisplayName("POST: /api/logout 로그아웃 요청 성공")
    @Throws(
        Exception::class,
    )
    fun logout() {
        // given
        val tokenPair = genTokenPair(1L)
        val request = LogoutRequest(tokenPair.refreshToken)

        // when & then
        mockMvc
            .post("/api/logout") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
                header("Authorization", "Bearer ${tokenPair.accessToken}")
            }.andExpect {
                status { isOk() }
            }
    }

    @Test
    @DataSet(value = ["datasets/v2/member.yml"], cleanBefore = true, cleanAfter = true)
    @DisplayName("POST: /api/signup 회원가입 성공")
    @Throws(
        Exception::class,
    )
    fun signup() {
        // given
        val memberId = 1L
        val tokenPair = genTokenPair(memberId)

        val request =
            SignUpRequest("신규가입자", "DEVELOPER", "SPRING")

        // when & then
        mockMvc
            .post("/api/signup") {
                header("Authorization", "Bearer ${tokenPair.accessToken}")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isCreated() }
                jsonPath("$.id") { value(memberId) }
                jsonPath("$.name") { value(request.name) }
                jsonPath("$.job") { value(request.job) }
                jsonPath("$.course") { value(request.course) }
            }
    }

    private fun genTokenPair(memberId: Long): TokenPair {
        val member = memberRepository.findById(memberId).orElseThrow()
        return jwtTokenProvider.generateTokenPair(member)
    }
}
