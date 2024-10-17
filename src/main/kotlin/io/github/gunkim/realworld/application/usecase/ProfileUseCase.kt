package io.github.gunkim.realworld.application.usecase

import io.github.gunkim.realworld.application.UserFollowFindService
import io.github.gunkim.realworld.application.UserService
import io.github.gunkim.realworld.domain.user.User
import io.github.gunkim.realworld.domain.user.UserFollow
import io.github.gunkim.realworld.domain.user.UserFollowService
import io.github.gunkim.realworld.domain.user.UserName
import io.github.gunkim.realworld.web.model.AuthenticatedUser
import io.github.gunkim.realworld.application.usecase.response.ProfileResponse
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ProfileUseCase(
    private val userService: UserService,
    private val userFollowService: UserFollowService,
    private val userFollowFindService: UserFollowFindService,
) {
    @Transactional(readOnly = true)
    fun get(authenticatedUser: AuthenticatedUser?, username: UserName): ProfileResponse {
        val me: User? = authenticatedUser?.let { userService.findUserById(it.id) }
        val user = userService.findUserByName(username)

        val isFollowing = me?.let { userFollowFindService.isFollowing(me, user) } ?: false
        return ProfileResponse.of(user, isFollowing)
    }

    @Transactional
    fun follow(authenticatedUser: AuthenticatedUser, username: UserName) {
        val me = userService.findUserById(authenticatedUser.id)
        val user = userService.findUserByName(username)

        userFollowService.follow(UserFollow.of(me, user))
    }

    @Transactional
    fun unfollow(authenticatedUser: AuthenticatedUser, username: UserName) {
        val me = userService.findUserById(authenticatedUser.id)
        val user = userService.findUserByName(username)

        userFollowService.unfollow(me, user)
    }
}