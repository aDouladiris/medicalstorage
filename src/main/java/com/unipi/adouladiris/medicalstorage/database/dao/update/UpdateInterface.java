package com.unipi.adouladiris.medicalstorage.database.dao.update;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass.Operable;

import java.util.LinkedHashMap;

public interface UpdateInterface {
    //TODO all need better attribute update
    // Replaces entities, not adding new.
    DbResult entityById(@NotNull Integer id, @NotNull Operable operable);
    DbResult entityByName(@NotNull String name, @NotNull Operable operable);
    DbResult product(@NotNull Product product);
    DbResult replaceProduct(@NotNull Product product, @NotNull LinkedHashMap body) throws Exception;

//    DbResult product(@NotNull String name, Operable... operables);
//    DbResult product(@NotNull String name, String... cols, Operable... operables);

}
