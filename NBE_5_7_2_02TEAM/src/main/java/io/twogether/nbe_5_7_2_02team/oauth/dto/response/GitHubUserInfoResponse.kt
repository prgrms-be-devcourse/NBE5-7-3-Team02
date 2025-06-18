package io.twogether.nbe_5_7_2_02team.oauth.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "GitHub 사용자 정보 응답 DTO")
data class GitHubUserInfoResponse(
    @Schema(
        description = "GitHub 사용자 ID",
        example = "twogether",
    )
    val githubId: String,
    @Schema(
        description = "GitHub에 등록된 이메일 주소",
        example = "twogether@github.com",
    )
    val email: String,
    @Schema(
        description = "GitHub 프로필 이미지 URL",
        example = "https://avatars.githubusercontent.com/u/583231?v=4",
    )
    val avatarUrl: String,
    @Schema(
        description = "사용자가 속한 조직 목록",
        example = "[\"prgrms-web-devcourse\", \"prgrms-be-devcourse\"]",
    )
    val organizations: List<String>,
)
