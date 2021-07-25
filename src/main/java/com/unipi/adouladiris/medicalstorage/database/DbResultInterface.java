package com.unipi.adouladiris.medicalstorage.database;

import com.sun.istack.NotNull;

public interface DbResultInterface <T> {

    Boolean isEmpty();
    Object getResult();

    //    Object getResult(@NotNull Class<T> type);
    void setResult(@NotNull T result);

    Exception getException();
    void setException(@NotNull Exception exception);
}
