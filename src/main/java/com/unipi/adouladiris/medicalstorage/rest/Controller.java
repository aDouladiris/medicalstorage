package com.unipi.adouladiris.medicalstorage.rest;
import com.unipi.adouladiris.medicalstorage.businessmodel.Product;
import com.unipi.adouladiris.medicalstorage.database.operations.select.Select;
import net.minidev.json.JSONObject;
import netscape.javascript.JSObject;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.data.rest.webmvc.json.PersistentEntityJackson2Module;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;


@RestController
public class Controller {

    @GetMapping("/product")
    public String helloWorld(){

        Product product = new Select().findProduct("Aspirine").getResult(Product.class);

        return product.getJson().toJSONString();

        //return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
