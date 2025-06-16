package io.twogether.nbe_5_7_2_02team.tag.dto.response

import io.twogether.nbe_5_7_2_02team.tag.domain.Tag

data class TagGetResponse(
    val tags: List<String>
) {
    companion object {
        fun of(tags: List<Tag>): TagGetResponse {
            return TagGetResponse(tags.map { it.name })
        }
    }
}
