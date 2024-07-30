package io.github.gunkim.realworld.domain.entity

import io.github.gunkim.realworld.domain.vo.Email
import io.github.gunkim.realworld.domain.vo.UserId
import io.github.gunkim.realworld.domain.vo.UserName
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class User(
    @Id
    val id: UserId,
    @Embedded
    val email: Email,
    name: UserName,
    password: String,
    @OneToOne(fetch = FetchType.LAZY)
    val profile: UserProfile,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
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
}
