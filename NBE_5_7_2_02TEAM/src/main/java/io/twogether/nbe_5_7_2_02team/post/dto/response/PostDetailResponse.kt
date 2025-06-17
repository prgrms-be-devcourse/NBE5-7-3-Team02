package io.twogether.nbe_5_7_2_02team.post.dto.response

import io.twogether.nbe_5_7_2_02team.post.domain.RecruitmentStatus

data class PostDetailResponse(
    val title: String,
    val content: String,
    val recruitmentStatus: RecruitmentStatus,
    val tags: List<String>,
    val imageUrls: List<String>,
)
