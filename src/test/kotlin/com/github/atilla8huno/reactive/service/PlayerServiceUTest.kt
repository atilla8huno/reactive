package com.github.atilla8huno.reactive.service

import com.github.atilla8huno.reactive.dto.PlayerDTO
import com.github.atilla8huno.reactive.model.Player
import com.github.atilla8huno.reactive.model.Team
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
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
    fun `transferTeam should receive old and new team ID and player ID and call PlayerProvider to update the team ID`() {
        // given
        val playerId = 1L
        val currentTeamId = 100L
        val newTeamId = 200L
        every { playerProvider.findById(eq(playerId)) } returns Mono.just(
            Player(id = playerId, name = "Bob Dinamite").apply { team = Team(id = currentTeamId, name = "Vasco") }
        )
        every { playerProvider.savePlayer(any()) } returns Mono.just(
            Player(id = playerId, name = "Bob Dinamite").apply { team = Team(id = newTeamId, name = "Real Madrid") }
        )

        // when
        val response: Mono<PlayerDTO> = underTest.transferTeam(playerId, currentTeamId, newTeamId)

        // then
        StepVerifier
            .create(response)
            .assertNext { savedPlayer ->
                assertThat(savedPlayer.id).isEqualTo(playerId)
                assertThat(savedPlayer.team!!.id).isEqualTo(newTeamId)

                verify { playerProvider.savePlayer(any()) }
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun `transferTeam should throw exception if currentTeamId received is different from teamId of player`() {
        // given
        val playerId = 1L
        val actualCurrentTeamId = 100L
        val notReallyTheCurrentTeamId = 200L

        every { playerProvider.findById(eq(playerId)) } returns Mono.just(
            Player(id = playerId, name = "Bob Dinamite").apply { team = Team(id = actualCurrentTeamId, name = "Vasco") }
        )

        // when
        val response: Mono<PlayerDTO> = underTest.transferTeam(playerId, notReallyTheCurrentTeamId, 123L)

        // then
        StepVerifier
            .create(response)
            .expectErrorMatches { throwable ->
                throwable is IllegalStateException &&
                        throwable.message.equals("[CurrentTeamID] differs from actual [TeamID]")
            }
            .verify()
    }

    @Test
    fun `createPlayer should receive PlayerDTO and call PlayerProvider to save a new player`() {
        // given
        val request = PlayerDTO(
            name = "Roberto Dinamite",
            number = 10
        )
        every { playerProvider.savePlayer(any()) } returns Mono.just(PlayerDTO.toEntity(request))

        // when
        val response: Mono<PlayerDTO> = underTest.createPlayer(request)

        // then
        StepVerifier
            .create(response)
            .assertNext { savedPlayer ->
                assertThat(savedPlayer.name).isEqualTo(request.name)
                verify { playerProvider.savePlayer(any()) }
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun `updatePlayer should receive PlayerDTO and call PlayerProvider to update an existing player`() {
        // given
        val request = PlayerDTO(
            id = 1L,
            name = "Roberto Dinamite",
            number = 10
        )
        every { playerProvider.findById(eq(1L)) } returns Mono.just(Player(id = 1L, name = "Bob Dinamite"))
        every { playerProvider.savePlayer(any()) } returns Mono.just(PlayerDTO.toEntity(request))

        // when
        val response: Mono<PlayerDTO> = underTest.updatePlayer(request.id!!, request)

        // then
        StepVerifier
            .create(response)
            .assertNext { savedPlayer ->
                assertThat(savedPlayer.id).isEqualTo(request.id)
                assertThat(savedPlayer.name).isEqualTo(request.name)

                verify { playerProvider.savePlayer(any()) }
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun `fetchDetails should receive a player ID and call PlayerProvider to find a player by ID`() {
        // given
        val id = 1L
        every { playerProvider.findById(eq(id)) } returns Mono.just(Player(id = 1L, name = "Bob Dinamite"))

        // when
        val response: Mono<PlayerDTO> = underTest.fetchDetails(id)

        // then
        StepVerifier
            .create(response)
            .assertNext { player ->
                assertThat(player.id).isEqualTo(id)

                verify { playerProvider.findById(eq(id)) }
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun `fetchAll should call PlayerProvider to find all players`() {
        // given
        every { playerProvider.findAll() } returns Flux.just(
            Player(id = 1L, name = "Bob Dinamite", number = 10),
            Player(id = 2L, name = "Romario", number = 11)
        )

        // when
        val response: Flux<PlayerDTO> = underTest.fetchAll()

        // then
        StepVerifier
            .create(response)
            .assertNext {
                verify { playerProvider.findAll() }
            }
            .expectNextCount(1)
            .expectComplete()
            .verify()
    }

    @Test
    fun `fetchTeamPlayers should call PlayerProvider to find all players by Team`() {
        // given
        val teamId = 1L
        every { playerProvider.findByTeam(any()) } returns Flux.just(
            Player(id = 1L, name = "Bob Dinamite", number = 10)
                .apply { team = Team(id = teamId, name = "Vasco da Gama") },
            Player(id = 2L, name = "Romario", number = 11, teamId = teamId)
                .apply { team = Team(id = teamId, name = "Vasco da Gama") }
        )

        // when
        val response: Flux<PlayerDTO> = underTest.fetchTeamPlayers(teamId)

        // then
        StepVerifier
            .create(response)
            .assertNext { player ->
                assertThat(player.team!!.id).isEqualTo(teamId)

                verify { playerProvider.findByTeam(any()) }
            }
            .expectNextCount(1)
            .expectComplete()
            .verify()
    }
}
