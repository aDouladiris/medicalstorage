package com.unipi.adouladiris.medicalstorage.configuration.swagger;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.web.scanners.ApiListingScanner;
import springfox.documentation.spring.web.scanners.ApiModelReader;
import springfox.documentation.spring.web.scanners.ApiModelSpecificationReader;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


//https://www.javainuse.com/spring/boot_swagger
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Medical Storage API v1")
                .apiInfo(apiInfo())
                .select()
                .apis( RequestHandlerSelectors.basePackage( "com.unipi.adouladiris.medicalstorage.rest.controllers" ) )
                .paths(PathSelectors.ant("/api/v1/**"))
                //.paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MedicalStorage API")
                .description("A Java RestFul API web application utilizing Spring Boot and Hibernate frameworks.")
                .license("No License")
                .licenseUrl("cloudaros@homail.com").version("1").build();
    }

//    @Primary
//    @Bean
//    public ApiListingScanner addExtraOperations(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader,
//                                                ApiModelSpecificationReader modelSpecificationReader,
//                                                DocumentationPluginsManager pluginsManager)
//    {
//        return new FormLoginOperations(apiDescriptionReader, apiModelReader, modelSpecificationReader, pluginsManager);
//    }

}
