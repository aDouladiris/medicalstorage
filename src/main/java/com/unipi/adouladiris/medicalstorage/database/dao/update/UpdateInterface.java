package com.unipi.adouladiris.medicalstorage.database.dao.update;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;

public interface UpdateInterface {
    //TODO all need better attribute update
    // Replaces entities, not adding new.
    DbResult entityById(@NotNull Integer id, @NotNull Operable operable);
    DbResult entityByName(@NotNull String name, @NotNull Operable operable);

//    DbResult product(@NotNull String name, Operable... operables);
//    DbResult product(@NotNull String name, String... cols, Operable... operables);

}
