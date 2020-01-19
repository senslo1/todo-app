package epo.todo.backend.exception

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.time.Instant

/**
 * Data class used for custom error response bodies.
 */
data class ApiError(val status: HttpStatus,
                    val message: String = "Unexpected error",
                    var debugMessage: String? = null) {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private val timestamp: Instant = Instant.now()
}