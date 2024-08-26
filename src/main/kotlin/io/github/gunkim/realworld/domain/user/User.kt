package io.github.gunkim.realworld.domain.user

import io.github.gunkim.realworld.domain.common.AggregateRoot
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.time.LocalDateTime
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity(name = "users")
@EntityListeners(AuditingEntityListener::class)
class User(
    @Id
    override val id: UserId?,
    @Embedded
    @Column(unique = true)
    var email: Email,
    password: EncodedPassword,
    @Embedded
    val profile: Profile,
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime? = null,
) : AggregateRoot<User, UserId>() {
    @Embedded
    var password = password
        protected set

    @LastModifiedDate
    var updatedAt: LocalDateTime? = updatedAt
        protected set

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

    override fun toString(): String {
        return "User(id=$id, email=$email, profile=$profile, createdAt=$createdAt, password=$password, updatedAt=$updatedAt)"
    }

    companion object {
        fun create(id: UserId, name: UserName, email: Email, encodedPassword: EncodedPassword) = User(
            id,
            email,
            encodedPassword,
            Profile.create(name)
        )

        fun create(name: UserName, email: Email, encodedPassword: EncodedPassword) = create(
            UserId(),
            name,
            email,
            encodedPassword
        )
    }
}
