package com.unipi.adouladiris.medicalstorage.database.dao;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.database.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.session.DbEntitySessionManager;
import com.unipi.adouladiris.medicalstorage.entities.jointables.*;
import com.unipi.adouladiris.medicalstorage.entities.jointables.abstractClass.Joinable;
import com.unipi.adouladiris.medicalstorage.entities.operables.*;
import com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass.Operable;
import com.unipi.adouladiris.medicalstorage.entities.users.User;
import com.unipi.adouladiris.medicalstorage.entities.users.UserRole;

import javax.persistence.Query;
import java.util.*;

public class Select extends DbEntitySessionManager {

    // Find all Product trees by joining all entities according to join tables.
    public DbResult findAllProducts(){
        String select = "SELECT " + // returns a list of objects
                "st.substance, " +  // Substance Object
                "st.tab, " +        // Tab Object
                "stc.category, " +  // Category Object
                "stci.item, " +     // Item Object
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

        DbResult dbResult = new DbResult();
        if( queryResultList.isEmpty() ){ return dbResult; }

        TreeSet<Product> productSet = new TreeSet<>();
        TreeSet<Tag> tagSet;
        Product product = null;

        for(Object[] row: queryResultList){

            Substance substance = (Substance)row[0];
            Tab tab = (Tab)row[1];
            Category category = (Category)row[2];
            Item item = (Item)row[3];
            Tag tag = (Tag)row[4];
            if(!productSet.isEmpty()) {
                for (Product containedProduct : productSet){
                    if(containedProduct.getProduct().containsKey( substance )){
                        product = containedProduct;
                    }
                }
            }

            // If updateProduct is uninitialized or do not contain a key that we can update, then create a new path and add it to the Set.
            if( product == null || !product.getProduct().containsKey( substance ) ){
                // Create new Product.
                product = new Product();
                // Create new TreeSet of Tags.
                tagSet = new TreeSet<>();
                // Insert Tag to TreeSet
                tagSet.add( tag );
                // Create new TreeMap of Items and TreeSet of Tags.
                TreeMap<Item, TreeSet<Tag>> itemTagTreeMap = new TreeMap<>();
                // Insert item and TreeSet of Tags to the map.
                itemTagTreeMap.put( item, tagSet );
                // Create new TreeMap of Category and TreeMap of Item and TreeSet of Tags.
                TreeMap<Category, TreeMap<Item, TreeSet<Tag>> > categoryItemTagTreeMap = new TreeMap<>();
                // Insert category and TreeMap of Item and TreeSet of Tags to the TreeMap.
                categoryItemTagTreeMap.put(category, itemTagTreeMap);
                // Create new TreeMap of Tabs and TreeMap of Category and TreeMap of Item and TreeSet of Tags.
                TreeMap<Tab, TreeMap<Category, TreeMap<Item, TreeSet<Tag>>> > tmpTabCatItemTagTreeMap = new TreeMap<>();
                // Insert tab and TreeMap of Category and TreeMap of Item and TreeSet of Tags to the TreeMap.
                tmpTabCatItemTagTreeMap.put(tab, categoryItemTagTreeMap);
                // Insert substance and nested TreeMap to the updateProduct nested TreeMap.
                product.getProduct().put(substance, tmpTabCatItemTagTreeMap);
                // Add updateProduct ot the result TreeSet of Products.
                productSet.add(product);
            }
            else{
                // If tab exists
                if( product.getProduct().get(substance) != null && product.getProduct().get(substance).containsKey(tab) ) {
                    // If category exists
                    if( product.getProduct().get(substance).get(tab).containsKey( category ) ){
                        // If item exists
                        if( product.getProduct().get(substance).get(tab).get(category).containsKey( item ) ){
                            product.getProduct().get(substance).get(tab).get(category).get(item).add( tag );
                        }
                        // If item not exists
                        else{
                            tagSet = new TreeSet<>();
                            tagSet.add( tag );
                            product.getProduct().get(substance).get(tab).get( category ).put( item, tagSet );
                        }
                    }
                    // If category not exists
                    else{
                        tagSet = new TreeSet<>();
                        tagSet.add( tag );
                        TreeMap<Item, TreeSet<Tag> > itemTagTreeMap = new TreeMap<>();
                        itemTagTreeMap.put( item, tagSet );
                        product.getProduct().get(substance).get(tab).put( category, itemTagTreeMap );
                    }
                }
                // If tab not exists
                else{
                    tagSet = new TreeSet<>();
                    tagSet.add( tag );
                    TreeMap<Item, TreeSet<Tag> > itemTagTreeMap = new TreeMap<>();
                    itemTagTreeMap.put( item, tagSet );
                    TreeMap<Category, TreeMap<Item, TreeSet<Tag>> > categoryItemTagTreeMap = new TreeMap<>();
                    categoryItemTagTreeMap.put( category, itemTagTreeMap );
                    product.getProduct().get(substance).put( tab, categoryItemTagTreeMap );
                }
            }
        }

        dbResult.setResult(productSet);
        return dbResult;
    }

