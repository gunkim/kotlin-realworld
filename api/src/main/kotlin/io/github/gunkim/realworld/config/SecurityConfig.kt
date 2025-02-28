package io.github.gunkim.realworld.config

import io.github.gunkim.realworld.config.security.CustomJwtAuthenticationConverter
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver
import org.springframework.security.web.SecurityFilterChain
import javax.crypto.spec.SecretKeySpec

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val secretKey: SecretKeySpec,
    @Value("\${jwt.header-prefix}")
    private val headerPrefix: String,
    @Value("\${jwt.header-name}")
    private val headerName: String,
    @Value("\${spring.h2.console.enabled:false}")
    private val h2ConsoleEnabled: Boolean,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http.csrf { it.disable() }
            .cors { it.disable() }
            .sessionManagement { it.disable() }
            .headers { configureHeaders(it) }
            .authorizeHttpRequests { configureAuthorization(it) }
            .oauth2ResourceServer {
                configureJwt(it)
            }
            .build()

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withSecretKey(secretKey).build()
    }

    /**
     * Configures the JWT authentication for the OAuth2 resource server.
     *
     * @param configurer the OAuth2ResourceServerConfigurer used to configure the JWT authentication
     */
    private fun configureJwt(configurer: OAuth2ResourceServerConfigurer<HttpSecurity>) {
        configurer.jwt {
            it.decoder(jwtDecoder())
            it.jwtAuthenticationConverter(CustomJwtAuthenticationConverter())
        }.also { it.bearerTokenResolver(customBearerTokenResolver()) }
    }

    /**
     * Resolves a bearer token from the "Authorization" header in an HTTP request.
     * Specifically, it looks for a token prefixed with "Token " and extracts the value after this prefix.
     *
     * @return An instance of BearerTokenResolver used to extract bearer tokens from HTTP requests.
     */
    private fun customBearerTokenResolver(): BearerTokenResolver = BearerTokenResolver { request: HttpServletRequest ->
        val authHeader = request.getHeader(headerName)
        if (authHeader != null && authHeader.startsWith(headerPrefix)) {
            authHeader.substring(headerPrefix.length)
        } else {
            null
        }
    }

    /**
     * Configures authorization for HTTP requests.
     *
     * @param it The AuthorizationManagerRequestMatcherRegistry instance to configure
     */
    private fun configureAuthorization(it: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry) {
        // users
        it.requestMatchers("/api/users/**").permitAll()
        it.requestMatchers("/api/profiles/**").permitAll()

        // articles
        it.requestMatchers("/api/articles/**").permitAll()

        // tags
        it.requestMatchers("/api/tags/**").permitAll()

        // H2 Console
        if (h2ConsoleEnabled) {
            it.requestMatchers(PathRequest.toH2Console()).permitAll()
        }
        it.anyRequest().authenticated()
    }

    /**
     * Configures the headers for the HTTP security.
     *
     * @param it The HeadersConfigurer<HttpSecurity> instance to configure
     */
    private fun configureHeaders(it: HeadersConfigurer<HttpSecurity>) {
        it.frameOptions { it.sameOrigin() }
    }
}