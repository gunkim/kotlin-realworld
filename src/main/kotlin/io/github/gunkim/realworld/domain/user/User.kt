package io.github.gunkim.realworld.domain.user

import io.github.gunkim.realworld.domain.common.AggregateRoot
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "users")
class User(
    @Id
    override val id: UserId?,
    @Embedded
    @Column(unique = true)
    var email: Email,
    encodedPassword: EncodedPassword,
    @OneToOne(cascade = [CascadeType.ALL])
    val profile: UserProfile,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) : AggregateRoot<User, UserId>() {
    var password = encodedPassword
        protected set
    var updatedAt: LocalDateTime? = null

    @PreUpdate
    fun preUpdate() {
        this.updatedAt = LocalDateTime.now()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun updateWhenNotNull(
        email: Email?,
        encodedPassword: EncodedPassword?,
        username: UserName? = null,
        image: Image? = null,
        bio: String? = null,
    ) {
        email?.let { this.email = it }
        encodedPassword?.let { this.password = it }

        this.profile.updateWhenNotNull(username, image, bio)
    }

    companion object {
        fun create(name: UserName, email: Email, encodedPassword: EncodedPassword): User {
            val userId = UserId()
            return User(
                userId,
                email,
                encodedPassword,
                UserProfile.create(userId, name)
            )
        }

        fun create(id: UserId, name: UserName, email: Email, encodedPassword: EncodedPassword): User {
            return User(
                id,
                email,
                encodedPassword,
                UserProfile.create(id, name)
            )
        }
    }
}
