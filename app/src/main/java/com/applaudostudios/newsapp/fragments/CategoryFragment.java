package com.applaudostudios.newsapp.fragments;

import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applaudostudios.newsapp.activities.DetailsActivity;
import com.applaudostudios.newsapp.data.NewsContract.NewsEntry;
import com.applaudostudios.newsapp.loaders.NewsLoader;
import com.applaudostudios.newsapp.R;
import com.applaudostudios.newsapp.adapters.RecyclerViewAdapter;
import com.applaudostudios.newsapp.model.News;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements RecyclerViewAdapter.CallBack, LoaderManager.LoaderCallbacks {

    private List<News> mData;
    public static final int NEWS_LOADER_ID = 0;
    public static final int CURSOR_LOADER_ID = 1;
    private RecyclerViewAdapter recyclerViewAdapter;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a fragment depending on the category that it takes as an argument.
     *
     * @param url in this case, it's an url build by the buildUrl method
     * @return an instance of CategoryFragment
     */
    public static CategoryFragment newInstance(String url) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString("someUrl", url);
        categoryFragment.setArguments(args);
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_category, container, false);

        // Inflates the layout for this fragment
        TextView mEmptyStateTextView = mView.findViewById(R.id.empty_view);

        // Checks the status of the network connection
        ConnectivityManager connMgr = (ConnectivityManager)
                (getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If there is a network connection, fetches data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Gets a reference to the LoaderManager, in order to interact with loaders
            LoaderManager loaderManager = getLoaderManager();

            // Initializes the loader
            loaderManager.restartLoader(NEWS_LOADER_ID, null, this);

        } else {
            // Otherwise, displays error
            // First, hides loading indicator so error message will be visible
            View loadingIndicator = mView.findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            LoaderManager loaderManager2 = getLoaderManager();

            loaderManager2.initLoader(CURSOR_LOADER_ID, null, this);

//            // Updates empty state with no connection error message
//            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        // Declares and initializes the recyclerView object
        RecyclerView myRecyclerView = mView.findViewById(R.id.recycler_view_news);
        myRecyclerView.setHasFixedSize(true);
        // Sets up the adapter
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(recyclerViewAdapter);
        return mView;
    }


    // Methods required because of the implementation of the LoaderCallbacks interface
    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case 0:
                return new NewsLoader(getActivity(), (getArguments()).getString("someUrl"));

            case 1:
//                // Define a projection that specifies the columns from the table we care about.
//                String[] projection = {
//                        NewsEntry._ID,
//                        NewsEntry.COLUMN_NEWS_CATEGORY,
//                        NewsEntry.COLUMN_NEWS_HEADLINE,
//                        NewsEntry.COLUMN_NEWS_BODY_TEXT,
//                        NewsEntry.COLUMN_NEWS_THUMBNAIL,
//                        NewsEntry.COLUMN_NEWS_WEB_URL};
//
//                // This loader will execute the ContentProvider's query method on a background thread
//                return new CursorLoader( getContext(),   // Parent activity context
//                        NewsEntry.CONTENT_URI,   // Provider content URI to query
//                        projection,             // Columns to include in the resulting Cursor
//                        null,                   // No selection clause
//                        null,                   // No selection arguments
//                        null);                  // Default sort order

            default:
                break;
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {

        switch (loader.getId()) {
            case 0:
                // Removing loading indicator
                View loadingIndicator = (getView()).findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);
                // Updates the data in the adapter
                recyclerViewAdapter.setData((List<News>) data);
                // Saving data to use it later on on the onItemClickMethod
                mData = (List<News>) data;
                break;

            case 1:

                break;

            default:
                break;
        }
    }

//    public void getString

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        switch (loader.getId()){
            case 1:

        }
    }

//    @Override
//    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
//        // Removing loading indicator
//        View loadingIndicator = (getView()).findViewById(R.id.loading_indicator);
//        loadingIndicator.setVisibility(View.GONE);
//        // Updates the data in the adapter
//        recyclerViewAdapter.setData(data);
//        // Saving data to use it later on on the onItemClickMethod
//        mData = data;
//    }


    // Method required because of the CallBack interface implementation.
    @Override
    public void onItemClick(int position) {
        startActivity(DetailsActivity.putNews(getContext(), mData.get(position)));
    }

}
