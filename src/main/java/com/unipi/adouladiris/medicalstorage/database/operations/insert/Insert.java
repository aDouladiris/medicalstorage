package com.unipi.adouladiris.medicalstorage.database.operations.insert;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.businessmodel.Product;
import com.unipi.adouladiris.medicalstorage.database.DbResult;
import com.unipi.adouladiris.medicalstorage.database.operations.select.Select;
import com.unipi.adouladiris.medicalstorage.database.operations.session.SessionManager;
import com.unipi.adouladiris.medicalstorage.entities.Queryable;
import com.unipi.adouladiris.medicalstorage.entities.jointables.*;
import com.unipi.adouladiris.medicalstorage.entities.operable.*;

import java.io.Serializable;
import java.util.HashMap;

public class Insert extends SessionManager implements InsertInterface {

    private DbResult dbResult;

//    public Insert(){
//        if ( !session.isOpen() ) { session = sessionFactory.openSession(); }
//    }

    @Override
    public DbResult product(@NotNull Product product) {

        for( Substance substance: product.getProduct().keySet() ){
//            System.out.println("Substance : " + substance.getName() );

            for( Tab tab : product.getProduct().get(substance).keySet() ){
//                System.out.println("Tab       : " + tab.getName() );

                for ( Category category : product.getProduct().get(substance).get(tab).keySet() ){
//                    System.out.println("Category  : " + category.getName() );

                    for ( Item item : product.getProduct().get(substance).get(tab).get(category).keySet() ){
//                        System.out.println("Item      : <" + item.getName() + "> " + item.getDescription() );

                        for ( Tag tag : product.getProduct().get(substance).get(tab).get(category).get( item ) ){
//                            System.out.println("Tag       : #" + tag.getName() );

                            // Insert keys with value at each iteration.
                            DbResult dbResult = product(substance, tab, category, item, tag);

//                            Map<String,Integer> map = dbResult.getResult( Map.class );
//
//                            for ( Map.Entry<String, Integer> entry : map.entrySet() ){
//                                System.out.println(entry.getKey() + " => " + entry.getValue() );
//                            }

                            return dbResult;

                        }
                    }
                }
            }
        }

        return new DbResult();
    }

    @Override
    public DbResult queryableEntity(Queryable queryable) {
        dbResult = new DbResult();

        try {
            if ( !session.getTransaction().isActive()) { session.getTransaction().begin(); }
            Serializable insertedId =  session.save(queryable);
            session.getTransaction().commit();
            dbResult.setResult( insertedId );
        }
        catch ( Exception ex ) {
            if ( session.getTransaction().isActive() ) { session.getTransaction().rollback(); }
            dbResult.setException( ex );
        }
        finally {
//            session.close();
        }
        return dbResult;
    }

