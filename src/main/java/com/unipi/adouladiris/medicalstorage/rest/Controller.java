package com.unipi.adouladiris.medicalstorage.rest;
import com.unipi.adouladiris.medicalstorage.businessmodel.Product;
import com.unipi.adouladiris.medicalstorage.database.dao.delete.Delete;
import com.unipi.adouladiris.medicalstorage.database.dao.insert.Insert;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.entities.operable.*;
import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;
import com.unipi.adouladiris.medicalstorage.rest.dto.DataTransferObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;


import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
public class Controller {

    //TODO need parent class

    @ApiIgnore
    @GetMapping("")
    public ModelAndView redirectWithUsingRedirectPrefix(ModelMap model) {
        return new ModelAndView("redirect:/swagger-ui.html#/controller", model);
    }

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
        DbResult dbResult = new Delete().deleteEntityByName(Substance.class, name);
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        if(dbResult.getException() != null) {
            return new ResponseEntity(dbResult.getException().getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity(dbResult.getResult(), HttpStatus.OK);
    }

    @PostMapping("/newProduct")
    public ResponseEntity<String> insertProduct(@RequestBody ArrayList<Object> body) {
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

    @PutMapping("/product/{name}")
    public ResponseEntity<String> updateProduct(@PathVariable String name22, @RequestBody String name) {

        DbResult dbResult = new Select().findProduct(name);

        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        Product product = dbResult.getResult(Product.class);
        return new ResponseEntity(HttpStatus.OK);
    }




}
