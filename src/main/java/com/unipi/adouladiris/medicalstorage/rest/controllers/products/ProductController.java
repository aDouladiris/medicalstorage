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

//        System.out.println("Body: " + body.getClass().getSimpleName() );
//        System.out.println("Body: " + body.toString() );
//        System.out.println("Body: " + body.get(0).toString() );
        SecurityContextHolder.getContext().setAuthentication(null);
//        return new ResponseEntity("Check", HttpStatus.OK);


//        Iterator iterator = body.entrySet().iterator();
//
//        while (iterator.hasNext()){
//
//            Object object = iterator.next();
//
//            if(object.getClass().getSimpleName().equals("Entry")){
//
//                Map.Entry entry = (Map.Entry) object;
//                String key = (String) entry.getKey();
//
//                if(!entry.getValue().getClass().getSimpleName().equals("ArrayList") &&
//                        !entry.getValue().getClass().getSimpleName().equals("LinkedHashMap") ){
//                    System.out.println(key + ": " + entry.getValue().toString());
//
//                }
//                else if (key.equals("Tag")){
//                    ArrayList arrayList = (ArrayList) entry.getValue();
//                    System.out.println(key + ": " + arrayList.toString());
//                }
//                else System.out.println(key);
//
//                if(entry.getValue().getClass().getSimpleName().equals("ArrayList") ){
//                    ArrayList arrayList = (ArrayList) entry.getValue();
//                    Iterator outer = iterator;
//                    Iterator inner = arrayList.iterator();
//                    iterator = Iterators.concat(inner, outer);
//                }
//                else if(entry.getValue().getClass().getSimpleName().equals("LinkedHashMap")  ){
//                    LinkedHashMap linkedHashMap = (LinkedHashMap) entry.getValue();
//                    Iterator outer = iterator;
//                    Iterator inner = linkedHashMap.entrySet().iterator();
//                    iterator = Iterators.concat(inner, outer);
//                }
//            }
//            else if(object.getClass().getSimpleName().equals("LinkedHashMap") ){
//                LinkedHashMap linkedHashMap = (LinkedHashMap) object;
//                Iterator outer = iterator;
//                Iterator inner = linkedHashMap.entrySet().iterator();
//                iterator = Iterators.concat(inner, outer);
//            }
//
//        }


        Set<Product> productSet = new DataTransferObject(body).getProductSet();
        Map<String, Integer> results = new HashMap();
        System.out.println("pSet size: " + productSet.size());
        for (Product p : productSet){
//            System.out.println(p.getProduct().toString());
            p.printProduct();
            System.out.println("-------------------------------------");
//            DbResult dbResult = new Insert().product(p);
//            HashMap<String, Integer> resultMap =  dbResult.getResult(HashMap.class);
//            results = resultMap;
        }

        //TODO review response format
//        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
//        return new ResponseEntity(results.toString(), HttpStatus.OK);

        return new ResponseEntity("hello", HttpStatus.OK);

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
