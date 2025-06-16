package io.twogether.nbe_5_7_2_02team.member.dto.request

import jakarta.validation.constraints.NotBlank

data class SignUpRequest(

    @field:NotBlank (message = "이름은 필수 입력입니다.")
    val name: String,
    @field:NotBlank (message = "직업을 선택해야 합니다.")
    val job: String,
    @field:NotBlank (message = "수료했던 코스를 선택해야 합니다.")
    val course: String
)
