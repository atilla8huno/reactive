package com.github.atilla8huno.reactive.service

import com.github.atilla8huno.reactive.dto.PlayerDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

interface PlayerService {
    fun createPlayer(request: PlayerDTO): Mono<PlayerDTO>
}

@Service
class PlayerServiceImpl(
    @Autowired private val playerProvider: PlayerProvider
) : PlayerService {
    override fun createPlayer(request: PlayerDTO): Mono<PlayerDTO> {
        return Mono.just(request)
            .map(PlayerDTO.Companion::toEntity)
            .flatMap { player ->
                playerProvider.savePlayer(player)
            }
            .map(PlayerDTO.Companion::fromEntity)
    }
}
