package com.unipi.adouladiris.medicalstorage.rest.controllers.products;

import com.unipi.adouladiris.medicalstorage.configuration.swagger.SwaggerConfiguration;
import com.unipi.adouladiris.medicalstorage.database.dao.delete.Delete;
import com.unipi.adouladiris.medicalstorage.database.dao.insert.Insert;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.entities.operable.Substance;
import com.unipi.adouladiris.medicalstorage.rest.dto.*;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.google.common.collect.Iterators;
import org.apache.commons.collections.iterators.IteratorChain;

import java.lang.reflect.Array;
import java.util.*;

@RestController
@RequestMapping("/api/v1/product/")
@Api(tags = { SwaggerConfiguration.ProductController })
//@RequestMapping("/product/")
public class ProductController {

    @GetMapping("all")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    @ApiOperation(value = "Retrieve all available products.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Products received."),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource."),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found."),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request.")
    })
    // When we name a header specifically, the header is required by default.
    public ResponseEntity<String> getAllProducts() {
        // Http request will be intercepted by Token filter before proceeding.
        SecurityContextHolder.getContext().setAuthentication(null);
        DbResult dbResult = new Select().findAllProducts();
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        TreeSet<Product> productSet = dbResult.getResult(TreeSet.class);
        DataTransferObject dto = new DataTransferObject(productSet);
        return new ResponseEntity(dto.getJsonSet().toString(), HttpStatus.OK);
    }

    @GetMapping("{name}")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    @ApiOperation(value = "Retrieve available product by name.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product received."),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource."),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found."),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request.")
    })
    public ResponseEntity<String> getProduct(@PathVariable String name) {
        SecurityContextHolder.getContext().setAuthentication(null);
        DbResult dbResult = new Select().findProduct(name);
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        Product product = dbResult.getResult(Product.class);
        DataTransferObject dto = new DataTransferObject(product);
        return new ResponseEntity(dto.getJsonSet().toString(), HttpStatus.OK);
    }

    @DeleteMapping("{name}")
    @PreAuthorize("hasRole('admin')")
    @ApiOperation(value = "Delete product by name.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product deleted."),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource."),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found."),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request.")
    })
    public ResponseEntity<String> deleteProduct(@PathVariable String name) {
        SecurityContextHolder.getContext().setAuthentication(null);
        DbResult dbResult = new Delete().deleteEntityByName(Substance.class, name);
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        if(dbResult.getException() != null) {
            return new ResponseEntity(dbResult.getException().getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity(dbResult.getResult(), HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('admin')")
    @ApiOperation(value = "Insert product.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product inserted."),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource."),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found."),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request.")
    })
    @ApiImplicitParam(name = "body", dataTypeClass = ProductInsertRequestBody.class)
    public ResponseEntity<String> insertProduct(@RequestBody LinkedHashMap body) {
        SecurityContextHolder.getContext().setAuthentication(null);
        Set<Product> productSet = new DataTransferObject(body).getProductSet();
        Set<HashSet> results = new HashSet();
        productSet.forEach(product -> {DbResult dbResult = new Insert().product(product); results.add( dbResult.getResult(HashSet.class) );});
        //TODO review response format
        //if (dbResult.isEmpty()) return new ResponseEntity("Product not created!", HttpStatus.NOT_FOUND);
        return new ResponseEntity(results.toString(), HttpStatus.OK);
    }

    // TODO needs fix. Not working.
    @PutMapping("{name}")
    @PreAuthorize("hasRole('admin')")
    @ApiOperation(value = "Update available product by name.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product updated."),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource."),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found."),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request.")
    })
    @ApiImplicitParam(name = "body", dataTypeClass = ProductUpdateRequestBody.class)
    public ResponseEntity<String> updateProduct(@RequestBody ProductUpdateRequestBody body) {
//        SecurityContextHolder.getContext().setAuthentication(null);
//        DbResult dbResult = new Select().findProduct(name);
//        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
//        Product product = dbResult.getResult(Product.class);
        return new ResponseEntity(HttpStatus.OK);
    }



}
