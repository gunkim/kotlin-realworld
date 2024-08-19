package io.github.gunkim.realworld.config.request

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class JsonRequestArgumentResolver(
    private val objectMapper: ObjectMapper,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(JsonRequest::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val annotation = parameter.getParameterAnnotation(JsonRequest::class.java)!!
        val subTreeKey = annotation.value

        val body = webRequest.getNativeRequest(HttpServletRequest::class.java)!!.reader.use { it.readText() }
        val node = objectMapper.readTree(body)
        val subTreeNode =
            node.get(subTreeKey) ?: throw IllegalArgumentException("JSON does not contain '$subTreeKey' field")

        return objectMapper.treeToValue(subTreeNode, parameter.parameterType)
    }
}