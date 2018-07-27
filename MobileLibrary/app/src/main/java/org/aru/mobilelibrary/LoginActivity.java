package org.aru.mobilelibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.aru.mobilelibrary.services.ServiceFactory;
import org.aru.mobilelibrary.services.login.LoginResult;
import org.aru.mobilelibrary.services.login.LoginService;
import org.aru.mobilelibrary.services.resources.ResourceSearchResult;
import org.aru.mobilelibrary.services.resources.ResourceService;

/**
 * Created by mmichalek on 10/21/15.
 */
public class LoginActivity extends AppCompatActivity
        implements ResourceListFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        final EditText editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        final EditText editTextPassword = (EditText)findViewById(R.id.editTextPassword);

        Button button = (Button) findViewById(R.id.loginButton);

        final LoginService loginService = ServiceFactory.instance().getService(LoginService.class);
        final ProgressDialog progress = new ProgressDialog(this);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AsyncTask<String, Void, LoginResult>() {

                    protected void onPreExecute() {
                        progress.setTitle("Logging in...");
                        progress.show();
                    }

                    protected LoginResult doInBackground(String... params) {
                        LoginResult result =
                                loginService.performLogin(params[0], params[1]);
                        return result;
                    }

                    protected void onPostExecute(LoginResult result) {
                        progress.dismiss();
                        if (result.isOk()) {
                            Intent intent = new Intent(LoginActivity.this, ResourceSearchActivity.class);
                            Scope.setValue(Scope.USER_MODEL, result);
                            startActivity(intent);
                        }
                        else {
                            Scope.setValue(Scope.USER_MODEL, null);
                            Context context = getApplicationContext();
                            CharSequence text = result.getMessage();
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text,  duration);
                            toast.show();
                        }
                    }
                }.execute(editTextUsername.getText().toString(),
                        editTextPassword.getText().toString()
                );

            }
        });



        TextView libraryLinks = (TextView)findViewById(R.id.libraryLinks);
        libraryLinks.setText(Html.fromHtml(
                "<br/><a href=\"http://www.theologicalbooknetwork.org/Resources\">Theological Book Network</a>" +
                        "<br/><a href=\"http://www.jstor.org/action/showBasicSearch\">JSTOR</a>" +
                        "<br/><a href=\"http://muse.jhu.edu/\">Project Muse</a>" +
                        "<br/><a href=\"https://www.africaportal.org/publications/\">Africa Portal</a>" +
                        "<br/><a href=\"https://www.ajol.info/\">African Journals Online (AJOL)</a>" +
                        "<br/><a href=\"http://www.bioline.org.br/journals\">Bioline International</a>" +
                        "<br/><a href=\"https://doaj.org/\">Directory of Open Access Journals</a>" +
                        "<br/><a href=\"https://books.google.com/\">Google Books</a>" +
                        "<br/><a href=\"https://openknowledge.worldbank.org/\">Open Knowledge Repository (World Bank Group)</a>"
        ));
        libraryLinks.setMovementMethod(LinkMovementMethod.getInstance());

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                LoginResult result =
//                        loginService.performLogin(editTextUsername.getText().toString(),
//                                editTextPassword.getText().toString().toCharArray());
//
//            }
//        });
    }

    @Override
    public void onItemSelected(String id) {

    }
}