package com.github.atilla8huno.reactive.dto

import com.github.atilla8huno.reactive.model.Player

data class PlayerDTO(
    var id: Long? = null,
    var name: String? = null,
    var number: Int? = null,
    var teamId: Long? = null
) {
    companion object : EntityMapper<Player, PlayerDTO> {
        override fun toEntity(dto: PlayerDTO): Player {
            return Player(
                id = dto.id,
                name = dto.name!!,
                number = dto.number,
                teamId = dto.teamId
            )
        }

        override fun fromEntity(entity: Player): PlayerDTO {
            return PlayerDTO(
                id = entity.id,
                name = entity.name,
                number = entity.number,
                teamId = entity.teamId
            )
        }
    }
}
