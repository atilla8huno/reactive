package com.github.atilla8huno.reactive.service

import com.github.atilla8huno.reactive.model.Team
import com.github.atilla8huno.reactive.repository.PlayerRepository
import com.github.atilla8huno.reactive.repository.TeamRepository
import java.util.stream.Collectors.toSet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

interface TeamProvider {
    fun findById(id: Long): Mono<Team>
}

@Service
class TeamProviderImpl(
    @Autowired private val teamRepository: TeamRepository,
    @Autowired private val playerRepository: PlayerRepository,
) : TeamProvider {
    override fun findById(id: Long): Mono<Team> {
        val players = playerRepository.findByTeamId(id).collect(toSet())

        return teamRepository.findById(id)
            .zipWith(players)
            .map { tuple ->
                val team = tuple.t1
                val playersSet = tuple.t2

                playersSet.onEach { player ->
                    player.team = team
                }

                team
            }
    }
}
