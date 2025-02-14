package io.github.gunkim.realworld.domain.user.service

import io.github.gunkim.realworld.domain.user.repository.UserRepository
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class FollowUserService(
    private val userRepository: UserRepository,
    private val getUserService: GetUserService,
) {
    fun followUser(uuid: UUID, targetUsername: String) {
        val targetUser = getUserService.getByUsername(targetUsername)

        userRepository.follow(uuid, targetUser.uuid)
    }

    fun unfollowUser(uuid: UUID, targetUsername: String) {
        val targetUser = getUserService.getByUsername(targetUsername)

        userRepository.unfollow(uuid, targetUser.uuid)
    }

    fun isFollowing(uuid: UUID, targetUsername: String): Boolean {
        val targetUser = getUserService.getByUsername(targetUsername)

        return userRepository.existsFollowingIdAndFollowerUsername(uuid, targetUser.uuid)
    }

    fun getFollowingUserUuids(userUuid: UUID): List<UUID> = userRepository.findFollowingUserUuids(userUuid)
}