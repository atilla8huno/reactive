package com.github.atilla8huno.reactive.api

import com.github.atilla8huno.reactive.dto.TeamDTO
import com.github.atilla8huno.reactive.service.TeamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/teams")
class TeamCtrl(
    @Autowired private val teamService: TeamService
) {
    @GetMapping("/{id}")
    fun fetchDetails(
        @PathVariable id: Long
    ): Mono<ResponseEntity<TeamDTO>> {
        return teamService
            .fetchDetails(id)
            .map { ResponseEntity.ok(it) }
    }
}
