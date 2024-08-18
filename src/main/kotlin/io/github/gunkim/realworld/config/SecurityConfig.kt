package io.github.gunkim.realworld.config

import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .cors { it.disable() }
            .sessionManagement { it.disable() }
            .headers { it.frameOptions { it.sameOrigin() } }
            .authorizeHttpRequests {
                it.requestMatchers("/api/users/**").permitAll()
                it.requestMatchers(PathRequest.toH2Console()).permitAll()
                it.anyRequest().authenticated()
            }
            .build()
    }
}