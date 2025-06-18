package io.twogether.nbe_5_7_2_02team.member

import com.github.database.rider.core.api.dataset.DataSet
import io.twogether.nbe_5_7_2_02team.browser.template.BrowserTestTemplate
import io.twogether.nbe_5_7_2_02team.global.annotation.FlywayReset
import io.twogether.nbe_5_7_2_02team.member.dao.MemberRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@FlywayReset
class MemberBrowserFailureTest : BrowserTestTemplate() {
    @Autowired
    lateinit var memberRepository: MemberRepository

    @BeforeEach
    fun setupTimestamps() {
        val now = LocalDateTime.now()
        memberRepository.findAll().forEach {
            ReflectionTestUtils.setField(it, "createdAt", now)
            ReflectionTestUtils.setField(it, "updatedAt", now)
        }
    }

    @Test
    @DataSet(value = ["datasets/v2/member.yml"], cleanBefore = true, cleanAfter = true)
    @DisplayName("GET: /api/member/me - 인증 토큰 없음")
    fun `GET api member me - unauthorized`() {
        mockMvc
            .perform(get("/api/member/me"))
            .andExpect(status().isUnauthorized())
    }

    @Test
    @DataSet(value = ["datasets/v2/member.yml"], cleanBefore = true, cleanAfter = true)
    @DisplayName("GET: /api/member/{id} - 존재하지 않는 회원 ID 조회")
    fun `GET api member {id} - not found`() {
        val memberId = 1L
        val member = memberRepository.findById(memberId).orElseThrow()
        val tokenPair = jwtTokenProvider.generateTokenPair(member)

        val invalidId = 9999L

        mockMvc
            .perform(
                get("/api/member/$invalidId")
                    .header("Authorization", "Bearer ${tokenPair.accessToken}"),
            ).andExpect(status().isNotFound()) // 404
    }

    @Test
    @DataSet(value = ["datasets/v2/member.yml"], cleanBefore = true, cleanAfter = true)
    @DisplayName("PATCH: /api/member/me - 인증 토큰 없음")
    fun `PATCH api member me - unauthorized`() {
        val image =
            MockMultipartFile("image", "a.png", "image/png", "dummy".toByteArray())

        mockMvc
            .perform(
                multipart(HttpMethod.PATCH, "/api/member/me")
                    .file(image)
                    .param("nickname", "memberNickname"),
            ).andExpect(status().isUnauthorized())
    }

    @Test
    @DataSet(value = ["datasets/v2/member.yml"], cleanBefore = true, cleanAfter = true)
    @DisplayName("PATCH: /api/member/me - 닉네임 누락 (공백)")
    fun `PATCH api member me - blank nickname`() {
        val memberId = 1L

        val member = memberRepository.findById(memberId).orElseThrow()
        val tokenPair = jwtTokenProvider.generateTokenPair(member)

        val image =
            MockMultipartFile("image", "a.png", "image/png", "dummy".toByteArray())

        mockMvc
            .perform(
                multipart(HttpMethod.PATCH, "/api/member/me")
                    .file(image)
                    .param("nickname", " ") // 공백
                    .header("Authorization", "Bearer ${tokenPair.accessToken}"),
            ).andExpect(status().isBadRequest())
    }
}
