package com.github.atilla8huno.reactive.dto

interface EntityMapper<E, D> {
    fun toEntity(dto: D): E
    fun fromEntity(entity: E): D
}
