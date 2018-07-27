package org.aru.mobilelibrary.services.login;

import android.util.Log;

import org.aru.mobilelibrary.services.resources.CheckedOutResource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by mmichalek on 10/21/15.
 */
public class KohaHTMLLoginService implements LoginService {
    @Override
    public LoginResult performLogin(String username, String password) {
        LoginResult result = loadLoginPage(username, password);
        return result;
    }

    public LoginResult loadLoginPage(String username, String password) {

        try {
            URL url = new URL("http://41.210.169.134:4141/cgi-bin/koha/opac-user.pl");
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setDoOutput(true);

            String loginForm = String.format("koha_login_context=opac&userid=%s&password=%s", username, password);

            byte[] out = loginForm.toString().getBytes("UTF8");
            int length = out.length;

            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            http.connect();
            OutputStream os = http.getOutputStream();
            os.write(out);

            BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                result.append(line);
            }
            System.out.println(result.toString());
            String cookie = http.getHeaderField("Set-Cookie");


            System.out.println("The cookie is " + cookie);

            LoginResult loginResult = parseDocument(username, cookie, result.toString());
            if (loginResult.isOk()) {
                System.out.println(loginResult.getUser().getFullName());
                if (loginResult.getUser().getCheckedOutResources().isEmpty()) {
                    System.out.println("No items checked out.");
                } else {
                    System.out.println(loginResult.getUser().getCheckedOutResources().size() + " items checked out.");
                }
            } else {
                System.out.println("Login failed.");
            }

            return loginResult;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            LoginResult result = new LoginResult();
            result.setOk(false);
            result.setMessage(ex.getMessage());
            return result;
        }
    }

    protected static LoginResult parseDocument(String username, String cookie, String loginDocument) {
        LoginResult loginResult = new LoginResult();
        UserModel userModel = new UserModel();
        loginResult.setUser(userModel);
        userModel.setUsername(username);

        Document document = Jsoup.parse(loginDocument);
        Elements elements = document.select("div#userdetails > h2");
        if (elements.size() > 0) {
            Element element = elements.get(0);
            String helloFirstNameLastName = element.text();
            String fullName = helloFirstNameLastName.replace("Hello,", "").trim();
            //System.out.println(fullName);
            userModel.setFullName(fullName);
            loginResult.setCookie(cookie);
            loginResult.setOk(true);

            loadCheckoutResources(loginResult, document);
        }

        return loginResult;
    }

    private static void loadCheckoutResources(LoginResult loginResult, Document document) {
        Elements checkedOutRows = document.select("table#checkoutst > tbody > tr");

//        CheckedOutResource demoResource = new CheckedOutResource();
//        demoResource.setTitle("Systematic Theology");
//        demoResource.setAuthor("W. Grudem");
//        demoResource.setDue("Feb 12, 2018");
//        loginResult.getUser().getCheckedOutResources().add(demoResource);
//        loginResult.getUser().getCheckedOutResources().add(demoResource);

        for (Element element : checkedOutRows) {
            CheckedOutResource resource = new CheckedOutResource();
            {
                String value = element.select("td.title > a").text();
                System.out.println("Title " + value);
                resource.setTitle(value);
            }
            {
                String value = element.select("td.author").text();
                resource.setAuthor(value);
            }
            {
                String value = element.select("td.date_due").text();
                value = value.replace("Date due:", "").trim();
                resource.setDue(value);
            }
            {
                String value = element.select("td.call_no").text();
                value = value.replace("Call no.:", "").trim();
                resource.setCallNumber(value);
            }
            {
                String value = element.select("td.renew").text();
                resource.setRenewalText(value);
            }
            {
                String value = element.select("td.fines").text();
                value = value.replace("Fines:", "").trim();
                resource.setFines(value);
            }

            loginResult.getUser().getCheckedOutResources().add(resource);

        }



    }

}
