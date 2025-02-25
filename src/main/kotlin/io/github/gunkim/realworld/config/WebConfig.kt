package io.github.gunkim.realworld.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.gunkim.realworld.config.resolver.JsonRequestArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val objectMapper: ObjectMapper,
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(JsonRequestArgumentResolver(objectMapper))
    }
}