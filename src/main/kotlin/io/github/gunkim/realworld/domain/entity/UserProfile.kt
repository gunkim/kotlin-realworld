package io.github.gunkim.realworld.domain.entity

import io.github.gunkim.realworld.domain.base.DomainEntity
import io.github.gunkim.realworld.domain.vo.UserId
import io.github.gunkim.realworld.domain.vo.UserName
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class UserProfile(
    @Id
    override val id: UserId?,
    name: UserName,
    bio: String?,
    image: String?,
) : DomainEntity<UserProfile, UserId>() {
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

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
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
