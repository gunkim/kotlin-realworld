package io.github.gunkim.realworld.domain.entity

import io.github.gunkim.realworld.domain.vo.UserId
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class UserProfile(
    @Id
    val userId: UserId,
    name: String,
    bio: String,
    image: String,
) {
    var name = name
        protected set
    var bio = bio
        protected set
    var image = image
        protected set
}
