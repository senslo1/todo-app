package epo.todo.backend.config

import com.atlassian.oai.validator.OpenApiInteractionValidator
import com.atlassian.oai.validator.springmvc.OpenApiValidationFilter
import com.atlassian.oai.validator.springmvc.OpenApiValidationInterceptor
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration class serving an [OpenApiValidationInterceptor] bean.
 * This interceptor is registered in the [WebMvcConfig].
 */
@Configuration
class OpenApiValidationConfig(@Value("\${swagger.specification.document-path}") val openApiSpec: String) {

    @Bean
    fun validationFilter(): OpenApiValidationFilter {
        return OpenApiValidationFilter(
                true,   // enable request validation
                true    // enable response validation
        )
    }

    @Bean
    fun openApiValidationInterceptor(): OpenApiValidationInterceptor {
        // TODO: 12/08/2021 remove this dirty hack once it has been fixed on Atlassian's end
        io.swagger.util.Json.mapper().registerModule(JavaTimeModule())

        val openApiInteractionValidator = OpenApiInteractionValidator
                .createForSpecificationUrl(openApiSpec)
                .build()
        return OpenApiValidationInterceptor(openApiInteractionValidator)
    }
}
