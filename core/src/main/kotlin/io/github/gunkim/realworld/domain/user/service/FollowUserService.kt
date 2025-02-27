package io.github.gunkim.realworld.domain.user.service

import io.github.gunkim.realworld.domain.user.UserFindable
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

typealias FollowPredicate = (UserId) -> Boolean

@Service
class FollowUserService(
    override val userRepository: UserRepository,
) : UserFindable {
    fun followUser(followerId: UserId, followeeUsername: String): User {
        val followee = findFollowee(followeeUsername)
        userRepository.follow(followerId, followee.id)
        return followee
    }

    fun unfollowUser(followerId: UserId, followeeUsername: String): User {
        val followee = findFollowee(followeeUsername)
        userRepository.unfollow(followerId, followee.id)
        return followee
    }

    /**
     * Checks if the user identified by `followerId` is following another user identified by `followeeUsername`.
     *
     * For checking multiple follow relationships, consider using the `getFollowingPredicate` method
     * for improved performance.
     *
     * @param followerId The ID of the follower.
     * @param followeeUsername The username of the followee.
     * @return `true` if the user is following, otherwise `false`.
     */
    fun isFollowing(followerId: UserId, followeeUsername: String): Boolean {
        val followee = findFollowee(followeeUsername)
        val followingPredicate = getFollowingPredicate(followerId)
        return followingPredicate(followee.id)
    }

    /**
     * Returns a predicate function to check if a given user is in the list of users being followed by `userId`.
     *
     * @param userId The ID of the user.
     * @return A predicate function to evaluate follow relationships.
     */
    fun getFollowingPredicate(userId: UserId): FollowPredicate {
        val followings: List<UserId> = userRepository.findFollowedUserIdsFor(userId)
        return followings::contains
    }

    private fun findFollowee(username: String) = getUserByName(username)
}