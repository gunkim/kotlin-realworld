package io.github.gunkim.realworld.domain.user

import io.github.gunkim.realworld.domain.common.AggregateRoot
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "users")
class User(
    @Id
    override val id: UserId?,
    @Embedded
    val email: Email,
    password: String,
    @OneToOne(cascade = [CascadeType.ALL])
    val profile: UserProfile,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) : AggregateRoot<User, UserId>() {
    var password = password
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

    companion object {
        fun create(name: UserName, email: Email, password: String): User {
            val userId = UserId()
            return User(
                userId,
                email,
                password,
                UserProfile.create(userId, name)
            )
        }
    }
}
