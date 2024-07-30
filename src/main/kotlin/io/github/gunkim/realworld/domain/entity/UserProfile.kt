package io.github.gunkim.realworld.domain.entity

import io.github.gunkim.realworld.domain.vo.UserId
import io.github.gunkim.realworld.domain.vo.UserName
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class UserProfile(
    @Id
    val userId: UserId?,
    name: UserName,
    bio: String?,
    image: String?,
) {
    var name = name
        protected set
    var bio = bio
        protected set
    var image = image
        protected set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserProfile

        return userId == other.userId
    }

    override fun hashCode(): Int {
        return userId.hashCode()
    }

    companion object {
        fun create(userId: UserId, name: UserName) = UserProfile(
            userId,
            name,
            null,
            null
        )
    }
}
