package epo.todo.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Exception thrown when returning a 400 BAD REQUEST HTTP response.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class BadRequestException(message: String) : RuntimeException(message)
