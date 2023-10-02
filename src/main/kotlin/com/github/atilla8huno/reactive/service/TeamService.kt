package com.github.atilla8huno.reactive.service

import com.github.atilla8huno.reactive.dto.EntityMapper
import com.github.atilla8huno.reactive.dto.PlayerDTO
import com.github.atilla8huno.reactive.dto.TeamDTO
import com.github.atilla8huno.reactive.model.Team
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

interface TeamService {
    fun fetchDetails(id: Long): Mono<TeamDTO>
}

@Service
class TeamServiceImpl(
    @Autowired private val teamProvider: TeamProvider,
    @Autowired(required = false) private val teamMapper: EntityMapper<Team, TeamDTO> = TeamDTO.Companion
) : TeamService {

    override fun fetchDetails(id: Long): Mono<TeamDTO> {
        return teamProvider
            .findById(id)
            .map { team ->
                val teamDTO = teamMapper.fromEntity(team)
                teamDTO.apply {
                    players = team.players.map(PlayerDTO::fromEntity)
                }
                teamDTO
            }
    }
}
