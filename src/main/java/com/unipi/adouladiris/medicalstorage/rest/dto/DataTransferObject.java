package com.unipi.adouladiris.medicalstorage.rest.dto;

import com.unipi.adouladiris.medicalstorage.businessmodel.Product;
import com.unipi.adouladiris.medicalstorage.entities.operable.*;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class DataTransferObject {

    private Set<JSONObject> jsonSet;

    public DataTransferObject(Product product){
        TreeSet<Product> products = new TreeSet<>(){{add(product);}};
        this.jsonSet = productSetToJsonObject(products);
    }

    public DataTransferObject(TreeSet<Product> products){
        this.jsonSet = productSetToJsonObject(products);
    }

    private Set<JSONObject> productSetToJsonObject(TreeSet<Product> products){
        Set<JSONObject> productSet = new HashSet<>();
        for(Product product : products){
            JSONObject substanceJO = new JSONObject();
            for( Substance substance: product.getProduct().keySet() ){
                Set<JSONObject> tabList = new HashSet<>();
                for( Tab tab : product.getProduct().get(substance).keySet() ){
                    JSONObject tabJO = new JSONObject();
                    Set<JSONObject> categoryList = new HashSet<>();
                    for ( Category category : product.getProduct().get(substance).get(tab).keySet() ){
                        JSONObject categoryJO = new JSONObject();
                        Set<JSONObject> itemList = new HashSet<>();
                        for ( Item item : product.getProduct().get(substance).get(tab).get(category).keySet() ){
                            JSONObject itemJO = new JSONObject();
                            itemJO.put("Title", item.getName());
                            Set<String> tagList = new HashSet<>();
                            for ( Tag tag : product.getProduct().get(substance).get(tab).get(category).get( item ) ){
                                tagList.add(tag.getName());
                            }
                            itemJO.put("Tag", tagList);
                            itemJO.put("Descrption", item.getDescription());
                            itemList.add(itemJO);
                        }

                        ArrayList<Object> catTmp = new ArrayList();
                        catTmp.add(category.getName());
                        catTmp.add(itemList);
                        categoryJO.put("Category", catTmp);
                        categoryList.add(categoryJO);
                    }

                    ArrayList<Object> tabTmp = new ArrayList();
                    tabTmp.add(tab.getName());
                    tabTmp.add(categoryList);
                    tabJO.put("Tab", tabTmp);
                    tabList.add(tabJO);
                }

                ArrayList<Object> subTmp = new ArrayList();
                subTmp.add(substance.getName());
                subTmp.add(tabList);
                substanceJO.put("Substance", subTmp);
            }
            productSet.add(substanceJO);
        }
        return productSet;
    }

    public Set<JSONObject> getJsonSet() {
        return jsonSet;
    }

    public void setJsonSet(Set<JSONObject> jsonSet) {
        this.jsonSet = jsonSet;
    }


}