    // Find Product tree with the matching substance by joining all entities according to join tables.
    public DbResult findProduct(String name){

        String select = "SELECT " + // returns a list of objects
                "st.substance, " +  // Substance Object
                "st.tab, " +        // Tab Object
                "stc.category, " +  // Category Object
                "stci.item, " +     // Item Object
                "stcit.tag " +      // Tag Object
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

        DbResult dbResult = new DbResult();
        if( queryResultList.isEmpty() ){ return dbResult; }

        TreeSet<Product> productSet = new TreeSet<>();
        TreeSet<Tag> tagSet;
        Product product = null;

        for(Object[] row: queryResultList){

            Substance substance = (Substance)row[0];
            Tab tab = (Tab)row[1];
            Category category = (Category)row[2];
            Item item = (Item)row[3];
            Tag tag = (Tag)row[4];

            // If updateProduct is uninitialized or do not contain a key that we can update, then create a new path and add it to the Set.
            if( product == null || !product.getProduct().containsKey( substance ) ){
//                System.out.println("New updateProduct");
                // Create new Product.
                product = new Product();
                // Create new TreeSet of Tags.
                tagSet = new TreeSet<>();
                // Insert Tag to TreeSet
                tagSet.add( tag );
                // Create new TreeMap of Items and TreeSet of Tags.
                TreeMap<Item, TreeSet<Tag>> itemTagTreeMap = new TreeMap<>();
                // Insert item and TreeSet of Tags to the map.
                itemTagTreeMap.put( item, tagSet );
                // Create new TreeMap of Category and TreeMap of Item and TreeSet of Tags.
                TreeMap<Category, TreeMap<Item, TreeSet<Tag>> > categoryItemTagTreeMap = new TreeMap<>();
                // Insert category and TreeMap of Item and TreeSet of Tags to the TreeMap.
                categoryItemTagTreeMap.put(category, itemTagTreeMap);
                // Create new TreeMap of Tabs and TreeMap of Category and TreeMap of Item and TreeSet of Tags.
                TreeMap<Tab, TreeMap<Category, TreeMap<Item, TreeSet<Tag>>> > tmpTabCatItemTagTreeMap = new TreeMap<>();
                // Insert tab and TreeMap of Category and TreeMap of Item and TreeSet of Tags to the TreeMap.
                tmpTabCatItemTagTreeMap.put(tab, categoryItemTagTreeMap);
                // Insert substance and nested TreeMap to the updateProduct nested TreeMap.
                product.getProduct().put(substance, tmpTabCatItemTagTreeMap);
                // Add updateProduct ot the result TreeSet of Products.
                productSet.add(product);
            }
            else{
                // If tab exists
                if( product.getProduct().get(substance) != null && product.getProduct().get(substance).containsKey(tab) ) {
                    // If category exists
                    if( product.getProduct().get(substance).get(tab).containsKey( category ) ){
                        // If item exists
                        if( product.getProduct().get(substance).get(tab).get(category).containsKey( item ) ){
                            product.getProduct().get(substance).get(tab).get(category).get(item).add( tag );
                        }
                        // If item not exists
                        else{
                            tagSet = new TreeSet<>();
                            tagSet.add( tag );
                            product.getProduct().get(substance).get(tab).get( category ).put( item, tagSet );
                        }
                    }
                    // If category not exists
                    else{
                        tagSet = new TreeSet<>();
                        tagSet.add( tag );
                        TreeMap<Item, TreeSet<Tag> > itemTagTreeMap = new TreeMap<>();
                        itemTagTreeMap.put( item, tagSet );
                        product.getProduct().get(substance).get(tab).put( category, itemTagTreeMap );
                    }
                }
                // If tab not exists
                else{
                    tagSet = new TreeSet<>();
                    tagSet.add( tag );
                    TreeMap<Item, TreeSet<Tag> > itemTagTreeMap = new TreeMap<>();
                    itemTagTreeMap.put( item, tagSet );
                    TreeMap<Category, TreeMap<Item, TreeSet<Tag>> > categoryItemTagTreeMap = new TreeMap<>();
                    categoryItemTagTreeMap.put( category, itemTagTreeMap );
                    product.getProduct().get(substance).put( tab, categoryItemTagTreeMap );
                }
            }
        }

        dbResult.setResult(product);
        return dbResult;
    }

