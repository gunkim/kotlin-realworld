package io.github.gunkim.realworld.domain.article.exception

import io.github.gunkim.realworld.share.DomainException
import java.util.UUID

class NotArticleAuthorException internal constructor(
    message: String,
) : DomainException(message) {
    companion object {
        fun fromArticleUuidAndAuthUuid(
            articleUuid: UUID,
            authUuid: UUID,
        ) = NotArticleAuthorException(
            "User with UUID: $authUuid is not the author of the article with UUID: $articleUuid"
        )
    }
}