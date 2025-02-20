package io.github.gunkim.realworld.infrastructure.jdbc.user.model

import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.model.UserId
import io.github.gunkim.realworld.infrastructure.jdbc.share.Updatable
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Converter
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant
import java.util.UUID

@Converter(autoApply = true)
class UserIdConverter : AttributeConverter<UserId, UUID> {
    override fun convertToDatabaseColumn(attribute: UserId): UUID = attribute.value
    override fun convertToEntityAttribute(dbData: UUID): UserId = dbData.let(::UserId)
}

@Entity(name = "users")
class UserJpaEntity(
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val databaseId: Int? = null,
    @Column(name = "uuid")
    @Convert(converter = UserIdConverter::class)
    override val id: UserId,
    name: String,
    bio: String?,
    image: String?,
    email: String,
    password: String,
    override val createdAt: Instant,
    override var updatedAt: Instant,
) : User.Editor, Updatable {
    override var name: String = name
        set(value) {
            field = updateField(field, value)
        }
    override var bio: String? = bio
        set(value) {
            field = updateField(field, value)
        }
    override var image: String? = image
        set(value) {
            field = updateField(field, value)
        }
    override var email: String = email
        set(value) {
            field = updateField(field, value)
        }
    override var password: String = password
        set(value) {
            field = updateField(field, value)
        }

    companion object {
        fun from(user: User): UserJpaEntity = with(user) {
            UserJpaEntity(
                databaseId = if (this is UserJpaEntity) databaseId else null,
                id = user.id,
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

        if (databaseId != other.databaseId) return false
        if (id != other.id) return false
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
        var result = databaseId ?: 0
        result = 31 * result + id.hashCode()
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