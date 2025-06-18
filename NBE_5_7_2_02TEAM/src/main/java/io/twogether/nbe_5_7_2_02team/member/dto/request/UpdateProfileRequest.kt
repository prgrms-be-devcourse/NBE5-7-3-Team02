package io.twogether.nbe_5_7_2_02team.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

@Schema(description = "프로필 수정 요청 DTO")
data class UpdateProfileRequest(
    @field:Schema(description = "프로필 이미지 파일")
    val image: MultipartFile? = null,
    @field:Size(max = 30)
    @field:Schema(description = "닉네임", example = "gildong123")
    val nickname: String,
)
