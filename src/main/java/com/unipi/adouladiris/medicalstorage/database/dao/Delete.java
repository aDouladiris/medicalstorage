package com.unipi.adouladiris.medicalstorage.database.dao;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.database.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.session.DbEntitySessionManager;
import com.unipi.adouladiris.medicalstorage.entities.Queryable;
import com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass.Operable;

import javax.persistence.PersistenceException;

public class Delete extends DbEntitySessionManager {

    // Delete entity by Id. Pass the class type and the Id parameter to find the corresponding entity, remove row from records and clear db cache
    // to make deletion visible. The corresponding table is obtained by passing the class type as parameter.
    public DbResult deleteEntityById(@NotNull Class<? extends Queryable> typeClass, @NotNull Integer id) {
        try {
            if(!session.getTransaction().isActive()) session.getTransaction().begin();
            Object object = session.find(typeClass, id);
            session.remove(object);
            session.getTransaction().commit();
            session.clear();
            sessionFactory.getCache().evictAll();
            return new DbResult(true);
        } catch (PersistenceException ex ) {
            if ( session.getTransaction().isActive() ) { session.getTransaction().rollback(); }
            return new DbResult(ex);
        }
    }

    // Delete entity by Name. Pass the class type and the Name parameter to find the corresponding entity, remove row from records and clear db cache
    // to make deletion visible. The corresponding table is obtained by passing the class type as parameter.
    public DbResult deleteEntityByName(@NotNull Class<? extends Operable> typeClass, @NotNull String name) {
        Operable objectToDelete = new Select().findOperableEntityByName(typeClass, name).getResult( typeClass );
        try {
            if(!session.getTransaction().isActive()) session.getTransaction().begin();
            session.remove( objectToDelete );
            session.getTransaction().commit();
            session.clear();
            sessionFactory.getCache().evictAll();
            return new DbResult(true);
        } catch (Exception ex ) {
            if ( session.getTransaction().isActive() ) { session.getTransaction().rollback(); }
            return new DbResult(ex);
        }
    }

    // Delete Product by deleting the Substance entity which is the first single key using deleteEntityByName method.
    public DbResult deleteProduct(@NotNull Product product) {
        if ( product.getProduct().keySet().size() > 1 ) return new DbResult();
        return deleteEntityByName(product.getEntityContainingName().getClass(), product.getEntityContainingName().getName());
    }
}
