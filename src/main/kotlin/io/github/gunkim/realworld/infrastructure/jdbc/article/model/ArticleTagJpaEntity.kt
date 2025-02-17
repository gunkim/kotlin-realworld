package io.github.gunkim.realworld.infrastructure.jdbc.article.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.Instant

@Entity(name = "article_tag")
data class ArticleTagJpaEntity(
    @Id
    @Column(name = "article_tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id", nullable = false)
    var article: ArticleJpaEntity? = null,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [jakarta.persistence.CascadeType.PERSIST])
    @JoinColumn(name = "tag_id", nullable = false)
    val tag: TagJpaEntity,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant? = null,
)