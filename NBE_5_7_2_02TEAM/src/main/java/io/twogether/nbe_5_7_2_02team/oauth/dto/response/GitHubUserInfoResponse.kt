package io.twogether.nbe_5_7_2_02team.oauth.dto.response

import lombok.Builder
import lombok.Getter
import lombok.ToString

data class GitHubUserInfoResponse (
    val githubId: String,
    val email: String,
    val avatarUrl: String,
    val organizations: List<String>
)
