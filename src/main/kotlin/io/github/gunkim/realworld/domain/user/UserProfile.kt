package io.github.gunkim.realworld.domain.user

import io.github.gunkim.realworld.domain.common.ValueObject
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

@Embeddable
class UserProfile(
    name: UserName,
    bio: String?,
    image: Image?,
) : ValueObject<UserProfile>() {
    var name = name
        protected set
    var bio = bio
        protected set

    @Embedded
    var image = image
        protected set

    fun updateWhenNotNull(name: UserName?, image: Image?, bio: String?) {
        name?.let { this.name = it }
        image?.let { this.image = it }
        bio?.let { this.bio = it }
    }

    companion object {
        fun create(name: UserName) = UserProfile(
            name,
            null,
            null
        )
    }
}
