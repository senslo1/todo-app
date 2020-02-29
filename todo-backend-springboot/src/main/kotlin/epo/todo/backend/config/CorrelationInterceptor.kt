package epo.todo.backend.config

import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.util.UUID
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Implementation of [HandlerInterceptorAdapter] that attaches a correlation ID to SLF4J's MDC
 * (Mapped Diagnostic Context) when a REST endpoint is called. If one is provided as a header
 * then it will be used; otherwise, a new one will be created.
 * Before the response is returned, the correlation ID will be added as a response header and removed from the MDC.
 */
@Component
class CorrelationInterceptor : HandlerInterceptorAdapter() {

    override fun preHandle(request: HttpServletRequest,
                           response: HttpServletResponse,
                           handler: Any): Boolean {
        val correlationId = getCorrelationIdFromHeaderOrCreateNew(request)
        MDC.put(CORRELATION_ID_LOG_VAR_NAME, correlationId)
        return true
    }

    override fun afterCompletion(request: HttpServletRequest,
                                 response: HttpServletResponse,
                                 handler: Any,
                                 ex: Exception?) {
        response.addHeader(CORRELATION_ID_HEADER_NAME, MDC.get(CORRELATION_ID_LOG_VAR_NAME))
        MDC.remove(CORRELATION_ID_LOG_VAR_NAME)
    }

    private fun getCorrelationIdFromHeaderOrCreateNew(request: HttpServletRequest): String {
        return try {
            // It's a bit counter intuitive to call fromString and then toString, but by calling UUID.fromString(),
            // the string gets automatically validated. Any string that is not in a valid UUID format will
            // cause an IllegalArgumentException to be thrown, which allows us to assign a new one instead.
            UUID.fromString(request.getHeader(CORRELATION_ID_HEADER_NAME) ?: "").toString()
        } catch (e: IllegalArgumentException) {
            UUID.randomUUID().toString()
        }
    }

    companion object {
        private const val CORRELATION_ID_HEADER_NAME = "X-Correlation-Id"
        private const val CORRELATION_ID_LOG_VAR_NAME = "correlationId"
    }
}
