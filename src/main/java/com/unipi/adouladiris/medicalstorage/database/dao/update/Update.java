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
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategory;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategoryItem;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategoryItemTag;
import com.unipi.adouladiris.medicalstorage.entities.jointables.abstractClass.Joinable;
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

        for (Substance newSubstance : product.getProduct().keySet() ){
            if(new Select().findProduct(newSubstance.getName()).isEmpty()){
                return new DbResult();
            }
        }

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
                (substanceKey, substanceValue) -> { if(bodyMap.containsKey("Substance")){ processBodyKeys((ArrayList) bodyMap.get("Substance"), substanceKey); }
                    substanceValue.forEach(
                            (tabKey, tabValue) -> { if(bodyMap.containsKey("Tab")) processBodyKeys((ArrayList) bodyMap.get("Tab"), substanceKey, tabKey);
                                tabValue.forEach(
                                        (categoryKey, categoryValue) -> { if(bodyMap.containsKey("Category")) processBodyKeys((ArrayList) bodyMap.get("Category"), substanceKey, tabKey, categoryKey);
                                        categoryValue.forEach(
                                                (itemKey, itemValue) -> {
                                                    if(bodyMap.containsKey("Item")) processBodyKeys((ArrayList) bodyMap.get("Item"), substanceKey, tabKey, categoryKey, itemKey);
                                                    itemValue.forEach(
                                                            tag -> { if(bodyMap.containsKey("Tag")) processBodyKeys((ArrayList) bodyMap.get("Tag"), substanceKey, tabKey, categoryKey, itemKey, tag);
                                                            });
                                                });
                                        });
                            });
                });


        return null;
    }

    private void processBodyKeys(ArrayList<HashMap> bodyArrayToProcess, Operable... indexEntities){

        bodyArrayToProcess.forEach(
                replacement -> {
                    replacement.forEach( (oldKey, newKey) -> { updateJoinTable(bodyArrayToProcess, indexEntities); }); }
        );
    }

    private void updateJoinTable(ArrayList<HashMap> bodyArrayToProcess, Operable... indexEntities){

        HashMap<Class<? extends Operable>, Operable> operableSet = new HashMap();
        for(Operable entity : indexEntities){ operableSet.put(entity.getClass(), entity); }

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
                                    }

                                });
                    });



        }
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
                                    }

                                });
                    });

        }
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
                                    }

                                });
                    });

        }
        else if(operableSet.containsKey(Substance.class) && operableSet.containsKey(Tab.class) && !operableSet.containsKey(Category.class) &&
                !operableSet.containsKey(Item.class) && operableSet.containsKey(Tag.class)){

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
                                    }

                                });
                    });

        }
        else if(operableSet.containsKey(Substance.class) && !operableSet.containsKey(Tab.class) && !operableSet.containsKey(Category.class) &&
                !operableSet.containsKey(Item.class) && operableSet.containsKey(Tag.class)){

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

                                });
                    });
        }


    }

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

        //String select = "FROM SubstanceTab jt INNER JOIN jt.tab WHERE jt.tab.name =: name ";
        String select = queryBuilder.toString();
        Query query = session.createQuery(select);
        query.setParameter("name", oldKey);
        List<Object[]> queryResultList = query.getResultList();
        return new DbResult(queryResultList.size());
    }

}