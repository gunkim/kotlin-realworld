package io.github.gunkim.realworld.infrastructure.jdbc.article.repository

import io.github.gunkim.realworld.domain.article.model.Tag
import io.github.gunkim.realworld.domain.article.model.TagReadRepository
import io.github.gunkim.realworld.infrastructure.jdbc.article.dao.TagDao
import org.springframework.stereotype.Repository

@Repository
class TagReadRepositoryImpl(
    private val tagDao: TagDao,
) : TagReadRepository {
    override fun findAll(): List<Tag> =
        tagDao.findAll()
            .map { Tag.create(it.name) }
}