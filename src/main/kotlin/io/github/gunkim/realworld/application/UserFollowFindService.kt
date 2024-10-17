package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.domain.user.User
import io.github.gunkim.realworld.domain.user.UserFollowRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserFollowFindService(
    private val userFollowRepository: UserFollowRepository,
) {
    @Transactional(readOnly = true)
    fun isFollowing(followerId: User, followeeId: User): Boolean {
        return userFollowRepository.existsUserFollowByFollowerAndFollowee(followerId, followeeId)
    }
}