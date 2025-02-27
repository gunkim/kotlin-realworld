package io.github.gunkim.realworld.share

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.gunkim.realworld.domain.auth.service.CreateTokenService
import io.github.gunkim.realworld.domain.user.model.User
import io.github.gunkim.realworld.domain.user.service.AuthenticateUserService
import io.github.gunkim.realworld.domain.user.service.CreateUserService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.core.annotation.Tags
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional

/**
 * A common class for API integration tests.
 * Integration tests implemented by inheriting this class are designed to reuse the Spring Context. This speeds up the integration tests.
 * When using the database, each test case is rolled back after execution to maintain a clean database state.
 */

val logger = KotlinLogging.logger { }

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Tags("Integration Test")
abstract class IntegrationTest : FreeSpec() {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var createUserService: CreateUserService

    @Autowired
    private lateinit var authenticationUserService: AuthenticateUserService

    @Autowired
    private lateinit var createTokenService: CreateTokenService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Value("\${jwt.header-prefix}")
    private lateinit var authHeaderPrefix: String

    @Value("\${jwt.header-name}")
    protected lateinit var authHeaderName: String

    init {
        extensions(SpringTestExtension(SpringTestLifecycleMode.Root))
    }

    final override suspend fun beforeEach(testCase: TestCase) {
        beforeEachTest(testCase)
        printLog(testCase)
    }

    fun printLog(testCase: TestCase) {
        logger.info { "Running test: ${testCase.name.testName}" }
    }

    suspend fun beforeEachTest(testCase: TestCase) {
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

        return user to createTokenService.createToken(user.id).let(::toToken)
    }

    protected fun toJsonString(any: Any) = objectMapper.writeValueAsString(any)

    private fun toToken(token: String) = "$authHeaderPrefix$token"
}