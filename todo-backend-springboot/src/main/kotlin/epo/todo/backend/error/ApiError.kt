package epo.todo.backend.error

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.time.Instant
import java.util.UUID

/**
 * Data class used for custom error response bodies.
 */
data class ApiError(val status: HttpStatus,
                    val message: String,
                    val correlationId: UUID) {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private val timestamp: Instant = Instant.now()
}
