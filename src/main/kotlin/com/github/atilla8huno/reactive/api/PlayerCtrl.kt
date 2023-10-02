package com.github.atilla8huno.reactive.api

import com.github.atilla8huno.reactive.dto.PlayerDTO
import com.github.atilla8huno.reactive.service.PlayerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/players")
class PlayerCtrl(
    @Autowired private val playerService: PlayerService
) {
    @GetMapping
    fun fetchAll(): Flux<PlayerDTO> {
        return playerService.fetchAll()
    }
}
