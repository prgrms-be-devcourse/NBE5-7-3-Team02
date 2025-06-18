package io.twogether.nbe_5_7_2_02team.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

@Schema(description = "프로필 수정 요청 DTO")
data class UpdateProfileRequest(
    @field:NotBlank(message = "이미지를 선택해주세요")
    @field:Schema(description = "프로필 이미지 파일")
    val image: MultipartFile,
    @field:Size(max = 30)
    @field:NotNull(message = "닉네임을 입력해 주세요")
    @field:Schema(description = "닉네임", example = "gildong123")
    val nickname: String,
)
