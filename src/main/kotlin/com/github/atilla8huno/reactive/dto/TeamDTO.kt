package com.github.atilla8huno.reactive.dto

import com.github.atilla8huno.reactive.model.Team
import java.time.LocalDate

data class TeamDTO(
    var id: Long? = null,
    var name: String? = null,
    var foundedAt: LocalDate? = null,
    var players: List<PlayerDTO>? = null
) {
    companion object : EntityMapper<Team, TeamDTO> {
        override fun toEntity(dto: TeamDTO): Team {
            return Team(
                id = dto.id,
                name = dto.name!!,
                foundedAt = dto.foundedAt
            )
        }

        override fun fromEntity(entity: Team): TeamDTO {
            return TeamDTO(
                id = entity.id,
                name = entity.name,
                foundedAt = entity.foundedAt
            )
        }
    }
}
