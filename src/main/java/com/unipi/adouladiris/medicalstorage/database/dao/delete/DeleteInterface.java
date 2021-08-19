package com.unipi.adouladiris.medicalstorage.database.dao.delete;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.entities.Queryable;
import com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass.Operable;

public interface DeleteInterface {

    DbResult deleteEntityByName(@NotNull Class<? extends Operable> typeClass, @NotNull String name);
    DbResult deleteEntityById(@NotNull Class<? extends Queryable> typeClass, @NotNull Integer id);
    DbResult deleteProduct(@NotNull Product product);

}
