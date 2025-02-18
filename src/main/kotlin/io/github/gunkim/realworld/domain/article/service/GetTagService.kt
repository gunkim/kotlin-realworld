package io.github.gunkim.realworld.domain.article.service

import io.github.gunkim.realworld.domain.article.model.Tag
import io.github.gunkim.realworld.domain.article.model.TagReadRepository
import org.springframework.stereotype.Service

@Service
class GetTagService(
    private val tagReadRepository: TagReadRepository,
) {
    fun getTags(): List<Tag> = tagReadRepository.findAll()
}