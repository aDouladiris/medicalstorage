package com.unipi.adouladiris.medicalstorage.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.unipi.adouladiris.medicalstorage.swagger.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;


//https://www.javainuse.com/spring/boot_swagger
@Configuration
@EnableSwagger2
public class SwaggerConfiguration  {

    private TypeResolver typeResolver;
    @Autowired
    public void setTypeResolver(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    public static final String UserController = "User";
    public static final String ProductController = "Product";

    // Configure User endpoint documentation.
    @Bean
    public Docket UserConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2)
                .tags(new Tag(UserController, "MedicalStorage User API"))
                .groupName("Medical Storage Api v1 - User")
                .additionalModels(
                        typeResolver.resolve(UserRequestBody.class),
                        typeResolver.resolve(RegisterUserRequestBody.class)
                )
                .apiInfo(apiInfo())
                .select()
                .apis( RequestHandlerSelectors.basePackage( "com.unipi.adouladiris.medicalstorage.rest.controllers" ) )
                .paths(PathSelectors.ant("/api/v1/user/**"))
                .build();
    }

    // Configure Product endpoint documentation.
    @Bean
    public Docket ProductConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2)
                .tags(new Tag(ProductController, "MedicalStorage Product API"))
                .groupName("Medical Storage Api v1 - Product")
                .additionalModels(
                        typeResolver.resolve(ProductUpdateRequestBody.class),
                        typeResolver.resolve(ProductInsertRequestBody.class),
                        typeResolver.resolve(ProductReplaceRequestBody.class)
                )
                .apiInfo(apiInfo())
                .globalRequestParameters(authorizationParameter())
                .select()
                .apis( RequestHandlerSelectors.basePackage( "com.unipi.adouladiris.medicalstorage.rest.controllers" ) )
                .paths(PathSelectors.ant("/api/v1/product/**"))
                .build();
    }

    // Configure general documentation information about API host.
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MedicalStorage API")
                .description("A Java RestFul API web application utilizing Spring Boot, Spring Security and Hibernate frameworks.")
                .license("No License")
                .licenseUrl("cloudaros@homail.com").version("1").build();
    }

    // Generate a headers field to assign JwT token. Each endpoint will have to use it explicitly.
    private List<RequestParameter> authorizationParameter() {
        RequestParameterBuilder tokenBuilder = new RequestParameterBuilder();
        tokenBuilder
                .name("Bearer")
                .description("Auth Token")
                .required(false)
                .in("header")
                .accepts(Collections.singleton(MediaType.APPLICATION_JSON))
                .build();

        return Collections.singletonList(tokenBuilder.build());
    }

}
