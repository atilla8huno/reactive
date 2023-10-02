package com.github.atilla8huno.reactive.service

import com.github.atilla8huno.reactive.dto.TeamDTO
import com.github.atilla8huno.reactive.model.Team
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class TeamServiceUTest {

    private lateinit var teamProvider: TeamProvider

    private lateinit var underTest: TeamService

    @BeforeEach
    fun setUp() {
        teamProvider = mockk<TeamProvider>()
        underTest = TeamServiceImpl(teamProvider)
    }

    @Test
    fun `fetchDetails should receive a team ID and call TeamProvider to find a team by ID`() {
        // given
        val id = 1L
        every { teamProvider.findById(eq(id)) } returns Mono.just(Team(id = 1L, name = "Vasco"))

        // when
        val response: Mono<TeamDTO> = underTest.fetchDetails(id)

        // then
        StepVerifier
            .create(response)
            .assertNext { team ->
                assertThat(team.id).isEqualTo(id)

                verify { teamProvider.findById(eq(id)) }
            }
            .expectComplete()
            .verify()
    }
}
