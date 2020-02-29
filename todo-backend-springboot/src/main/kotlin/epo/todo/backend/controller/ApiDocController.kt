package epo.todo.backend.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File

/**
 * REST Controller that serves API documentation that follows the OpenApi 3 specification.
 */
@RestController
class ApiDocController(@Value("\${swagger.specification.document-path}") val openApiSpecLocation: String) {

    @GetMapping(path = ["/", "/api-doc"])
    fun get(): String {
        val swaggerFileReader = File(openApiSpecLocation).reader()
        return FileCopyUtils.copyToString(swaggerFileReader)
                .also { swaggerFileReader.close() }
    }
}
