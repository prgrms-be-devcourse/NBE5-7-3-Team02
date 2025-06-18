package io.twogether.nbe_5_7_2_02team.member

import com.github.database.rider.core.api.dataset.DataSet
import io.twogether.nbe_5_7_2_02team.browser.template.BrowserTestTemplate
import io.twogether.nbe_5_7_2_02team.global.annotation.FlywayReset
import io.twogether.nbe_5_7_2_02team.member.dao.MemberRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.mock.web.MockPart
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.nio.charset.StandardCharsets

@FlywayReset
class MemberBrowserSuccessTest : BrowserTestTemplate() {
    @Autowired
    lateinit var memberRepository: MemberRepository

    @Test
    @DataSet(value = ["datasets/v2/member.yml"], cleanBefore = true, cleanAfter = true)
    @DisplayName("GET: /api/member/me - 자기 정보 조회 성공")
    fun `GET api member me - success`() {
        val memberId = 1L
        val member = memberRepository.findById(memberId).orElseThrow()
        val tokenPair = jwtTokenProvider.generateTokenPair(member)

        mockMvc.perform(
            get("/api/member/me")
                .header("Authorization", "Bearer ${tokenPair.accessToken}")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(member.id))
            .andExpect(jsonPath("$.name").value(member.name))
            .andExpect(jsonPath("$.email").value(member.email))
    }

    @Test
    @DataSet(value = ["datasets/v2/member.yml"], cleanBefore = true, cleanAfter = true)
    @DisplayName("GET: /api/member/{id} - 타인 정보 조회 성공")
    fun `GET api member {id} - other member success`() {
        val loginMemberId = 1L
        val targetMemberId = 2L

        val loginMember = memberRepository.findById(loginMemberId).orElseThrow()
        val targetMember = memberRepository.findById(targetMemberId).orElseThrow()

        val tokenPair = jwtTokenProvider.generateTokenPair(loginMember)

        mockMvc.perform(
            get("/api/member/${targetMemberId}")
                .header("Authorization", "Bearer ${tokenPair.accessToken}" )
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(targetMember.id))
            .andExpect(jsonPath("$.name").value(targetMember.name))
            .andExpect(jsonPath("$.email").value(targetMember.email))
    }

    @Test
    @DataSet(value = ["datasets/v2/member.yml"], cleanBefore = true, cleanAfter = true)
    @DisplayName("PATCH: /api/member/me - 본인 닉네임과 이미지 수정 성공")
    fun `PATCH api member me - success`() {
        val memberId = 1L
        val member = memberRepository.findById(memberId).orElseThrow()
        val tokenPair = jwtTokenProvider.generateTokenPair(member)

        val newImage =
            MockMultipartFile(
                "image", "profile.png", "image/png", "fake image content".toByteArray()
            )
        val nicknamePart =
            MockPart("nickname", "newNickName".toByteArray())
        nicknamePart.headers.contentType = MediaType.TEXT_PLAIN

        mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/member/me")
                .file(newImage)
                .part(nicknamePart)
                .header("Authorization", "Bearer ${tokenPair.accessToken}")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("newNickName"))
            .andExpect(jsonPath("$.profile_image").exists())
            .andExpect(jsonPath("$.profile_image").isNotEmpty())
    }
}