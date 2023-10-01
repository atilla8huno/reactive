package com.github.atilla8huno.reactive.service

import com.github.atilla8huno.reactive.model.Player
import com.github.atilla8huno.reactive.model.Team
import com.github.atilla8huno.reactive.repository.PlayerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PlayerProvider {
    fun savePlayer(player: Player): Mono<Player>
    fun findById(id: Long): Mono<Player>
    fun findAll(): Flux<Player>
    fun findByTeam(team: Team): Flux<Player>
}

@Service
class PlayerProviderImpl(
    @Autowired private val playerRepository: PlayerRepository
) : PlayerProvider {
    override fun savePlayer(player: Player): Mono<Player> {
        return playerRepository.save(player)
    }

    override fun findById(id: Long): Mono<Player> {
        return playerRepository.findById(id)
    }

    override fun findAll(): Flux<Player> {
        return playerRepository.findAll()
    }

    override fun findByTeam(team: Team): Flux<Player> {
        return playerRepository.findByTeamId(team.id!!)
    }
}
