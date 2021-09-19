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

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.*;


public class Update extends DbEntitySessionManager {

    // Retrieve entity instance by Id, update content at the instance and save to db.
    public DbResult entityById(@NotNull Integer id, @NotNull Operable operable) {
        try {
            if(!session.getTransaction().isActive()) session.getTransaction().begin();
            Operable object = session.find(operable.getClass(), id);
            object.setName(operable.getName());
            if( operable.getClass().getSimpleName().equals("Item") ){
                Item itemOld = (Item) object;
                Item itemNew = (Item) operable;
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

    // Retrieve entity instance by Name, update content at the instance and save to db.
//    public DbResult entityByName(@NotNull String name, @NotNull Operable operable) {
//        Operable object = new Select().findOperableEntityByName(operable.getClass(), name).getResult( Operable.class );
//        if(object == null) return new DbResult();
//        return entityById(object.getId(), operable);
//    }

    // Retrieve each entity from Product tree. If entity exists, update accordingly. If not, created new entity and
    // append Id to the corresponding JoinTable.
    public DbResult product(@NotNull Product product) {

        for (Substance newSubstance : product.getProduct().keySet() ){
            if(new Select().findProduct(newSubstance.getName()).isEmpty()){
                return new DbResult("Product not exists. Cannot update.");
            }
        }
        Set<HashMap> results = new HashSet();
        for( Substance substance: product.getProduct().keySet() ){
            for( Tab tab : product.getProduct().get(substance).keySet() ){
                for ( Category category : product.getProduct().get(substance).get(tab).keySet() ){
                    for ( Item item : product.getProduct().get(substance).get(tab).get(category).keySet() ){
                        for ( Tag tag : product.getProduct().get(substance).get(tab).get(category).get( item ) ){
                            // Insert keys with value at each iteration.
                            results.add(new Insert().product(substance, tab, category, item, tag).getResult(HashMap.class));
                        }
                    }
                }
            }
        }

        return new DbResult(results);
    }

    // If http request body for update contains keyword 'replacement', then it finds matching entity and replace it with new values.
    public DbResult replaceProduct(@NotNull Product product, @NotNull LinkedHashMap body) throws Exception {

        HashMap bodyMap = (HashMap) body.get("replacement");
        Set<ArrayList<HashMap>> results = new HashSet();

        product.getProduct().forEach(
                (substanceKey, substanceValue) -> { if(bodyMap.containsKey("Substance")){
                    results.add(processBodyKeys((ArrayList) bodyMap.get("Substance"), substanceKey));
                    }
                    substanceValue.forEach(
                            (tabKey, tabValue) -> { if(bodyMap.containsKey("Tab")) {
                                results.add(processBodyKeys((ArrayList) bodyMap.get("Tab"), substanceKey, tabKey));
                            }
                                tabValue.forEach(
                                        (categoryKey, categoryValue) -> { if(bodyMap.containsKey("Category")) {
                                            results.add(processBodyKeys((ArrayList) bodyMap.get("Category"), substanceKey, tabKey, categoryKey));
                                        }
                                        categoryValue.forEach(
                                                (itemKey, itemValue) -> {
                                                    if(bodyMap.containsKey("Item")) {
                                                        results.add(processBodyKeys((ArrayList) bodyMap.get("Item"), substanceKey, tabKey, categoryKey, itemKey));
                                                    }
                                                    itemValue.forEach(
                                                            tag -> { if(bodyMap.containsKey("Tag")) {
                                                                results.add(processBodyKeys((ArrayList) bodyMap.get("Tag"), substanceKey, tabKey, categoryKey, itemKey, tag));
                                                            }
                                                            });
                                                });
                                        });
                            });
                });

        if(results.isEmpty()) return new DbResult();
        else return new DbResult(results);
    }

    // Replace entity Id at JoinTables.
    private ArrayList<HashMap> processBodyKeys(ArrayList<HashMap> bodyArrayToProcess, Operable... indexEntities){

        ArrayList<HashMap> results = new ArrayList();
        bodyArrayToProcess.forEach(
                replacement -> {
                    replacement.forEach( (oldKey, newKey) -> { results.add(updateJoinTable(bodyArrayToProcess, indexEntities)); }); }
        );

        return results;
    }

    // Performs actual replacement of entity Id at joinTable by checking key paths of entities in order to find the corresponding entity.
    private HashMap<String, String> updateJoinTable(ArrayList<HashMap> bodyArrayToProcess, Operable... indexEntities){

        HashMap<Class<? extends Operable>, Operable> operableSet = new HashMap();
        for(Operable entity : indexEntities){ operableSet.put(entity.getClass(), entity); }
        HashMap<String, String> results = new HashMap();

        // If path: Substance->Tab->Category->Item->Tag exists, replace tag entities.
        if(operableSet.containsKey(Substance.class) && operableSet.containsKey(Tab.class) && operableSet.containsKey(Category.class) &&
                operableSet.containsKey(Item.class) && operableSet.containsKey(Tag.class)){

            SubstanceTab substanceTab =
                    new Select().findJoinableEntityByName(SubstanceTab.class, operableSet.get(Substance.class), operableSet.get(Tab.class))
                            .getResult(SubstanceTab.class);

            SubstanceTabCategory substanceTabCategory =
                    new Select().findJoinableEntityByName(SubstanceTabCategory.class, substanceTab, operableSet.get(Category.class))
                            .getResult(SubstanceTabCategory.class);

            SubstanceTabCategoryItem substanceTabCategoryItem =
                    new Select().findJoinableEntityByName(SubstanceTabCategoryItem.class, substanceTabCategory, operableSet.get(Item.class))
                            .getResult(SubstanceTabCategoryItem.class);

            SubstanceTabCategoryItemTag substanceTabCategoryItemTag =
                    new Select().findJoinableEntityByName(SubstanceTabCategoryItemTag.class, substanceTabCategoryItem, operableSet.get(Tag.class))
                            .getResult(SubstanceTabCategoryItemTag.class);

            bodyArrayToProcess.forEach(
                    replacement -> {
                        replacement.forEach(
                                (oldKey, newKey) -> {

                                    if(!session.getTransaction().isActive()) session.getTransaction().begin();
                                    Tag newTag;
                                    DbResult dbResult = new Select().findOperableEntityByName(Tag.class, newKey.toString());
                                    if(dbResult.isEmpty()){
                                        newTag = new Tag(newKey.toString());
                                        Integer newInsertedId = new Insert().queryableEntity(newTag).getResult(Integer.class);
                                        newTag = session.find(Tag.class, newInsertedId);
                                    }
                                    else newTag = dbResult.getResult(Tag.class);

                                    if(!session.getTransaction().isActive()) session.getTransaction().begin();
                                    //session.getReference(SubstanceTab.class, )
                                    substanceTabCategoryItemTag.setTag(newTag);
                                    session.merge(substanceTabCategoryItemTag);
                                    session.getTransaction().commit();

                                    Integer oldKeyJoins = getRecordsCount(substanceTabCategoryItemTag.getClass(), newTag.getClass(), oldKey.toString()).getResult(Integer.class);
                                    if(oldKeyJoins == 0){
                                        new Delete().deleteEntityByName(newTag.getClass(), oldKey.toString());
                                        results.put(oldKey.toString()+"{removed}", newTag.getName());
                                    }
                                    else results.put(oldKey.toString(), newTag.getName());

                                });
                    });
        }
        // If path: Substance->Tab->Category->Item exists, replace item entities.
        else if(operableSet.containsKey(Substance.class) && operableSet.containsKey(Tab.class) && operableSet.containsKey(Category.class) &&
                operableSet.containsKey(Item.class) && !operableSet.containsKey(Tag.class)){

            SubstanceTab substanceTab =
                    new Select().findJoinableEntityByName(SubstanceTab.class, operableSet.get(Substance.class), operableSet.get(Tab.class))
                            .getResult(SubstanceTab.class);

            SubstanceTabCategory substanceTabCategory =
                    new Select().findJoinableEntityByName(SubstanceTabCategory.class, substanceTab, operableSet.get(Category.class))
                            .getResult(SubstanceTabCategory.class);

            SubstanceTabCategoryItem substanceTabCategoryItem =
                    new Select().findJoinableEntityByName(SubstanceTabCategoryItem.class, substanceTabCategory, operableSet.get(Item.class))
                            .getResult(SubstanceTabCategoryItem.class);

            bodyArrayToProcess.forEach(
                    replacement -> {
                        replacement.forEach(
                                (oldKey, newKey) -> {

                                    Item oldItem = (Item) operableSet.get(Item.class);
                                    if(!session.getTransaction().isActive()) session.getTransaction().begin();
                                    Item newItem;
                                    DbResult dbResult = new Select().findOperableEntityByName(Item.class, newKey.toString());
                                    if(dbResult.isEmpty()){

                                        if(oldKey.toString().equals(oldItem.getName())){
                                            newItem = new Item(newKey.toString(), oldItem.getDescription());
                                        }
                                        else{
                                            newItem = new Item(oldItem.getName(), newKey.toString());
                                        }

                                        Integer newInsertedId = new Insert().queryableEntity(newItem).getResult(Integer.class);
                                        newItem = session.find(Item.class, newInsertedId);
                                    }
                                    else newItem = dbResult.getResult(Item.class);
                                    if(!session.getTransaction().isActive()) session.getTransaction().begin();
                                    //session.getReference(SubstanceTab.class, )
                                    substanceTabCategoryItem.setItem(newItem);
                                    session.merge(substanceTabCategoryItem);
                                    session.getTransaction().commit();

                                    Integer oldKeyJoins = getRecordsCount(substanceTabCategoryItem.getClass(), newItem.getClass(), oldKey.toString()).getResult(Integer.class);
                                    if(oldKeyJoins == 0){
                                        new Delete().deleteEntityByName(newItem.getClass(), oldKey.toString());
                                        results.put(oldKey.toString()+"{removed}", newItem.getName() + " " + newItem.getDescription());
                                    }
                                    else results.put(oldKey.toString(), newItem.getName() + " " + newItem.getDescription());

                                });
                    });

        }
        // If path: Substance->Tab->Category exists, replace category entities.
        else if(operableSet.containsKey(Substance.class) && operableSet.containsKey(Tab.class) && operableSet.containsKey(Category.class) &&
                !operableSet.containsKey(Item.class) && !operableSet.containsKey(Tag.class)){

            SubstanceTab substanceTab =
                    new Select().findJoinableEntityByName(SubstanceTab.class, operableSet.get(Substance.class), operableSet.get(Tab.class))
                            .getResult(SubstanceTab.class);

            SubstanceTabCategory substanceTabCategory =
                    new Select().findJoinableEntityByName(SubstanceTabCategory.class, substanceTab, operableSet.get(Category.class))
                            .getResult(SubstanceTabCategory.class);

            bodyArrayToProcess.forEach(
                    replacement -> {
                        replacement.forEach(
                                (oldKey, newKey) -> {

                                    if(!session.getTransaction().isActive()) session.getTransaction().begin();
                                    Category newCategory;
                                    DbResult dbResult = new Select().findOperableEntityByName(Category.class, newKey.toString());
                                    if(dbResult.isEmpty()){
                                        newCategory = new Category(newKey.toString());
                                        Integer newInsertedId = new Insert().queryableEntity(newCategory).getResult(Integer.class);
                                        newCategory = session.find(Category.class, newInsertedId);
                                    }
                                    else newCategory = dbResult.getResult(Category.class);

                                    if(!session.getTransaction().isActive()) session.getTransaction().begin();
                                    //session.getReference(SubstanceTab.class, )
                                    substanceTabCategory.setCategory(newCategory);
                                    session.merge(substanceTabCategory);
                                    session.getTransaction().commit();

                                    Integer oldKeyJoins = getRecordsCount(substanceTabCategory.getClass(), newCategory.getClass(), oldKey.toString()).getResult(Integer.class);
                                    if(oldKeyJoins == 0){
                                        new Delete().deleteEntityByName(newCategory.getClass(), oldKey.toString());
                                        results.put(oldKey.toString()+"{removed}", newCategory.getName());
                                    }
                                    else results.put(oldKey.toString(), newCategory.getName());

                                });
                    });

        }
        // If path: Substance->Tab exists, replace tab entities.
        else if(operableSet.containsKey(Substance.class) && operableSet.containsKey(Tab.class) && !operableSet.containsKey(Category.class) &&
                !operableSet.containsKey(Item.class) && !operableSet.containsKey(Tag.class)){

            SubstanceTab substanceTab =
                    new Select().findJoinableEntityByName(SubstanceTab.class, operableSet.get(Substance.class), operableSet.get(Tab.class))
                            .getResult(SubstanceTab.class);

            bodyArrayToProcess.forEach(
                    replacement -> {
                        replacement.forEach(
                                (oldKey, newKey) -> {

                                    if(!session.getTransaction().isActive()) session.getTransaction().begin();
                                    Tab newTab;
                                    DbResult dbResult = new Select().findOperableEntityByName(Tab.class, newKey.toString());
                                    if(dbResult.isEmpty()){
                                        newTab = new Tab(newKey.toString());
                                        Integer newInsertedId = new Insert().queryableEntity(newTab).getResult(Integer.class);
                                        newTab = session.find(newTab.getClass(), newInsertedId);
                                    }
                                    else newTab = dbResult.getResult(Tab.class);

                                    if(!session.getTransaction().isActive()) session.getTransaction().begin();
                                    //session.getReference(SubstanceTab.class, )
                                    substanceTab.setTab(newTab);
                                    session.merge(substanceTab);
                                    session.getTransaction().commit();


                                    Integer oldKeyJoins = getRecordsCount(substanceTab.getClass(), newTab.getClass(), oldKey.toString()).getResult(Integer.class);
                                    if(oldKeyJoins == 0){
                                        new Delete().deleteEntityByName(newTab.getClass(), oldKey.toString());
                                        results.put(oldKey.toString()+"{removed}", newTab.getName());
                                    }
                                    else results.put(oldKey.toString(), newTab.getName());

                                });
                    });

        }
        // If path: Substance exists, replace substance entities.
        else if(operableSet.containsKey(Substance.class) && !operableSet.containsKey(Tab.class) && !operableSet.containsKey(Category.class) &&
                !operableSet.containsKey(Item.class) && !operableSet.containsKey(Tag.class)){

            if(!session.getTransaction().isActive()) session.getTransaction().begin();
            bodyArrayToProcess.forEach(
                    replacement -> {
                        replacement.forEach(
                                (oldKey, newKey) -> {

                                    if(!session.getTransaction().isActive()) session.getTransaction().begin();
                                    Substance oldSubstance = new Select().findOperableEntityByName(Substance.class, oldKey.toString()).getResult(Substance.class);
                                    oldSubstance.setName(newKey.toString());;

                                    if(!session.getTransaction().isActive()) session.getTransaction().begin();
                                    //session.getReference(SubstanceTab.class, );
                                    session.merge(oldSubstance);
                                    session.getTransaction().commit();

                                    results.put(oldKey.toString(), newKey.toString());
                                });
                    });
        }

        return results;

    }

    // Count entities at join tables. If entity reference (as a foreign key) to a join table is removed and is not attached to a join table,
    // then delete it from the corresponding table.
    private DbResult getRecordsCount(Class<? extends Joinable> joinTableName, Class<? extends Operable> operableName, String oldKey){

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("FROM ");

        if(joinTableName.equals(SubstanceTab.class)){
            queryBuilder.append("SubstanceTab jt ");
        }
        else if(joinTableName.equals(SubstanceTabCategory.class)){
            queryBuilder.append("SubstanceTabCategory jt ");
        }
        else if(joinTableName.equals(SubstanceTabCategoryItem.class)){
            queryBuilder.append("SubstanceTabCategoryItem jt ");
        }
        else if(joinTableName.equals(SubstanceTabCategoryItemTag.class)){
            queryBuilder.append("SubstanceTabCategoryItemTag jt ");
        }

        queryBuilder.append("INNER JOIN ");

        if(operableName.equals(Substance.class)){
            queryBuilder.append("jt.substance WHERE jt.substance.name =: name ");
        }
        else if(operableName.equals(Tab.class)){
            queryBuilder.append("jt.tab WHERE jt.tab.name =: name ");
        }
        else if(operableName.equals(Category.class)){
            queryBuilder.append("jt.category WHERE jt.category.name =: name ");
        }
        else if(operableName.equals(Item.class)){
            queryBuilder.append("jt.item WHERE jt.item.name =: name ");
        }
        else if(operableName.equals(Tag.class)){
            queryBuilder.append("jt.tag WHERE jt.tag.name =: name ");
        }

        String select = queryBuilder.toString();
        Query query = session.createQuery(select);
        query.setParameter("name", oldKey);
        List<Object[]> queryResultList = query.getResultList();
        return new DbResult(queryResultList.size());
    }

}