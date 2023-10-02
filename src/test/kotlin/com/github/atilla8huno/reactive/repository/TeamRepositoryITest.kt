package com.github.atilla8huno.reactive.repository

import com.github.atilla8huno.reactive.model.Team
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import reactor.test.StepVerifier

@DataR2dbcTest
internal class TeamRepositoryITest {

    @Autowired
    private lateinit var teamRepository: TeamRepository

    @AfterEach
    fun tearDown() {
        teamRepository.deleteAll().block()
    }

    @Test
    fun `should save teams`() {
        // given
        val team = Team(
            name = "Real Madrid FC",
            foundedAt = LocalDate.of(1900, 5, 23)
        )

        // when
        val monoTeam = teamRepository.save(team)

        // then
        StepVerifier
            .create(monoTeam)
            .assertNext { savedTeam ->
                assertThat(savedTeam.id).isNotNull()
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun `should list previously saved teams`() {
        // given
        val expectedNumberOfTeams = 10
        createRandomTeams(expectedNumberOfTeams)

        // when
        val fluxTeams = teamRepository.findAll()

        // then
        StepVerifier
            .create(fluxTeams)
            .assertNext { firstTeam ->
                assertThat(firstTeam.id).isNotNull()
            }
            .expectNextCount(expectedNumberOfTeams.toLong())
            .expectComplete()
            .verify()
    }

    private fun createRandomTeams(quantity: Int) {
        for (i in 0..quantity) {
            val team = Team(
                name = "Team $i FC",
                foundedAt = LocalDate.now(),
            )
            teamRepository.save(team).block()
        }
    }
}
