package io.github.gunkim.realworld.domain.user.service

import io.github.gunkim.realworld.domain.UserFindable
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class FollowUserService(
    override val userRepository: UserRepository,
) : UserFindable {
    fun followUser(followerId: UserId, followeeUsername: String) {
        val followee = getUserByName(followeeUsername)

        userRepository.follow(followerId, followee.id)
    }

    fun unfollowUser(followerId: UserId, followeeUsername: String) {
        val followee = getUserByName(followeeUsername)

        userRepository.unfollow(followerId, followee.id)
    }

    fun isFollowing(followerId: UserId, followeeUsername: String): Boolean {
        val followee = getUserByName(followeeUsername)

        return userRepository.existsFollowingIdAndFollowerUsername(followerId, followee.id)
    }

    fun getFollowingUserIds(userId: UserId): List<UserId> = userRepository.findFollowingUserIds(userId)
}