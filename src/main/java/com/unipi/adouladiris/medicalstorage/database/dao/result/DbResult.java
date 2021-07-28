package com.unipi.adouladiris.medicalstorage.database.dao.result;

import com.sun.istack.NotNull;

//TODO create constructor
public class DbResult implements DbResultInterface {

    private Boolean empty = true;
    private Object result = null;
    private Exception exception = null;

    public DbResult() { this.empty = true; }
    public DbResult(@NotNull Object result){
        if(result instanceof Exception ) this.exception = (Exception)result;
        else this.result = result;
        this.empty = false;
    }

    @Override
    public Boolean isEmpty() {
        if (this.result == null && this.exception == null) this.empty = true;
        else this.empty = false;
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
