package com.github.atilla8huno.reactive.api

import com.github.atilla8huno.reactive.dto.PlayerDTO
import com.github.atilla8huno.reactive.model.Player
import com.github.atilla8huno.reactive.repository.PlayerRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import reactor.core.publisher.Flux

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class PlayerCtrlITest(
    @Autowired private val client: WebTestClient
) {

    @MockkBean
    private lateinit var playerRepository: PlayerRepository

    @Test
    fun `fetchAll should return list of all players`() {
        // given
        every { playerRepository.findAll() } returns Flux.just(
            Player(id = 1, name = "Ronaldo", number = 9),
            Player(id = 2, name = "Romário", number = 11),
            Player(id = 3, name = "Ronaldinho", number = 10)
        )

        // when
        client.get()
            .uri("/api/players")
            .exchange()
            // then
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList<PlayerDTO>()
            .hasSize(3)
            .contains(
                PlayerDTO(id = 2, name = "Romário", number = 11)
            )
    }
}
