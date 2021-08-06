package com.unipi.adouladiris.medicalstorage.configuration.swagger;

import com.unipi.adouladiris.medicalstorage.entities.users.User;
import com.unipi.adouladiris.medicalstorage.rest.dto.RegisterUserRequestBody;
import com.unipi.adouladiris.medicalstorage.rest.dto.UserRequestBody;
import io.swagger.annotations.Api;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.web.scanners.ApiListingScanner;
import springfox.documentation.spring.web.scanners.ApiModelReader;
import springfox.documentation.spring.web.scanners.ApiModelSpecificationReader;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import com.fasterxml.classmate.TypeResolver;

import java.util.*;


//https://www.javainuse.com/spring/boot_swagger
@Configuration
@EnableSwagger2
public class SwaggerConfiguration  {

    private TypeResolver typeResolver;

    @Autowired
    public void setTypeResolver(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Bean
    public Docket UserConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Medical Storage Api v1 - User")
                .additionalModels(
                        typeResolver.resolve(UserRequestBody.class),
                        typeResolver.resolve(RegisterUserRequestBody.class)
                )
                .apiInfo(apiInfo())
                .select()
                .apis( RequestHandlerSelectors.basePackage( "com.unipi.adouladiris.medicalstorage.rest.controllers.users" ) )
                .paths(PathSelectors.ant("/api/v1/user/**"))
                .build();
    }

    @Bean
    public Docket ProductConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Medical Storage Api v1 - Product")
                .apiInfo(apiInfo())
                .globalRequestParameters(authorizationParameter())
                .select()
                .apis( RequestHandlerSelectors.basePackage( "com.unipi.adouladiris.medicalstorage.rest.controllers.products" ) )
                .paths(PathSelectors.ant("/api/v1/product/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MedicalStorage API")
                .description("A Java RestFul API web application utilizing Spring Boot, Spring Security and Hibernate frameworks.")
                .license("No License")
                .licenseUrl("cloudaros@homail.com").version("1").build();
    }

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



//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry)
//    {
//        //enabling swagger-ui part for visual documentation
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        //registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }




//    @Primary
//    @Bean
//    public SwaggerResourcesProvider swaggerResourcesProvider(
//            InMemorySwaggerResourcesProvider defaultResourcesProvider) {
//        return () -> {
//            List<SwaggerResource> resources = new ArrayList<>();
//            Arrays.asList("api1")
//                    .forEach(resourceName -> resources.add(loadResource(resourceName)));
//            return resources;
//        };
//    }
//
//    private SwaggerResource loadResource(String resource) {
//        SwaggerResource wsResource = new SwaggerResource();
//        wsResource.setName(resource);
//        wsResource.setSwaggerVersion("2.0");
//        wsResource.setLocation("/swagger-apis/" + resource + "/swaggerTTT.yamlkjdhgfklfd");
//        return wsResource;
//    }




//    @Primary
//    @Bean
//    public ApiListingScanner addExtraOperations(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader,
//                                                ApiModelSpecificationReader modelSpecificationReader,
//                                                DocumentationPluginsManager pluginsManager)
//    {
//        return new FormLoginOperations(apiDescriptionReader, apiModelReader, modelSpecificationReader, pluginsManager);
//    }

}
