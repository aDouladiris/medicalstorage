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
import com.unipi.adouladiris.medicalstorage.entities.users.Role;
import com.unipi.adouladiris.medicalstorage.entities.users.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.Query;
import java.io.Serializable;
import java.util.*;


public class Update extends DbEntitySessionManager {

    // Retrieve entity instance by Id, update content at the instance and save to db.
//    public DbResult entityById(@NotNull Integer id, @NotNull Operable operable) {
//        try {
//            if(!session.getTransaction().isActive()) session.getTransaction().begin();
//            Operable object = session.find(operable.getClass(), id);
//            object.setName(operable.getName());
//            if( operable.getClass().getSimpleName().equals("Item") ){
//                Item itemOld = (Item) object;
//                Item itemNew = (Item) operable;
//                itemOld.setDescription(itemNew.getDescription());
//                object = itemOld;
//            }
//            session.update(object);
//            session.getTransaction().commit();
//            if(!session.getTransaction().isActive()) session.getTransaction().begin();
//            object = session.find(operable.getClass(), id);
//            session.getTransaction().commit();
//            return new DbResult(object);
//        } catch (PersistenceException ex ) {
//            if ( session.getTransaction().isActive() ) { session.getTransaction().rollback(); }
//            return new DbResult(ex);
//        }
//    }

    // Retrieve entity instance by Name, update content at the instance and save to db.
//    public DbResult entityByName(@NotNull String name, @NotNull Operable operable) {
//        Operable object = new Select().findOperableEntityByName(operable.getClass(), name).getResult( Operable.class );
//        if(object == null) return new DbResult();
//        return entityById(object.getId(), operable);
//    }

