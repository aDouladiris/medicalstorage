package com.unipi.adouladiris.medicalstorage.rest.dto;

import com.google.common.collect.Iterators;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.entities.operable.*;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
    public DataTransferObject(LinkedHashMap body)    { this.productSet = payloadToProductSet(body);     }

    private Set<Product> payloadToProductSet(LinkedHashMap body){

        Set<Product> productSet = new HashSet<>();
        //TreeMap<Tab, TreeMap<Category, TreeMap<Item, TreeSet<Tag> >>> tabMap = null;
        //TreeMap<Category, TreeMap<Item, TreeSet<Tag> >> categoryMap = null;
        //TreeMap<Item, TreeSet<Tag>> itemMap = null;
//
//
//        String previousKey=null;
//
//        Product product = null;
//        Substance substance = null;
//        Tab tab = null;
//        Category category = null;
//        Item item = null;



        Iterator iterator = body.entrySet().iterator();

        while (iterator.hasNext()){

            Object object = iterator.next();
            if(object.getClass().getSimpleName().equals("Entry")){
                Map.Entry entry = (Map.Entry) object;
                String key = (String) entry.getKey();
                System.out.println("Key to evaluate: " + key);



                if(key.equals("Substance")){
                    HashMap substanceMap = (HashMap) entry.getValue();

                    substanceMap.forEach(
                            (subkey, subValue) -> {
                                Product product = new Product();
                                System.out.println("subkey: " + subkey); ((HashMap) substanceMap.get(subkey)).values().forEach(
                                    tabArrayWrapped -> {System.out.println("tabArrayWrapped: " + tabArrayWrapped); ((ArrayList) tabArrayWrapped).forEach(
                                            tabArrayUnwrapped -> {System.out.println("tabArrayUnwrapped: " + tabArrayUnwrapped); ((HashMap) tabArrayUnwrapped).forEach(
                                                    (tabKey, tabValue) -> {
                                                        TreeMap<Tab, TreeMap<Category, TreeMap<Item, TreeSet<Tag> >>> tabMap = new TreeMap();
                                                        System.out.println("tabValue: " + tabValue); ((HashMap) tabValue).values().forEach(
                                                            categoryWrapped -> {System.out.println("categoryWrapped: " + categoryWrapped); ((ArrayList) categoryWrapped).forEach(
                                                                    categoryUnwrapped -> {System.out.println("categoryUnwrapped: " + categoryUnwrapped); ((HashMap) categoryUnwrapped).forEach(
                                                                            (categoryKey, categoryValue) -> {
                                                                                TreeMap<Category, TreeMap<Item, TreeSet<Tag>> > categoryMap = new TreeMap<>();
                                                                                System.out.println("categoryValue: " + categoryValue); ((HashMap) categoryValue).values().forEach(
                                                                                    itemWrapped -> {System.out.println("itemWrapped: " + itemWrapped); ((ArrayList) itemWrapped).forEach(
                                                                                            itemUnwrapped -> {
                                                                                                TreeMap<Item, TreeSet<Tag>> itemMap = new TreeMap();
                                                                                                System.out.println("itemUnwrapped: " + itemUnwrapped); ((HashMap) itemUnwrapped).forEach(
                                                                                                    (itemKey, itemValue) -> {
                                                                                                        System.out.println("itemValue: " + itemValue);
                                                                                                        Item item = new Item();
                                                                                                        TreeSet<Tag> tagSet = new TreeSet();
                                                                                                        if(itemKey.equals("Title")) {item.setName(itemValue.toString());}
                                                                                                        if(itemKey.equals("Description")) {item.setDescription(itemValue.toString());}
                                                                                                        if(itemKey.equals("Tag")) {
                                                                                                            ((ArrayList) itemValue).forEach( tagName -> tagSet.add(new Tag(tagName.toString())) );
                                                                                                        }
                                                                                                        itemMap.put(item, tagSet);
                                                                                                        categoryMap.put(new Category(categoryKey.toString()), itemMap);
                                                                                                        tabMap.put(new Tab(tabKey.toString()), categoryMap);
                                                                                                        product.getProduct().put(new Substance(subkey.toString()), tabMap);
                                                                                                        product.printProduct();
                                                                                                        productSet.add(product);
                                                                                                    }
                                                                                            );}
                                                                                    );}
                                                                            );}
                                                                    );}
                                                            );}
                                                    );}
                                            );}
                                    );}
                            );}
                    );

                }

//                if(previousKey != null && previousKey.equals("Substance")){
//                    //System.out.println("tabMap: " + tabMap.toString() );
//                    System.out.println("Clear all Treemaps");
//                    tabMap = new TreeMap<>();
//                    categoryMap = new TreeMap<>();
//                    itemMap = new TreeMap<>();
//                    product = new Product();
//                    System.out.print("Create empty{" + product.getProduct().isEmpty() + "} product and add it to Set{"+productSet.size()+"}" );
//                    productSet.add(product);
//                    System.out.println(" => Set{"+productSet.size()+"}." );
//
//                    System.out.println("Create Substance{"+key+"} and append it to product with empty{"+tabMap.isEmpty()+"} tabMap.");
//                    substance = new Substance(key);
//                    product.getProduct().put(substance, tabMap);
//                }
//                else if(previousKey != null && previousKey.equals("Tab")){
//                    //System.out.println("categoryMap: " + categoryMap.toString() );
//                    System.out.println("Create Tab {"+key+"} and append it to tabMap with empty{"+categoryMap.isEmpty()+"} categoryMap.");
//                    tab = new Tab(key);
//                    tabMap.put(tab, categoryMap);
//                }
//                else if(previousKey != null && previousKey.equals("Category")){
//                    //System.out.println("ItemMap: " + itemMap.toString() );
//                    System.out.println("Create Category {"+key+"} and append it to categoryMap with empty{"+itemMap.isEmpty()+"} itemMap.");
//                    category = new Category(key);
//                    categoryMap.put(category, itemMap);
//                }
//                else if(key.equals("Item")) {
//                    System.out.println(entry.getValue().toString());
//
//                    ArrayList<LinkedHashMap> linkedHashMapItems = (ArrayList) entry.getValue();
//
//                    // Contains e.x. 3 items
//                    for (LinkedHashMap innerItem: linkedHashMapItems ){
//
//                        item = new Item();
//                        item.setName(innerItem.get("Title").toString());
//                        item.setDescription(innerItem.get("Description").toString());
//
//                        TreeSet<Tag> tagSet = new TreeSet();
//                        ArrayList<String> tagArray = (ArrayList) innerItem.get("Tag");
//                        tagArray.forEach( tag -> tagSet.add(new Tag(tag)) );
//
//                        itemMap.put(item, tagSet);
//                    }
//
//
//                }
//                else if(!entry.getValue().getClass().getSimpleName().equals("ArrayList") && !entry.getValue().getClass().getSimpleName().equals("LinkedHashMap") ){
//                    //System.out.println(key + ": " + entry.getValue().toString());
//
//
//
//                    if(key.equals("Title")) {
//                        System.out.println("Create Item{"+entry.getValue().toString()+"}.");
//                        item = new Item();
//                        item.setName(entry.getValue().toString());
//                    }
//                    else if(key.equals("Description")) {
//                        System.out.println("Add description{"+entry.getValue().toString()+"}.");
//                        item.setDescription(entry.getValue().toString());
//                    }
//                }
//                else if (key.equals("Tag")){
//                    //System.out.println(key + ": " + tagList.toString());
//                    System.out.println("Create tags.");
//                    ArrayList<String> tagList = (ArrayList) entry.getValue();
//                    TreeSet<Tag> tagSet = new TreeSet<>();
//                    for(String tag: tagList ){Tag newTag = new Tag(tag); tagSet.add(newTag);}
//                    System.out.println("Append tags{"+tagList.toString()+"} and item to itemMap.");
//                    itemMap.put(item, tagSet);
//                }

//                previousKey = key;
                //////////////////////////////////////////////////////////////////////////////////////////////
                if(entry.getValue().getClass().equals(ArrayList.class) && !entry.getKey().equals("Item") && !entry.getKey().equals("Tag") ){
                    System.out.println("Inner ArrayList: " + entry.getKey());
                    ArrayList arrayList = (ArrayList) entry.getValue();
                    Iterator outer = iterator;
                    Iterator inner = arrayList.iterator();
                    iterator = Iterators.concat(inner, outer);
                    //if(previousKey != null && previousKey.equals("product")) {System.out.println("product counter: " + arrayList.size());}
                }
                else if(entry.getValue().getClass().equals(LinkedHashMap.class)  ){
                    System.out.println("Inner LinkedHashMap: " + entry.getKey());
                    LinkedHashMap linkedHashMap = (LinkedHashMap) entry.getValue();
                    Iterator outer = iterator;
                    Iterator inner = linkedHashMap.entrySet().iterator();
                    iterator = Iterators.concat(inner, outer);
                }

            }
            else if(object.getClass().equals(LinkedHashMap.class) ){
                LinkedHashMap linkedHashMap = (LinkedHashMap) object;
                System.out.println("Outer LinkedHashMap: " + linkedHashMap.keySet().toString() );
                Iterator outer = iterator;
                Iterator inner = linkedHashMap.entrySet().iterator();
                iterator = Iterators.concat(inner, outer);
            }
        }

        return productSet;
    }

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
