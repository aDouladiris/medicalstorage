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
    // TODO Delete entity by Name. Pass the class type and the Name parameter to find the corresponding entity and remove row from records.
    // The corresponding table is obtained by passing the class type as parameter.
    public DbResult deleteEntityById(@NotNull Class<? extends Operable> typeClass, @NotNull Integer Id) {
            try {
                if (!session.getTransaction().isActive()) session.getTransaction().begin();

                Object object = session.find(typeClass, Id);
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


    // Delete entity by Name. Pass the class type and the Name parameter to find the corresponding entity and remove row from records.
    // The corresponding table is obtained by passing the class type as parameter.
    public DbResult deleteEntityByName(@NotNull Class<? extends Operable> typeClass, @NotNull String name) {

        // Before perform deleting, search for entity.
        DbResult dbResult = new Select().findOperableEntityByName(typeClass, name);
        if (dbResult.isEmpty()) {
            return new DbResult(false);
        } else {
            Operable objectToDelete = dbResult.getResult(typeClass);
            try {
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

    // If request body for update contains keyword 'product', then it finds matching entity and replace it with new values.
    public DbResult deleteEntityFromProduct(@NotNull LinkedHashMap body) throws Exception {

        ArrayList<HashMap> bodyMap = (ArrayList) body.get("product");
        Set<ArrayList<HashMap>> results = new HashSet();

        for (HashMap nestedMap : bodyMap){

            if(nestedMap.containsKey("Substance")){

                LinkedHashMap<String, Object> pathToDeleteValues = (LinkedHashMap)nestedMap.get("Substance");
                for(Map.Entry mapEntrySubstance : pathToDeleteValues.entrySet()){
                    String substanceName = (String) mapEntrySubstance.getKey();
                    System.out.println("substanceName: " + substanceName);

                    Substance substance = new Select().findOperableEntityByName(Substance.class, substanceName).getResult(Substance.class);

                    Set<SubstanceTab> substanceTabSet = substance.getSubstanceTabSet();
                    for(SubstanceTab st : substanceTabSet){
                        System.out.println("st tab: " + st.getTab().getName());

                        Set<SubstanceTabCategory> substanceTabCategorySet = st.getSubstanceTabCategorySet();
                        for(SubstanceTabCategory stc : substanceTabCategorySet){
                            System.out.println("stc category: " + stc.getCategory().getName());

                            Set<SubstanceTabCategoryItem> substanceTabCategoryItemSet = stc.getSubstanceTabCategoryItemSet();
                            for(SubstanceTabCategoryItem stci : substanceTabCategoryItemSet){
                                System.out.println("stci item: " + stci.getItem().getName());

                                Set<SubstanceTabCategoryItemTag> substanceTabCategoryItemTagSet = stci.getSubstanceTabCategoryItemTagSet();
                                for(SubstanceTabCategoryItemTag stcit : substanceTabCategoryItemTagSet){
                                    System.out.println("stcit tag: " + stcit.getTag().getName());

                                }

                            }

                        }

                    }



//                    LinkedHashMap  mapEntryTab = (LinkedHashMap ) mapEntrySubstance.getValue();
//                    if(mapEntryTab.containsKey("Tab")){
//
//                        ArrayList<LinkedHashMap>  pathToDeleteTab = (ArrayList)mapEntryTab.get("Tab");
//                        for(LinkedHashMap tabHashMap : pathToDeleteTab){
//                            for(Object entryTab : tabHashMap.entrySet()){
//                                Map.Entry<String, LinkedHashMap> castedEntryTab = (Map.Entry)entryTab;
//                                String tabName = castedEntryTab.getKey();
//                                System.out.println("tabName: " + tabName);
//
////                                if(castedEntryTab.getValue().containsKey("Category")){
////                                    ArrayList<LinkedHashMap> categoryArraylist = (ArrayList<LinkedHashMap>) castedEntryTab.getValue().get("Category");
////
////                                    for(LinkedHashMap categoryHashMap : categoryArraylist){
////
//////                                        categoryHashMap.
//////
//////                                        //Map.Entry<String, Object> category = (Map.Entry<String, Object>) categoryHashMap.entrySet();
//////
//////                                        System.out.println(categoryHashMap.getClass().getSimpleName());
//////                                        System.out.println(categoryHashMap.toString());
////
////                                    }
////                                }
////                                else{
////                                    return new DbResult(results);
////                                }
//                            }
//                        }
//                    }
//                    else{
//                        return new DbResult(results);
//                    }
                }
            }
            else{
                return new DbResult(results);
            }
//            for(Object pathToDelete : nestedMap.values()){
//
//                LinkedHashMap pathToDeleteValues = (LinkedHashMap)pathToDelete;
//
//
//                for(Object entrySubstance : pathToDeleteValues.entrySet()){
//                    Map.Entry castedEntrySubstance = (Map.Entry)entrySubstance;
//                    String substanceName = (String) castedEntrySubstance.getKey();
//                    System.out.println("substanceName: " + substanceName);
//
//                    LinkedHashMap castedEntrySubstanceValue = (LinkedHashMap) castedEntrySubstance.getValue();
//                    if(castedEntrySubstanceValue.containsKey("Tab")){
//
//                    }
//                    else{
//                        return new DbResult(results);
//                    }

//                    System.out.println(castedEntrySubstanceValue.get("Tab").getClass().getSimpleName());
//                    System.out.println(castedEntrySubstanceValue.get("Tab").toString());
//                    for(Object entryTab : castedEntrySubstanceValue.entrySet()){
//                        Map.Entry castedEntryTab = (Map.Entry)entryTab;
//                        String tabName = (String) castedEntryTab.getKey();
//                        System.out.println("tabName: " + tabName);
//                        System.out.println(castedEntryTab.getValue().getClass().getSimpleName());
//                        System.out.println(castedEntryTab.getValue().toString());
//
//                    }


//                }
//
//            }

        }




        return new DbResult(results);

    }

    // Cascading delete will not delete related entities.
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

        // Create four different hashmaps to contain different Id groups of join tables.
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

        // Delete each group from each table from the end to the beginning and every time commit each batch delete:
        // SubstanceTabCategoryItemTag -> SubstanceTabCategoryItem -> SubstanceTabCategory -> SubstanceTab
        try {
            if (!session.getTransaction().isActive()) session.getTransaction().begin();
            objectsToDeleteSubstanceTabCategoryItemTag.forEach(
                    (joinable,jId) -> {
                        //System.out.println("grouped objects: " +jId + " " + joinable.getClass().getSimpleName());
                        Object jointableToDelete = session.find(joinable.getClass(), jId);
                        session.remove(jointableToDelete);
                    });
            session.getTransaction().commit();

            // Clear entities without relations.
            objectsToDeleteSubstanceTabCategoryItemTag.forEach(
                    (joinable,jId) -> {
                        SubstanceTabCategoryItemTag stcit = (SubstanceTabCategoryItemTag)joinable;
                        Tag tag = stcit.getTag();

                        if(!tag.getName().equals("")){
                            Integer counter = getRecordsCount(SubstanceTabCategoryItemTag.class,tag.getClass(),tag.getName() ).getResult(Integer.class);
                            //System.out.println("entity Id:" + tag.getId() + " name: " + tag.getName() + " counter: " + counter );
                            if(counter == 0){
                                deleteEntityById(tag.getClass(), tag.getId());
                            }
                        }
                    });

            if (!session.getTransaction().isActive()) session.getTransaction().begin();
            objectsToDeleteSubstanceTabCategoryItem.forEach(
                    (joinable,jId) -> {
                        //System.out.println("grouped objects: " +jId + " " + joinable.getClass().getSimpleName());
                        Object jointableToDelete = session.find(joinable.getClass(), jId);
                        session.remove(jointableToDelete);
                    });
            session.getTransaction().commit();

            // Clear entities without relations.
            objectsToDeleteSubstanceTabCategoryItem.forEach(
                    (joinable,jId) -> {
                        SubstanceTabCategoryItem stci = (SubstanceTabCategoryItem)joinable;
                        Item item = stci.getItem();

                        Integer counter = getRecordsCount(SubstanceTabCategoryItem.class,item.getClass(),item.getName() ).getResult(Integer.class);
                        //System.out.println("entity Id:" + item.getId() + " name: " + item.getName() + " counter: " + counter );
                        if(counter == 0){
                            deleteEntityById(item.getClass(), item.getId());
                        }
                    });

            if (!session.getTransaction().isActive()) session.getTransaction().begin();
            objectsToDeleteSubstanceTabCategory.forEach(
                    (joinable,jId) -> {
                        //System.out.println("grouped objects: " +jId + " " + joinable.getClass().getSimpleName());
                        Object jointableToDelete = session.find(joinable.getClass(), jId);
                        session.remove(jointableToDelete);
                    });
            session.getTransaction().commit();

            // Clear entities without relations.
            objectsToDeleteSubstanceTabCategory.forEach(
                    (joinable,jId) -> {
                        SubstanceTabCategory stc = (SubstanceTabCategory)joinable;
                        Category category = stc.getCategory();

                        Integer counter = getRecordsCount(SubstanceTabCategory.class,category.getClass(),category.getName() ).getResult(Integer.class);
                        //System.out.println("entity Id:" + category.getId() + " name: " + category.getName() + " counter: " + counter );
                        if(counter == 0){
                            deleteEntityById(category.getClass(), category.getId());
                        }
                    });

            if (!session.getTransaction().isActive()) session.getTransaction().begin();
            objectsToDeleteSubstanceTab.forEach(
                    (joinable,jId) -> {
                        //System.out.println("grouped objects: " +jId + " " + joinable.getClass().getSimpleName());
                        Object jointableToDelete = session.find(joinable.getClass(), jId);
                        session.remove(jointableToDelete);
                    });
            session.getTransaction().commit();

            // Clear entities without relations.
            objectsToDeleteSubstanceTab.forEach(
                    (joinable,jId) -> {
                        SubstanceTab st = (SubstanceTab)joinable;
                        Substance substance = st.getSubstance();
                        Tab tab = st.getTab();

                        Integer substanceCounter = getRecordsCount(SubstanceTab.class,substance.getClass(),substance.getName() ).getResult(Integer.class);
                        Integer tabCounter = getRecordsCount(SubstanceTab.class,tab.getClass(),tab.getName() ).getResult(Integer.class);
//                        System.out.println("entity Id:" + substance.getId() + " name: " + substance.getName() + " counter: " + substanceCounter );
//                        System.out.println("entity Id:" + tab.getId() + " name: " + tab.getName() + " counter: " + tabCounter );
                        if(substanceCounter == 0){
                            deleteEntityById(substance.getClass(), substance.getId());
                        }
                        if(tabCounter == 0){
                            deleteEntityById(tab.getClass(), tab.getId());
                        }
                    });

            return new DbResult(true);
        } catch (Exception ex) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            return new DbResult(ex);
        }

    }

    // Delete Product by deleting the Substance entity which is the first single key using deleteEntityByName method.
//    public DbResult deleteProduct(@NotNull Product updateProduct) {
//        if ( updateProduct.getProduct().keySet().size() > 1 ) return new DbResult();
//        return deleteEntityByName(updateProduct.getEntityContainingName().getClass(), updateProduct.getEntityContainingName().getName());
//    }

}
