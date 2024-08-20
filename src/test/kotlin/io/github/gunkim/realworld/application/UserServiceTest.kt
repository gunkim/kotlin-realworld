package io.github.gunkim.realworld.application

import io.github.gunkim.realworld.domain.user.*
import io.github.gunkim.realworld.web.request.UserRegistrationRequest
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

private val mockUserRepository = mockk<UserRepository>()
private val passwordEncoder = BCryptPasswordEncoder()

private val testEmail = Email("test@example.com")
private val testPassword = "test"

private fun createTestUser(email: Email = testEmail, password: String = testPassword): User {
    return User.create(UserName("TestUser2"), email, EncodedPassword.of(password, passwordEncoder::encode))
}

@DisplayName("UserService is")
class UserServiceTest : StringSpec({
    val sut = UserService(mockUserRepository, passwordEncoder)

    "should find user by email" {
        every { mockUserRepository.findByEmail(any()) } returns createTestUser()
        shouldNotThrow<IllegalArgumentException> {
            sut.findUserByEmail(testEmail)
        }
    }
    "should throw an exception when no user found for the email" {
        every { mockUserRepository.findByEmail(any()) } returns null
        shouldThrow<IllegalArgumentException> {
            sut.findUserByEmail(testEmail)
        }.message shouldBe "User not found"
    }
    "should register user" {
        val registrationRequest = UserRegistrationRequest("TestUser0", "test@example.com", "test0")
        every { mockUserRepository.findByEmail(any()) } returns null
        every { mockUserRepository.save(any()) } answers { firstArg() }
        shouldNotThrow<IllegalArgumentException> {
            sut.registerUser(registrationRequest)
        }
    }
    "should throw an exception when registering a user with an already registered email" {
        val registrationRequest = UserRegistrationRequest("TestUser0", "test@example.com", "test0")
        every { mockUserRepository.findByEmail(any()) } returns mockk()
        shouldThrow<IllegalArgumentException> {
            sut.registerUser(registrationRequest)
        }.message shouldBe "User already exists"
    }
    "should authenticate user" {
        val user = createTestUser()
        shouldNotThrow<IllegalArgumentException> {
            sut.authenticate(user, testPassword)
        }
    }
    "should throw an exception when the password does not match" {
        val user = createTestUser()
        shouldThrow<IllegalArgumentException> {
            sut.authenticate(user, "wrong password")
        }.message shouldBe "Password does not match"
    }
    "should find user by id" {
        val userId = UserId(UUID.randomUUID())
        every { mockUserRepository.findById(userId) } returns createTestUser(
            Email("test2@example.com"), "test2"
        )
        shouldNotThrow<IllegalArgumentException> {
            sut.findUserById(userId)
        }
    }
    "should throw an exception when no user found for the id" {
        val userId = UserId(UUID.randomUUID())
        every { mockUserRepository.findById(userId) } returns null
        shouldThrow<IllegalArgumentException> {
            sut.findUserById(userId)
        }.message shouldBe "User not found"
    }
    "should update user" {
        val user = createTestUser(
            Email("test3@example.com"), "test3"
        )
        every { mockUserRepository.save(any()) } returns user
        sut.update(user)
        verify { mockUserRepository.save(user) }
    }
})