package kr.hs.dgsw.gg.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket


@Configuration
class SwaggerConfiguration {
    private fun swaggerInfo(): ApiInfo {
        return ApiInfoBuilder().title("DGSWGG API")
            .description("DGSWGG API Docs").build()
    }

    @Bean
    fun swaggerApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .consumes(consumeContentTypes)
            .produces(produceContentTypes)
            .apiInfo(swaggerInfo()).select()
            .apis(RequestHandlerSelectors.basePackage("kr.hs.dgsw.gg.controller"))
            .paths(PathSelectors.any())
            .build()
            .useDefaultResponseMessages(false)
    }

    private val consumeContentTypes: Set<String>
        get() {
            val consumes: MutableSet<String> = HashSet()
            consumes.add("application/json;charset=UTF-8")
            consumes.add("application/x-www-form-urlencoded")
            return consumes
        }
    private val produceContentTypes: Set<String>
        get() {
            val produces: MutableSet<String> = HashSet()
            produces.add("application/json;charset=UTF-8")
            return produces
        }
}