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
import org.springframework.web.bind.annotation.*;


import java.util.*;

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

        DbResult dbResult = new Delete().deleteEntityByName(Substance.class, name);
        if (dbResult.isEmpty()) return new ResponseEntity("Product not found!", HttpStatus.NOT_FOUND);
        String msg = dbResult.getResult().getClass().getSimpleName();
        System.out.println(msg);
        return new ResponseEntity(msg, HttpStatus.OK);
    }

    @PostMapping(path = "newProduct", consumes = "application/json")
    public ResponseEntity<String> insertProduct(@RequestBody ArrayList<Object> body) {

        Set<Product> productSet = new HashSet<>();
        for(int i=0; i<body.size(); i++){
            Product product = new Product();
            Map<String, HashMap> subMap = Map.class.cast(body.get(i));
            HashMap<String, HashMap> nameTabMap = subMap.get("Substance");
            for (Map.Entry e : nameTabMap.entrySet() ){

//                System.out.println(e.getKey());
                Substance substance = new Substance(e.getKey().toString());
                TreeMap<Tab, TreeMap<Category, TreeMap<Item, TreeSet<Tag> >>> tabMap = new TreeMap<>();
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                ArrayList<HashMap> tabValueJSONList = ArrayList.class.cast(HashMap.class.cast(e.getValue()).get("Tab"));
                for(HashMap<String, HashMap> tabValueJSON : tabValueJSONList ){
                    for (Map.Entry tabValueJSONEntry : tabValueJSON.entrySet() ){

//                        System.out.println(tabValueJSONEntry.getKey());
                        Tab tab = new Tab(tabValueJSONEntry.getKey().toString());
                        TreeMap<Category, TreeMap<Item, TreeSet<Tag> >> categoryMap = new TreeMap<>();
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        ArrayList<HashMap> categoryValueJSONList = ArrayList.class.cast(HashMap.class.cast(tabValueJSONEntry.getValue()).get("Category"));
                        for(HashMap<String, HashMap> categoryValueJSON : categoryValueJSONList ){
                            for (Map.Entry categoryValueJSONEntry : categoryValueJSON.entrySet() ){

//                                System.out.println(categoryValueJSONEntry.getKey());
                                Category category = new Category(categoryValueJSONEntry.getKey().toString());
                                TreeMap<Item, TreeSet<Tag> > itemMap = new TreeMap<>();
                                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                ArrayList<HashMap> itemValueJSONList = ArrayList.class.cast(HashMap.class.cast(categoryValueJSONEntry.getValue()).get("Item"));
                                for (HashMap itemValueJSON : itemValueJSONList){

//                                    System.out.println(itemValueJSON.get("Title"));
//                                    System.out.println(itemValueJSON.get("Description"));
                                    ArrayList<String> tagList = ArrayList.class.cast(itemValueJSON.get("Tag"));
                                    TreeSet<Tag> tagSet = new TreeSet<>();
                                    for(String tag: tagList ){
//                                        System.out.println(tag);
                                        Tag newTag = new Tag(tag);
                                        tagSet.add(newTag);
                                    }
                                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                    Item newItem = new Item(itemValueJSON.get("Title").toString(), itemValueJSON.get("Description").toString() );
                                    itemMap.put(newItem, tagSet);
                                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                }
                                categoryMap.put(category, itemMap);
                            }
                        }
                        tabMap.put(tab, categoryMap);
                    }
                }
                product.getProduct().put(substance, tabMap);
            }
            productSet.add(product);
        }
        System.out.println("----------------------------------");
        Map<String, Integer> results = new HashMap();
        for (Product p : productSet){
            DbResult dbResult = new Insert().product(p);

            System.out.println(dbResult.isEmpty());
            HashMap<String, Integer> resultMap =  dbResult.getResult(HashMap.class);
            for (Map.Entry entry : resultMap.entrySet()){
                System.out.println(entry.getKey() + " => " + entry.getValue() );
            }
            results = resultMap;
        }
        System.out.println("----------------------------------");

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
