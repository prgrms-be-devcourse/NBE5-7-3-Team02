package io.twogether.nbe_5_7_2_02team.member.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

data class UpdateProfileRequest(
    @NotBlank(message = "이미지를 선택해주세요")
    val image: MultipartFile,
    @field:[Size(max = 30) NotNull(message = "닉네임을 입력해 주세요")] val nickname: String,
)
