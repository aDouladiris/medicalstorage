package com.unipi.adouladiris.medicalstorage.configuration.swagger;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Operation;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.spring.web.scanners.*;

import java.util.*;

//public class FormLoginOperations extends ApiListingScanner {
//    @Autowired
//    private TypeResolver typeResolver;
//
//    @Autowired
//    public FormLoginOperations(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader,
//                               ApiModelSpecificationReader modelSpecificationReader,
//                               DocumentationPluginsManager pluginsManager)
//    {
//
//        super(apiDescriptionReader, apiModelReader, modelSpecificationReader, pluginsManager);
//    }
//
//    @Override
//    public Map<String, List<ApiListing>> scan(ApiListingScanningContext context) {
//        final Map<String, List<ApiListing>> def = super.scan(context);
//
//        final List<Operation> operations = new ArrayList<>();
//
//        operations.add(new OperationBuilder(new CachingOperationNameGenerator())
//                .method(HttpMethod.POST)
//                .uniqueId("login")
//                .parameters(Arrays.asList(new ParameterBuilder()
//                                .name("username")
//                                .description("The username")
//                                .parameterType("query")
//                                .type(typeResolver.resolve(String.class))
//                                .modelRef(new ModelRef("string"))
//                                .build(),
//                        new ParameterBuilder()
//                                .name("password")
//                                .description("The password")
//                                .parameterType("query")
//                                .type(typeResolver.resolve(String.class))
//                                .modelRef(new ModelRef("string"))
//                                .build()))
//                .summary("Log in") //
//                .notes("Here you can log in")
//                .build());
//        ////////////////////////////////////////////////////////////////////////
//        final List<ApiDescription> apis = new LinkedList<>();
//        apis.add(new ApiDescription("public", "/api/v1/login/", "Authentication documentation", "TEST", operations, false));
//        ////////////////////////////////////////////////////////////////////////
//        List<ApiListing> apiListingList = new ArrayList<>();
//
//        ApiListingBuilder builder = new ApiListingBuilder(context.getDocumentationContext().getApiDescriptionOrdering());
//        builder
//                .apis(apis)
//                .description("Custom authentication");
//
//
//        apiListingList.add( builder.build() );
//        def.put("authentication", apiListingList);
//        return def;
//    }
//}