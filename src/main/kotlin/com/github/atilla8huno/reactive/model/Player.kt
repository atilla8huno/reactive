package com.github.atilla8huno.reactive.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Table

@Table("players")
data class Player(
    @Id val id: Long? = null,
    val name: String,
    val number: Int? = null,
    val email: String? = null,
    var teamId: Long? = null
) {
    @Transient var team: Team? = null
        set(value) {
            field = value
            teamId = team?.id
            team?.addPlayers(this)
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Player) return false

        return id != other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
