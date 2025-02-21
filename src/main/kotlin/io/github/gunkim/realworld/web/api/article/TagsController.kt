package io.github.gunkim.realworld.web.api.article

import io.github.gunkim.realworld.domain.article.model.Tag
import io.github.gunkim.realworld.domain.article.service.GetTagService
import io.github.gunkim.realworld.web.api.article.model.response.TagResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/tags")
interface TagsResource {
    @GetMapping
    fun getTags(): TagResponse
}

@RestController
class TagsController(
    private val getTagService: GetTagService,
) : TagsResource {
    override fun getTags() =
        getTagService.getTags()
            .map(Tag::name)
            .let(::TagResponse)
}