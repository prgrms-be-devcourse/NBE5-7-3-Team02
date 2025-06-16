package io.twogether.nbe_5_7_2_02team.tag.api

import io.twogether.nbe_5_7_2_02team.tag.dto.response.TagGetResponse
import io.twogether.nbe_5_7_2_02team.tag.service.TagService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tags")
class TagController(
    private val tagService: TagService
) {

    @GetMapping
    fun getTags(): ResponseEntity<TagGetResponse> {
        val tags = tagService.getAllTags()
        val response = TagGetResponse.of(tags)

        return if (response.tags.isEmpty())
            ResponseEntity.noContent().build()
        else
            ResponseEntity.ok(response)
    }
}
