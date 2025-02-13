package io.github.gunkim.realworld.share

import io.github.gunkim.realworld.application.AuthenticationService
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.service.AuthenticateUserService
import io.github.gunkim.realworld.domain.user.service.CreateUserService
import io.kotest.core.spec.style.FreeSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional

/**
 * A common class for API integration tests.
 * Integration tests implemented by inheriting this class are designed to reuse the Spring Context. This speeds up the integration tests.
 * When using the database, each test case is rolled back after execution to maintain a clean database state.
 */
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
abstract class IntegrationTest : FreeSpec() {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var createUserService: CreateUserService

    @Autowired
    protected lateinit var authenticationUserService: AuthenticateUserService

    @Autowired
    protected lateinit var authenticationService: AuthenticationService

    init {
        extensions(SpringTestExtension(SpringTestLifecycleMode.Root))
    }

    /**
     * Creates a new user and generates an authentication token for the user.
     *
     * @param email the email address of the user to create
     * @param username the username of the user to create
     * @param password the raw password of the user to create
     * @return a pair consisting of the created user and the generated authentication token
     */
    protected fun createUser(email: String, username: String, password: String): Pair<User, String> {
        val user = createUserService.createUser(
            email = email,
            username = username,
            encodedPassword = authenticationUserService.encodePassword(password)
        )

        return user to authenticationService.createToken(user.uuid).let(::toToken)
    }

    private fun toToken(token: String) = "$BEARER_TOKEN_PREFIX$token"

    companion object {
        private const val BEARER_TOKEN_PREFIX = "Bearer "
    }
}