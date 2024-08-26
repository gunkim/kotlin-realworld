package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.domain.user.User
import io.github.gunkim.realworld.domain.user.UserFollowRepository
import org.springframework.stereotype.Service

@Service
class UserFollowFindService(
    private val userFollowRepository: UserFollowRepository,
) {
    fun isFollowing(followerId: User, followeeId: User): Boolean {
        return userFollowRepository.existsUserFollowByFollowerAndFollowee(followerId, followeeId)
    }
}