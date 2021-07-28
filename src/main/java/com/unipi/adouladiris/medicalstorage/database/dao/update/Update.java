package com.unipi.adouladiris.medicalstorage.database.dao.update;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.database.session.SessionManager;
import com.unipi.adouladiris.medicalstorage.entities.operable.*;
import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;

import javax.persistence.PersistenceException;


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

}