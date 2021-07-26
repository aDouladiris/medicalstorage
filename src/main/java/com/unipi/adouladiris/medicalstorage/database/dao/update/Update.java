package com.unipi.adouladiris.medicalstorage.database.dao.update;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.database.session.SessionManager;
import com.unipi.adouladiris.medicalstorage.entities.operable.*;
import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;


public class Update extends SessionManager implements UpdateInterface {

    @Override
    public DbResult entityById(@NotNull Integer id, @NotNull Operable operable) {
        try {
            session.getTransaction().begin();
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
            session.getTransaction().begin();
            object = session.find(operable.getClass(), id);
            session.getTransaction().commit();
            return new DbResult(object);
        } catch (Exception ex ) {
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

//    @Override
//    public DbResult product(@NotNull String name, Operable... operables) {
//        //TODO operables order need check
//        DbResult dbResult = new DbResult();
//        if ( !session.getTransaction().isActive() ) { session.getTransaction().begin(); }
//
//        Product product = new Select().findProduct(name).getResult(Product.class);
//        if(product == null) return new DbResult();
//
//        Substance substance = null;
//
//        for( Substance sub: product.getProduct().keySet() ){ substance = sub; }
//
//        for (int i=0; i<operables.length; i++){
//
//            // Search if Tab.class exists inside Product
//            if( operables[i].getClass().getSimpleName().equals("Tab") && product.getProduct().get(substance).containsKey(operables[i]) ){
//                for( Tab tab : product.getProduct().get(substance).keySet() ){
//                    updateEntityFromOperables(tab, operables);
//
//                    ++i; // Move to Category
//
//                    // Search if Category.class exists inside Product
//                    if( operables[i].getClass().getSimpleName().equals("Category") && product.getProduct().get(substance).get(tab).containsKey(operables[i]) ){
//                        for ( Category category : product.getProduct().get(substance).get(tab).keySet() ){
//                            updateEntityFromOperables(category, operables);
//
//                            ++i; // Move to Item
//
//                            // Search if Item.class exists inside Product
//                            if( operables[i].getClass().getSimpleName().equals("Item") && product.getProduct().get(substance).get(tab).get(category).containsKey(operables[i]) ){
//                                for ( Item item : product.getProduct().get(substance).get(tab).get(category).keySet() ){
//                                    updateEntityFromOperables(item, operables);
//
//                                    ++i; // Move to Tag
//
//                                    // Search if Tag.class exists inside Product
//                                    if( operables[i].getClass().getSimpleName().equals("Tag") ){
//
//                                        Tag tagToInsert = Tag.class.cast(operables[i]);
//                                        System.out.println("Ins: " + tagToInsert.getName() );
//
//                                        for ( Tag tag : product.getProduct().get(substance).get(tab).get(category).get( item ) ){
//                                            System.out.println("Existing tags: " + tag.getName() );
//
//                                            updateEntityFromOperables(tag, operables);
//
////                                            if( tag.getName().equals(operables[i].getName()) ){
////
//////                                                dbResult = new Select().findOperableEntityByName( Tag.class, tag.getName() );
//////                                                tag = dbResult.getResult( Tag.class );
////                                                updateEntityFromOperables(tag, operables);
////
////                                            }
//
//                                        }
//
//                                    }
//                                    // If Tag.class not exists, create new one.
//                                    else {   }
//                                }
//                            }
//                            // If Item.class not exists, create new one.
//                            else {   }
//                        }
//                    }
//                    // If Category.class not exists, create new one.
//                    else {   }
//                }
//            }
//            // If Tab.class not exists, create new one.
//            else{   }
//
//        }
//
//
//        return dbResult;
//    }




//    private void updateIndexedEntity(Operable operable){
//
//        if ( !session.getTransaction().isActive() ) { session.getTransaction().begin(); }
//        session.update(operable);
//        session.getTransaction().commit();
//
//    }
//
//    private void updateEntityFromOperables(Object object, Operable... operables){
//
//        for ( Operable operable : operables){
//            if(operable.getClass().getSimpleName().equals(object.getClass().getSimpleName())){
//                Operable oldOp = (Operable)object;
//                oldOp.setName(operable.getName());
//                updateIndexedEntity(oldOp);
//            }
//        }
//
//    }





}