package io.github.gunkim.realworld.infrastructure.jpa.article.dao

import io.github.gunkim.realworld.infrastructure.jpa.article.model.TagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagDao : JpaRepository<TagEntity, Int> {
    fun findByNameIn(names: List<String>): List<TagEntity>
}