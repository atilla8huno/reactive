package com.github.atilla8huno.reactive.service

import com.github.atilla8huno.reactive.dto.PlayerDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class PlayerServiceUTest {

    private lateinit var playerProvider: PlayerProvider

    private lateinit var underTest: PlayerService

    @BeforeEach
    fun setUp() {
        playerProvider = mockk<PlayerProvider>()
        underTest = PlayerServiceImpl(playerProvider)
    }

    @Test
    fun `createPlayer should receive PlayerDTO and call PlayerProvider to save a new player`() {
        // given
        val request = PlayerDTO(
            name = "Roberto Dinamite",
            number = 10,
            email = "dinamite@crvg.com"
        )
        every { playerProvider.savePlayer(any()) } returns Mono.just(PlayerDTO.toEntity(request))

        // when
        val response: Mono<PlayerDTO> = underTest.createPlayer(request)

        // then
        StepVerifier
            .create(response)
            .assertNext { savedPlayer ->
                assertThat(savedPlayer.email).isEqualTo(request.email)
                verify { playerProvider.savePlayer(any()) }
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun `updatePlayer should receive PlayerDTO and call PlayerProvider to update an existing player`() {
        // given
        // when
        // then
    }

    @Test
    fun `fetchDetails should receive a player ID and call PlayerProvider to find a player by ID`() {
        // given
        // when
        // then
    }

    @Test
    fun `fetchAll should call PlayerProvider to find all players`() {
        // given
        // when
        // then
    }

    @Test
    fun `fetchTeamPlayers should call PlayerProvider to find all players by Team`() {
        // given
        // when
        // then
    }
}
