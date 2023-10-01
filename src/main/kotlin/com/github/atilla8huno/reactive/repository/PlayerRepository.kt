package com.github.atilla8huno.reactive.repository

import com.github.atilla8huno.reactive.model.Player
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface PlayerRepository : ReactiveCrudRepository<Player, Long> {
    companion object {
        val DEFAULT_PAGEABLE = Pageable.ofSize(10).first()
    }

    fun findByTeamId(
        teamId: Long,
        pageable: Pageable = DEFAULT_PAGEABLE
    ): Flux<Player>
}
