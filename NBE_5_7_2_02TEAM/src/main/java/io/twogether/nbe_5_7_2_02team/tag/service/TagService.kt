package io.twogether.nbe_5_7_2_02team.tag.service

import io.twogether.nbe_5_7_2_02team.tag.dao.TagRepository
import io.twogether.nbe_5_7_2_02team.tag.domain.Tag
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TagService(
    private val tagRepository: TagRepository
) {

    fun getAllTags(): List<Tag> = tagRepository.findAll()
}
