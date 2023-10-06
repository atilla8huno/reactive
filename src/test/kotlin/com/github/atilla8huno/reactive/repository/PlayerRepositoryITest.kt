package com.github.atilla8huno.reactive.repository

import com.github.atilla8huno.reactive.model.Player
import com.github.atilla8huno.reactive.model.Team
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.data.domain.Pageable
import reactor.test.StepVerifier

@DataR2dbcTest
internal class PlayerRepositoryITest {

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Autowired
    private lateinit var teamRepository: TeamRepository

    @AfterEach
    fun tearDown() {
        playerRepository.deleteAll().block()
        teamRepository.deleteAll().block()
    }

    @Test
    fun `should save players`() {
        // given
        val player = Player(
            name = "Cristiano Ronaldo",
            number = 7
        )

        // when
        val monoPlayer = playerRepository.save(player)

        // then
        StepVerifier
            .create(monoPlayer)
            .assertNext { savedPlayer ->
                assertThat(savedPlayer.id).isNotNull()
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun `should list previously saved players`() {
        // given
        val expectedNumberOfPlayers = 10
        createRandomPlayers(expectedNumberOfPlayers)

        // when
        val fluxPlayers = playerRepository.findAll()

        // then
        StepVerifier
            .create(fluxPlayers)
            .assertNext { firstPlayer ->
                assertThat(firstPlayer.id).isNotNull()
            }
            .expectNextCount(expectedNumberOfPlayers.toLong() - 1)
            .expectComplete()
            .verify()
    }

    @Test
    fun `should find players by teamId pageable`() {
        // given
        val expectedNumberOfPlayers = 10
        val expectedTeamId = createRandomPlayers(20)

        // when
        val fluxPlayers = playerRepository.findByTeamId(expectedTeamId, Pageable.ofSize(expectedNumberOfPlayers))

        // then
        StepVerifier
            .create(fluxPlayers)
            .assertNext { firstPlayer ->
                assertThat(firstPlayer.id).isNotNull()
            }
            .expectNextCount(expectedNumberOfPlayers - 1L)
            .expectComplete()
            .verify()
    }

    private fun createRandomPlayers(quantity: Int): Long {
        val team = Team(
            name = "Real Madrid FC"
        ).let {
            teamRepository.save(it).block()
        }
        for (i in 1..quantity) {
            val player = Player(
                name = "Player $i",
                number = 1 + i
            ).apply {
                this.team = team
            }
            playerRepository.save(player).block()
        }
        return team?.id!!
    }
}
