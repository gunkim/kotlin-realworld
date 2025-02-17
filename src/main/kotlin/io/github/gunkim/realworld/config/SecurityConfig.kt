package io.github.gunkim.realworld.config

import io.github.gunkim.realworld.config.security.CustomJwtAuthenticationConverter
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain
import javax.crypto.spec.SecretKeySpec

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val secretKey: SecretKeySpec,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http.csrf { it.disable() }
            .cors { it.disable() }
            .sessionManagement { it.disable() }
            .headers { configureHeaders(it) }
            .authorizeHttpRequests { configureAuthorization(it) }
            .oauth2ResourceServer { configureJwt(it) }
            .build()

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withSecretKey(secretKey).build()
    }

    /**
     * Configures authorization for HTTP requests.
     *
     * @param it The AuthorizationManagerRequestMatcherRegistry instance to configure
     */
    private fun configureAuthorization(it: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry) {
        // users
        it.requestMatchers("/api/users/**").permitAll()
        it.requestMatchers(HttpMethod.GET, "/api/profiles/**").permitAll()

        // articles
        it.requestMatchers("/api/articles/**").permitAll()

        // tags
        it.requestMatchers("/api/tags/**").permitAll()

        // H2 Console
        it.requestMatchers(PathRequest.toH2Console()).permitAll()
        it.anyRequest().authenticated()
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
        }
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