package com.github.atilla8huno.reactive.dto

import com.github.atilla8huno.reactive.model.Player

data class PlayerDTO(
    var id: Long? = null,
    var name: String? = null,
    var number: Int? = null,
    var email: String? = null,
    var team: TeamDTO? = null
) {
    companion object {
        fun toEntity(playerDTO: PlayerDTO): Player {
            return Player(
                id = playerDTO.id,
                name = playerDTO.name!!,
                email = playerDTO.email,
                number = playerDTO.number,
                teamId = playerDTO.team?.id
            )
        }

        fun fromEntity(player: Player): PlayerDTO {
            return PlayerDTO(
                id = player.id,
                name = player.name,
                email = player.email,
                number = player.number
            )
        }
    }
}
