package com.unipi.adouladiris.medicalstorage.database;

import com.sun.istack.NotNull;

//TODO create constructor
public class DbResult implements DbResultInterface {

    private Boolean empty;
    private Object result;
    private Exception exception;

    public DbResult() { this.empty = true; }
    public DbResult(@NotNull Object result){ this.result = result; this.empty = false; }

    @Override
    public Boolean isEmpty() {
        if (this.result == null) this.empty = true;
        else this.empty = this.exception != null;
        return empty;
    }

    @Override
    public Object getResult() { return result; }

    //    @Override TODO review interface
    public <T> T getResult(@NotNull Class<T> type) { this.empty = false; return type.cast( result ); } // Requires class. Returns the casted object from the argument class type

    @Override
    public void setResult(@NotNull Object result) { this.result = result; this.empty = false; }

    @Override
    public Exception getException() { return exception; }

    @Override
    public void setException(@NotNull Exception exception) { this.exception = exception; }


}
