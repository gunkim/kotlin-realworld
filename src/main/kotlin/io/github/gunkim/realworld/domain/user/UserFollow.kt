package io.github.gunkim.realworld.domain.user

import io.github.gunkim.realworld.domain.common.DomainEntity
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.util.*

@Entity
class UserFollow(
    @Id
    override val id: UserFollowId,
    @AttributeOverride(name = "value", column = jakarta.persistence.Column(name = "follower_id"))
    val follower: UserId,
    @AttributeOverride(name = "value", column = jakarta.persistence.Column(name = "followee_id"))
    val followee: UserId,
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
) : DomainEntity<UserFollow, UserFollowId>() {
    override fun toString(): String {
        return "UserFollow(id=$id, follower=$follower, followee=$followee, createdAt=$createdAt)"
    }

    companion object {
        fun of(follower: UserId, followee: UserId) = UserFollow(UserFollowId(UUID.randomUUID()), follower, followee)
    }
}