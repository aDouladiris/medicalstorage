package com.unipi.adouladiris.medicalstorage.rest;
import com.unipi.adouladiris.medicalstorage.businessmodel.Product;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {

    @GetMapping("/product/{name}")
    public String helloWorld(@PathVariable String name){

        Product product = new Select().findProduct(name).getResult(Product.class);

        product.printProduct();

        System.out.println(product.getJson().toJSONString());


        return product.getJson().toJSONString();

        //return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
