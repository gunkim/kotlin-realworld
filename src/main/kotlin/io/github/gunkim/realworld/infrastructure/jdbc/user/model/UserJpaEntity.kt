package io.github.gunkim.realworld.infrastructure.jdbc.user.model

import io.github.gunkim.realworld.domain.user.model.User
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.net.URL
import java.time.Instant
import java.util.UUID

@Entity(name = "users")
class UserJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Int?,
    override val uuid: UUID,
    name: String,
    bio: String?,
    image: URL?,
    email: String,
    password: String,
    override val createdAt: Instant,
    override var updatedAt: Instant = Instant.now(),
) : User.Editor {
    override var name: String = name
        set(value) {
            field = value
            updatedAt = Instant.now()
        }
    override var bio: String? = bio
        set(value) {
            field = value
            updatedAt = Instant.now()
        }
    override var image: URL? = image
        set(value) {
            field = value
            updatedAt = Instant.now()
        }
    override var email: String = email
        set(value) {
            field = value
            updatedAt = Instant.now()
        }
    override var password: String = password
        set(value) {
            field = value
            updatedAt = Instant.now()
        }

    companion object {
        fun from(user: User): UserJpaEntity = with(user) {
            UserJpaEntity(
                userId = if (this is UserJpaEntity) userId else null,
                uuid = uuid,
                name = name,
                bio = bio,
                image = image,
                email = email,
                password = password,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserJpaEntity

        if (userId != other.userId) return false
        if (uuid != other.uuid) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false
        if (name != other.name) return false
        if (bio != other.bio) return false
        if (image != other.image) return false
        if (email != other.email) return false
        if (password != other.password) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + uuid.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (bio?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        return result
    }
}