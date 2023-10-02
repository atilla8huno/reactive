package com.github.atilla8huno.reactive.service

import com.github.atilla8huno.reactive.model.Player
import com.github.atilla8huno.reactive.model.Team
import com.github.atilla8huno.reactive.repository.PlayerRepository
import com.github.atilla8huno.reactive.repository.TeamRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class TeamProviderUTest {

    private lateinit var playerRepository: PlayerRepository
    private lateinit var teamRepository: TeamRepository

    private lateinit var underTest: TeamProvider

    @BeforeEach
    fun setUp() {
        playerRepository = mockk<PlayerRepository>()
        teamRepository = mockk<TeamRepository>()

        underTest = TeamProviderImpl(teamRepository, playerRepository)
    }

    @Test
    fun `findById should call teamRepository findById and also fetch team's players`() {
        // given
        val expectedId = 10L
        val team = Team(id = expectedId, name = "Real Madrid FC")

        every { teamRepository.findById(eq(expectedId)) } returns Mono.just(team)
        every { playerRepository.findByTeamId(eq(expectedId)) } returns Flux.just(
            Player(id = 1, name = "Cristiano Ronaldo", number = 7, teamId = expectedId),
            Player(id = 2, name = "Zinedine Zidane", number = 5, teamId = expectedId),
            Player(id = 3, name = "Ronaldo", number = 9, teamId = expectedId)
        )

        // when
        val teamMono = underTest.findById(expectedId)

        // then
        StepVerifier
            .create(teamMono)
            .assertNext {
                assertThat(it.id).isEqualTo(expectedId)

                assertThat(it.players).isNotEmpty()
                assertThat(it.players).hasSize(3)
                assertThat(it.players).contains(
                    Player(id = 1, name = "Cristiano Ronaldo")
                )

                verify { teamRepository.findById(eq(expectedId)) }
                verify { playerRepository.findByTeamId(eq(expectedId)) }
            }
            .expectComplete()
            .verify()
    }
}