    // Find entity instance by its name property.
    public DbResult findOperableEntityByName(@NotNull Class<? extends Operable> classType, @NotNull String name) {
        String entityClassName = classType.getSimpleName();
        // Tables cannot be parameter values
        StringBuilder select = new StringBuilder();
        if( entityClassName.equals( "Substance" ) ){
            select.append("FROM Substance as sub WHERE sub.name = :tmpName ");
        }
        else if( entityClassName.equals( "Tab" ) ){
            select.append("FROM Tab as tab WHERE tab.name = :tmpName ");
        }
        else if( entityClassName.equals( "Category" ) ){
            select.append("FROM Category as cat WHERE cat.name = :tmpName ");
        }
        else if( entityClassName.equals( "Item" ) ){
            select.append("FROM Item as item WHERE item.name = :tmpName ");
        }
        else if( entityClassName.equals( "Tag" ) ){
            select.append("FROM Tag as tag WHERE tag.name = :tmpName ");
        }

        Query query = session.createQuery( select.toString() );
        query.setParameter("tmpName", name);
        List<Object[]> queryResultList = query.getResultList(); // Result contains rows, row contains columns

        DbResult dbResult = new DbResult();
        if( queryResultList.isEmpty() ){ return dbResult; }
        dbResult.setResult( queryResultList.get(0) );
        return dbResult;
    }

    public DbResult findItemEntityByNameAndDescription(@NotNull String name, @NotNull String description) {
        // Tables cannot be parameter values
        StringBuilder select = new StringBuilder();
        select.append("FROM Item as item WHERE item.name = :tmpName AND item.description = :tmpDescription ");

        Query query = session.createQuery( select.toString() );
        query.setParameter("tmpName", name);
        query.setParameter("tmpDescription", description);

        List<Object[]> queryResultList = query.getResultList(); // Result contains rows, row contains columns
        DbResult dbResult = new DbResult();
        if( queryResultList.isEmpty() ){ return dbResult; }
        dbResult.setResult( queryResultList.get(0) );
        return dbResult;
    }

    // Find Role entity by its name.
    public DbResult findRole(@NotNull Class<? extends UserRole> classType, @NotNull String name) {
        String entityClassName = classType.getSimpleName();
        // Tables cannot be parameter values
        StringBuilder select = new StringBuilder();
         if( entityClassName.equals( "Role" ) ){
            select.append("FROM Role as role WHERE role.authority = :tmpName ");
        }

        Query query = session.createQuery( select.toString() );
        query.setParameter("tmpName", name);
        List<Object[]> queryResultList = query.getResultList(); // Result contains rows, row contains columns

        DbResult dbResult = new DbResult();
        if( queryResultList.isEmpty() ){ return dbResult; }
        dbResult.setResult( queryResultList.get(0) );

        return dbResult;
    }

    // Find join table entity (which is SubstanceTab) containing foreign keys from entities that are not join tables.
    public DbResult findJoinableEntityByName(@NotNull Class<? extends Joinable> classType, @NotNull Operable op1, @NotNull Operable op2) {
        String entityClassName = classType.getSimpleName();
        // Tables cannot be parameter values
        StringBuilder select = new StringBuilder();
        if( entityClassName.equals( "SubstanceTab" ) ){
            if( op1.getClass().getSimpleName().equals("Substance") )
                select.append("FROM SubstanceTab as st WHERE st.substance.Id = :op1 AND st.tab.Id = :op2 ");
            else select.append("FROM SubstanceTab as st WHERE st.tab.Id = :op1 AND st.substance.Id = :op2 ");
        }

        Query query = session.createQuery( select.toString() );
        query.setParameter("op1", op1.getId());
        query.setParameter("op2", op2.getId());
        List<Object[]> queryResultList = query.getResultList(); // Result contains rows, row contains columns

        DbResult dbResult = new DbResult();
        if( queryResultList.isEmpty() ){ return dbResult; }
        dbResult.setResult( queryResultList.get(0) );
        return dbResult;
    }

