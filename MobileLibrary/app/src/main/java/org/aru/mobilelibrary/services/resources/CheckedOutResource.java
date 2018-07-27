package org.aru.mobilelibrary.services.resources;

/**
 * Created by mmichalek on 2/11/18.
 */

public class CheckedOutResource {
    private String title;
    private String author;
    private String due;
    private String callNumber;
    private String renewalText;
    private String fines;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getDue() {
        return due;
    }
    public void setDue(String due) {
        this.due = due;
    }
    public String getCallNumber() {
        return callNumber;
    }
    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }
    public String getRenewalText() {
        return renewalText;
    }
    public void setRenewalText(String renewalText) {
        this.renewalText = renewalText;
    }
    public String getFines() {
        return fines;
    }
    public void setFines(String fines) {
        this.fines = fines;
    }

}
