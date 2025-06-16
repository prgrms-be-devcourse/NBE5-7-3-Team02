package io.twogether.nbe_5_7_2_02team.post.dto.request

import io.twogether.nbe_5_7_2_02team.post.domain.RecruitmentStatus
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

data class PostUpdateRequest(
    val title: String,
    val content: String,
    val recruitmentStatus: RecruitmentStatus,
    val recruitmentDeadline: LocalDate?,
    var images: List<MultipartFile> = listOf(),
    val tags: List<String> = listOf(),
    val recruitmentFields: List<RecruitmentFieldRequest> = listOf(),
)
