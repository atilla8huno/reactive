package com.github.atilla8huno.reactive.dto

import java.time.LocalDate

data class TeamDTO(
    var id: Long? = null,
    var name: String? = null,
    var foundedAt: LocalDate? = null,
    var players: List<PlayerDTO>? = null
)
