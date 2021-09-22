package com.unipi.adouladiris.medicalstorage.domain;

import com.unipi.adouladiris.medicalstorage.entities.operables.*;
import com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass.Operable;
import java.util.*;

// Compare updateProduct with the help of custom Substance comparator.
// Class Product is the domain model that contains all entities in the form of TreeMap.
// Each key is an entity that is not a join table. With join tables, we assign each key
// entity to the corresponding entity.
public class Product extends Operable {

    private TreeMap<Substance, TreeMap<Tab,TreeMap<Category, TreeMap<Item, TreeSet<Tag>>>>> product;

    public Product (){ product = new TreeMap<>(); }
//    public Product (Substance substance, Tab tab, Category category, Item item, Tag tag){
//
//        TreeSet<Tag> tagTreeSet = new TreeSet<>();
//        tagTreeSet.add( tag );
//
//        TreeMap<Item, TreeSet<Tag> > itemTagTreeMap = new TreeMap<>();
//        itemTagTreeMap.put( item, tagTreeSet );
//
//        TreeMap<Category, TreeMap<Item, TreeSet<Tag> > > categoryItemTagTreeMap = new TreeMap<>();
//        categoryItemTagTreeMap.put( category, itemTagTreeMap );
//
//        TreeMap<Tab, TreeMap<Category, TreeMap<Item, TreeSet<Tag> > > > tabCategoryItemTagTreeMap = new TreeMap<>();
//        tabCategoryItemTagTreeMap.put( tab, categoryItemTagTreeMap );
//
//        updateProduct = new TreeMap<>();
//        updateProduct.put( substance, tabCategoryItemTagTreeMap );
//    }

    public TreeMap<Substance, TreeMap<Tab, TreeMap<Category, TreeMap<Item, TreeSet<Tag> >>>> getProduct() { return product; }
    public void setProduct(TreeMap<Substance, TreeMap<Tab, TreeMap<Category, TreeMap<Item, TreeSet<Tag> >>>> product) { this.product = product; }

    public Substance getEntityContainingName(){
        for ( Object substanceKey : this.getProduct().keySet() ){ return Substance.class.cast(substanceKey); }
        return null;
    }

}