    // Find join table entities(all join tables except SubstanceTab) containing foreign keys from entities that are join tables and entities that are not.
    public DbResult findJoinableEntityByName(@NotNull Class<? extends Joinable> classType, @NotNull Joinable op1, @NotNull Operable op2) {
        String entityClassName = classType.getSimpleName();
        // Tables cannot be parameter values
        StringBuilder select = new StringBuilder();
        if( entityClassName.equals( "SubstanceTabCategory" ) ){
            if( op1.getClass().getSimpleName().equals("Category") )
                select.append("FROM SubstanceTabCategory as stc WHERE stc.category.Id = :op1 AND stc.substanceTab.Id = :op2 ");
            else select.append("FROM SubstanceTabCategory as stc WHERE stc.substanceTab.Id = :op1 AND stc.category.Id = :op2 ");
        }
        else if( entityClassName.equals( "SubstanceTabCategoryItem" ) ){
            if( op1.getClass().getSimpleName().equals("Item") )
                select.append("FROM SubstanceTabCategoryItem as stci WHERE stci.item.Id = :op1 AND stci.substanceTabCategory.Id = :op2 ");
            else select.append("FROM SubstanceTabCategoryItem as stci WHERE stci.substanceTabCategory.Id = :op1 AND stci.item.Id = :op2 ");
        }
        else if( entityClassName.equals( "SubstanceTabCategoryItemTag" ) ){
            if( op1.getClass().getSimpleName().equals("Tag") )
                select.append("FROM SubstanceTabCategoryItemTag as stcit WHERE stcit.tag.Id = :op1 AND stcit.substanceTabCategoryItem.Id = :op2 ");
            else select.append("FROM SubstanceTabCategoryItemTag as stcit WHERE stcit.substanceTabCategoryItem.Id = :op1 AND stcit.tag.Id = :op2 ");
        }

        Query query = session.createQuery( select.toString() );
        query.setParameter("op1", op1.getId());
        query.setParameter("op2", op2.getId());
        List<Object[]> queryResultList = query.getResultList(); // Result contains rows, row contains columns

        DbResult dbResult = new DbResult();
        if( queryResultList.isEmpty() ){ return dbResult; }
        dbResult.setResult( queryResultList.get(0) );
        return dbResult;
    }

    // Find Substance matching tag entities names and retrieve the corresponding Product tree.
    public DbResult findByTag(String tag){

        Set<Product> results = new HashSet();

        String select = "SELECT " +         // returns a list of objects
                "st.substance " +  // Substance Object
                "FROM SubstanceTabCategoryItemTag stcit " +
                "INNER JOIN stcit.substanceTabCategoryItem AS stci " +
                "INNER JOIN stcit.tag " +

                "INNER JOIN stci.substanceTabCategory AS stc " +
                "INNER JOIN stci.item " +

                "INNER JOIN stc.substanceTab AS st " +
                "INNER JOIN stc.category " +

                "INNER JOIN st.substance " +
                "INNER JOIN st.tab " +

                "WHERE stcit.tag.name = :tag ";

        Query query = session.createQuery(select);
        query.setParameter("tag", tag);
        List<Object[]> queryResultList = query.getResultList(); // Result contains rows, row contains columns

        for( Object o : queryResultList ) {
            Substance substance = (Substance)o;
            results.add(findProduct( substance.getName() ).getResult(Product.class));
        }
        return new DbResult(results);
    }

    public DbResult findUser(String username){
        String select = "SELECT " +
                "user " +               // User Object
                "FROM User user " +
                "INNER JOIN user.role AS role " +
                "WHERE user.username = :username ";

        Query query = session.createQuery(select);
        query.setParameter("username", username);
        List<Object[]> queryResultList = query.getResultList(); // Result contains rows, row contains columns

        for( Object o : queryResultList ) { return new DbResult(User.class.cast(o)); }
        return new DbResult();
    }


