package org.aru.mobilelibrary.services;

/**
 * Created by mmichalek on 10/21/15.
 */
public abstract class ServiceResult {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
