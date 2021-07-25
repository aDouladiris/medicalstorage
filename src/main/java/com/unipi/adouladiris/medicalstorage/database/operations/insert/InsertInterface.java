package com.unipi.adouladiris.medicalstorage.database.operations.insert;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.businessmodel.Product;
import com.unipi.adouladiris.medicalstorage.database.DbResult;
import com.unipi.adouladiris.medicalstorage.entities.Queryable;
import com.unipi.adouladiris.medicalstorage.entities.operable.*;
import com.unipi.adouladiris.medicalstorage.entities.operable.Substance;

public interface InsertInterface {

    DbResult queryableEntity(@NotNull Queryable queryable);
    DbResult product(@NotNull Product product);
    DbResult product(@NotNull Substance substance, @NotNull Tab tab, @NotNull Category category, @NotNull Item item, @NotNull Tag tag);
    //TODO need review
//    DbResult product(@NotNull Substance substance, @NotNull Tab tab, @NotNull Category category, @NotNull Item item, @NotNull TreeSet<Tag> tagSet);
//    DbResult product(@NotNull Substance substance, @NotNull Tab tab, @NotNull Category category, @NotNull TreeSet<Item> itemTreeSet, @NotNull TreeSet<Tag> tagTreeSet);

}