package com.github.atilla8huno.reactive.dto

data class PostDTO(
    val id: Long,
    val userId: Long,
    val title: String,
    val body: String
)
