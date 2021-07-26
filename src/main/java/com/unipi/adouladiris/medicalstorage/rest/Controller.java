package com.unipi.adouladiris.medicalstorage.rest;
import com.unipi.adouladiris.medicalstorage.businessmodel.Product;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.rest.dto.DataTransferObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.TreeSet;


@RestController
public class Controller {

    //TODO need parent class

    @GetMapping("/product/{name}")
    public ResponseEntity<String> getProduct(@PathVariable String name) {

        DbResult dbResult = new Select().findProduct(name);
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        Product product = dbResult.getResult(Product.class);
        DataTransferObject dto = new DataTransferObject(product);
        return new ResponseEntity(dto.getJsonSet(), HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<String> getAllProducts() {

        DbResult dbResult = new Select().findAllProducts();
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        TreeSet<Product> productSet = dbResult.getResult(TreeSet.class);
        DataTransferObject dto = new DataTransferObject(productSet);
        return new ResponseEntity(dto.getJsonSet(), HttpStatus.OK);
    }

    @DeleteMapping("/product/{name}")
    public ResponseEntity<String> deleteProduct(@PathVariable String name) {

        DbResult dbResult = new Select().findProduct(name);

        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        Product product = dbResult.getResult(Product.class);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/product/{name}")
    public ResponseEntity<String> newProduct(@RequestBody String name) {

        DbResult dbResult = new Select().findProduct(name);

        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        Product product = dbResult.getResult(Product.class);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/product/{name}")
    public ResponseEntity<String> updateProduct(@PathVariable String name22, @RequestBody String name) {

        DbResult dbResult = new Select().findProduct(name);

        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        Product product = dbResult.getResult(Product.class);
        return new ResponseEntity(HttpStatus.OK);
    }




}
