package io.twogether.nbe_5_7_2_02team.tag.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.twogether.nbe_5_7_2_02team.tag.dto.response.TagGetResponse
import io.twogether.nbe_5_7_2_02team.tag.service.TagService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tags")
@Tag(name = "Tag", description = "태그 관련 API")
class TagController(
    private val tagService: TagService,
) {
    @GetMapping
    @Operation(
        summary = "모든 태그 조회",
        description = "전체 태그 목록을 조회합니다.",
    )
    fun getTags(): ResponseEntity<TagGetResponse> {
        val tags = tagService.getAllTags()
        val response = TagGetResponse.of(tags)

        return if (response.tags.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(response)
        }
    }
}
