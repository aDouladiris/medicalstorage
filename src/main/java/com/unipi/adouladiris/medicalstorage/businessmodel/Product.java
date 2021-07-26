package com.unipi.adouladiris.medicalstorage.businessmodel;

import com.unipi.adouladiris.medicalstorage.entities.operable.*;
import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;
//import net.minidev.json.JSONObject;
import org.json.simple.JSONObject;

import java.util.*;

public class Product extends Operable {

    private TreeMap<Substance, TreeMap<Tab,TreeMap<Category, TreeMap<Item, TreeSet<Tag>>>>> product;

    public Product (){ product = new TreeMap<>(); }

    public Product (Substance substance, Tab tab, Category category, Item item, Tag tag){

        TreeSet<Tag> tagTreeSet = new TreeSet<>();
        tagTreeSet.add( tag );

        TreeMap<Item, TreeSet<Tag> > itemTagTreeMap = new TreeMap<>();
        itemTagTreeMap.put( item, tagTreeSet );

        TreeMap<Category, TreeMap<Item, TreeSet<Tag> > > categoryItemTagTreeMap = new TreeMap<>();
        categoryItemTagTreeMap.put( category, itemTagTreeMap );

        TreeMap<Tab, TreeMap<Category, TreeMap<Item, TreeSet<Tag> > > > tabCategoryItemTagTreeMap = new TreeMap<>();
        tabCategoryItemTagTreeMap.put( tab, categoryItemTagTreeMap );

        product = new TreeMap<>();
        product.put( substance, tabCategoryItemTagTreeMap );
    }

    public TreeMap<Substance, TreeMap<Tab, TreeMap<Category, TreeMap<Item, TreeSet<Tag> >>>> getProduct() { return product; }
    public void setProduct(TreeMap<Substance, TreeMap<Tab, TreeMap<Category, TreeMap<Item, TreeSet<Tag> >>>> product) { this.product = product; }

    public void printProduct(){
        for( Substance substance: this.product.keySet() ){
            System.out.println("Substance : " + substance.getName() );

            for( Tab tab : this.product.get(substance).keySet() ){
                System.out.println("Tab       : " + tab.getName() );

                for ( Category category : this.product.get(substance).get(tab).keySet() ){
                    System.out.println("Category  : " + category.getName() );

                    for ( Item item : this.product.get(substance).get(tab).get(category).keySet() ){
                        System.out.println("Item      : <" + item.getName() + "> " + item.getDescription() );

                        for ( Tag tag : this.product.get(substance).get(tab).get(category).get( item ) ){
                            System.out.println("Tag       : #" + tag.getName() );
                        }
                    }
                }
            }
        }
    }

    public JSONObject getJson(){

//        TreeMap<String, TreeMap<String,TreeMap<String, TreeMap<String, TreeMap<String, TreeSet<String> >>>>> simplifiedProduct = new TreeMap<>();
        JSONObject substanceJO = new JSONObject();
        for( Substance substance: this.product.keySet() ){
            Set<JSONObject> tabList = new HashSet<>();
            for( Tab tab : this.product.get(substance).keySet() ){
                JSONObject tabJO = new JSONObject();
                Set<JSONObject> categoryList = new HashSet<>();
                for ( Category category : this.product.get(substance).get(tab).keySet() ){
                    JSONObject categoryJO = new JSONObject();
                    Set<JSONObject> itemList = new HashSet<>();
                    for ( Item item : this.product.get(substance).get(tab).get(category).keySet() ){
                        JSONObject itemJO = new JSONObject();
                        itemJO.put("Title", item.getName());
                        Set<String> tagList = new HashSet<>();
                        for ( Tag tag : this.product.get(substance).get(tab).get(category).get( item ) ){
                            tagList.add(tag.getName());
                        }
                        itemJO.put("Tag", tagList);
                        itemJO.put("Descrption", item.getDescription());
                        itemList.add(itemJO);
                    }
//                    JSONObject catTmp = new JSONObject();
                    ArrayList<Object> catTmp = new ArrayList();
                    catTmp.add(category.getName());
                    catTmp.add(itemList);
                    categoryJO.put("Category", catTmp);
//                    categoryJO.put(category.getName(), itemList);
                    categoryList.add(categoryJO);
                }
//                JSONObject tabTmp = new JSONObject();
                ArrayList<Object> tabTmp = new ArrayList();
                tabTmp.add(tab.getName());
                tabTmp.add(categoryList);
                tabJO.put("Tab", tabTmp);
//                tabJO.put(tab.getName(), categoryList);
                tabList.add(tabJO);
            }
//            JSONObject subTmp = new JSONObject();
            ArrayList<Object> subTmp = new ArrayList();
            subTmp.add(substance.getName());
            subTmp.add(tabList);
            substanceJO.put("Substance", subTmp);
            substanceJO.put(substance.getName(), tabList);
        }
        return substanceJO;
    }

    public class tmpObject{
        String _key;
        String _value;
        tmpObject(String k, String v){
            _key = k; _value = v;
        }
    }


    //                            TreeSet tagSet = new TreeSet();
//                            tagSet.add(item.getDescription());
//
//                            TreeMap itemMapDesc = new TreeMap();
//                            itemMapDesc.put(tag.getName(), tagSet );
//
//                            TreeMap itemMapName = new TreeMap();
//                            itemMapName.put(item.getName(), itemMapDesc );
//
//                            TreeMap catMap = new TreeMap();
//                            catMap.put(category.getName(), itemMapName);
//
//                            TreeMap tabMap = new TreeMap();
//                            tabMap.put(tab.getName(), catMap);
//
////                            JSONObject jo = new JSONObject();
////                            jo.put(substance.getClass().getSimpleName(), substance.getName());
////
//                            simplifiedProduct.put(substance.getName(), tabMap);

//                            Set<tmpObject> ll = new HashSet<>();
//                            ll.add( new tmpObject(substance.getClass().getSimpleName(), substance.getName() ) );



}

