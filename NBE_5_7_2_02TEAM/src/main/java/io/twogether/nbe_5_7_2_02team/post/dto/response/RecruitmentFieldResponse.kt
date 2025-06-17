package io.twogether.nbe_5_7_2_02team.post.dto.response

data class RecruitmentFieldResponse (
    val fieldName: String,
    val totalCount: Int,
    val currentCount: Int,
    val closed: Boolean,
)
