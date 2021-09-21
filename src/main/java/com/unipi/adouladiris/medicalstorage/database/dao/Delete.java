package com.unipi.adouladiris.medicalstorage.database.dao;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.database.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.session.DbEntitySessionManager;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTab;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategory;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategoryItem;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategoryItemTag;
import com.unipi.adouladiris.medicalstorage.entities.jointables.abstractClass.Joinable;
import com.unipi.adouladiris.medicalstorage.entities.operables.*;
import com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass.Operable;
import org.springframework.data.util.Pair;

import javax.persistence.Query;
import java.util.*;


public class Delete extends DbEntitySessionManager {

    // Delete entity by Id. Pass the class type and the Id parameter to find the corresponding entity, remove row from records and clear db cache
    // to make deletion visible. The corresponding table is obtained by passing the class type as parameter.
//    public DbResult deleteEntityById(@NotNull Class<? extends Queryable> typeClass, @NotNull Integer id) {
//        try {
//            if(!session.getTransaction().isActive()) session.getTransaction().begin();
//            Object object = session.find(typeClass, id);
//            session.remove(object);
//            session.getTransaction().commit();
//            session.clear();
//            sessionFactory.getCache().evictAll();
//            return new DbResult(true);
//        } catch (PersistenceException ex ) {
//            if ( session.getTransaction().isActive() ) { session.getTransaction().rollback(); }
//            return new DbResult(ex);
//        }
//    }

    // Delete entity by Name. Pass the class type and the Name parameter to find the corresponding entity, remove row from records and clear db cache
    // to make deletion visible. The corresponding table is obtained by passing the class type as parameter.
    public DbResult deleteEntityByName(@NotNull Class<? extends Operable> typeClass, @NotNull String name) {

        // Before perform deleting, search for entity.
        DbResult dbResult = new Select().findOperableEntityByName(typeClass, name);
        if (dbResult.isEmpty()) {
            return new DbResult(false);
        } else {
            Operable objectToDelete = dbResult.getResult(typeClass);
            try {
                // Clears L1 cache by entity manager to commit pending transactions.

                // Clears L2 cache.
//                sessionFactory.getCache().evictAll();
//                session.clear();

                if (!session.getTransaction().isActive()) session.getTransaction().begin();

                Object object = session.find(typeClass, objectToDelete.getId());
                session.remove(object);
                session.getTransaction().commit();


                return new DbResult(true);
            } catch (Exception ex) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                return new DbResult(ex);
            }
        }

    }


    // Cascading delete will not delete related entities, only from the same class.
    public DbResult deleteProductByName(@NotNull String name) {

        // Find all related classes to a Product.
        String select = "SELECT " + // returns a list of objects
                "st, "   +  // SubstanceTab Object
                "stc, "  +  // SubstanceTabCategory Object
                "stci, " +  // SubstanceTabCategoryItem Object
                "stcit " +  // SubstanceTabCategoryItemTag Object
                "FROM SubstanceTabCategoryItemTag stcit " +
                "INNER JOIN stcit.substanceTabCategoryItem AS stci " +
                "INNER JOIN stcit.tag " +

                "INNER JOIN stci.substanceTabCategory AS stc " +
                "INNER JOIN stci.item " +

                "INNER JOIN stc.substanceTab AS st " +
                "INNER JOIN stc.category " +

                "INNER JOIN st.substance " +
                "INNER JOIN st.tab " +

                "WHERE st.substance.name = :subName ";

        Query query = session.createQuery(select);
        query.setParameter("subName", name);
        List<Object[]> queryResultList = query.getResultList(); // Result contains rows, row contains columns

        if( queryResultList.isEmpty() ){ return new DbResult(); }

        HashMap<Object, Integer> objectsToDelete = new HashMap();

        // Group jointables as keys to remove duplicates.
        for (Object[] objects : queryResultList){
            for (Object object : objects){
                Joinable joinTable = (Joinable)object;
                objectsToDelete.put(joinTable, joinTable.getId());
            }
        }

        // Create four different hashmaps to contain different Ids.
        HashMap<Object, Integer>
                objectsToDeleteSubstanceTabCategoryItemTag = new HashMap(),
                objectsToDeleteSubstanceTabCategoryItem = new HashMap(),
                objectsToDeleteSubstanceTabCategory = new HashMap(),
                objectsToDeleteSubstanceTab = new HashMap();

        objectsToDelete.forEach((joinable,jId) ->{
            if(joinable instanceof SubstanceTabCategoryItemTag){
                objectsToDeleteSubstanceTabCategoryItemTag.put(joinable,jId);
            }
            if(joinable instanceof SubstanceTabCategoryItem){
                objectsToDeleteSubstanceTabCategoryItem.put(joinable,jId);
            }
            if(joinable instanceof SubstanceTabCategory){
                objectsToDeleteSubstanceTabCategory.put(joinable,jId);
            }
            if(joinable instanceof SubstanceTab){
                objectsToDeleteSubstanceTab.put(joinable,jId);
            }
        });

        try {
            if (!session.getTransaction().isActive()) session.getTransaction().begin();
            objectsToDeleteSubstanceTabCategoryItemTag.forEach(
                    (joinable,jId) -> {
                        //System.out.println("grouped objects: " +jId + " " + joinable.getClass().getSimpleName());
                        Object jointableToDelete = session.find(joinable.getClass(), jId);
                        session.remove(jointableToDelete);
                    });
            session.getTransaction().commit();

            if (!session.getTransaction().isActive()) session.getTransaction().begin();
            objectsToDeleteSubstanceTabCategoryItem.forEach(
                    (joinable,jId) -> {
                        //System.out.println("grouped objects: " +jId + " " + joinable.getClass().getSimpleName());
                        Object jointableToDelete = session.find(joinable.getClass(), jId);
                        session.remove(jointableToDelete);
                    });
            session.getTransaction().commit();

            if (!session.getTransaction().isActive()) session.getTransaction().begin();
            objectsToDeleteSubstanceTabCategory.forEach(
                    (joinable,jId) -> {
                        //System.out.println("grouped objects: " +jId + " " + joinable.getClass().getSimpleName());
                        Object jointableToDelete = session.find(joinable.getClass(), jId);
                        session.remove(jointableToDelete);
                    });
            session.getTransaction().commit();

            if (!session.getTransaction().isActive()) session.getTransaction().begin();
            objectsToDeleteSubstanceTab.forEach(
                    (joinable,jId) -> {
                        //System.out.println("grouped objects: " +jId + " " + joinable.getClass().getSimpleName());
                        Object jointableToDelete = session.find(joinable.getClass(), jId);
                        session.remove(jointableToDelete);
                    });
            session.getTransaction().commit();

            return new DbResult(true);
        } catch (Exception ex) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            return new DbResult(ex);
        }

    }

    // Delete Product by deleting the Substance entity which is the first single key using deleteEntityByName method.
//    public DbResult deleteProduct(@NotNull Product product) {
//        if ( product.getProduct().keySet().size() > 1 ) return new DbResult();
//        return deleteEntityByName(product.getEntityContainingName().getClass(), product.getEntityContainingName().getName());
//    }

}
