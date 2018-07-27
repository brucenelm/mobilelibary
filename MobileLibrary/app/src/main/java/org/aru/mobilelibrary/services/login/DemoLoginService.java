package org.aru.mobilelibrary.services.login;

/**
 * Created by mmichalek on 10/21/15.
 */
public class DemoLoginService implements LoginService {

    @Override
    public LoginResult performLogin(String username, String password) {

        if (username.equalsIgnoreCase("aru")) {
            return buildSuccessfulLogin(username);
        }
        else {
            return buildFailedLogin();
        }
    }

    private LoginResult buildSuccessfulLogin(String username) {
        LoginResult loginResult = new LoginResult();
        loginResult.setOk(true);
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setFullName("Richard Mugarura");
        return  loginResult;
    }

    protected LoginResult buildFailedLogin() {
        LoginResult failedLogin = new LoginResult();
        failedLogin.setOk(false);
        failedLogin.setMessage("Unknown username or password.");
        return  failedLogin;
    }
}
