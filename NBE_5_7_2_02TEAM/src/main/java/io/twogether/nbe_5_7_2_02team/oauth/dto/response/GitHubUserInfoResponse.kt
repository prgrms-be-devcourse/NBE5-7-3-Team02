package io.twogether.nbe_5_7_2_02team.oauth.dto.response

data class GitHubUserInfoResponse(
    val githubId: String,
    val email: String,
    val avatarUrl: String,
    val organizations: List<String>,
)
