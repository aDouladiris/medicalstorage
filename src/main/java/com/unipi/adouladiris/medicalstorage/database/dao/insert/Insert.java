package com.unipi.adouladiris.medicalstorage.database.dao.insert;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.database.session.DbEntitySessionManager;
import com.unipi.adouladiris.medicalstorage.entities.Queryable;
import com.unipi.adouladiris.medicalstorage.entities.jointables.*;
import com.unipi.adouladiris.medicalstorage.entities.operables.*;
import com.unipi.adouladiris.medicalstorage.entities.users.Role;
import com.unipi.adouladiris.medicalstorage.entities.users.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class Insert extends DbEntitySessionManager implements InsertInterface {

    @Override
    public DbResult product(@NotNull Product product) {
        for (Substance newSubstance : product.getProduct().keySet() ){
            if(!new Select().findProduct(newSubstance.getName()).isEmpty()){return new DbResult(newSubstance);}
        }

        Set<HashMap> results = new HashSet();
        for( Substance substance: product.getProduct().keySet() ){
            for( Tab tab : product.getProduct().get(substance).keySet() ){
                for ( Category category : product.getProduct().get(substance).get(tab).keySet() ){
                    for ( Item item : product.getProduct().get(substance).get(tab).get(category).keySet() ){
                        for ( Tag tag : product.getProduct().get(substance).get(tab).get(category).get( item ) ){
                            // Insert keys with value at each iteration.
                            results.add(product(substance, tab, category, item, tag).getResult(HashMap.class));
                        }
                    }
                }
            }
        }
        return new DbResult(results);
    }

    @Override
    public DbResult queryableEntity(Queryable queryable) {
        try {
            if(!session.getTransaction().isActive()) session.getTransaction().begin();
            Serializable insertedId =  session.save(queryable);
            session.getTransaction().commit();
            return new DbResult(insertedId);
        }
        catch ( PersistenceException ex ) {
            if ( session.getTransaction().isActive() ) { session.getTransaction().rollback(); }
            return new DbResult(ex);
        }
    }

    @Override
    public DbResult product(Substance substance, Tab tab, Category category, Item item, Tag tag) {
        if ( !session.getTransaction().isActive() ) { session.getTransaction().begin(); }

        //TODO extract method
        DbResult dbResult = new Select().findOperableEntityByName( Substance.class, substance.getName() );
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

    public DbResult user(@NotNull PasswordEncoder passwordEncoder, @NotNull String username, @NotNull String password, @NotNull String authority){
        try {
            if(!session.getTransaction().isActive()) session.getTransaction().begin();
            Role userRole;
            DbResult dbResult = new Select().findRole(Role.class, authority);
            if ( dbResult.isEmpty() ){
                Serializable insertedRoleUserId =  session.save(new Role(authority));
                userRole = session.find(Role.class, insertedRoleUserId );
            }
            else userRole = dbResult.getResult( Role.class );
            session.getTransaction().commit();
            if(!session.getTransaction().isActive()) session.getTransaction().begin();
            User user = new User(username, passwordEncoder.encode(password), userRole );
            Serializable insertedUserId =  session.save(user);
            session.getTransaction().commit();
            return new DbResult(insertedUserId);
        }
        catch ( PersistenceException ex ) {
            if ( session.getTransaction().isActive() ) { session.getTransaction().rollback(); }
            return new DbResult(ex);
        }
    }
}