    @Override
    public DbResult product(Substance substance, Tab tab, Category category, Item item, Tag tag) {
        if ( !session.getTransaction().isActive() ) { session.getTransaction().begin(); }

        //TODO extract method
        dbResult = new Select().findOperableEntityByName( Substance.class, substance.getName() );
        if ( dbResult.isEmpty() ){ dbResult = queryableEntity(substance); substance = session.find(Substance.class, dbResult.getResult( Integer.class ) ); }
        else substance = dbResult.getResult( Substance.class );

        dbResult = new Select().findOperableEntityByName( Tab.class, tab.getName() );
        if ( dbResult.isEmpty() ){ dbResult = queryableEntity(tab); tab = session.find(Tab.class, dbResult.getResult( Integer.class ) ); }
        else tab = dbResult.getResult( Tab.class );

        dbResult = new Select().findOperableEntityByName( Category.class, category.getName() );
        if ( dbResult.isEmpty() ){ dbResult = queryableEntity(category); category = session.find(Category.class, dbResult.getResult( Integer.class ) ); }
        else category = dbResult.getResult( Category.class );

        dbResult = new Select().findOperableEntityByName( Item.class, item.getName() );
        if ( dbResult.isEmpty() ){ dbResult = queryableEntity(item); item = session.find(Item.class, dbResult.getResult( Integer.class ) ); }
        else item = dbResult.getResult( Item.class );

        dbResult = new Select().findOperableEntityByName( Tag.class, tag.getName() );
        if ( dbResult.isEmpty() ){ dbResult = queryableEntity(tag); tag = session.find(Tag.class, dbResult.getResult( Integer.class ) ); }
        else tag = dbResult.getResult( Tag.class );

        SubstanceTab substanceTab;
        dbResult = new Select().findJoinableEntityByName(SubstanceTab.class, substance, tab );
        if ( dbResult.isEmpty() ) {
            substanceTab = new SubstanceTab( substance, tab );
            dbResult = queryableEntity( substanceTab );
            substanceTab = session.find(SubstanceTab.class, dbResult.getResult( Integer.class ) );
        }
        else substanceTab = dbResult.getResult( SubstanceTab.class );

        SubstanceTabCategory substanceTabCategory;
        dbResult = new Select().findJoinableEntityByName(SubstanceTabCategory.class, substanceTab, category );
        if ( dbResult.isEmpty() ) {
            substanceTabCategory = new SubstanceTabCategory( substanceTab, category );
            dbResult = queryableEntity( substanceTabCategory );
            substanceTabCategory = session.find(SubstanceTabCategory.class, dbResult.getResult( Integer.class ) );
        }
        else substanceTabCategory = dbResult.getResult( SubstanceTabCategory.class );


        SubstanceTabCategoryItem substanceTabCategoryItem;
        dbResult = new Select().findJoinableEntityByName(SubstanceTabCategoryItem.class, substanceTabCategory, item );
        if ( dbResult.isEmpty() ) {
            substanceTabCategoryItem = new SubstanceTabCategoryItem( substanceTabCategory, item );
            dbResult = queryableEntity( substanceTabCategoryItem );
            substanceTabCategoryItem = session.find(SubstanceTabCategoryItem.class, dbResult.getResult( Integer.class ) );
        }
        else substanceTabCategoryItem = dbResult.getResult( SubstanceTabCategoryItem.class );


        SubstanceTabCategoryItemTag substanceTabCategoryItemTag;
        dbResult = new Select().findJoinableEntityByName(SubstanceTabCategoryItemTag.class, substanceTabCategoryItem, tag );
        if ( dbResult.isEmpty() ) {
            substanceTabCategoryItemTag = new SubstanceTabCategoryItemTag( substanceTabCategoryItem, tag );
            dbResult = queryableEntity( substanceTabCategoryItemTag );
            substanceTabCategoryItemTag = session.find(SubstanceTabCategoryItemTag.class, dbResult.getResult( Integer.class ) );
        }
        else substanceTabCategoryItemTag = dbResult.getResult( SubstanceTabCategoryItemTag.class );

        HashMap<String, Integer> idMap = new HashMap();
        idMap.put( Substance.class.getSimpleName(), substance.getId() );
        idMap.put( Tab.class.getSimpleName(), tab.getId() );
        idMap.put( Category.class.getSimpleName(), category.getId() );
        idMap.put( Item.class.getSimpleName(), item.getId() );
        idMap.put( Tag.class.getSimpleName(), tag.getId() );

        idMap.put( SubstanceTab.class.getSimpleName(), substanceTab.getId() );
        idMap.put( SubstanceTabCategory.class.getSimpleName(), substanceTabCategory.getId() );
        idMap.put( SubstanceTabCategoryItem.class.getSimpleName(), substanceTabCategoryItem.getId() );
        idMap.put( SubstanceTabCategoryItemTag.class.getSimpleName(), substanceTabCategoryItemTag.getId() );

        dbResult.setResult( idMap );
        return dbResult;
    }



}
