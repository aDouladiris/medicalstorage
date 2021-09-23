package com.unipi.adouladiris.medicalstorage.rest.controllers;

import com.unipi.adouladiris.medicalstorage.swagger.SwaggerConfiguration;
import com.unipi.adouladiris.medicalstorage.database.dao.Delete;
import com.unipi.adouladiris.medicalstorage.database.dao.Insert;
import com.unipi.adouladiris.medicalstorage.database.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.Select;
import com.unipi.adouladiris.medicalstorage.database.dao.Update;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.entities.operables.Substance;
import com.unipi.adouladiris.medicalstorage.rest.dto.*;
import com.unipi.adouladiris.medicalstorage.swagger.models.ProductEntityDeleteRequestBody;
import com.unipi.adouladiris.medicalstorage.swagger.models.ProductInsertRequestBody;
import com.unipi.adouladiris.medicalstorage.swagger.models.ProductReplaceRequestBody;
import com.unipi.adouladiris.medicalstorage.swagger.models.ProductUpdateRequestBody;
import io.swagger.annotations.*;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/v1/product/")
@Api(tags = { SwaggerConfiguration.ProductController })
public class ProductController {

    //********************************************************************
    // Http request will be intercepted by Token filter before proceeding.
    //********************************************************************

    //************************** GET/ **************************
    @GetMapping("all")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    @ApiOperation(value = "Retrieve all available products.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Products received."),
            @ApiResponse(code = 400, message = "Conflict while parsing."),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource."),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found."),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request.")
    })
    // Retrieve all products in database.
    public ResponseEntity<String> getAllProducts() {
        // When request successfully reach controller, each controller will empty security context.
        SecurityContextHolder.getContext().setAuthentication(null);
        // Query database using Data Access Object classes.
        DbResult dbResult = new Select().findAllProducts();
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found.", HttpStatus.NOT_FOUND);
        TreeSet<Product> productSet = dbResult.getResult(TreeSet.class);
        // Each database successful response will be wrapped in a ResponseEntity object after parsed to a Data Transfer Object.
        // In case of exception, the response will be wrapped in a ResponseStatusException object.
        try{
            RequestBodyParse dto = new RequestBodyParse(productSet);
            return new ResponseEntity(dto.getJsonSet().toString(), HttpStatus.OK);
        }
        catch (Exception exc){
            throw new ResponseStatusException(HttpStatus.CONFLICT, exc.getMessage(), exc);
        }
    }

    @GetMapping("{name}")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    @ApiOperation(value = "Retrieve available product by name.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product received."),
            @ApiResponse(code = 400, message = "Conflict while parsing."),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource."),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)."),
            @ApiResponse(code = 404, message = "The requested resource could not be found."),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request.")
    })
    public ResponseEntity<String> getProductByName(@PathVariable String name) {
        // When request successfully reach controller, each controller will empty security context.
        SecurityContextHolder.getContext().setAuthentication(null);
        // Query database using Data Access Object classes.
        DbResult dbResult = new Select().findProduct(name.toUpperCase());
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found.", HttpStatus.NOT_FOUND);
        Product product = dbResult.getResult(Product.class);
        // Each database successful response will be wrapped in a ResponseEntity object after parsed to a Data Transfer Object.
        // In case of exception, the response will be wrapped in a ResponseStatusException object.
        try{
            RequestBodyParse dto = new RequestBodyParse(product);
            return new ResponseEntity(dto.getJsonSet().toString(), HttpStatus.OK);
        }
        catch (Exception exc){
            throw new ResponseStatusException(HttpStatus.CONFLICT, exc.getMessage(), exc);
        }
    }

    @GetMapping("tag/{tag}")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    @ApiOperation(value = "Retrieve all available products by tag.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product received."),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource."),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found."),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request.")
    })
    public ResponseEntity<String> getProductByTag(@PathVariable String tag) {
        // When request successfully reach controller, each controller will empty security context.
        SecurityContextHolder.getContext().setAuthentication(null);
        // Query database using Data Access Object classes.
        Set<Product> results =  new Select().findByTag(tag).getResult(HashSet.class);
        // Products will be assigned to a HashSet.
        Set<String> resultNames = new HashSet<>();
        results.forEach(product -> {
            product.getProduct().keySet().forEach( substance -> resultNames.add(substance.getName()) );
        });
        // Each database successful response will be wrapped in a ResponseEntity object.
        if(resultNames.isEmpty()) return new ResponseEntity("Tag not found.", HttpStatus.NOT_FOUND);
        else return new ResponseEntity(resultNames.toString(), HttpStatus.OK);
    }
    //**********************************************************

    //************************** DELETE/ ***********************
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
        // When request successfully reach controller, each controller will empty security context.
        SecurityContextHolder.getContext().setAuthentication(null);
        // Query database using Data Access Object classes.
        DbResult dbResult = new Delete().deleteProductByName(name.toUpperCase());
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found.", HttpStatus.NOT_FOUND);
        // Dao Delete class will return result field as true and a null exception field or a non-empty exception
        // field containing an exception and a null result field.
        // To avoid checking a null result field as a boolean which needs two checks, we check once the exception field.
        if(dbResult.getException() != null) {
            return new ResponseEntity(dbResult.getException().getMessage(), HttpStatus.OK);
        }
        if(dbResult.getResult(Boolean.class)){
            return new ResponseEntity("Product deleted along with entities without relations.", HttpStatus.OK);
        }
        else{
            // Return something unexpected.
            return new ResponseEntity(dbResult.getResult().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('admin')")
    @ApiOperation(value = "Delete items from product by name.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Entity deleted."),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource."),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found."),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request.")
    })
    @ApiImplicitParam(name = "body", dataTypeClass = ProductEntityDeleteRequestBody.class)
    public ResponseEntity<String> deleteEntityFromProduct(@RequestBody LinkedHashMap body) {
        // When request successfully reach controller, each controller will empty security context.
        SecurityContextHolder.getContext().setAuthentication(null);
        try{
            // Query database using Data Access Object classes.
            //DbResult dbResult = new Delete().deleteProductByName(name.toUpperCase());
            DbResult dbResult = new Delete().deleteEntityFromProduct(body);
            if (dbResult.isEmpty()) return new ResponseEntity("Item not found.", HttpStatus.NOT_FOUND);
            // Dao Delete class will return result field as true and a null exception field or a non-empty exception
            // field containing an exception and a null result field.
            // To avoid checking a null result field as a boolean which needs two checks, we check once the exception field.
            if(dbResult.getException() != null) {
                return new ResponseEntity(dbResult.getException().getMessage(), HttpStatus.OK);
            }
            if(dbResult.getResult(Boolean.class)){
                return new ResponseEntity("Item deleted.", HttpStatus.OK);
            }
            else{
                // Return something unexpected.
                return new ResponseEntity(dbResult.getResult().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        }
    }
    //**********************************************************

    //************************** POST/ *************************
    @PostMapping("")
    @PreAuthorize("hasRole('admin')")
    @ApiOperation(value = "Insert Product.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product inserted."),
            @ApiResponse(code = 400, message = "Request body malformed."),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource."),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)."),
            @ApiResponse(code = 404, message = "The requested resource could not be found."),
            @ApiResponse(code = 409, message = "Product already exists."),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request.")
    })
    @ApiImplicitParam(name = "body", dataTypeClass = ProductInsertRequestBody.class)
    public ResponseEntity<String> insertProduct(@RequestBody LinkedHashMap body) {
        // When request successfully reach controller, each controller will empty security context.
        SecurityContextHolder.getContext().setAuthentication(null);
        // Each database successful response will be wrapped in a ResponseEntity object.
        // In case of exception, the response will be wrapped in a ResponseStatusException object.
        try{
            Set<Product> productSet = new RequestBodyParse(body).getProductSet();
            Set<HashSet> results = new HashSet();
            Set<String> failures = new HashSet();

            // Each Product will be queried separately.
            for(Product product : productSet){
                // Change Substance name to capital letters.
                String productName = product.getEntityContainingName().getName();
                product.getEntityContainingName().setName(productName.toUpperCase());
                // Query database using Data Access Object classes.
                DbResult dbResult = new Insert().product(product);
                if(dbResult.getResult().getClass().equals(Substance.class)) failures.add(dbResult.getResult(Substance.class).getName());
                else results.add(dbResult.getResult(HashSet.class));
            }

            JSONObject response = new JSONObject();
            if(results.isEmpty() && !failures.isEmpty()){
                response.put("failures", failures);
                return new ResponseEntity(response.toString() + " already exists.", HttpStatus.CONFLICT);
            }
            else if(!results.isEmpty() && failures.isEmpty()){
                response.put("results", results);
                return new ResponseEntity(response.toString() + " all products successfully inserted.", HttpStatus.OK);
            }
            else{
                response.put("results", results);
                response.put("failures", failures);
                return new ResponseEntity(response.toString(), HttpStatus.OK);
            }
        }
        catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        }
    }
    //**********************************************************

    //************************** PUT/ **************************
    @PutMapping("")
    @PreAuthorize("hasRole('admin')")
    @ApiOperation(value = "Update existing product.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product updated."),
            @ApiResponse(code = 400, message = "Request body malformed."),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource."),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found."),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request.")
    })
    @ApiImplicitParam(name = "body", dataTypeClass = ProductUpdateRequestBody.class)
    public ResponseEntity<String> updateProduct(@RequestBody LinkedHashMap body) {
        // When request successfully reach controller, each controller will empty security context.
        SecurityContextHolder.getContext().setAuthentication(null);
        // Each database successful response will be wrapped in a ResponseEntity object.
        // In case of exception, the response will be wrapped in a ResponseStatusException object.
        try {
            Set<Product> productSet = new RequestBodyParse(body).getProductSet();
            Set<HashSet> results = new HashSet();
            Set<String> failures = new HashSet();

            // Each Product will be queried separately.
            for(Product product : productSet){
                // Query database using Data Access Object classes.
                DbResult dbResult = new Update().updateProduct(product);
                if(dbResult.isEmpty()) failures.add(dbResult.getResult(String.class));
                else results.add(dbResult.getResult(HashSet.class));
            }
            JSONObject response = new JSONObject();
            response.put("results", results);
            response.put("failures", failures);
            return new ResponseEntity(response.toString(), HttpStatus.OK);
        }
        catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        }
    }

    @PutMapping("{name}")
    @PreAuthorize("hasRole('admin')")
    @ApiOperation(value = "Replace existing product.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product updated."),
            @ApiResponse(code = 400, message = "Request body malformed."),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource."),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found."),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request.")
    })
    @ApiImplicitParam(name = "body", dataTypeClass = ProductReplaceRequestBody.class)
    public ResponseEntity<String> replaceProduct(@PathVariable String name, @RequestBody LinkedHashMap body) {
        // When request successfully reach controller, each controller will empty security context.
        SecurityContextHolder.getContext().setAuthentication(null);
        // Query database using Data Access Object classes.
        DbResult dbResult = new Select().findProduct(name.toUpperCase());
        if(dbResult.isEmpty()) return new ResponseEntity("Product not found.", HttpStatus.NOT_FOUND);
        Product product = dbResult.getResult(Product.class);
        // Each database successful response will be wrapped in a ResponseEntity object.
        // In case of exception, the response will be wrapped in a ResponseStatusException object.
        try{
            dbResult = new Update().replaceProduct(product, body);
            if(dbResult.isEmpty()) return new ResponseEntity("Could not update.", HttpStatus.CONFLICT);
            Set<ArrayList<HashMap>> results = dbResult.getResult(HashSet.class);
            return new ResponseEntity(results.toString(), HttpStatus.OK);
        }
        catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        }
    }
    //**********************************************************

}
