package com.unipi.adouladiris.medicalstorage.rest;
import com.unipi.adouladiris.medicalstorage.businessmodel.Product;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.entities.operable.*;
import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;
import com.unipi.adouladiris.medicalstorage.rest.dto.DataTransferObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.util.Map;
import java.util.TreeSet;

import static org.springframework.web.bind.annotation.RequestMethod.POST;


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

    private Object analyze(Object object){

        if(object.getClass().getSimpleName().equals("String")){
            return String.class.cast(Map.Entry.class.cast(object).getKey());
        }

//        if(object.getClass().getSimpleName().equals("ArrayList")){
//            for (Object o2 : ArrayList.class.cast(Map.Entry.class.cast(o1).getValue()) ){
//                System.out.println(o2.getClass().getSimpleName());
//            }
//        }
        return null;
    }

    @PostMapping(path = "newProduct", consumes = "application/json")
    public ResponseEntity<ArrayList<Object>> insertProduct(@RequestBody ArrayList<Object> body) {

        for (Object o : body ){
            System.out.println(o.getClass().getSimpleName());
            if(o.getClass().getSimpleName().equals("LinkedHashMap")){
                Map lhm = LinkedHashMap.class.cast(o);
                for (Object o1 : lhm.entrySet() ){
//                    System.out.println(Map.Entry.class.cast(o1).getKey().getClass().getSimpleName());
                    System.out.println(analyze(o1));

//                    System.out.println(Map.Entry.class.cast(o1).getValue().getClass().getSimpleName());
                    if(Map.Entry.class.cast(o1).getValue().getClass().getSimpleName().equals("ArrayList")){
                        for (Object o2 : ArrayList.class.cast(Map.Entry.class.cast(o1).getValue()) ){
                            System.out.println(analyze(o2));
                        }
                    }
                }
            }
        }

//        for(Map.Entry e : body.entrySet()){
//            System.out.print(e.getKey() + " => ");
//            if(e.getValue().getClass().getSimpleName().equals("ArrayList")){
//                ArrayList arr = ArrayList.class.cast(e.getValue());
//                for ( Object o : arr ){
//                    String str = String.class.cast(o);
//                    System.out.print(str+" ");
//                }
//                System.out.print("\n");
//            }
//            else{
//                System.out.println(e.getValue());
//            }
//
//        }

        System.out.println("----------------------------------");

//        Substance substance = null;
//        Tab tab = null;
//        Category category = null;
//        Item item = null;
//        Tag tag = null;
//
//        for (Operable operable : operables){
//            if(operable.getClass().getSimpleName().equals(Substance.class.getSimpleName())){
//                substance = Substance.class.cast(operable);
//            }
//            else if(operable.getClass().getSimpleName().equals(Tab.class.getSimpleName())){
//                tab = Tab.class.cast(operable);
//            }
//            else if(operable.getClass().getSimpleName().equals(Category.class.getSimpleName())){
//                category = Category.class.cast(operable);
//            }
//            else if(operable.getClass().getSimpleName().equals(Item.class.getSimpleName())){
//                item = Item.class.cast(operable);
//            }
//            else if(operable.getClass().getSimpleName().equals(Tag.class.getSimpleName())){
//                tag = Tag.class.cast(operable);
//            }
//        }



//        DbResult dbResult = new Select().findProduct("asp");
//
//        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
//        Product product = dbResult.getResult(Product.class);
//        return new ResponseEntity(HttpStatus.OK);

        Map<String, String> jsonMessage = new HashMap<>();
        jsonMessage.put("message", body.toString());

        return new ResponseEntity(jsonMessage, HttpStatus.OK);
    }

    @PutMapping("/product/{name}")
    public ResponseEntity<String> updateProduct(@PathVariable String name22, @RequestBody String name) {

        DbResult dbResult = new Select().findProduct(name);

        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        Product product = dbResult.getResult(Product.class);
        return new ResponseEntity(HttpStatus.OK);
    }




}
