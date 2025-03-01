package io.github.gunkim.realworld.infrastructure.jpa.article.dao

import io.github.gunkim.realworld.infrastructure.jpa.article.model.TagJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagDao : JpaRepository<TagJpaEntity, Int> {
    fun findByNameIn(names: List<String>): List<TagJpaEntity>
}