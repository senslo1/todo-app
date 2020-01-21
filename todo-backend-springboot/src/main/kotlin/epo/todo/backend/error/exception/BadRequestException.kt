package epo.todo.backend.error.exception

/**
 * Exception thrown when returning a 400 BAD REQUEST HTTP response.
 */
class BadRequestException(message: String) : RuntimeException(message)
