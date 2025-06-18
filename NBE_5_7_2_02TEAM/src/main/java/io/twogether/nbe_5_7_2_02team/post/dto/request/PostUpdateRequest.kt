package io.twogether.nbe_5_7_2_02team.post.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import io.twogether.nbe_5_7_2_02team.post.domain.RecruitmentStatus
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

@Schema(description = "게시글 수정 요청")
data class PostUpdateRequest(
    @Schema(description = "게시글 제목", example = "프로젝트 팀원 모집합니다")
    val title: String,

    @Schema(description = "게시글 내용", example = "함께 할 팀원을 모집해요")
    val content: String,

    @Schema(description = "모집 상태", example = "OPEN")
    val recruitmentStatus: RecruitmentStatus,

    @Schema(description = "모집 마감일", example = "2025-08-15")
    val recruitmentDeadline: LocalDate?,

    @Schema(description = "수정할 이미지 파일 리스트", required = false)
    var images: List<MultipartFile> = listOf(),

    @Schema(description = "태그 리스트", example = "[\"Java\", \"Spring\"]")
    val tags: List<String> = listOf(),

    @Schema(description = "모집 분야 리스트")
    val recruitmentFields: List<RecruitmentFieldRequest> = listOf(),
)
