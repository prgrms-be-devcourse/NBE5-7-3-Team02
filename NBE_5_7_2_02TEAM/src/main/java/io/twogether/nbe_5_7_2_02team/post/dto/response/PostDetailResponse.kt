package io.twogether.nbe_5_7_2_02team.post.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import io.twogether.nbe_5_7_2_02team.post.domain.RecruitmentStatus

@Schema(description = "게시글 상세 응답")
data class PostDetailResponse(
    @Schema(description = "게시글 제목", example = "함께 개발할 팀원 모집합니다")
    val title: String,
    @Schema(description = "게시글 내용", example = "React, Spring Boot 기반 프로젝트입니다.")
    val content: String,
    @Schema(description = "모집 상태", example = "RECRUITING")
    val recruitmentStatus: RecruitmentStatus,
    @Schema(description = "태그 리스트", example = "[\"Spring\", \"Backend\"]")
    val tags: List<String>,
    @Schema(description = "이미지 URL 리스트")
    val imageUrls: List<String>,
)
