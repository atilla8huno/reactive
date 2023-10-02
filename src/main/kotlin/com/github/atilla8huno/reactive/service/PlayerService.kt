package com.github.atilla8huno.reactive.service

import com.github.atilla8huno.reactive.dto.EntityMapper
import com.github.atilla8huno.reactive.dto.PlayerDTO
import com.github.atilla8huno.reactive.model.Player
import com.github.atilla8huno.reactive.model.Team
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PlayerService {
    fun createPlayer(request: PlayerDTO): Mono<PlayerDTO>
    fun updatePlayer(id: Long, request: PlayerDTO): Mono<PlayerDTO>
    fun fetchDetails(id: Long): Mono<PlayerDTO>
    fun fetchAll(): Flux<PlayerDTO>
    fun fetchTeamPlayers(teamId: Long): Flux<PlayerDTO>
    fun transferTeam(playerId: Long, currentTeamId: Long, newTeamId: Long): Mono<PlayerDTO>
}

@Service
class PlayerServiceImpl(
    @Autowired private val playerProvider: PlayerProvider,
    private val playerMapper: EntityMapper<Player, PlayerDTO> = PlayerDTO.Companion
) : PlayerService {
    override fun createPlayer(request: PlayerDTO): Mono<PlayerDTO> {
        return Mono.just(request)
            .map(playerMapper::toEntity)
            .flatMap { player ->
                playerProvider.savePlayer(player)
            }
            .map(playerMapper::fromEntity)
    }

    override fun updatePlayer(id: Long, request: PlayerDTO): Mono<PlayerDTO> {
        return playerProvider
            .findById(id)
            .doOnNext { player ->
                player.apply {
                    name = request.name ?: name
                    number = request.number ?: number
                }
            }
            .flatMap(playerProvider::savePlayer)
            .map(playerMapper::fromEntity)
    }

    override fun fetchDetails(id: Long): Mono<PlayerDTO> {
        return playerProvider
            .findById(id)
            .map(playerMapper::fromEntity)
    }

    override fun fetchAll(): Flux<PlayerDTO> {
        return playerProvider
            .findAll()
            .map(playerMapper::fromEntity)
    }

    override fun fetchTeamPlayers(teamId: Long): Flux<PlayerDTO> {
        return playerProvider
            .findByTeam(Team(id = teamId, name = ""))
            .map(playerMapper::fromEntity)
    }

    override fun transferTeam(playerId: Long, currentTeamId: Long, newTeamId: Long): Mono<PlayerDTO> {
        return playerProvider
            .findById(playerId)
            .doOnNext { player ->
                check(currentTeamId == player.teamId) { "[CurrentTeamID] differs from actual [TeamID]" }
            }
            .doOnNext { player ->
                player.apply {
                    teamId = newTeamId
                }
            }
            .flatMap(playerProvider::savePlayer)
            .map(playerMapper::fromEntity)
    }
}
