package epo.todo.backend.controller

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApiDocControllerTest {

    private val log = KotlinLogging.logger {}

    @Autowired
    lateinit var apiDocController: ApiDocController

    @Test
    fun `get api spec location should not fail`() {
        log.info { apiDocController.openApiSpecLocation }
    }

    @Test
    fun `get api spec should not fail`() {
        log.info { apiDocController.get() }
    }
}
