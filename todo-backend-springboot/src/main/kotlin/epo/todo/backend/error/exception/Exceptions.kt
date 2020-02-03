package epo.todo.backend.error.exception

/**
 * Exception thrown when returning a 400 BAD REQUEST HTTP response.
 */
class BadRequestException(message: String) : RuntimeException(message)

/**
 * Exception thrown when returning a 404 NOT FOUND HTTP response.
 */
class NotFoundException(message: String) : RuntimeException(message)
