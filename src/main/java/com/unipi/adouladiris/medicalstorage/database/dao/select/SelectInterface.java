package com.unipi.adouladiris.medicalstorage.database.dao.select;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.entities.jointables.abstractClass.Joinable;
import com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass.Operable;

public interface SelectInterface {
    // A Product is a composite class containing references from Substance, Tab, Category
    // and a set of Items in a form of a HashMap.
    DbResult findProduct(@NotNull String name);
    DbResult findAllProducts();

    // Entities that have one-to-many and many-to-many relationships are Substance, Tab, Category, Item.
    DbResult findOperableEntityByName(@NotNull String name);
    DbResult findOperableEntityByName(@NotNull Class<? extends Operable> classType, @NotNull String name);
    DbResult findJoinableEntityByName(@NotNull Class<? extends Joinable> classType, @NotNull Operable op1, @NotNull Operable op2);
    DbResult findJoinableEntityByName(@NotNull Class<? extends Joinable> classType, @NotNull Joinable op1, @NotNull Operable op2);

    // Search Product or Substance, Tab, Category, Item by keyword?
    DbResult findByTag(String tag);
}