    // To insert a Product tree, we insert each entity individually. If the instance of each entity exists, we retrieve the instance.
    // If not, we create a new instance of the entity.
    // After each entity insertion, we use their Id to join them at JoinTables.
    // At the end, we return a HashMap containing the participated entities Ids.
    private DbResult findOrCreateEntity(Substance substance, Tab tab, Category category, Item item, Tag tag) {
        if ( !session.getTransaction().isActive() ) { session.getTransaction().begin(); }
        Insert insertion = new Insert();
        Select select = new Select();

        DbResult dbResult = select.findOperableEntityByName( Substance.class, substance.getName() );
        if ( dbResult.isEmpty() ){ dbResult = insertion.queryableEntity(substance); substance = session.find(Substance.class, dbResult.getResult( Integer.class ) ); }
        else substance = dbResult.getResult( Substance.class );

        dbResult = select.findOperableEntityByName( Tab.class, tab.getName() );
        if ( dbResult.isEmpty() ){ dbResult = insertion.queryableEntity(tab); tab = session.find(Tab.class, dbResult.getResult( Integer.class ) ); }
        else tab = dbResult.getResult( Tab.class );

        dbResult = select.findOperableEntityByName( Category.class, category.getName() );
        if ( dbResult.isEmpty() ){ dbResult = insertion.queryableEntity(category); category = session.find(Category.class, dbResult.getResult( Integer.class ) ); }
        else category = dbResult.getResult( Category.class );

        dbResult = select.findOperableEntityByName( Item.class, item.getName() );
        if ( dbResult.isEmpty() ){ dbResult = insertion.queryableEntity(item); item = session.find(Item.class, dbResult.getResult( Integer.class ) ); }
        else item = dbResult.getResult( Item.class );

        dbResult = select.findOperableEntityByName( Tag.class, tag.getName() );
        if ( dbResult.isEmpty() ){ dbResult = insertion.queryableEntity(tag); tag = session.find(Tag.class, dbResult.getResult( Integer.class ) ); }
        else tag = dbResult.getResult( Tag.class );

        SubstanceTab substanceTab;
        dbResult = select.findJoinableEntityByName(SubstanceTab.class, substance, tab );
        if ( dbResult.isEmpty() ) {
            substanceTab = new SubstanceTab( substance, tab );
            dbResult = insertion.queryableEntity( substanceTab );
            substanceTab = session.find(SubstanceTab.class, dbResult.getResult( Integer.class ) );
        }
        else substanceTab = dbResult.getResult( SubstanceTab.class );

        SubstanceTabCategory substanceTabCategory;
        dbResult = select.findJoinableEntityByName(SubstanceTabCategory.class, substanceTab, category );
        if ( dbResult.isEmpty() ) {
            substanceTabCategory = new SubstanceTabCategory( substanceTab, category );
            dbResult = insertion.queryableEntity( substanceTabCategory );
            substanceTabCategory = session.find(SubstanceTabCategory.class, dbResult.getResult( Integer.class ) );
        }
        else substanceTabCategory = dbResult.getResult( SubstanceTabCategory.class );


        SubstanceTabCategoryItem substanceTabCategoryItem;
        dbResult = select.findJoinableEntityByName(SubstanceTabCategoryItem.class, substanceTabCategory, item );
        if ( dbResult.isEmpty() ) {
            substanceTabCategoryItem = new SubstanceTabCategoryItem( substanceTabCategory, item );
            dbResult = insertion.queryableEntity( substanceTabCategoryItem );
            substanceTabCategoryItem = session.find(SubstanceTabCategoryItem.class, dbResult.getResult( Integer.class ) );
        }
        else substanceTabCategoryItem = dbResult.getResult( SubstanceTabCategoryItem.class );


        SubstanceTabCategoryItemTag substanceTabCategoryItemTag;
        dbResult = select.findJoinableEntityByName(SubstanceTabCategoryItemTag.class, substanceTabCategoryItem, tag );
        if ( dbResult.isEmpty() ) {
            substanceTabCategoryItemTag = new SubstanceTabCategoryItemTag( substanceTabCategoryItem, tag );
            dbResult = insertion.queryableEntity( substanceTabCategoryItemTag );
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

    // Retrieve each entity from Product tree. If entity exists, update accordingly. If not, created new entity and
    // append Id to the corresponding JoinTable.
    public DbResult updateProduct(@NotNull Product product) throws Exception {

        for (Substance newSubstance : product.getProduct().keySet() ){
            if(new Select().findProduct(newSubstance.getName()).isEmpty()){
                //return new DbResult("Product not exists. Cannot update.");
                throw new Exception("Product not exists. Cannot update.");
            }
        }

        Set<HashMap> results = new HashSet();
        for( Substance substance: product.getProduct().keySet() ){
            for( Tab tab : product.getProduct().get(substance).keySet() ){
                for ( Category category : product.getProduct().get(substance).get(tab).keySet() ){
                    for ( Item item : product.getProduct().get(substance).get(tab).get(category).keySet() ){
                        for ( Tag tag : product.getProduct().get(substance).get(tab).get(category).get( item ) ){
                            // Insert keys with value at each iteration.
                            results.add(findOrCreateEntity(substance, tab, category, item, tag).getResult(HashMap.class));
                        }
                    }
                }
            }
        }

        return new DbResult(results);
    }

    // If request body for update contains keyword 'replacement', then it finds matching entity and replace it with new values.
    public DbResult replaceProduct(@NotNull Product product, @NotNull LinkedHashMap body) throws Exception {

        HashMap bodyMap = (HashMap) body.get("replacement");
        Set<ArrayList<HashMap>> results = new HashSet();

        product.getProduct().forEach(
                (substanceKey, substanceValue) -> {
                    matchEntitiesToReplace("Substance", bodyMap, results, substanceKey, substanceKey );
                    substanceValue.forEach(
                            (tabKey, tabValue) -> {
                                matchEntitiesToReplace("Tab", bodyMap, results, tabKey, substanceKey, tabKey );
                                tabValue.forEach(
                                        (categoryKey, categoryValue) -> {
                                            matchEntitiesToReplace("Category", bodyMap, results, categoryKey, substanceKey, tabKey, categoryKey );
                                        categoryValue.forEach(
                                                (itemKey, itemValue) -> {
                                                    matchEntitiesToReplace("Item", bodyMap, results, itemKey, substanceKey, tabKey, categoryKey, itemKey );
                                                    itemValue.forEach(
                                                            tag -> {
                                                                matchEntitiesToReplace("Tag", bodyMap, results, tag, substanceKey, tabKey, categoryKey, itemKey, tag );
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

    private void matchEntitiesToReplace(String keyName, HashMap bodyMap, Set<ArrayList<HashMap>> results, Operable entity, Operable... collection){
        if(bodyMap.containsKey(keyName)) {
            bodyMap.values().forEach(pairToReplace -> {
                ArrayList<LinkedHashMap > valsToReplace = (ArrayList)pairToReplace;
                valsToReplace.forEach(val -> {
                    if(val.containsKey(entity.getName())){
                        results.add(processBodyKeys((ArrayList) bodyMap.get(keyName), collection));
                    }
                });
            });
        }
    }

    // Performs actual replacement of entity Id at joinTable by checking key paths of entities in order to find the corresponding entity.
    private HashMap<String, String> updateJoinTable(ArrayList<HashMap> bodyArrayToProcess, Operable... indexEntities){

        HashMap<Class<? extends Operable>, Operable> operableSet = new HashMap();
        for(Operable entity : indexEntities){ operableSet.put(entity.getClass(), entity); }
        HashMap<String, String> results = new HashMap();
        Delete delete = new Delete();

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
                                        delete.deleteEntityByName(newTag.getClass(), oldKey.toString());
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
                                        delete.deleteEntityByName(newItem.getClass(), oldKey.toString());
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
                                        delete.deleteEntityByName(newCategory.getClass(), oldKey.toString());
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

                                    System.out.println("toReplace: " + newTab.getName() );

                                    if(!session.getTransaction().isActive()) session.getTransaction().begin();
                                    //session.getReference(SubstanceTab.class, )
                                    substanceTab.setTab(newTab);
                                    session.merge(substanceTab);
                                    session.getTransaction().commit();


                                    Integer oldKeyJoins = getRecordsCount(substanceTab.getClass(), newTab.getClass(), oldKey.toString()).getResult(Integer.class);
                                    if(oldKeyJoins == 0){
                                        delete.deleteEntityByName(newTab.getClass(), oldKey.toString());
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

    // Change User password
    public DbResult modifyUserPassword(@NotNull BCryptPasswordEncoder bCryptPasswordEncoder,
                                       @NotNull String username, @NotNull String oldPassword,
                                       @NotNull String newPassword) throws Exception {

        if(oldPassword.equals(newPassword)){
            throw new Exception("Old password and new password is the same");
        }

        DbResult dbResult = new Select().findUser(username);
        if(dbResult.isEmpty()){
            throw new Exception("User not found");
        }

        if(dbResult.getException() != null){
            throw dbResult.getException();
        }

        User user = dbResult.getResult(User.class);
        if(!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())){
            throw new Exception("Wrong password");
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        try{
            if (!session.getTransaction().isActive()) session.getTransaction().begin();
            session.merge(user);
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

    // Change User Enabled status and Role.
    public DbResult modifyUserRoleAndStatus(@NotNull BCryptPasswordEncoder bCryptPasswordEncoder,
                                            @NotNull String username, @NotNull String password,
                                            @NotNull String role, @NotNull char enabled) throws Exception {

        Select select = new Select();
        DbResult dbResult = select.findUser(username);
        if(dbResult.isEmpty()){
            throw new Exception("User not found");
        }
        else{
            User user = dbResult.getResult(User.class);
            if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
                throw new Exception("Wrong password");
            }

            dbResult = select.findRole(Role.class, role);
            Role roleEntity;
            if(dbResult.isEmpty()){
                Serializable insertedRoleUserId =  session.save(new Role(role));
                roleEntity = session.find(Role.class, insertedRoleUserId );
            }
            else{
                roleEntity = dbResult.getResult(Role.class);
            }

            user.setRole(roleEntity);
            user.setEnabled(enabled);

            try {
                if (!session.getTransaction().isActive()) session.getTransaction().begin();
                session.merge(user);
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