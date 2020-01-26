package epo.todo.backend.error.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.Instant
import java.util.UUID

/**
 * Data class used for custom error response bodies.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiError(val status: String,
                    val message: String,
                    val debugMessage: SwaggerValidationMessageList?,
                    val correlationId: UUID) {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private val timestamp: Instant = Instant.now()
}
