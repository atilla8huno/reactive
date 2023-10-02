package com.github.atilla8huno.reactive.api

import com.github.atilla8huno.reactive.dto.PlayerDTO
import com.github.atilla8huno.reactive.dto.TransferTeamDTO
import com.github.atilla8huno.reactive.model.Player
import com.github.atilla8huno.reactive.model.Team
import com.github.atilla8huno.reactive.repository.PlayerRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class PlayerCtrlITest(
    @Autowired private val client: WebTestClient
) {
    @MockkBean
    private lateinit var playerRepository: PlayerRepository

    @Test
    fun `transferTeam should update a player's team`() {
        // given
        val currentTeam = 1L
        val newTeam = 2L
        val request = TransferTeamDTO(
            currentTeam = currentTeam,
            newTeam = newTeam
        )

        every { playerRepository.findById(eq(1L)) } returns Mono.just(
            Player(id = 1, name = "CR7", number = 7).apply {
                team = Team(id = currentTeam, name = "Real Madrid")
            }
        )
        every { playerRepository.save(any()) } returns Mono.just(
            Player(id = 1, name = "CR7", number = 7).apply {
                team = Team(id = newTeam, name = "Juventus")
            }
        )

        // when
        client.patch()
            .uri("/api/players/1/transfer-team")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(Mono.just(request), TransferTeamDTO::class.java)
            .exchange()
            // then
            .expectStatus().is2xxSuccessful
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.team.id").isEqualTo(newTeam)
    }

    @Test
    fun `createPlayer should save new player and return 201`() {
        // given
        every { playerRepository.save(any()) } returns Mono.just(
            Player(id = 1, name = "CR7", number = 7)
        )
        val request = PlayerDTO(name = "CR7", number = 7)

        // when
        client.post()
            .uri("/api/players")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(Mono.just(request), PlayerDTO::class.java)
            .exchange()
            // then
            .expectStatus().isCreated
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectHeader().location("/api/players/1")
            .expectBody()
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.name").isEqualTo("CR7")
            .jsonPath("$.number").isEqualTo(7)
    }

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
