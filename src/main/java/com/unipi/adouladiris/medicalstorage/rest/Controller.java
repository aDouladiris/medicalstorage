package com.unipi.adouladiris.medicalstorage.rest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JSONObject t1 = new JSONObject();
        t1.put("key", "value");

        JSONObject t2 = new JSONObject();
        t2.put("Another", t1);

        String json = gson.toJson(t2);

        return t2.toJSONString();

        //return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
