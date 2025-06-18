package io.twogether.nbe_5_7_2_02team.post.dto.request

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.v3.oas.annotations.media.Schema
import io.twogether.nbe_5_7_2_02team.post.domain.RecruitmentStatus
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

@Schema(description = "게시글 생성 요청")
data class PostCreateRequest(
    @field:NotBlank(message = "제목은 필수입니다.")
    @Schema(description = "게시글 제목", example = "함께 할 개발자 모집합니다", required = true)
    val title: String,
    @field:NotBlank(message = "내용은 필수입니다.")
    @Schema(description = "게시글 내용", example = "React 프로젝트를 함께 개발할 팀원 모집", required = true)
    val content: String,
    @field:NotNull(message = "모집 상태는 필수입니다.")
    @Schema(description = "모집 상태", example = "RECRUITING")
    val recruitmentStatus: RecruitmentStatus,
    @field:FutureOrPresent(message = "마감일은 오늘 이후의 날짜여야 합니다.")
    @Schema(description = "모집 마감일", example = "2025-07-31")
    val recruitmentDeadline: LocalDate?,
    @field:Size(
        max = 10,
        message = "이미지는 최대 10개까지만 업로드할 수 있습니다.",
    )
    @Schema(description = "업로드할 이미지 파일 리스트 (최대 10개)", required = false)
    val images: List<MultipartFile> = listOf(),
    @Schema(description = "태그 리스트", example = "[\"Spring\", \"Backend\"]")
    val tags: List<String> = listOf(),
    @Schema(description = "모집 분야 JSON 문자열 (내부용)")
    val recruitmentFieldsJson: String?,
    @field:JsonIgnore
    var recruitmentFields: List<RecruitmentFieldRequest> = listOf(),
)
