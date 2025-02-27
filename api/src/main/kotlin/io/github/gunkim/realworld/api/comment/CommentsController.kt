package io.github.gunkim.realworld.api.comment

import io.github.gunkim.realworld.api.JsonRequest
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.comment.model.CommentId
import io.github.gunkim.realworld.domain.comment.service.AddCommentService
import io.github.gunkim.realworld.domain.comment.service.DeleteCommentService
import io.github.gunkim.realworld.domain.comment.service.GetCommentService
import io.github.gunkim.realworld.domain.user.service.FollowUserService
import io.github.gunkim.realworld.api.AuthenticatedUser
import io.github.gunkim.realworld.api.comment.model.request.AddCommentRequest
import io.github.gunkim.realworld.api.comment.model.response.CommentResponse
import io.github.gunkim.realworld.api.comment.model.response.wrapper.CommentWrapper
import io.github.gunkim.realworld.api.comment.model.response.wrapper.CommentsWrapper
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
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
    ): CommentWrapper

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable slug: String,
        @PathVariable commentId: Int,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    )

    @GetMapping
    fun getCommentsFromArticle(
        @PathVariable slug: String,
        @AuthenticationPrincipal authenticatedUser: AuthenticatedUser,
    ): CommentsWrapper
}

@RestController
class CommentsController(
    private val addCommentService: AddCommentService,
    private val followUserService: FollowUserService,
    private val deleteCommentService: DeleteCommentService,
    private val getCommentService: GetCommentService,
) : CommentsResource {
    override fun addComment(
        slug: String,
        request: AddCommentRequest,
        authenticatedUser: AuthenticatedUser,
    ) = addCommentService.addComment(
        slug = Slug.from(slug),
        commentBody = request.body,
        authorId = authenticatedUser.userId,
    ).let { CommentResponse.from(it, followUserService.isFollowing(authenticatedUser.userId, it.author.name)) }
        .let(::CommentWrapper)

    override fun deleteComment(
        slug: String,
        commentId: Int,
        authenticatedUser: AuthenticatedUser,
    ) {
        deleteCommentService.deleteComment(
            slug = Slug.from(slug),
            commentId = CommentId.from(commentId),
            deleterId = authenticatedUser.userId
        )
    }

    override fun getCommentsFromArticle(
        slug: String,
        authenticatedUser: AuthenticatedUser,
    ): CommentsWrapper {
        val followingPredicate = followUserService.getFollowingPredicate(authenticatedUser.userId)

        return getCommentService.getComments(Slug.from(slug))
            .map { CommentResponse.from(it, followingPredicate(it.author.id)) }
            .let(::CommentsWrapper)
    }
}