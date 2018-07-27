package org.aru.mobilelibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Toast;

import org.aru.mobilelibrary.services.ServiceFactory;
import org.aru.mobilelibrary.services.resources.LoadResourceResult;
import org.aru.mobilelibrary.services.resources.ResourceSearchResult;
import org.aru.mobilelibrary.services.resources.ResourceService;

/**
 * An activity representing a single Resource detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ResourceListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link ResourceDetailFragment}.
 */
public class ResourceDetailActivity extends AppCompatActivity {

    ResourceDetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final ResourceService resourceService = ServiceFactory.instance().getService(ResourceService.class);
        final ProgressDialog progress = new ProgressDialog(this);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ResourceDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ResourceDetailFragment.ARG_ITEM_ID));
            fragment = new ResourceDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.resource_detail_container, fragment)
                    .commit();
        }

        // TODO detail lookup HERE.
        new AsyncTask<String, Void, LoadResourceResult>() {

            protected void onPreExecute() {
                progress.setTitle("Loading resource...");
                progress.show();
            }

            protected LoadResourceResult doInBackground(String... params) {
                return resourceService.loadResource(params[0]);
            }

            protected void onPostExecute(LoadResourceResult result) {
                progress.dismiss();
                if (result != null && result.getResourceModel() != null) {

                    if (fragment != null) {
                        fragment.setResourceModel(result.getResourceModel());
                    }
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = result.getMessage();
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text,  duration);
                    toast.show();
                }
            }
        }.execute(getIntent().getStringExtra(ResourceDetailFragment.ARG_ITEM_ID));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, ResourceListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
