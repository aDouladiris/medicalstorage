package com.unipi.adouladiris.medicalstorage.database.dao.update;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.database.dao.insert.Insert;
import com.unipi.adouladiris.medicalstorage.database.dao.insert.InsertInterface;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.database.session.SessionManager;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.entities.operable.*;
import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;

import javax.persistence.PersistenceException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;


public class Update extends SessionManager implements UpdateInterface {

    @Override
    public DbResult entityById(@NotNull Integer id, @NotNull Operable operable) {
        try {
            if(!session.getTransaction().isActive()) session.getTransaction().begin();
            Operable object = session.find(operable.getClass(), id);
            object.setName(operable.getName());
            if( operable.getClass().getSimpleName().equals("Item") ){
                Item itemOld = Item.class.cast(object);
                Item itemNew = Item.class.cast(operable);
                itemOld.setDescription(itemNew.getDescription());
                object = itemOld;
            }
            session.update(object);
            session.getTransaction().commit();
            if(!session.getTransaction().isActive()) session.getTransaction().begin();
            object = session.find(operable.getClass(), id);
            session.getTransaction().commit();
            return new DbResult(object);
        } catch (PersistenceException ex ) {
            if ( session.getTransaction().isActive() ) { session.getTransaction().rollback(); }
            return new DbResult(ex);
        }
    }

    @Override
    public DbResult entityByName(@NotNull String name, @NotNull Operable operable) {
        Operable object = new Select().findOperableEntityByName(operable.getClass(), name).getResult( Operable.class );
        if(object == null) return new DbResult();
        return entityById(object.getId(), operable);
    }

    @Override
    public DbResult product(@NotNull Product product) {

        Set<HashMap> results = new HashSet();

        InsertInterface insertInterface = new Insert();

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
                            results.add(insertInterface.product(substance, tab, category, item, tag).getResult(HashMap.class));
                        }
                    }
                }
            }
        }

        return new DbResult(results);
    }

    @Override
    public DbResult replaceProduct(@NotNull Product product, @NotNull LinkedHashMap body) {

        HashMap bodyMap = (HashMap) body.get("replacement");
        System.out.println("bodyMap: " + bodyMap.toString());

        product.getProduct().forEach(
                (substanceKey, substanceValue)  -> {
                    if(bodyMap.containsKey("Substance")) System.out.println("Replace substance");
                    substanceValue.forEach(
                            (tabKey, tabValue) -> {
                                if(bodyMap.containsKey("Tab")) System.out.println("Replace tab");
                            });

                });


        return null;
    }

}