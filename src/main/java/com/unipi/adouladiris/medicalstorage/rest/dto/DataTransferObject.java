package com.unipi.adouladiris.medicalstorage.rest.dto;

import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.entities.operable.*;
import org.json.JSONObject;

import java.util.*;

//TODO needs interface
public class DataTransferObject {

    private Set<JSONObject> jsonSet;
    private Set<Product> productSet;

    public DataTransferObject(Product product){
        TreeSet<Product> products = new TreeSet<>(){{add(product);}};
        this.jsonSet = productSetToJsonObject(products);
    }

    public DataTransferObject(TreeSet<Product> products){
        this.jsonSet = productSetToJsonObject(products);
    }
    public DataTransferObject(ArrayList<Object> body)    { this.productSet = payloadToProductSet(body);     }

    private Set<Product> payloadToProductSet(ArrayList<Object> body){
        Set<Product> productSet = new HashSet<>();
        for(int i=0; i<body.size(); i++){
            System.out.println("Deserialize start");
            Product product = new Product();
            Map<String, HashMap> subMap = Map.class.cast(body.get(i));
            HashMap<String, HashMap> nameTabMap = subMap.get("Substance");
            for (Map.Entry e : nameTabMap.entrySet() ){

                System.out.println(e.getKey());
                Substance substance = new Substance(e.getKey().toString());
                TreeMap<Tab, TreeMap<Category, TreeMap<Item, TreeSet<Tag> >>> tabMap = new TreeMap<>();
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                ArrayList<HashMap> tabValueJSONList = ArrayList.class.cast(HashMap.class.cast(e.getValue()).get("Tab"));
                for(HashMap<String, HashMap> tabValueJSON : tabValueJSONList ){
                    for (Map.Entry tabValueJSONEntry : tabValueJSON.entrySet() ){

                        System.out.println(tabValueJSONEntry.getKey());
                        Tab tab = new Tab(tabValueJSONEntry.getKey().toString());
                        TreeMap<Category, TreeMap<Item, TreeSet<Tag> >> categoryMap = new TreeMap<>();
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        ArrayList<HashMap> categoryValueJSONList = ArrayList.class.cast(HashMap.class.cast(tabValueJSONEntry.getValue()).get("Category"));
                        for(HashMap<String, HashMap> categoryValueJSON : categoryValueJSONList ){
                            for (Map.Entry categoryValueJSONEntry : categoryValueJSON.entrySet() ){

                                System.out.println(categoryValueJSONEntry.getKey());
                                Category category = new Category(categoryValueJSONEntry.getKey().toString());
                                TreeMap<Item, TreeSet<Tag> > itemMap = new TreeMap<>();
                                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                ArrayList<HashMap> itemValueJSONList = ArrayList.class.cast(HashMap.class.cast(categoryValueJSONEntry.getValue()).get("Item"));
                                for (HashMap itemValueJSON : itemValueJSONList){

                                    System.out.println(itemValueJSON.get("Title"));
                                    System.out.println(itemValueJSON.get("Description"));
                                    ArrayList<String> tagList = ArrayList.class.cast(itemValueJSON.get("Tag"));
                                    TreeSet<Tag> tagSet = new TreeSet<>();
                                    for(String tag: tagList ){
                                        System.out.println(tag);
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
        return productSet;
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
    public Set<Product> getProductSet() { return productSet; }
}
