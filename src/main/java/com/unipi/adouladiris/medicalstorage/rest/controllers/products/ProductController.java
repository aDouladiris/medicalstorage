package com.unipi.adouladiris.medicalstorage.rest.controllers.products;

import com.unipi.adouladiris.medicalstorage.configuration.swagger.SwaggerConfiguration;
import com.unipi.adouladiris.medicalstorage.database.dao.delete.Delete;
import com.unipi.adouladiris.medicalstorage.database.dao.insert.Insert;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.entities.operable.Substance;
import com.unipi.adouladiris.medicalstorage.rest.dto.DataTransferObject;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/product/")
@Api(tags = { SwaggerConfiguration.ProductController })
//@RequestMapping("/product/")
public class ProductController {

    @GetMapping("all")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
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
    public ResponseEntity<String> insertProduct(@RequestBody ArrayList<Object> body) {
        SecurityContextHolder.getContext().setAuthentication(null);
        Set<Product> productSet = new DataTransferObject(body).getProductSet();
        Map<String, Integer> results = new HashMap();
        for (Product p : productSet){
            DbResult dbResult = new Insert().product(p);
            HashMap<String, Integer> resultMap =  dbResult.getResult(HashMap.class);
            results = resultMap;
        }

        //TODO review response format
//        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        return new ResponseEntity(results.toString(), HttpStatus.OK);

    }

    // TODO needs fix. Not working.
    @PutMapping("{name}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> updateProduct(@PathVariable String name22, @RequestBody String name) {
        SecurityContextHolder.getContext().setAuthentication(null);
        DbResult dbResult = new Select().findProduct(name);
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        Product product = dbResult.getResult(Product.class);
        return new ResponseEntity(HttpStatus.OK);
    }



}
