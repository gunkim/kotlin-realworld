package io.github.gunkim.realworld.domain.user

import io.github.gunkim.realworld.domain.common.AggregateRoot
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.data.annotation.CreatedDate

@Entity
class UserFollow(
    @Id
    override val id: UserFollowId,
    @ManyToOne
    @JoinColumn(name = "follower_id")
    val follower: User,
    @ManyToOne
    @JoinColumn(name = "followee_id")
    val followee: User,
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
) : AggregateRoot<UserFollow, UserFollowId>() {
    override fun toString(): String {
        return "UserFollow(id=$id, follower=${follower}, followee=${followee}, createdAt=$createdAt)"
    }

    companion object {
        fun of(follower: User, followee: User) = UserFollow(UserFollowId(UUID.randomUUID()), follower, followee)
    }
}