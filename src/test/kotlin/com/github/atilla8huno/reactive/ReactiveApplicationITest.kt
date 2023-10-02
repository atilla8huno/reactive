package com.github.atilla8huno.reactive

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ReactiveApplicationITest {

	@Test
	fun contextLoads() {
	}
}
