package io.twogether.nbe_5_7_2_02team.tag.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import io.twogether.nbe_5_7_2_02team.tag.domain.Tag

@Schema(description = "태그 목록 응답")
data class TagGetResponse(
    @Schema(description = "태그 이름 목록", example = "[\"운동\", \"스터디\", \"취미\"]")
    val tags: List<String>,
) {
    companion object {
        fun of(tags: List<Tag>): TagGetResponse = TagGetResponse(tags.map { it.name })
    }
}
