package com.github.atilla8huno.reactive.api

import com.github.atilla8huno.reactive.dto.TeamDTO
import com.github.atilla8huno.reactive.model.Player
import com.github.atilla8huno.reactive.model.Team
import com.github.atilla8huno.reactive.repository.PlayerRepository
import com.github.atilla8huno.reactive.repository.TeamRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class TeamCtrlITest(
    @Autowired private val client: WebTestClient
) {
    @MockkBean
    private lateinit var teamRepository: TeamRepository

    @MockkBean
    private lateinit var playerRepository: PlayerRepository

    @Test
    fun `fetchDetails should return details of a team including its players`() {
        // given
        val expectedTeamId = 1L
        every { teamRepository.findById(eq(expectedTeamId)) } returns Mono.just(
            Team(id = expectedTeamId, name = "Vasco da Gama")
        )
        every { playerRepository.findByTeamId(eq(expectedTeamId)) } returns Flux.just(
            Player(id = 1, name = "Ronaldo", number = 9, teamId = expectedTeamId),
            Player(id = 2, name = "Romário", number = 11, teamId = expectedTeamId),
            Player(id = 3, name = "Ronaldinho", number = 10, teamId = expectedTeamId)
        )

        // when
        client.get()
            .uri("/api/teams/$expectedTeamId")
            .exchange()
            // then
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(TeamDTO::class.java)
            .consumeWith { response ->
                val body = response.responseBody
                assertThat(body).isNotNull
                assertThat(body!!.id).isEqualTo(expectedTeamId)
                assertThat(body.players).hasSize(3)
            }
    }
}
