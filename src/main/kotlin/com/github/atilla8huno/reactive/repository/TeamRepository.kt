package com.github.atilla8huno.reactive.repository

import com.github.atilla8huno.reactive.model.Team
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface TeamRepository : ReactiveCrudRepository<Team, Long>
