package io.github.gunkim.realworld.web.api.article

import io.github.gunkim.realworld.config.request.JsonRequest
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.article.service.AddCommentService
import io.github.gunkim.realworld.domain.user.service.FollowUserService
import io.github.gunkim.realworld.share.AuthenticatedUser
import io.github.gunkim.realworld.web.api.article.model.request.AddCommentRequest
import io.github.gunkim.realworld.web.api.article.model.response.CommentResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/articles/{slug}/comments")
interface CommentsResource {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addComment(
        @PathVariable slug: String,
        @JsonRequest("comment") request: AddCommentRequest,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ): CommentResponse
}

@RestController
class CommentsController(
    private val addCommentService: AddCommentService,
    private val followUserService: FollowUserService,
) : CommentsResource {
    override fun addComment(
        slug: String,
        request: AddCommentRequest,
        authenticatedUser: AuthenticatedUser,
    ) = addCommentService.addComment(
        slug = Slug(slug),
        commentBody = request.body,
        authorId = authenticatedUser.userId,
    ).let { CommentResponse.from(it, followUserService.isFollowing(authenticatedUser.userId, it.author.name)) }
}