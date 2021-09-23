package com.unipi.adouladiris.medicalstorage.database.dao;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.database.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.session.DbEntitySessionManager;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTab;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategory;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategoryItem;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategoryItemTag;
import com.unipi.adouladiris.medicalstorage.entities.jointables.abstractClass.Joinable;
import com.unipi.adouladiris.medicalstorage.entities.operables.*;
import com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass.Operable;
import com.unipi.adouladiris.medicalstorage.entities.users.Role;
import com.unipi.adouladiris.medicalstorage.entities.users.User;

import javax.persistence.Query;
import java.util.*;


public class Delete extends DbEntitySessionManager {
    // Delete entity by Id. Pass the class type and the Id parameter to find the corresponding entity and remove row from records.
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

    // If request body for update contains keyword 'product', then it finds matching item and delete it.
    // This method deletes only items in a specific format!!!
    public DbResult deleteEntityFromProduct(@NotNull LinkedHashMap body) throws Exception {

        ArrayList<HashMap> bodyMap = (ArrayList) body.get("product");
        //Set<ArrayList<HashMap>> results = new HashSet();
        Select select = new Select();

        for (HashMap nestedMap : bodyMap){

            for(Object pathToDelete : nestedMap.values()){

                LinkedHashMap pathToDeleteValues = (LinkedHashMap)pathToDelete;

                for(Object entrySubstance : pathToDeleteValues.entrySet()){
                    Map.Entry castedEntrySubstance = (Map.Entry)entrySubstance;
                    //String substanceName = (String) castedEntrySubstance.getKey();
                    //System.out.println("substanceName: " + substanceName);

                    LinkedHashMap castedEntrySubstanceValue = (LinkedHashMap) castedEntrySubstance.getValue();
                    if(castedEntrySubstanceValue.containsKey("Tab")){
                        ArrayList<LinkedHashMap> tabArraylist = (ArrayList)castedEntrySubstanceValue.get("Tab");
                        for(LinkedHashMap tabHashmap : tabArraylist ){
                            Set<String> tabHashmapKeys = tabHashmap.keySet();
                            for(String tabHashmapKey : tabHashmapKeys){
                                //System.out.println("tabHashmapKey: " + tabHashmapKey);
                                LinkedHashMap categoryLinkedHashmap = (LinkedHashMap) tabHashmap.get(tabHashmapKey);
                                if(categoryLinkedHashmap.containsKey("Category")){
                                    ArrayList<LinkedHashMap> categoryArraylist = (ArrayList<LinkedHashMap>) categoryLinkedHashmap.get("Category");
                                    for(LinkedHashMap categoryInnerMap : categoryArraylist){
                                        Set<String> categoryHashmapKeys = categoryInnerMap.keySet();
                                        for(String categoryHashmapKey : categoryHashmapKeys){
                                            //System.out.println("categoryHashmapKey: " + categoryHashmapKey);
                                            LinkedHashMap itemLinkedHashmap = (LinkedHashMap) categoryInnerMap.get(categoryHashmapKey);
                                            if(itemLinkedHashmap.containsKey("Item")){
                                                ArrayList<LinkedHashMap> itemArraylist = (ArrayList<LinkedHashMap>) itemLinkedHashmap.get("Item");
                                                for(LinkedHashMap itemInnerMap : itemArraylist){
                                                    Set<String> itemHashmapKeys = itemInnerMap.keySet();
                                                    if(itemHashmapKeys.contains("Tag")){
                                                        for(String itemHashmapKey : itemHashmapKeys){
                                                            //System.out.println("itemHashmapKey: " + itemHashmapKey + " Value: " + itemInnerMap.get(itemHashmapKey));
                                                            if(itemHashmapKey.equals("Tag")){
//                                                                System.out.println("Tag: " + itemInnerMap.get(itemHashmapKey));
//
//                                                                String itemName = itemInnerMap.get("Title").toString();
//                                                                String itemDesc = itemInnerMap.get("Description").toString();
//                                                                Item item = select.findItemEntityByNameAndDescription(itemName,itemDesc).getResult(Item.class);
//                                                                String tagName = itemInnerMap.get(itemHashmapKey).toString();
//                                                                Tag tag = new Tag(tagName);
//                                                                SubstanceTabCategoryItemTag stcit = select.findJoinableEntityByName(SubstanceTabCategoryItemTag.class,item,tag).getResult(SubstanceTabCategoryItemTag.class);
//
//                                                                if(stcit != null){
//                                                                    System.out.println("tag test: " + stcit.getTag().getId().toString());
//                                                                }
                                                            }
                                                        }
                                                    }
                                                    else{
                                                        //System.out.println("itemHashmapKey: " + itemInnerMap.get("Title") + " Value: " + itemInnerMap.get("Description"));
                                                        String itemName = itemInnerMap.get("Title").toString();
                                                        String itemDesc = itemInnerMap.get("Description").toString();
                                                        // Find item to delete.
                                                        DbResult dbResult = select.findItemEntityByNameAndDescription(itemName,itemDesc);
                                                        if(dbResult.isEmpty()){
                                                            return new DbResult();
                                                        }else{
                                                            Item item = dbResult.getResult(Item.class);
                                                            return deleteEntityById(Item.class,item.getId());
                                                        }
                                                    }
                                                }
                                            }
                                            else{
//                                                Substance substance = select.findOperableEntityByName(Substance.class,substanceName).getResult(Substance.class);
//                                                Tab tab = select.findOperableEntityByName(Tab.class,tabHashmapKey).getResult(Tab.class);
//                                                SubstanceTab st = select.findJoinableEntityByName(SubstanceTab.class,substance,tab).getResult(SubstanceTab.class);
//
//
//                                                Category category = select.findOperableEntityByName(Category.class,categoryHashmapKey).getResult(Category.class);
//                                                SubstanceTabCategory stc = select.findJoinableEntityByName(SubstanceTabCategory.class,st,category).getResult(SubstanceTabCategory.class);
//
//                                                // Find category to delete.
//                                                return new DbResult(deleteEntityById(SubstanceTabCategory.class,stc.getId()).getResult(Boolean.class));
//
//                                                System.out.println("stc sub: " + stc.getSubstanceTab().getSubstance().getName());
//                                                System.out.println("stc tab: " + stc.getSubstanceTab().getTab().getName());
//                                                System.out.println("stc cat: " + stc.getCategory().getName());
                                            }
                                        }
                                    }
                                }
                                else{
//                                    Substance substance = select.findOperableEntityByName(Substance.class,substanceName).getResult(Substance.class);
//                                    Tab tab = select.findOperableEntityByName(Tab.class,tabHashmapKey).getResult(Tab.class);
//                                    SubstanceTab st = select.findJoinableEntityByName(SubstanceTab.class,substance,tab).getResult(SubstanceTab.class);
//
//                                    // Find tab to delete.
//                                    //st.getTab()
//
//                                    System.out.println("st sub: " + st.getSubstance().getName());
//                                    System.out.println("st tab: " + st.getTab().getName());
                                }
                            }
                        }
                    }
                    else{
                        //return new DbResult();
                    }
                }
            }
        }

        return new DbResult();

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

    // Delete User
    public DbResult deleteUserByName(@NotNull String username) throws Exception {

        DbResult dbResult = new Select().findUser(username);
        if(dbResult.isEmpty()){
            throw new Exception("User not found");
        }
        else{
            User user = dbResult.getResult(User.class);
            try{
                if (!session.getTransaction().isActive()) session.getTransaction().begin();
                session.remove(user);
                session.getTransaction().commit();
                return new DbResult(true);
            }
            catch(Exception ex){
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                return new DbResult(ex);
            }
        }
    }

}
