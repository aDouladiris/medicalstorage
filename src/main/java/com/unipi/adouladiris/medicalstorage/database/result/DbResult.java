package com.unipi.adouladiris.medicalstorage.database.result;
import com.sun.istack.NotNull;

// Class that will contain every database result.
public class DbResult {

    private Boolean empty = true;
    private Object result = null;
    private Exception exception = null;

    public DbResult() { this.empty = true; }

    // Assign object to the corresponding field by identifying its class.
    public DbResult(@NotNull Object result){
        if(result instanceof Exception ) this.exception = (Exception)result;
        else this.result = result;
        this.empty = false;
    }

    // Returns if result is empty by checking exception and result properties (if they are null).
    public Boolean isEmpty() {
        if (this.result == null && this.exception == null) this.empty = true;
        else this.empty = false;
        return empty;
    }

    public Object getResult() { return result; }

    // Requires class. Returns the casted object from the argument class type
    public <T> T getResult(@NotNull Class<T> type) { this.empty = false; return type.cast( result ); }

    public void setResult(@NotNull Object result) { this.result = result; this.empty = false; }
    public Exception getException() { return exception; }
    //public void setException(@NotNull Exception exception) { this.exception = exception; }
}
