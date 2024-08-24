package io.github.gunkim.realworld.domain.user

import io.github.gunkim.realworld.domain.common.AggregateRoot
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
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

    @OneToMany(fetch = FetchType.EAGER, cascade = [jakarta.persistence.CascadeType.ALL])
    @JoinColumn(name = "follower_id")
    private val _followings: MutableSet<UserFollow> = mutableSetOf()
    val followings: Set<UserFollow> get() = _followings.toSet()

    @OneToMany(fetch = FetchType.EAGER, cascade = [jakarta.persistence.CascadeType.ALL])
    @JoinColumn(name = "followee_id")
    private val _followers: MutableSet<UserFollow> = mutableSetOf()
    val followers: Set<UserFollow> get() = _followers.toSet()

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

    fun follow(user: User) {
        check(user.id != null) { "User must be persisted" }
        check(this.id != null) { "User must be persisted" }
        check(user.id != this.id) { "User can't follow itself" }
        check(!_followings.any { it.followee == user.id }) { "Already following" }

        val follow = UserFollow.of(this.id!!, user.id!!)
        _followings.add(follow)
        user._followers.add(follow)
    }

    fun unfollow(user: User) {
        _followings.removeIf { it.followee == user.id }
    }

    fun isFollowing(id: UserId) = _followings.any { it.followee == id }

    fun isFollowedBy(id: UserId) = _followers.any { it.follower == id }

    override fun toString(): String {
        return "User(id=$id, email=$email, profile=$profile, createdAt=$createdAt, password=$password, followings=$_followings, followers=$_followers, updatedAt=$updatedAt)"
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
