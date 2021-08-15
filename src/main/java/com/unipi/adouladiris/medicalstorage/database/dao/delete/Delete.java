package com.unipi.adouladiris.medicalstorage.database.dao.delete;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.database.session.DbEntitySessionManager;
import com.unipi.adouladiris.medicalstorage.entities.Queryable;
import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;

import javax.persistence.PersistenceException;

public class Delete extends DbEntitySessionManager implements DeleteInterface {

    @Override
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

    @Override
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
        //return new Select().findOperableEntityByName(typeClass, name);
    }

    @Override
    public DbResult deleteProduct(@NotNull Product product) {
        if ( product.getProduct().keySet().size() > 1 ) return new DbResult();
        return deleteEntityByName(product.getEntityContainingName().getClass(), product.getEntityContainingName().getName());
    }
}