    //    public DbResult findJoinTables(String name){
//
//        String select =
//                "SELECT st, stc, stci, stcit " +
//                "FROM SubstanceTabCategoryItemTag stcit " +
//                "INNER JOIN stcit.substanceTabCategoryItem AS stci " +
//                "INNER JOIN stcit.tag " +
//                "INNER JOIN stci.substanceTabCategory AS stc " +
//                "INNER JOIN stci.item " +
//                "INNER JOIN stc.substanceTab AS st " +
//                "INNER JOIN stc.category " +
//                "INNER JOIN st.substance " +
//                "INNER JOIN st.tab " +
//                "WHERE st.substance.name = :subName ";
//
//        Query query = session.createQuery(select);
//        query.setParameter("subName", name);
//        List<Object[]> queryResultList = query.getResultList(); // Result contains rows, row contains columns
//
//        DbResult dbResult = new DbResult();
//        if( queryResultList.isEmpty() ){ return dbResult; }
//
//        TreeSet<Joinable> joinableSet = new TreeSet<>();
//
//        for(Object[] row: queryResultList) {
//            SubstanceTab substanceTab = (SubstanceTab) row[0];
//            SubstanceTabCategory substanceTabCategory = (SubstanceTabCategory) row[1];
//            SubstanceTabCategoryItem substanceTabCategoryItem = (SubstanceTabCategoryItem) row[2];
//            SubstanceTabCategoryItemTag substanceTabCategoryItemTag = (SubstanceTabCategoryItemTag) row[3];
//            joinableSet.add(substanceTab);
//            joinableSet.add(substanceTabCategory);
//            joinableSet.add(substanceTabCategoryItem);
//            joinableSet.add(substanceTabCategoryItemTag);
//        }
//
//        dbResult.setResult(joinableSet);
//        return dbResult;
//    }

//    public DbResult findJoinTable(Class<? extends Joinable> joinClass, String name){
//
//        String select =
//                "SELECT st, stc, stci, stcit " +
//                "FROM SubstanceTabCategoryItemTag stcit " +
//                "INNER JOIN stcit.substanceTabCategoryItem AS stci " +
//                "INNER JOIN stcit.tag " +
//                "INNER JOIN stci.substanceTabCategory AS stc " +
//                "INNER JOIN stci.item " +
//                "INNER JOIN stc.substanceTab AS st " +
//                "INNER JOIN stc.category " +
//                "INNER JOIN st.substance " +
//                "INNER JOIN st.tab " +
//                "WHERE st.substance.name = :subName ";
//
//        Query query = session.createQuery(select);
//        query.setParameter("subName", name);
//        List<Object[]> queryResultList = query.getResultList(); // Result contains rows, row contains columns
//
//        DbResult dbResult = new DbResult();
//        if( queryResultList.isEmpty() ){ return dbResult; }
//
//        TreeSet<Joinable> joinableSet = new TreeSet<>();
//
//        for(Object[] row: queryResultList) {
//            SubstanceTab substanceTab = (SubstanceTab) row[0];
//            SubstanceTabCategory substanceTabCategory = (SubstanceTabCategory) row[1];
//            SubstanceTabCategoryItem substanceTabCategoryItem = (SubstanceTabCategoryItem) row[2];
//            SubstanceTabCategoryItemTag substanceTabCategoryItemTag = (SubstanceTabCategoryItemTag) row[3];
//            if(substanceTab.getClass().equals(joinClass)) joinableSet.add(substanceTab);
//            if(substanceTabCategory.getClass().equals(joinClass)) joinableSet.add(substanceTabCategory);
//            if(substanceTabCategoryItem.getClass().equals(joinClass)) joinableSet.add(substanceTabCategoryItem);
//            if(substanceTabCategoryItemTag.getClass().equals(joinClass)) joinableSet.add(substanceTabCategoryItemTag);
//        }
//
//        dbResult.setResult(joinableSet);
//        return dbResult;
//    }

//    public DbResult findOperableEntityByName(String name) {
//        // Tables cannot be parameter values
//        Set<String> multiSelect = new HashSet<>();
//        multiSelect.add( "FROM Substance as sub WHERE sub.name = :tmpName " );
//        multiSelect.add( "FROM Tab as tab WHERE tab.name = :tmpName " );
//        multiSelect.add( "FROM Category as cat WHERE cat.name = :tmpName " );
//        multiSelect.add( "FROM Item as item WHERE item.name = :tmpName " );
//        multiSelect.add( "FROM Tag as tag WHERE tag.name = :tmpName " );
//
//        DbResult dbResult = new DbResult();
//        for ( String select : multiSelect ){
//
//            Query query = session.createQuery( select );
//            query.setParameter("tmpName", name);
//            List<Object[]> queryResultList = query.getResultList(); // Result contains rows, row contains columns
//            for (Object ob : queryResultList ){
//
//                if( queryResultList.size() > 0 ){
//
//                    if( queryResultList.isEmpty() ){ return dbResult; }
//                    Object o = queryResultList.get(0);
//                    if( ob.getClass().getSimpleName().equals( "Substance" ) ){
//                        dbResult.setResult( (Substance)o );
//                    }
//                    else if( ob.getClass().getSimpleName().equals( "Tab" ) ){
//                        dbResult.setResult( (Tab)o );
//                    }
//                    else if( ob.getClass().getSimpleName().equals( "Category" ) ){
//                        dbResult.setResult( (Category)o );
//                    }
//                    else if( ob.getClass().getSimpleName().equals( "Item" ) ){
//                        dbResult.setResult( (Item)o );
//                    }
//                    return dbResult;
//                }
//            }
//        }
//        return dbResult;
//    }


}

