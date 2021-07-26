package com.unipi.adouladiris.medicalstorage.database.dao.delete;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.businessmodel.Product;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.entities.Queryable;
import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;

public interface DeleteInterface {

    DbResult deleteEntityByName(@NotNull Class<? extends Operable> typeClass, @NotNull String name);
    DbResult deleteEntityById(@NotNull Class<? extends Queryable> typeClass, @NotNull Integer id);
    DbResult deleteProduct(@NotNull Product product);

}
