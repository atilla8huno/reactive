package com.github.atilla8huno.reactive.api

import com.github.atilla8huno.reactive.dto.PlayerDTO
import com.github.atilla8huno.reactive.dto.TransferTeamDTO
import com.github.atilla8huno.reactive.service.PlayerService
import java.net.URI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/players")
class PlayerCtrl(
    @Autowired private val playerService: PlayerService
) {
    @GetMapping
    fun fetchAll(): Flux<PlayerDTO> {
        return playerService.fetchAll()
    }

    @PostMapping
    fun createPlayer(
        @RequestBody request: PlayerDTO
    ): Mono<ResponseEntity<PlayerDTO>> {
        return playerService.createPlayer(request)
            .map { player ->
                ResponseEntity
                    .created(URI.create("/api/players/${player.id}"))
                    .body(player)
            }
    }

    @PatchMapping("/{id}/transfer-team")
    fun transferTeam(
        @PathVariable id: Long,
        @RequestBody request: TransferTeamDTO
    ): Mono<ResponseEntity<PlayerDTO>> {
        return playerService.transferTeam(id, request.currentTeam, request.newTeam)
            .map { player ->
                ResponseEntity.ok(player)
            }
    }
}
