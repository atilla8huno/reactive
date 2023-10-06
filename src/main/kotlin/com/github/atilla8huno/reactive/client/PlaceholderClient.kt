package com.github.atilla8huno.reactive.client

import com.github.atilla8huno.reactive.dto.PostDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PlaceholderClient {
    fun fetchPosts(): Flux<PostDTO>
    fun createPost(post: PostDTO): Mono<PostDTO>
}

@Service
class PlaceholderClientImpl(
    @Autowired(required = false) baseUrl: String = "https://jsonplaceholder.typicode.com"
) : PlaceholderClient {

    private val client: WebClient = WebClient.create(baseUrl)

    override fun fetchPosts(): Flux<PostDTO> {
        return client.get()
            .uri("/posts")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToFlux(PostDTO::class.java)
    }

    override fun createPost(post: PostDTO): Mono<PostDTO> {
        return client.post()
            .uri("/posts")
            .body(BodyInserters.fromValue(post))
            .retrieve()
            .bodyToMono(PostDTO::class.java)
    }
}
