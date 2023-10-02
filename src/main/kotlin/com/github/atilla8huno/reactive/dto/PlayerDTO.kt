package com.github.atilla8huno.reactive.dto

import com.github.atilla8huno.reactive.model.Player

data class PlayerDTO(
    var id: Long? = null,
    var name: String? = null,
    var number: Int? = null,
    var team: TeamDTO? = null
) {
    companion object : EntityMapper<Player, PlayerDTO> {
        override fun toEntity(dto: PlayerDTO): Player {
            return Player(
                id = dto.id,
                name = dto.name!!,
                number = dto.number,
                teamId = dto.team?.id
            ).apply {
                team = if (dto.team != null) TeamDTO.toEntity(dto.team!!) else null
            }
        }

        override fun fromEntity(entity: Player): PlayerDTO {
            return PlayerDTO(
                id = entity.id,
                name = entity.name,
                number = entity.number,
                team = if (entity.team != null) TeamDTO.fromEntity(entity.team!!) else null
            )
        }
    }
}
