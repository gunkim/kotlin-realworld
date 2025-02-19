package io.github.gunkim.realworld.domain.user.service

import io.github.gunkim.realworld.domain.UserFindable
import io.github.gunkim.realworld.domain.user.repository.UserRepository
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class FollowUserService(
    override val userRepository: UserRepository,
) : UserFindable {
    fun followUser(followerUuid: UUID, followeeUsername: String) {
        val targetUser = getUserByName(followeeUsername)

        userRepository.follow(followerUuid, targetUser.uuid)
    }

    fun unfollowUser(uuid: UUID, targetUsername: String) {
        val targetUser = getUserByName(targetUsername)

        userRepository.unfollow(uuid, targetUser.uuid)
    }

    fun isFollowing(uuid: UUID, targetUsername: String): Boolean {
        val targetUser = getUserByName(targetUsername)

        return userRepository.existsFollowingIdAndFollowerUsername(uuid, targetUser.uuid)
    }

    fun getFollowingUserUuids(userUuid: UUID): List<UUID> = userRepository.findFollowingUserUuids(userUuid)
}