package io.pivotal.shinyay.config

import org.springframework.boot.info.BuildProperties
import org.springframework.boot.info.GitProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.*
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*

@Configuration
@EnableSwagger2
class SwaggerConfig(var build: Optional<BuildProperties>, var git: Optional<GitProperties>) {

    @Bean
    fun uiConfig(): UiConfiguration = UiConfigurationBuilder.builder()
            .deepLinking(true)
            .displayOperationId(true)
            .displayRequestDuration(true)
            .docExpansion(DocExpansion.NONE)
            .filter(true)
            .operationsSorter(OperationsSorter.ALPHA)
            .showExtensions(true)
            .tagsSorter(TagsSorter.ALPHA)
            .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
            .validatorUrl(null)
            .build()

    private fun apiInfo(version: String): ApiInfo = ApiInfoBuilder()
            .title("API for Person")
            .description("Person Management Service")
            .version(version)
            .build()

    @Bean
    fun docApi(): Docket {
        var version = "0.0.1"
        if(build.isPresent && git.isPresent) {
            val buildInfo = build.get()
            val gitInfo = git.get()
            version = "${buildInfo.version}-${gitInfo.shortCommitId}-${gitInfo.branch}"
        }
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(version))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths { it.equals("/persons") }
                .build()
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
    }
}