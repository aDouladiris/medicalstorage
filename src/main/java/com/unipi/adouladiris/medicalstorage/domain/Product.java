package com.unipi.adouladiris.medicalstorage.domain;

import com.unipi.adouladiris.medicalstorage.entities.operable.*;
import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;
import io.swagger.annotations.ApiModelProperty;
//import net.minidev.json.JSONObject;

import java.util.*;

//TODO check extends. Why???
public class Product extends Operable {

    @ApiModelProperty(
            notes = "Role of the User",
            name="Role",
            dataType = "Array<String>",
            required=true
    )
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
                System.out.println("    Tab       : " + tab.getName() );

                for ( Category category : this.product.get(substance).get(tab).keySet() ){
                    System.out.println("        Category  : " + category.getName() );

                    for ( Item item : this.product.get(substance).get(tab).get(category).keySet() ){
                        System.out.println("            Item      : <" + item.getName() + "> " + item.getDescription() );
                        ArrayList<String> tagList = new ArrayList();
                        for(Tag tag : this.product.get(substance).get(tab).get(category).get( item ) ){
                            tagList.add(tag.getName());
                        }
                        System.out.println("                Tag       : #" + tagList);




                    }
                }
            }
        }
    }

    public Substance getEntityContainingName(){
        for ( Object substanceKey : this.getProduct().keySet() ){ return Substance.class.cast(substanceKey); }
        return null;
    }

}

