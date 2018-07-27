package org.aru.mobilelibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.aru.mobilelibrary.dummy.DummyContent;
import org.aru.mobilelibrary.services.resources.CheckedOutResource;
import org.aru.mobilelibrary.services.resources.ResourceModel;
import org.aru.mobilelibrary.services.resources.ResourceSearchResult;

import java.io.InputStream;
import java.net.URI;

/**
 * A fragment representing a single Resource detail screen.
 * This fragment is either contained in a {@link ResourceListActivity}
 * in two-pane mode (on tablets) or a {@link ResourceDetailActivity}
 * on handsets.
 */
public class ResourceDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";


    private String resourceId;

    View rootView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ResourceDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            resourceId = getArguments().getString(ARG_ITEM_ID);


            // mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                // appBarLayout.setTitle(mItem.content);
                appBarLayout.setTitle("");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_resource_detail, container, false);

        // Show the dummy content as text in a TextView.
        //    if (mItem != null) {
        //       ((TextView) rootView.findViewById(R.id.resource_detail)).setText(mItem.details);
        ((TextView) rootView.findViewById(R.id.resource_detail)).setText("");

        //  }

        return rootView;
    }

    public void setResourceModel(ResourceModel resource) {
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            // appBarLayout.setTitle(mItem.content);
            appBarLayout.setTitle(resource.getTitle());
        }

        TextView detailTextView = ((TextView) rootView.findViewById(R.id.resource_detail));


        String detailString = "<b>" + resource.getTitle() + "</b>" +
                "<br/><b>ISBN </b>" + resource.getIsbn() +
                "<br/><b>Author: </b>" + resource.getAuthor() +
                "<br/><b>Publisher </b>" + resource.getPublisher() +
                "<br/><b>Location </b>" + resource.getLocation() +
                "<br/><b>Call Number </b>" + resource.getCallNumber() +
                "<br/><b>Status </b>" + resource.getStatus();

        //  detailTextView.setText(resource.getAuthor());

        detailTextView.setText(Html.fromHtml(detailString));

        try {
            new ResourceDetailFragment.DownloadImageTask((ImageView) rootView.findViewById(R.id.resourceImageView))
                    .execute(resource.getImageURL());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
