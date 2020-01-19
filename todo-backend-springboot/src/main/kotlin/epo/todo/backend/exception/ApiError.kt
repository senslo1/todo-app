package epo.todo.backend.exception

import org.springframework.http.HttpStatus
import java.time.Instant

/**
 * Data class used for custom error response bodies.
 */
data class ApiError(val status: HttpStatus,
                    val message: String = "Unexpected error",
                    var debugMessage: String? = null) {

    private var timestamp = Instant.now()
}