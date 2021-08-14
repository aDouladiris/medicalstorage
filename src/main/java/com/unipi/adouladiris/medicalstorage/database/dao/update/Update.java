package com.unipi.adouladiris.medicalstorage.database.dao.update;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.database.dao.delete.Delete;
import com.unipi.adouladiris.medicalstorage.database.dao.delete.DeleteInterface;
import com.unipi.adouladiris.medicalstorage.database.dao.insert.Insert;
import com.unipi.adouladiris.medicalstorage.database.dao.insert.InsertInterface;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.database.session.SessionManager;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTab;
import com.unipi.adouladiris.medicalstorage.entities.operable.*;
import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.*;


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

        product.getProduct().forEach(
                (substanceKey, substanceValue) -> { if(bodyMap.containsKey("Substance")){ processBodyKey((ArrayList) bodyMap.get("Substance"), substanceKey); }
                    substanceValue.forEach(
                            (tabKey, tabValue) -> { if(bodyMap.containsKey("Tab")) processBodyKey((ArrayList) bodyMap.get("Tab"), substanceKey, tabKey);
                                tabValue.forEach(
                                        (categoryKey, categoryValue) -> { if(bodyMap.containsKey("Category")) processBodyKey((ArrayList) bodyMap.get("Category"), substanceKey, tabKey, categoryKey);
                                        categoryValue.forEach(
                                                (itemKey, itemValue) -> { if(bodyMap.containsKey("Item")) processBodyKey((ArrayList) bodyMap.get("Item"), substanceKey, tabKey, categoryKey, itemKey);
                                                    itemValue.forEach(
                                                            tag -> { if(bodyMap.containsKey("Tag")) processBodyKey((ArrayList) bodyMap.get("Tag"), substanceKey, tabKey, categoryKey, itemKey, tag); }
                                                            );
                                                });
                                        });
                            });
                });


        return null;
    }

    private void processBodyKey(ArrayList<HashMap> bodyArrayToProcess, Operable... indexEntities){

        bodyArrayToProcess.forEach(
                replacement -> {
                    replacement.forEach( (oldKey, newKey) -> { updateJoinTable(indexEntities); }); }
        );
    }

    private void updateJoinTable(Operable... indexEntities){

        HashMap<Class<? extends Operable>, Operable> operableSet = new HashMap();
        for(Operable entity : indexEntities){ operableSet.put(entity.getClass(), entity); }

        StringBuilder queryBuilder = new StringBuilder();

        if( operableSet.containsKey(Substance.class) && operableSet.containsKey(Tab.class) && operableSet.containsKey(Category.class) &&
                operableSet.containsKey(Item.class) && operableSet.containsKey(Tag.class)){

            String select = "SELECT " +         // returns a list of objects
                    "st.substance, " +  // Substance Object
                    "st.tab, " +        // Tab Object
                    "stc.category, " +  // Category Object
                    "stci.item, " +      // Item Object
                    "stcit.tag " +      // Tag Object
                    "FROM SubstanceTabCategoryItemTag stcit " +
                    "INNER JOIN stcit.substanceTabCategoryItem AS stci " +
                    "INNER JOIN stcit.tag " +

                    "INNER JOIN stci.substanceTabCategory AS stc " +
                    "INNER JOIN stci.item " +

                    "INNER JOIN stc.substanceTab AS st " +
                    "INNER JOIN stc.category " +

                    "INNER JOIN st.substance " +
                    "INNER JOIN st.tab ";

            Query query = session.createQuery(select);
            List<Object[]> queryResultList = query.getResultList(); // Result contains rows, row contains columns



        }


//            SubstanceTab substanceTab = new Select().findJoinableEntityByName(SubstanceTab.class, substanceKey, tabKey).getResult(SubstanceTab.class);
//
//            if(!session.getTransaction().isActive()) session.getTransaction().begin();
//            Tab newTab;
//            DbResult dbResult = new Select().findOperableEntityByName(Tab.class, newTabKey.toString());
//            if(dbResult.isEmpty()){
//                newTab = new Tab(newTabKey.toString());
//                Integer newInsertedId = new Insert().queryableEntity(newTab).getResult(Integer.class);
//                newTab = session.find(Tab.class, newInsertedId);
//            }
//            else newTab = dbResult.getResult(Tab.class);
//
//            if(!session.getTransaction().isActive()) session.getTransaction().begin();
//            //session.getReference(SubstanceTab.class, )
//            substanceTab.setTab(newTab);
//            session.merge(substanceTab);
//            session.getTransaction().commit();
//
//            Integer oldKeyPopulation = getRecordsCount(oldTabKey.toString()).getResult(Integer.class);
//
//            if(oldKeyPopulation == 0){
//                new Delete().deleteEntityByName(Tab.class, oldTabKey.toString());
//            }



    }

    private DbResult getRecordsCount(String oldKey){
        String select = "FROM SubstanceTab st INNER JOIN st.tab WHERE st.tab.name =: name ";
        Query query = session.createQuery(select);
        query.setParameter("name", oldKey);
        List<Object[]> queryResultList = query.getResultList();
        return new DbResult(queryResultList.size());
    }

}