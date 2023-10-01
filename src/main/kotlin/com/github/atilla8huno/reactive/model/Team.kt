package com.github.atilla8huno.reactive.model

import java.time.LocalDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("teams")
data class Team(
    @Id val id: Long? = null,
    val name: String,
    @Column("founded_at") val foundedAt: LocalDate? = null,
    @Transient val players: MutableSet<Player> = mutableSetOf()
) {
    @PersistenceCreator
    constructor(
        id: Long? = null,
        name: String,
        foundedAt: LocalDate? = null
    ) : this(id, name, foundedAt, mutableSetOf())

    fun addPlayers(vararg playersParam: Player) = players.addAll(playersParam)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Team) return false

        return id != other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
