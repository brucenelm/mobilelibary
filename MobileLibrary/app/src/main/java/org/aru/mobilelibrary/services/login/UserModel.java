package org.aru.mobilelibrary.services.login;

import org.aru.mobilelibrary.services.resources.CheckedOutResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mmichalek on 10/21/15.
 */
public class UserModel {

    private String username;
    private String fullName;
    private List<CheckedOutResource> checkedOutResources = new ArrayList<CheckedOutResource>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<CheckedOutResource> getCheckedOutResources() {
        return checkedOutResources;
    }

    public void setCheckedOutResources(List<CheckedOutResource> checkedOutResources) {
        this.checkedOutResources = checkedOutResources;
    }
}
