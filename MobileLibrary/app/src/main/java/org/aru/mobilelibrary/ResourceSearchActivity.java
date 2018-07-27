package org.aru.mobilelibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.aru.mobilelibrary.services.ServiceFactory;
import org.aru.mobilelibrary.services.login.LoginResult;
import org.aru.mobilelibrary.services.resources.CheckedOutResource;
import org.aru.mobilelibrary.services.resources.ResourceSearchResult;
import org.aru.mobilelibrary.services.resources.ResourceService;

import java.util.List;

/**
 * Created by mmichalek on 10/21/15.
 */
public class ResourceSearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resource_search_screen);




        final EditText searchEditText = (EditText)findViewById(R.id.SearchEditText);
        final EditText editAuthor = (EditText)findViewById(R.id.editAuthor);
        final EditText editTitle = (EditText)findViewById(R.id.editTitle);
        final EditText editISBN = (EditText)findViewById(R.id.editISBN);

        Button resourceSearchButton = (Button) findViewById(R.id.resourceSearchButton);

        final ProgressDialog progress = new ProgressDialog(this);

        resourceSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ResourceService resourceService = ServiceFactory.instance().getService(ResourceService.class);

                new AsyncTask<String, Void, ResourceSearchResult>() {

                    protected void onPreExecute() {

                        progress.setTitle("Searching resources...");
                        progress.show();
                    }

                    protected ResourceSearchResult doInBackground(String... params) {
                        String keyword = params[0];
                        String title = params[1];
                        String author = params[2];
                        String isbn = params[3];
                        return resourceService.searchResources(keyword, title, author, isbn);
                    }

                    protected void onPostExecute(ResourceSearchResult result) {
                        progress.dismiss();
                        if (result.getSearchResults() != null && result.getSearchResults().size() > 0) {

                            Scope.setValue(Scope.RESOURCE_SEARCH_RESULTS, result.getSearchResults());

                            Intent intent = new Intent(ResourceSearchActivity.this, ResourceListActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Context context = getApplicationContext();
                            CharSequence text = result.getMessage();
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text,  duration);
                            toast.show();
                        }
                    }
                }.execute(searchEditText.getText().toString(),
                        editTitle.getText().toString(),
                        editAuthor.getText().toString(),
                        editISBN.getText().toString()
                        );

            }
        });

        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResourceSearchActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        final TextView checkedOutResourcesList = (TextView)findViewById(R.id.checkedOutResourceList);

        StringBuilder buff = new StringBuilder();
        LoginResult login = Scope.getValue(Scope.USER_MODEL);
        if (login != null && !login.getUser().getCheckedOutResources().isEmpty()) {
            List<CheckedOutResource> checkedOutResources = login.getUser().getCheckedOutResources();
            for (CheckedOutResource checkedOutResource : checkedOutResources) {

                buff.append("<p><b>").append(checkedOutResource.getTitle()).append("</b>");
                buff.append("<br/>");
                buff.append("Author: ").append(checkedOutResource.getAuthor());
                buff.append("<br/>");
                buff.append("Due: ").append("<b>" + checkedOutResource.getDue() + "</b>");
                buff.append("</p><hr/>");
            }
        }

        // checkedOutResourcesList.setText(Html.fromHtml("<h2>" + + "</h2><br><p>Description here</p>"));
        checkedOutResourcesList.setText(Html.fromHtml(buff.toString()));


    }

}
