package io.github.gunkim.realworld.domain.article.model

interface TagReadRepository {
    fun findAll(): List<Tag>
}