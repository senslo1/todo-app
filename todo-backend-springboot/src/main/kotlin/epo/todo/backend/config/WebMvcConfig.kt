package epo.todo.backend.config

import com.atlassian.oai.validator.springmvc.OpenApiValidationInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * A [WebMvcConfigurer] used for adding custom handler interceptors.
 */
@Configuration
class WebMvcConfig(val correlationInterceptor: CorrelationInterceptor,
                   val openApiValidationInterceptor: OpenApiValidationInterceptor) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(correlationInterceptor)
        registry.addInterceptor(openApiValidationInterceptor)
    }
}
