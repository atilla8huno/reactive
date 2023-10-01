package com.github.atilla8huno.reactive.service

import com.github.atilla8huno.reactive.model.Player
import com.github.atilla8huno.reactive.model.Team
import com.github.atilla8huno.reactive.repository.PlayerRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class PlayerProviderUTest {

    private lateinit var playerRepository: PlayerRepository

    private lateinit var underTest: PlayerProvider

    @BeforeEach
    fun setUp() {
        playerRepository = mockk<PlayerRepository>()
        underTest = PlayerProviderImpl(playerRepository)
    }

    @Test
    fun `savePlayer should call playerRepository save`() {
        // given
        val player = Player(name = "Player One")

        every { playerRepository.save(eq(player)) } returns Mono.just(player)

        // when
        val playerMono = underTest.savePlayer(player)

        // then
        StepVerifier
            .create(playerMono)
            .assertNext {
                verify { playerRepository.save(eq(player)) }
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun `findById should call playerRepository findById`() {
        // given
        val expectedId = 10L
        val player = Player(id = expectedId, name = "Player One")

        every { playerRepository.findById(eq(expectedId)) } returns Mono.just(player)

        // when
        val playerMono = underTest.findById(expectedId)

        // then
        StepVerifier
            .create(playerMono)
            .assertNext {
                verify { playerRepository.findById(eq(expectedId)) }
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun `findAll should call playerRepository findAll`() {
        // given
        val player1 = Player(name = "Player One")
        val player2 = Player(name = "Player Two")

        every { playerRepository.findAll() } returns Flux.just(player1, player2)

        // when
        val playerMono = underTest.findAll()

        // then
        StepVerifier
            .create(playerMono)
            .assertNext {
                verify { playerRepository.findAll() }
            }
            .expectNextCount(1)
            .expectComplete()
            .verify()
    }

    @Test
    fun `findByTeam should call playerRepository findByTeamId`() {
        // given
        val expectedTeamId = 1L
        val player1 = Player(name = "Player One")
        val player2 = Player(name = "Player Two")

        every { playerRepository.findByTeamId(eq(expectedTeamId), any()) } returns Flux.just(player1, player2)

        // when
        val playerMono = underTest.findByTeam(Team(id = expectedTeamId, name = "Does Not Matter FC"))

        // then
        StepVerifier
            .create(playerMono)
            .assertNext {
                verify { playerRepository.findByTeamId(eq(expectedTeamId), any()) }
            }
            .expectNextCount(1)
            .expectComplete()
            .verify()
    }
}
