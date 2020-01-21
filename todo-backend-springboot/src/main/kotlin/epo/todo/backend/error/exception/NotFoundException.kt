package epo.todo.backend.error.exception

/**
 * Exception thrown when returning a 404 NOT FOUND HTTP response.
 */
class NotFoundException(message: String) : RuntimeException(message)
