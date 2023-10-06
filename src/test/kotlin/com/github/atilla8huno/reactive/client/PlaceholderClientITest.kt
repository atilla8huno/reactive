package com.github.atilla8huno.reactive.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.atilla8huno.reactive.dto.PostDTO
import javax.swing.text.html.HTML.Tag.I
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

internal class PlaceholderClientITest {

    private lateinit var underTest: PlaceholderClient

    @BeforeEach
    fun setUp() {
        val baseUrl = "http://localhost:${mockWebServer.port}"
        underTest = PlaceholderClientImpl(baseUrl)
    }

    @Test
    fun `should POST create a post`() {
        // given
        val request = PostDTO(1L, 1L, "Title", "Body")
        mockWebServer.enqueue(
            MockResponse()
                .setBody(ObjectMapper().writeValueAsString(request))
                .setHeader("Content-Type", "application/json")
        )

        // when
        val response = underTest.createPost(request)

        // then
        StepVerifier
            .create(response)
            .assertNext { postCreated ->
                assertThat(postCreated.id).isEqualTo(request.id)
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun `should GET a hundred posts`() {
        // given
        val expectedResponse = createPosts(100)
        mockWebServer.enqueue(
            MockResponse()
                .setBody(ObjectMapper().writeValueAsString(expectedResponse))
                .setHeader("Content-Type", "application/json")
        )

        // when
        val response = underTest.fetchPosts()

        // then
        StepVerifier
            .create(response)
            .assertNext { post ->
                assertThat(post.title).contains("Title ${post.id}")
                assertThat(post.body).contains("Body ${post.id}")
            }
            .expectNextCount(99)
            .expectComplete()
            .verify()
    }

    private fun createPosts(quantity: Int): List<PostDTO> {
        val posts = mutableListOf<PostDTO>()
        for (i in 1L..quantity) posts.add(PostDTO(i, i, "Title $i", "Body $i"))
        return posts
    }

    companion object {
        private lateinit var mockWebServer: MockWebServer

        @JvmStatic
        @BeforeAll
        fun beforeAllSetUp() {
            mockWebServer = MockWebServer()
            mockWebServer.start()
        }

        @JvmStatic
        @AfterAll
        fun afterAllTearDown() {
            mockWebServer.shutdown()
        }
    }
}
