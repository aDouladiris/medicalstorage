package com.unipi.adouladiris.medicalstorage.rest.controllers;

import com.unipi.adouladiris.medicalstorage.configuration.SwaggerConfiguration;
import com.unipi.adouladiris.medicalstorage.database.dao.Delete;
import com.unipi.adouladiris.medicalstorage.database.dao.Insert;
import com.unipi.adouladiris.medicalstorage.database.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.Select;
import com.unipi.adouladiris.medicalstorage.database.dao.Update;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.entities.operables.Substance;
import com.unipi.adouladiris.medicalstorage.rest.dto.*;
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

    // Http request will be intercepted by Token filter before proceeding.
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
    public ResponseEntity<String> getAllProducts() {
        SecurityContextHolder.getContext().setAuthentication(null);
        DbResult dbResult = new Select().findAllProducts();
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found.", HttpStatus.NOT_FOUND);
        TreeSet<Product> productSet = dbResult.getResult(TreeSet.class);
        try{
            DataTransferObject dto = new DataTransferObject(productSet);
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
        SecurityContextHolder.getContext().setAuthentication(null);
        DbResult dbResult = new Select().findProduct(name);
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found.", HttpStatus.NOT_FOUND);
        Product product = dbResult.getResult(Product.class);
        try{
            DataTransferObject dto = new DataTransferObject(product);
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
        SecurityContextHolder.getContext().setAuthentication(null);
        Set<Product> results =  new Select().findByTag(tag).getResult(HashSet.class);
        Set<String> resultNames = new HashSet<>();
        results.forEach(product -> {
            product.getProduct().keySet().forEach( substance -> resultNames.add(substance.getName()) );
        });
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
        SecurityContextHolder.getContext().setAuthentication(null);
        DbResult dbResult = new Delete().deleteEntityByName(Substance.class, name);
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found.", HttpStatus.NOT_FOUND);
        if(dbResult.getException() != null) {
            return new ResponseEntity(dbResult.getException().getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity(dbResult.getResult(), HttpStatus.OK);
    }
    //**********************************************************

    //************************** POST/ *************************
    @PostMapping("")
    @PreAuthorize("hasRole('admin')")
    @ApiOperation(value = "Insert product.")
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
        SecurityContextHolder.getContext().setAuthentication(null);
        try{
            Set<Product> productSet = new DataTransferObject(body).getProductSet();
            Set<HashSet> results = new HashSet();
            Set<String> failures = new HashSet();

            productSet.forEach(product -> {
                DbResult dbResult = new Insert().product(product);
                if(dbResult.getResult().getClass().equals(Substance.class)) failures.add(dbResult.getResult(Substance.class).getName());
                else results.add(dbResult.getResult(HashSet.class));
            });

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
    @ApiOperation(value = "Update matching products.")
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
        SecurityContextHolder.getContext().setAuthentication(null);
        try {
            Set<Product> productSet = new DataTransferObject(body).getProductSet();
            Set<HashSet> results = new HashSet();
            Set<String> failures = new HashSet();
            productSet.forEach(product -> {
                DbResult dbResult = new Update().product(product);
                if(dbResult.isEmpty()) failures.add(dbResult.getResult(String.class));
                else results.add(dbResult.getResult(HashSet.class));
            });
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
    @ApiOperation(value = "Update available product by name.")
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
        SecurityContextHolder.getContext().setAuthentication(null);
        DbResult dbResult = new Select().findProduct(name);
        if(dbResult.isEmpty()) return new ResponseEntity("Product not found.", HttpStatus.NOT_FOUND);
        Product product = dbResult.getResult(Product.class);
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
