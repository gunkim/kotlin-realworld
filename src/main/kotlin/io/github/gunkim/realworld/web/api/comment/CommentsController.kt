package io.github.gunkim.realworld.web.api.comment

import io.github.gunkim.realworld.config.request.JsonRequest
import io.github.gunkim.realworld.domain.article.model.Slug
import io.github.gunkim.realworld.domain.comment.model.CommentId
import io.github.gunkim.realworld.domain.comment.service.AddCommentService
import io.github.gunkim.realworld.domain.comment.service.DeleteCommentService
import io.github.gunkim.realworld.domain.comment.service.GetCommentService
import io.github.gunkim.realworld.domain.user.service.FollowUserService
import io.github.gunkim.realworld.share.AuthenticatedUser
import io.github.gunkim.realworld.web.api.comment.model.request.AddCommentRequest
import io.github.gunkim.realworld.web.api.comment.model.response.CommentResponse
import io.github.gunkim.realworld.web.api.comment.model.response.CommentsResponse
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
    ): CommentResponse

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
    ): CommentsResponse
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
        slug = Slug(slug),
        commentBody = request.body,
        authorId = authenticatedUser.userId,
    ).let { CommentResponse.from(it, followUserService.isFollowing(authenticatedUser.userId, it.author.name)) }

    override fun deleteComment(
        slug: String,
        commentId: Int,
        authenticatedUser: AuthenticatedUser,
    ) {
        deleteCommentService.deleteComment(
            slug = Slug(slug),
            commentId = CommentId(commentId),
            deleterId = authenticatedUser.userId
        )
    }

    override fun getCommentsFromArticle(
        slug: String,
        authenticatedUser: AuthenticatedUser,
    ): CommentsResponse {
        val followingPredicate = followUserService.getFollowingPredicate(authenticatedUser.userId)

        return getCommentService.getComments(Slug(slug))
            .map { CommentResponse.from(it, followingPredicate(it.author.id)) }
            .let(::CommentsResponse)
    }
}