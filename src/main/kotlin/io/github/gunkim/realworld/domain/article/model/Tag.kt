package io.github.gunkim.realworld.domain.article.model

interface Tag {
    val name: String

    companion object {
        class Model(
            override val name: String,
        ) : Tag

        fun create(name: String): Tag = Model(
            name = name,
        )
    }
}
