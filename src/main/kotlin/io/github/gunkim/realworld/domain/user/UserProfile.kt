package io.github.gunkim.realworld.domain.user

import io.github.gunkim.realworld.domain.common.DomainEntity
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class UserProfile(
    @Id
    override val id: UserId?,
    name: UserName,
    bio: String?,
    image: Image?,
) : DomainEntity<UserProfile, UserId>() {
    var name = name
        protected set
    var bio = bio
        protected set

    @Embedded
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

    fun updateWhenNotNull(name: UserName?, image: Image?, bio: String?) {
        name?.let { this.name = it }
        image?.let { this.image = it }
        bio?.let { this.bio = it }
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
