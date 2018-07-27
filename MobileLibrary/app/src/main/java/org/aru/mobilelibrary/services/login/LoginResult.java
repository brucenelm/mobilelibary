package org.aru.mobilelibrary.services.login;

import org.aru.mobilelibrary.services.ServiceResult;

/**
 * Created by mmichalek on 10/21/15.
 */
public class LoginResult extends ServiceResult {

    private boolean ok;
    private UserModel user;
    private String cookie;

    private String loginSummaryHtml;


    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }


    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getLoginSummaryHtml() {
        return loginSummaryHtml;
    }

    public void setLoginSummaryHtml(String loginSummaryHtml) {
        this.loginSummaryHtml = loginSummaryHtml;
    }
}
